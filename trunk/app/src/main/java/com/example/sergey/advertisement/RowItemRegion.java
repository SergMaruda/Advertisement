package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */
public class RowItemRegion
  {
  public String m_region;
  public String m_link;

  public RowItemRegion(String i_city, String i_link)
    {
    m_region = i_city;
    m_link = i_link;
    }

@Override
  public String toString()
    {
    return m_region + "\n" + m_link;
    }
}