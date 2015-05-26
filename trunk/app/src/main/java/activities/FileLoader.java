package activities;

import android.content.Context;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by Sergey on 24.05.2015.
 */
public class FileLoader
  {
  public String m_taget_file;
  public String url_down;

  FileLoader(String i_url, String i_link, Context i_ctx)
    {
    int end_picture = i_url.lastIndexOf("/");
    String file_name = i_url.substring(end_picture, i_url.length());

    int end_base = i_link.lastIndexOf("/");
    String url_base = i_link.substring(0, end_base);
    String sub_url = i_url.substring(1);
    url_down = url_base + sub_url;

    m_taget_file = i_ctx.getCacheDir().getAbsolutePath() + file_name; // context being the Activity pointer
    }


  public String Load(Map<String, String> i_cookies)
    {
    try
      {
      Connection.Response resultImageResponse = Jsoup.connect(url_down).cookies(i_cookies).ignoreContentType(true).execute();

      // output here
      FileOutputStream out = (new FileOutputStream(new java.io.File(m_taget_file)));
      out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.
      out.close();
      } catch (java.io.IOException e)
      {
      return "";
      }

    return m_taget_file;

    }

  }
