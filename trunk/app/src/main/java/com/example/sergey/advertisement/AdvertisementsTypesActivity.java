package com.example.sergey.advertisement;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sergey.advertisement.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class AdvertisementsTypesActivity extends ActionBarActivity
  {
  ListView listViewAdvertTypes;
  Vector<Pair<String, String>> m_links_text = new Vector<Pair<String, String>>();

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

        Elements a_elems = doc.select("a");
        for (Element a : a_elems)
          {
          String href = a.attr("href");

          if (href.contains("view_section"))
            {
            m_links_text.add(Pair.create(href, a.text()));
            }

          if (href.contains("view_subsection"))
            {
            m_links_text.add(Pair.create(href, a.text()));
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
      Vector<RowItemAdvertisementType> types  = new Vector<RowItemAdvertisementType>();

      for( Pair<String, String> entry : m_links_text)
        {
        types.add(new RowItemAdvertisementType(entry.second, entry.first));
        }

      m_links_text = null;

      CustomListViewAdvertTypeAdapter adapter = new CustomListViewAdvertTypeAdapter(context, R.layout.list_item_advert_type, types);
      listViewAdvertTypes.setAdapter(adapter);

      }
    }
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_advertisements_types);

    listViewAdvertTypes = (ListView) findViewById(R.id.listViewAdvertTypes);

    listViewAdvertTypes.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
    public void onItemClick(AdapterView parent, View view, int position, long id)
      {
      CustomListViewAdvertTypeAdapter adapt = (CustomListViewAdvertTypeAdapter)parent.getAdapter();
      String link = adapt.getItem(position).m_link;
      String link_text = adapt.getItem(position).m_text;
      Intent intent = new Intent(view.getContext(), AdvertisementsActivity.class);
      Bundle b = new Bundle();
      b.putString("link_text", link_text); //Your id
      b.putString("link", link);
      intent.putExtras(b); //Put your id to your next Intent
      startActivity(intent);
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
