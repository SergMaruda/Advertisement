package com.example.sergey.advertisement;

/**
 * Created by Sergey on 03.05.2015.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewRegionAdapter extends ArrayAdapter<RowItemRegion> {

Context context;

public CustomListViewRegionAdapter(Context context, int resourceId, List<RowItemRegion> items)
  {
  super(context, resourceId, items);
  this.context = context;
  }

/*private view holder class*/
private class ViewHolder
  {
  TextView textViewRegion;
  }

public View getView(int position, View convertView, ViewGroup parent)
  {
  ViewHolder holder = null;
  RowItemRegion rowItem = getItem(position);

  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item_region, null);
    holder = new ViewHolder();
    holder.textViewRegion = (TextView) convertView.findViewById(R.id.textViewRegion);

    convertView.setTag(holder);
    }
  else
    holder = (ViewHolder) convertView.getTag();

  holder.textViewRegion.setTextIsSelectable(true);
  holder.textViewRegion.setText(rowItem.m_region);

  return convertView;
  }
}