package com.codepath.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageModel;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends ActionBarActivity {
  public static final String IMAGE = "image";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
    ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
    ImageModel imageModel = (ImageModel) getIntent().getSerializableExtra(IMAGE);
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    Picasso.with(this)
        .load(imageModel.url)
        .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
        .centerInside()
        .placeholder(R.drawable.image)
        .into(fullScreenImageView);
  }
}
