package com.example.sergey.advertisement;

import android.content.Context;

/**
 * Created by maruda on 5/8/2015.
 */
public class AdvertTitleParser
  {
  public static AdvertTitleData Parse(Context i_ctx, String i_str)
    {
    AdvertTitleData data = new AdvertTitleData();

    String meta = i_str;
    meta.toLowerCase();
    String city_str = i_ctx.getString(R.string.city);
    String search_str = i_ctx.getString(R.string.search);

    int idx_city = meta.indexOf(city_str);
    if(idx_city != -1)
      {
      int city_begin = meta.indexOf(" ", idx_city);
      if(city_begin != -1)
        {
        int city_end = meta.indexOf(" ", city_begin+2);
        if(city_end != -1)
          data.m_city = meta.substring(city_begin, city_end);
        }
      }


    return data;
    }

  }
