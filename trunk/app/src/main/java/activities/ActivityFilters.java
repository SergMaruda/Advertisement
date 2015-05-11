package activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Vector;

public class ActivityFilters extends ActionBarActivity
  {
  public final static int RESULT_OK = 0;
  ListView listView;
  Vector<Filter> filters;

  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_filters);

    listView = (ListView) findViewById(R.id.listViewFilters);
    ArrayList<Filter> obj = (ArrayList<Filter>) getIntent().getExtras().get("filters");
    filters = new Vector<>(obj);

    final ListViewFiltersAdapter adapter = new ListViewFiltersAdapter(this, android.R.layout.simple_list_item_checked, filters);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id)
      {
      adapter.getItem(position).m_active = !adapter.getItem(position).m_active;
      }
    });


    for (int i = 0; i < filters.size(); ++i)
      {
      listView.setItemChecked(i, filters.get(i).m_active);
      }
    }

  @Override
  public void onDestroy()
    {

    super.onDestroy();
    }

  @Override
  public void onBackPressed()
    {
    Intent resultIntent = new Intent();
    resultIntent.putExtra("filters", filters);
    setResult(RESULT_OK, resultIntent);
    super.onBackPressed();
    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
    {
    return true;
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
    {
    return super.onOptionsItemSelected(item);
    }
  }


//Read image


