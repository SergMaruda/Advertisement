package com.example.sergey.advertisement;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import activities.AdvertTitleData;

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
ImageView imageViewSex;
TextView textViewAge;
ImageView imageViewCity;
ImageView imageViewCamera;
ImageView imageViewEye;
TextView textViewEye;
TextView textViewCity;
}

public View getView(int position, View convertView, ViewGroup parent)
  {
  ViewHolder holder;
  RowItemAdvertTitle rowItem = getItem(position);

  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item_advertisement_title, null);
    holder = new ViewHolder();
    holder.textViewDecription = (TextView) convertView.findViewById(R.id.textViewAdvertTitle);
    holder.imageViewSex = (ImageView)convertView.findViewById(R.id.imageViewSex);
    holder.textViewAge = (TextView)convertView.findViewById(R.id.textViewAge);
    holder.textViewCity = (TextView) convertView.findViewById(R.id.textViewCity);
    holder.imageViewCity = (ImageView) convertView.findViewById(R.id.imageViewCity);
    holder.imageViewEye = (ImageView) convertView.findViewById(R.id.imageViewEye);
    holder.textViewEye = (TextView) convertView.findViewById(R.id.textViewEye);
    holder.imageViewCamera = (ImageView) convertView.findViewById(R.id.imageViewCamera);
    convertView.setTag(holder);
    }
  else
    holder = (ViewHolder) convertView.getTag();

  holder.textViewDecription.setText(rowItem.m_description);

  Drawable drw = null;
  if(rowItem.m_sex != AdvertTitleData.ESex.NONE)
    {
    int sex_id;

    if (rowItem.m_sex == AdvertTitleData.ESex.WOMAN)
      sex_id = R.drawable.woman;
    else
      sex_id = R.drawable.man;

    drw = context.getResources().getDrawable(sex_id);
    }
  holder.imageViewSex.setImageDrawable(drw);

  if(rowItem.m_age != 0)
    holder.textViewAge.setText(Integer.toString(rowItem.m_age));

  drw = null;
  if(!rowItem.m_city.isEmpty())
    {
    drw = context.getResources().getDrawable(R.drawable.city);
    holder.textViewCity.setText(rowItem.m_city);
    }
  holder.imageViewCity.setImageDrawable(drw);

  drw = null;
  if(rowItem.m_has_photo)
    drw = context.getResources().getDrawable(R.drawable.camera);

  holder.imageViewCamera.setImageDrawable(drw);

  drw = null;
  if(rowItem.m_num_views > 0)
    {
    holder.textViewEye.setText(Integer.toString(rowItem.m_num_views));
    drw = context.getResources().getDrawable(R.drawable.eye);
    }
  else
    holder.textViewCity.setText("");

  holder.imageViewEye.setImageDrawable(drw);


  return convertView;
  }
}