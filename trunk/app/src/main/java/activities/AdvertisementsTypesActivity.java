package activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.sergey.advertisement.CustomListViewAdvertTypeAdapter;
import com.example.sergey.advertisement.R;
import com.example.sergey.advertisement.RowItemAdvertisementType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AdvertisementsTypesActivity extends ActionBarActivity
  {
  ExpandableListView listViewAdvertTypes;
  Vector<RowItemAdvertisementType> m_links_text = new Vector<>();

  class TaskScanTypes extends AsyncTask<Void, Void, Void>
    {
    private Context context;
    private String title;
    private InputStream input = null;
    private OutputStream output = null;

    public TaskScanTypes(Context context)
      {
      this.context = context;
      }

    @Override
    protected Void doInBackground(Void... params)
      {

      Document doc = null;
      try
        {
        String url_base = "http://kiev.ukrgo.com";
        doc = Jsoup.connect(url_base).get();

        Element title = doc.getElementsByTag("title").get(0);

        final String title_str = title.text().split(context.getResources().getString(R.string.doski))[0];

        runOnUiThread(new Runnable()
        {
        public void run()
          {
          AdvertisementsTypesActivity.this.setTitle(title_str);
          }
        });

        Elements a_elems = doc.select("a");
        for (Element a : a_elems)
          {
          String href = a.attr("href");

          if (href.contains("view_section"))
            {
            m_links_text.add(new RowItemAdvertisementType(a.text(), href));
            }

          if (href.contains("view_subsection"))
            {
            m_links_text.add(new RowItemAdvertisementType(a.text(), href));
            }
          }

        }
      catch (IOException e)
        {
        e.printStackTrace();
        }

      if (doc != null)
        title = doc.title();
      else
        title = "Error";

      return null;
      }

    @Override
    protected void onPostExecute(Void result)
      {
      super.onPostExecute(result);

      Vector<String> header  = new Vector<>();

      HashMap<String, List<RowItemAdvertisementType>> m_data = new HashMap<>();

      for( RowItemAdvertisementType entry : m_links_text)
      {
       if(entry.m_link.contains("view_section"))
       {
         header.add(entry.m_text);
         m_data.put(entry.m_text, new Vector<RowItemAdvertisementType>());
       }
        else {
         m_data.get(header.lastElement()).add(entry);
       }


      }

      m_links_text = null;

      CustomListViewAdvertTypeAdapter adapter = new CustomListViewAdvertTypeAdapter(context, header, m_data);
      listViewAdvertTypes.setAdapter(adapter);

      }
    }
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_advertisements_types);

    listViewAdvertTypes = (ExpandableListView) findViewById(R.id.listViewAdvertTypes);

    listViewAdvertTypes.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
      {
      @Override
      public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
        {
        /* You must make use of the View v, find the view by id and extract the text as below*/
        ExpandableListAdapter adapter =  parent.getExpandableListAdapter();
        CustomListViewAdvertTypeAdapter adapter_conc = (CustomListViewAdvertTypeAdapter)adapter;
        RowItemAdvertisementType adv_type = (RowItemAdvertisementType )adapter_conc.getChild(groupPosition, childPosition);
        String link = adv_type.m_link;
        String link_text = adv_type.m_text;

        Intent intent = new Intent(v.getContext(), AdvertisementsActivity.class);
        Bundle b = new Bundle();
        b.putString("link_text", link_text); //Your id
        b.putString("link", link);
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);

        return true;  // i missed this
        }
      });

    TaskScanTypes task = new TaskScanTypes(this);
    task.execute();
    }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
    {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_advertisements_types, menu);
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
    if (id == R.id.action_settings)
      {
      return true;
      }
    
    return super.onOptionsItemSelected(item);
    }
  }
