package com.example.sergey.advertisement;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import activities.AdvertisementsActivity;

public class ScanRegionsActivity extends ActionBarActivity
  {
  ListView listViewCities;
  LinkedHashMap<String, String> m_links_text = new LinkedHashMap<>();

  class ScanCitiesTask extends AsyncTask<Void, Void, Void>
    {
    private Context context;
    private String title;

    public ScanCitiesTask(Context context)
      {
      this.context = context;
      }

    @Override
    protected Void doInBackground(Void... params)
      {
      Document doc = null;
      try
        {
        String url_base = "http://ukrgo.com";
        doc = Jsoup.connect(url_base).get();

        String base_no_http = url_base.substring(7);

        String areas_str = getResources().getString(R.string.area);

          {
          Elements a_elements = doc.select("a");
          boolean found = false;
          Element table = null;
          for (Element a : a_elements)
            {
            String href = a.attr("href");

            int idx_sub_bein = href.indexOf("//") + 2;
            int idx_sub_end = href.indexOf(base_no_http);
            int idx_dot_com = href.lastIndexOf(".com/");

            if(href.length() == idx_dot_com + 5)
              {
              if(idx_sub_bein < idx_sub_end)
                {
                m_links_text.put(a.attr("href"), a.text());
                }
              }
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


      Vector<RowItemRegion> cities = new Vector<>();

      for(Map.Entry<String,String> entry : m_links_text.entrySet())
        {
        cities.add(new RowItemRegion(entry.getValue(), entry.getKey()));
        }

      CustomListViewRegionAdapter adapter = new CustomListViewRegionAdapter(context, R.layout.list_item_region, cities);
      listViewCities.setAdapter(adapter);
      }
    }
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan_cities);
    listViewCities = (ListView) findViewById(R.id.listViewCities);

    listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
    public void onItemClick(AdapterView parent, View view, int position, long id)
      {
      CustomListViewRegionAdapter cities = (CustomListViewRegionAdapter)listViewCities.getAdapter();
      RowItemRegion item = cities.getItem(position);
      Intent intent = new Intent(view.getContext(), AdvertisementsActivity.class);
      Bundle b = new Bundle();
      b.putString("link_text", item.m_region);
      b.putString("link_ref", item.m_link);
      intent.putExtras(b);
      startActivity(intent);
      }
    });

    ScanCitiesTask task = new ScanCitiesTask(this);
    task.execute();
    }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
    {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scan_cities, menu);
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
