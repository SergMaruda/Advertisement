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

import org.w3c.dom.Text;

import java.util.List;

public class CustomListViewCityAdapter extends ArrayAdapter<RowItemCity> {

Context context;

public CustomListViewCityAdapter(Context context, int resourceId, List<RowItemCity> items)
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
  RowItemCity rowItem = getItem(position);

  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item_city, null);
    holder = new ViewHolder();
    holder.textView = (TextView) convertView.findViewById(R.id.textViewCity);
    holder.textView.setTextIsSelectable(true);
    holder.textView.setText(rowItem.m_city);
    convertView.setTag(holder);
    }
  else
    holder = (ViewHolder) convertView.getTag();

  return convertView;
  }
}