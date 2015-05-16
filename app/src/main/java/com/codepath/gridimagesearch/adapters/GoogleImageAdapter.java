package com.codepath.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.GoogleImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoogleImageAdapter extends ArrayAdapter<GoogleImage> {
  private static class ViewHolder {
    ImageView itemImageView;
  }

  public GoogleImageAdapter(Context context, List<GoogleImage> objects) {
    super(context, R.layout.image_view, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    GoogleImage item = getItem(position);
    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.image_view, parent, false);
      viewHolder.itemImageView = (ImageView) convertView.findViewById(R.id.itemImageView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    GridView gridView = (GridView) parent;
    double scaledHeight = 0;
    int minIndex = position / gridView.getNumColumns();
    for (int i = minIndex; i < minIndex + gridView.getNumColumns(); i++) {
      GoogleImage itemAtIndex = getItem(i);
      scaledHeight = Math.max(scaledHeight, (itemAtIndex.thumb.height * gridView.getColumnWidth()) / itemAtIndex.thumb.width);
    }
    int intScaledWidth = (int)(item.thumb.width * scaledHeight / item.thumb.height);
    int intScaledHeight = (int)scaledHeight;
    viewHolder.itemImageView.getLayoutParams().height = intScaledHeight;
    Picasso.with(getContext()).load(item.thumb.url).resize(intScaledWidth, intScaledHeight).centerInside().placeholder(R.drawable.image).into(viewHolder.itemImageView);
    return convertView;
  }
}
