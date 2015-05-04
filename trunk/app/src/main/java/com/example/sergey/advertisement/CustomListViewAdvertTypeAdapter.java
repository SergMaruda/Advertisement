package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdvertTypeAdapter extends ArrayAdapter<RowItemAdvertisementType> {

Context context;

public CustomListViewAdvertTypeAdapter(Context context, int resourceId, List<RowItemAdvertisementType> items)
  {
  super(context, resourceId, items);
  this.context = context;
  }

/*private view holder class*/
private class ViewHolder
  {
  TextView textView;
  }

public View getView(int position, View convertView, ViewGroup parent)
  {
  ViewHolder holder = null;
  RowItemAdvertisementType rowItem = getItem(position);
  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  //if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item_advert_type, null);
    holder = new ViewHolder();
    holder.textView = (TextView) convertView.findViewById(R.id.textAdvertType);
    convertView.setTag(holder);
    }
 // else
    {
    //holder = (ViewHolder) convertView.getTag();
    }

  if(rowItem.m_link.contains("view_section"))
    holder.textView.setTypeface(null, Typeface.BOLD);
  holder.textView.setText(rowItem.m_text);

  return convertView;
  }
}