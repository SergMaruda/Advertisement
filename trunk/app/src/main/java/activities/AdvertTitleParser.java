package activities;

import android.content.Context;

import com.example.sergey.advertisement.R;

import activities.AdvertTitleData;


public class AdvertTitleParser
  {
  public static AdvertTitleData Parse(Context i_ctx, String i_str)
    {
    AdvertTitleData data = new AdvertTitleData();

    String meta = i_str.toLowerCase();

    String[] tokens = meta.split("(\\)|\\(| |,)+");
    String city_str = i_ctx.getString(R.string.city);
    String search_str = i_ctx.getString(R.string.search);
    String woman = i_ctx.getString(R.string.woman);
    String man = i_ctx.getString(R.string.man);
    String mne = i_ctx.getString(R.string.mne);

    for(int i = 0; i < tokens.length; ++i)
      {
      if(tokens[i].contains(woman))
        data.m_sex = AdvertTitleData.ESex.WOMAN;
      else if(tokens[i].contains(man))
        data.m_sex = AdvertTitleData.ESex.MAN;
      else if(tokens[i].contains(city_str))
          data.m_city = tokens[i+1];
      else if(tokens[i].contains(mne))
        data.m_age =  Integer.parseInt(tokens[i+1]);
      else if(tokens[i].contentEquals("|"))
        data.m_num_views = Integer.parseInt(tokens[i+1]);
      }

    return data;
    }

  }
