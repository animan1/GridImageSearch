package com.codepath.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import com.codepath.gridimagesearch.R;

public class ViewImageActivity extends ActionBarActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
    ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
    fullScreenImageView.setImageResource(R.drawable.ic_gear);
  }
}
