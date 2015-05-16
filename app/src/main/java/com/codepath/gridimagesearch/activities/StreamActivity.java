package com.codepath.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.GoogleImageAdapter;
import com.codepath.gridimagesearch.models.GoogleImage;

import java.util.ArrayList;


public class StreamActivity extends ActionBarActivity {

  private GoogleImageAdapter searchGridAdapter;
  private GridView searchGridView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stream);
    searchGridAdapter = new GoogleImageAdapter(this, new ArrayList<GoogleImage>());
    searchGridView = (GridView) findViewById(R.id.searchGridView);
    searchGridView.setAdapter(searchGridAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    initSearch(menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void initSearch(Menu menu) {
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        onSearch(query);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
  }

  private void onSearch(String query) {
    GoogleImage.search(query, new GoogleImage.ResponseHandler() {
      @Override
      public void onSuccess(ArrayList<GoogleImage> photos) {
        searchGridView.smoothScrollToPosition(0);
        searchGridAdapter.clear();
        searchGridAdapter.addAll(photos);
        searchGridAdapter.notifyDataSetChanged();
      }
    });
  }
}
