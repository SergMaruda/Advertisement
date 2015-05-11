package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sergey.advertisement.CustomListViewAdapterAdvertTitle;
import com.example.sergey.advertisement.R;
import com.example.sergey.advertisement.RowItemAdvertTitle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Vector;

public class AdvertisementsActivity extends ActionBarActivity
  {
  ListView listView;
  Vector<RowItemAdvertTitle> advert_titles = new Vector<>();
  Vector<Filter> filters = new Vector<>();
  Vector<Filter> new_filters = new Vector<>();
  ProgressDialog m_progress_dialog;

  final int FILTER_ACTIVITY = 0;

  String m_link;
  String php_script;
  String sub_section;

  class MyTask extends AsyncTask<Void, Void, Void>
    {

    private Context context;
    private String title;
    private InputStream input = null;
    private OutputStream output = null;
    HttpURLConnection connection = null;
    //File imageFile;

    public MyTask(Context context)
      {
      this.context = context;
      }

    @Override
    protected Void doInBackground(Void... params)
      {
      Document doc;
      try
        {

        m_link = php_script + "?" + sub_section;


        if (new_filters.size() == filters.size())
          {
          for (int i = 0; i < new_filters.size(); ++i)
            {
            if (new_filters.get(i).m_active != filters.get(i).m_active)
              {
              m_link += new_filters.get(i).m_url;
              }
            }
          }

        filters.clear();
        new_filters.clear();

        doc = Jsoup.connect(m_link).get();


        Elements links_elem = doc.select("a");
        advert_titles = new Vector<>();
        for (Element l : links_elem)
          {
          String link_class = l.attr("class");
          String href = l.attr("href");
          if (href.contains("./view_subsection.php?"))
            {
            Filter filter = new Filter();
            filter.m_titile = l.text();
            String[] separated = href.split(sub_section);
            if(separated.length >= 2)
              filter.m_url = separated[1];
            filter.m_active = l.hasAttr("style");
            filters.add(filter);
            }

          if (link_class.contentEquals("link_post"))
            {
            Element parent = l.parent();
            Elements spans = parent.select("span");

            if(spans.size() >= 1)
              {
              String text = parent.text();

              String link_ref = l.attr("href");
              String linkText = l.text();

              String span_class = spans.get(0).attr("class");
              String meta_data = "";
              if (span_class.contentEquals("attribut_post"))
                {
                meta_data = spans.get(0).text();
                }

              Elements img_elems = parent.select("img");

              AdvertTitleData data = AdvertTitleParser.Parse(AdvertisementsActivity.this, meta_data);
              data.m_has_photo = !img_elems.isEmpty();
              advert_titles.add(new RowItemAdvertTitle(data.m_city, link_ref, linkText, data.m_sex, data.m_age, data.m_num_views, data.m_has_photo));
              }
            }
          }

        }
      catch (IOException e)
        {
        e.printStackTrace();
        }

      return null;
      }

    @Override
    protected void onPostExecute(Void result)
      {
      super.onPostExecute(result);
      m_progress_dialog.dismiss();
      m_progress_dialog = null;
      CustomListViewAdapterAdvertTitle adapter = new CustomListViewAdapterAdvertTitle(context, R.layout.list_item_advertisement_title, advert_titles);
      listView.setAdapter(adapter);
      }
    }

  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_advertisements);
    listView = (ListView) findViewById(R.id.listView);

    Bundle b = getIntent().getExtras();
    m_link = b.getString("link");

    String[] link_parsed = m_link.split("(\\?|&)");
    php_script = link_parsed[0];
    sub_section = link_parsed[1];

    String link_text = b.getString("link_text");
    setTitle(link_text);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
    public void onItemClick(AdapterView parent, View view, int position, long id)
      {
      String link = advert_titles.elementAt(position).m_description;
      String link_ref = advert_titles.elementAt(position).m_link;

      Intent intent = new Intent(view.getContext(), ActivityAdvertisement.class);
      Bundle b = new Bundle();
      b.putString("link_text", link); //Your id
      b.putString("link_ref", link_ref);
      intent.putExtras(b); //Put your id to your next Intent
      startActivity(intent);
      }
    });


    LoadData();
    }


  private void LoadData()
    {
    m_progress_dialog = new ProgressDialog(this);
    m_progress_dialog.setMessage("Downloading...");
    m_progress_dialog.setIndeterminate(true);
    m_progress_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    m_progress_dialog.setCancelable(false);

    MyTask mt = new MyTask(AdvertisementsActivity.this);
    mt.execute();
    m_progress_dialog.show();
    }


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
    {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_advertisements, menu);
    return true;
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
    {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_filter)
      {
      Intent intent = new Intent(this, ActivityFilters.class);
      intent.putExtra("filters", filters); //Put your id to your next Intent
      startActivityForResult(intent, FILTER_ACTIVITY);
      return true;
      }

    if (id == R.id.action_settings)
      {
      return true;
      }

    return super.onOptionsItemSelected(item);
    }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode)
      {
      case (FILTER_ACTIVITY):
      {
      if (resultCode == ActivityFilters.RESULT_OK)
        {
        ArrayList<Filter> array = (ArrayList<Filter>) data.getExtras().get("filters");
        if(array != null)
          {
          new_filters = new Vector<>(array);
          LoadData();
          }
        }
      break;
      }
      }
    }
  }




