package com.example.sergey.advertisement;

import java.util.List;
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

public class ListViewAdvert extends ArrayAdapter<RowItem> {

Context context;

public ListViewAdvert(Context context, int resourceId,
                      List<RowItem> items) {
super(context, resourceId, items);
this.context = context;
}

/*private view holder class*/
private class ViewHolder {
TextView textView;
ImageView imageView;

}

public View getView(int position, View convertView, ViewGroup parent)
  {
  ViewHolder holder;
  RowItem rowItem = getItem(position);

  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
  if (convertView == null)
    {
    convertView = mInflater.inflate(R.layout.list_item, null);
    holder = new ViewHolder();
    holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
    holder.textView = (TextView)convertView.findViewById(R.id.textViewImages);
    convertView.setTag(holder);
    }
  else
    holder = (ViewHolder) convertView.getTag();

  holder.textView.setTextIsSelectable(true);
  if(!rowItem.text.isEmpty())
    {
    holder.textView.setText(rowItem.text);
    }
  else
    holder.textView.setText("");

  if(!rowItem.getImageId().isEmpty())
    {
    Bitmap myBitmap = BitmapFactory.decodeFile(rowItem.getImageId());
    holder.imageView.setImageBitmap(myBitmap);
    }

  return convertView;
  }
}