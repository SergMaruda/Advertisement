package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */
public class RowItemAdvertTitle
  {
  public String m_link;
  public String m_city;
  public String m_description;
  public String m_sex;
  public int m_age;
  public int m_views;

  public RowItemAdvertTitle(String i_city, String i_link, String i_description, String i_sex, int i_age, int i_views)
    {
    m_link = i_link;
    m_city = i_city;
    m_description = i_description;
    m_sex = i_sex;
    m_age = i_age;
    m_views = i_views;
    }

@Override
  public String toString()
    {
    return m_city + "\n" + m_link + m_description;
    }
}