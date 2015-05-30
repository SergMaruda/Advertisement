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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

  static String EncodeString(String i_str)
    {
    try
      {
      return URLEncoder.encode(i_str, "windows-1251");
      } catch (UnsupportedEncodingException e)
      {
      }

    return i_str;
    }

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

    final String captcha = EncodeString(m_text_view_catcha.getText().toString());
    final String email = EncodeString(m_edit_text_email.getText().toString());
    final String name = EncodeString(m_edit_text_name.getText().toString());
    final String message = EncodeString(m_edit_text_message.getText().toString());
    final String target_url = "http://" + m_host + "/send_mail_user.php?id_post=" + m_post_id;

    m_send_message_task = new AsyncTask<Void, Void, Void>()
    {
    @Override
    protected Void doInBackground(Void... params)
      {
      try
        {
        String urlParameters = "name=" + name + "&" +
            "email=" + email + "&" +
            "text=" + message + "&" +
            "keystring=" + captcha;

        byte[] postData = urlParameters.getBytes();
        int postDataLength = postData.length;
        URL url = new URL(target_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "windows-1251");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        String cook = "";
        for (Map.Entry<String, String> entry : m_cookies.entrySet())
          {
          String key = entry.getKey();
          String value = entry.getValue();
          cook += key + "=" + value + ";";
          }
        conn.setRequestProperty("Cookie", cook);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream()))
          {
          wr.write(postData);
          conn.connect();
          conn.getResponseCode();
          } catch (IOException e)
          {
          }
        } catch (Exception e)
        {
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
