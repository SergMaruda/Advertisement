package activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sergey.advertisement.ListViewAdvert;
import com.example.sergey.advertisement.R;
import com.example.sergey.advertisement.RowItem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ActivityAdvertisement extends ActionBarActivity
  {
  TextView m_title;
  String m_link;
  ListView listViewImages;
  URL m_url;
  Vector<RowItem> listItems = new Vector<>();
  Vector<String> pictures = new Vector<>();
  Set<String> images_files = new HashSet<>();
  private MyTask m_task = null;
  private String m_post_id = "";
  private Map<String, String> m_cookies;
  private String m_captcha_url = "";

  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_advertisement);
    Bundle b = getIntent().getExtras();
    String link_text = b.getString("link_text");
    m_link = b.getString("link_ref");
    setTitle(link_text);

    // m_link = "http://kiev.ukrgo.com/post_6072435_vstrechus_v_avto_s_devushkoj_mat_pomosch.html";

    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(m_link);
    if (matcher.find())
      {
      m_post_id = matcher.group(0);
      }

    try
      {
      m_url = new URL(m_link);
      } catch (MalformedURLException e)
      {
      }

    final String host = m_url.getHost();


    listViewImages = (ListView) findViewById(R.id.listViewImages);

    ImageButton imageButtonSendMail = (ImageButton) findViewById(R.id.imageButtonSendMail);

    imageButtonSendMail.setOnClickListener(new View.OnClickListener()
    {
    @Override
    public void onClick(View v)
      {
      Intent intent = new Intent(ActivityAdvertisement.this, ActivitySendToAuthor.class);
      intent.putExtra("host", host);
      intent.putExtra("post_id", m_post_id);
      intent.putExtra("link", m_link);
      intent.putExtra("capatcha_url", ActivityAdvertisement.this.m_captcha_url);

      HashMap<String, String> map = new HashMap<>(ActivityAdvertisement.this.m_cookies);
      intent.putExtra("cookies", map);
      startActivity(intent);

      }
    });

    m_task = new MyTask(ActivityAdvertisement.this);
    m_task.execute();
    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
    {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main_activity2_activity_view_post, menu);
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

  class MyTask extends AsyncTask<Void, Void, Void>
    {
    HttpURLConnection connection = null;
    private Context context;
    private String title;
    private InputStream input = null;
    private OutputStream output = null;
    private String m_description = "";
    private String m_meta = "";

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
        String content_def = "margin-top: 15px; text-align: left; width: 100%; color: #2a2a2a; font-size: 14px;";
        String meta_def = "color: #242424; font-size: 12px; margin-top: 5px;";
        int idx = m_link.lastIndexOf("/");
        String head = m_link.substring(0, idx);
        String post = m_link.substring(idx + 1, m_link.length());
        post = URLEncoder.encode(post, "utf-8");

        Connection conn = Jsoup.connect(head + "/" + post);
        Connection.Response resp = conn.execute();

        m_cookies = resp.cookies();

        doc = conn.cookies(m_cookies).get();
        Elements div_elem = doc.select("div");

        boolean descr_read = false;
        boolean meta_read = false;

        for (Element de : div_elem)
          {
          String link_style = de.attr("style");
          if (link_style.contentEquals(content_def))
            {
            if (meta_read)
              m_description += "\n";

            m_description += de.text();
            descr_read = true;
            }

          if (link_style.contentEquals(meta_def))
            {
            if (descr_read)
              m_description += "\n";

            String meta = de.text();
            String city_str = getString(R.string.city);
            String search_str = getString(R.string.search);

            int idx_city = meta.indexOf(city_str);
            if (idx_city != -1)
              meta = meta.substring(idx_city) + "\n" + meta.substring(idx_city, meta.length());

            int idx_search = meta.indexOf(search_str);
            if (idx_search != -1)
              meta = meta.substring(idx_search) + "\n" + meta.substring(idx_search, meta.length());

            m_description += meta;
            meta_read = true;
            }

          if (descr_read && meta_read)
            break;
          }

        Elements img_elem = doc.select("img");
        pictures.clear();
        for (Element ie : img_elem)
          {
          String link_class = ie.attr("src");
          boolean captcha = (ie.hasAttr("id") && ie.attr("id").contains("captcha"));
          if (captcha)
            {
            m_captcha_url = link_class;
            }

          if (link_class.contains("pictures"))
            {
            pictures.add(link_class);
            }
          }

        //---- Read Images

        images_files.clear();
        for (String p : pictures)
          {
          FileLoader p_loader = new FileLoader(p, m_link, context);
          if (!images_files.contains(p_loader.m_taget_file))
            {
            String target_file = p_loader.Load(m_cookies);
            images_files.add(target_file);
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

      Vector<RowItem> items = new Vector<>();
      items.add(new RowItem("", m_description));

      for (String f : images_files)
        items.add(new RowItem(f, ""));

      listViewImages.setAdapter(new ListViewAdvert(context, R.layout.list_item_advert, items));
      }
    }
  }
