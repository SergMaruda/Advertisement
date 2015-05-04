package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */
public class RowItemAdvertisementType
  {
  public String m_text;
  public String m_link;

  public RowItemAdvertisementType(String i_text, String i_link)
    {
    m_text = i_text;
    m_link = i_link;
    }

@Override
  public String toString()
    {
    return m_text + "\n" + m_link;
    }
}