package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */
public class RowItemCity
  {
  public String m_city;
  public String m_link;

  public RowItemCity(String i_city, String i_link)
    {
    m_city = i_city;
    m_link = i_link;
    }

@Override
  public String toString()
    {
    return m_city + "\n" + m_link;
    }
}