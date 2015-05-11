package com.example.sergey.advertisement;

import activities.AdvertTitleData;


public class RowItemAdvertTitle
  {
  public String m_link;
  public String m_city;
  public String m_description;
  public AdvertTitleData.ESex m_sex;
  public int m_age;
  public int m_num_views;
  public boolean m_has_photo;

  public RowItemAdvertTitle(String i_city, String i_link, String i_description, AdvertTitleData.ESex i_sex, int i_age, int i_views, boolean i_has_photo)
    {
    m_link = i_link;
    m_city = i_city;
    m_description = i_description;
    m_sex = i_sex;
    m_age = i_age;
    m_num_views = i_views;
    m_has_photo  = i_has_photo;
    }

@Override
  public String toString()
    {
    return m_city + "\n" + m_link + m_description;
    }
}