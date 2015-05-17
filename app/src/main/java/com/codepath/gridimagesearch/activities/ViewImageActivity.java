package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewImageActivity extends ActionBarActivity {
  public static final String IMAGE = "image";
  private ShareActionProvider miShareAction;
  private ImageView fullScreenImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
    fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
    ImageModel imageModel = (ImageModel) getIntent().getSerializableExtra(IMAGE);
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    Picasso.with(this)
        .load(imageModel.url)
        .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
        .centerInside()
        .placeholder(R.drawable.image)
        .into(fullScreenImageView, new Callback() {
          @Override
          public void onSuccess() {
            setupShareIntent();
          }

          @Override
          public void onError() {
          }
        });
  }

  public Uri getLocalBitmapUri(ImageView imageView) {
    // Extract Bitmap from ImageView drawable
    Drawable drawable = imageView.getDrawable();
    Bitmap bmp;
    if (drawable instanceof BitmapDrawable){
      bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    } else {
      return null;
    }
    // Store image to default external storage directory
    Uri bmpUri = null;
    try {
      File file =  new File(Environment.getExternalStoragePublicDirectory(
          Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
      file.getParentFile().mkdirs();
      FileOutputStream out = new FileOutputStream(file);
      bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.close();
      bmpUri = Uri.fromFile(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmpUri;
  }

  private void setupShareIntent() {
    Uri bmpUri = getLocalBitmapUri(fullScreenImageView);
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
    shareIntent.setType("image/*");
    miShareAction.setShareIntent(shareIntent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.view_image_menu, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);
    miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    // clear any intent that might be set from a previous image
    miShareAction.setShareIntent(null);
    return super.onCreateOptionsMenu(menu);
  }
}
