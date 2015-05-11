package activities;

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

import com.example.sergey.advertisement.R;

import java.util.List;

import activities.Filter;

public class ListViewFiltersAdapter extends ArrayAdapter<Filter>
  {
  Context context;

  private class ViewHolder
    {
    TextView textView;
    ImageView imageView;
    }

  public ListViewFiltersAdapter(Context context, int resourceId, List<Filter> items)
    {
    super(context, resourceId, items);
    this.context = context;
    }
}