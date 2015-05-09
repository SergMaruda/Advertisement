package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapterAdvertTitle extends ArrayAdapter<RowItemAdvertTitle> {

Context context;

public CustomListViewAdapterAdvertTitle(Context context, int resourceId,
                                        List<RowItemAdvertTitle> items) {
super(context, resourceId, items);
this.context = context;
}

/*private view holder class*/
private class ViewHolder {
TextView textViewDecription;
ImageView imageViewCity;
TextView textViewCityName;
}

public View getView(int position, View convertView, ViewGroup parent)
  {
  ViewHolder holder = null;
  RowItemAdvertTitle rowItem = getItem(position);

  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item_advertisement_title, null);
    holder = new ViewHolder();
    holder.textViewDecription = (TextView) convertView.findViewById(R.id.textViewAdvertTitle);
    holder.imageViewCity = (ImageView)convertView.findViewById(R.id.ImageViewCity);
    holder.textViewCityName = (TextView)convertView.findViewById(R.id.textViewCity);
    convertView.setTag(holder);
    }
  else
    holder = (ViewHolder) convertView.getTag();

  holder.textViewDecription.setTextIsSelectable(true);
  holder.textViewCityName.setText(rowItem.m_city);
  holder.textViewDecription.setText(rowItem.m_description);
  return convertView;
  }
}