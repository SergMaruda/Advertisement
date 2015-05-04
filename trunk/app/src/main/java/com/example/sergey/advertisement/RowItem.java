package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */
public class RowItem
  {
  public String imagePath;
  public String text;

  public RowItem(String imageId, String i_text)
    {
    imagePath = imageId;
    text = i_text;
    }
  public String getImageId()
    {
    return imagePath;
    }

  public void setImageId(String imageId)
    {
    this.imagePath = imageId;
    }

@Override
  public String toString()
    {
    return imagePath;
    }
}