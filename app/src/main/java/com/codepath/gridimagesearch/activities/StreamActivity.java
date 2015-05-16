package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.GoogleImageAdapter;
import com.codepath.gridimagesearch.fragments.FiltersDialog;
import com.codepath.gridimagesearch.models.Filters;
import com.codepath.gridimagesearch.models.GoogleImage;

import java.util.ArrayList;


public class StreamActivity extends ActionBarActivity implements FiltersDialog.Listener {

  private GoogleImageAdapter searchGridAdapter;
  private GridView searchGridView;
  private String query = null;
  private Filters filters = new Filters();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stream);
    initGrid();
  }

  private void initGrid() {
    searchGridAdapter = new GoogleImageAdapter(this, new ArrayList<GoogleImage>());
    searchGridView = (GridView) findViewById(R.id.searchGridView);
    searchGridView.setAdapter(searchGridAdapter);
    searchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onImageClicked(searchGridAdapter.getItem(position));
      }
    });
  }

  private void onImageClicked(GoogleImage item) {
    Intent intent = new Intent(this, ViewImageActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    initSearch(menu);
    initFilter(menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void initFilter(Menu menu) {
    MenuItem filterItem = menu.findItem(R.id.action_filter);
    filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        showFiltersDialog();
        return true;
      }
    });
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
    this.query = query;
    if (this.query == null || this.query.isEmpty()) {
      return;
    }
    GoogleImage.search(query, filters, new GoogleImage.ResponseHandler() {
      @Override
      public void onSuccess(ArrayList<GoogleImage> photos) {
        searchGridView.smoothScrollToPosition(0);
        searchGridAdapter.clear();
        searchGridAdapter.addAll(photos);
        searchGridAdapter.notifyDataSetChanged();
      }
    });
  }

  private void showFiltersDialog() {
    FragmentManager fm = getSupportFragmentManager();
    FiltersDialog dialog = FiltersDialog.newInstance(this.filters);
    dialog.show(fm, "filters_dialog");
  }

  @Override
  public void onFilterDialogFinish(Filters filters) {
    this.filters = filters;
    onSearch(this.query);
  }
}
