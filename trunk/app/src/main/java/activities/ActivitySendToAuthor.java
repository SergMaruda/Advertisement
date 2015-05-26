package activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergey.advertisement.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class ActivitySendToAuthor extends ActionBarActivity
  {
  TextView m_text_view_catcha;
  TextView m_edit_text_name;
  TextView m_edit_text_email;
  TextView m_edit_text_message;
  AsyncTask<Void, Void, Void> m_load_captcha_task;
  AsyncTask<Void, Void, Void> m_send_message_task;

  ImageView m_image_view_captcha;
  HashMap<String, String> m_cookies;
  Button m_send_button;
  String m_post_id;
  String m_link;
  String m_captcha_url;
  String m_host;
  String m_captcha_file_name;

  @Override
  protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_to_author);

    Bundle b = getIntent().getExtras();
    m_cookies = (HashMap<String, String>) b.get("cookies");
    m_post_id = b.getString("post_id");
    m_link = b.getString("link");
    m_captcha_url = b.getString("capatcha_url");
    m_host = b.getString("host");

    m_text_view_catcha = (TextView) findViewById(R.id.textViewCaptcha);
    m_send_button = (Button) findViewById(R.id.buttonSendToAuthor);
    m_image_view_captcha = (ImageView) findViewById(R.id.imageViewCaptcha);
    m_edit_text_name = (TextView) findViewById(R.id.editTextName);
    m_edit_text_email = (TextView) findViewById(R.id.editTextEmail);
    m_edit_text_message = (TextView) findViewById(R.id.editTextMessage);

    LoadCaptcha();

    m_send_button.setOnClickListener(new View.OnClickListener()
    {
    @Override
    public void onClick(View v)
      {
      SendMessage();
      }
    });
    }

  @Override
  public void onDestroy()
    {
    super.onDestroy();
    }

  @Override
  public void onBackPressed()
    {
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

  protected void LoadCaptcha()
    {
    if (m_load_captcha_task != null && m_load_captcha_task.getStatus() == AsyncTask.Status.RUNNING)
      return;

    m_load_captcha_task = new AsyncTask<Void, Void, Void>()
    {
    @Override
    protected Void doInBackground(Void... params)
      {

      FileLoader p_loader = new FileLoader(m_captcha_url, m_link, ActivitySendToAuthor.this);
      m_captcha_file_name = p_loader.Load(m_cookies);
      return null;
      }

    @Override
    protected void onPostExecute(Void result)
      {
      super.onPostExecute(result);

      Bitmap myBitmap = BitmapFactory.decodeFile(m_captcha_file_name);
      m_image_view_captcha.setImageBitmap(myBitmap);
      }
    };

    m_load_captcha_task.execute();
    }

  protected void SendMessage()
    {
    if (m_send_message_task != null && m_send_message_task.getStatus() == AsyncTask.Status.RUNNING)
      return;

    final String captcha = m_text_view_catcha.getText().toString();
    final String email = m_edit_text_email.getText().toString();
    final String name = m_edit_text_name.getText().toString();
    final String message = m_edit_text_message.getText().toString();

    final String target_url = "http://" + m_host + "/send_mail_user.php?id_post=" + m_post_id;
    final String cap_txt = captcha;
    String cont = "Content-Type: text/html; charset=windows-1251";

    m_send_message_task = new AsyncTask<Void, Void, Void>()
    {
    @Override
    protected Void doInBackground(Void... params)
      {
      try
        {
        Connection.Response res = Jsoup.connect(target_url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .data("name", name)
            .data("email", email)
            .data("text", message)
            .data("keystring", captcha)
            .cookies(m_cookies)
            .timeout(10000)
            .method(Connection.Method.POST)
            .followRedirects(false)
            .execute();

        int code = res.statusCode();
        ++code;
        } catch (Exception e)
        {
        int i = 0;
        ++i;

        }

      return null;
      }


    @Override
    protected void onPostExecute(Void result)
      {
      super.onPostExecute(result);
      LoadCaptcha();
      }
    };

    m_send_message_task.execute();
    }

  }
