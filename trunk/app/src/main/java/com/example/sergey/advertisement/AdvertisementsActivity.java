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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Vector;

public class AdvertisementsActivity extends ActionBarActivity {

    ListView listView;
    Vector<RowItemAdvertTitle> links_text = new Vector<RowItemAdvertTitle>();
    Vector<String> links = new Vector<String>();
    String m_link;

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

            Document doc = null;
            try
                {
                String url_base = m_link;
                doc = Jsoup.connect(url_base ).get();

                //Element content = doc.getElementById("content");
                Elements links_elem = doc.select("a");
                links_text.clear();
                links.clear();
                for (Element l : links_elem)
                    {
                    String link_class = l.attr("class");

                    if (link_class.contentEquals("link_post"))
                        {
                        Element parent = l.parent();
                        Elements spans = parent.select("span");

                        String text = parent.text();

                        String link_ref = l.attr("href");
                        String linkText = l.text();
                        links.add(link_ref);

                        String span_class = spans.get(0).attr("class");
                        String meta_data = "";
                        if (span_class.contentEquals("attribut_post"))
                            {
                            String elem =spans.get(0).text();
                            meta_data = elem;
                            }

                        AdvertTitleData data = AdvertTitleParser.Parse(AdvertisementsActivity.this, meta_data);

                        links_text.add(new RowItemAdvertTitle(data.m_city, link_ref, linkText, "", 10,10));
                        }
                    }

                } catch (IOException e)
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

            CustomListViewAdapterAdvertTitle adapter = new CustomListViewAdapterAdvertTitle(context, R.layout.list_item_advertisement_title, links_text);
            listView.setAdapter(adapter);
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        Bundle b = getIntent().getExtras();
        m_link = b.getString("link");
        String link_text = b.getString("link_text");
        setTitle(link_text);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
        public void onItemClick(AdapterView parent, View view, int position, long id)
            {
            String link = links_text.elementAt(position).m_description;
            String link_ref = links_text.elementAt(position).m_link;

            Intent intent = new Intent(view.getContext(), MainActivity2ActivityViewPost.class);
            Bundle b = new Bundle();
            b.putString("link_text", link); //Your id
            b.putString("link_ref", link_ref);
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
            }
        });

        MyTask mt = new MyTask(AdvertisementsActivity.this);
        mt.execute();
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


//Read image


