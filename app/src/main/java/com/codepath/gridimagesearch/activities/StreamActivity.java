package com.codepath.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.GoogleImageAdapter;
import com.codepath.gridimagesearch.fragments.FiltersDialog;
import com.codepath.gridimagesearch.models.Filters;
import com.codepath.gridimagesearch.models.GoogleImage;

import java.util.ArrayList;


public class StreamActivity extends ActionBarActivity implements FiltersDialog.Listener {

  private abstract class ErrorResponseHandler implements GoogleImage.ResponseHandler {
    @Override
    public void onFailure(int page, String error) {
      showError(error);
    }
  }

  private GoogleImageAdapter searchGridAdapter;
  private GridView searchGridView;
  private String query = null;
  private Filters filters = new Filters();
  private EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
    @Override
    public void onLoadMore(int page, int totalItemsCount) {
      loadPage(page);
    }
  };

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
    searchGridView.setOnScrollListener(endlessScrollListener);
  }

  private void onImageClicked(GoogleImage item) {
    Intent intent = new Intent(this, ViewImageActivity.class);
    intent.putExtra(ViewImageActivity.IMAGE, item.full);
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

  private Boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }

  private void showError(String error) {
    if (error == null) {
      error = "There was a problem retrieving results.";
    }
    Toast.makeText(StreamActivity.this, error, Toast.LENGTH_SHORT).show();
  }

  private void onSearch(String query) {
    if (!isNetworkAvailable()) {
      showError("No internet connection available");
      return;
    }

    this.query = query;
    if (this.query == null || this.query.isEmpty()) {
      return;
    }
    searchGridView.smoothScrollToPosition(0);
    endlessScrollListener.reset();
    GoogleImage.search(query, filters, 0, new ErrorResponseHandler() {
      @Override
      public void onSuccess(int page, ArrayList<GoogleImage> photos) {
        searchGridAdapter.clear();
        searchGridAdapter.addAll(photos);
        searchGridAdapter.notifyDataSetChanged();
      }
    });
  }

  private void loadPage(final int page) {
    if (!isNetworkAvailable()) {
      showError("No internet connection available");
      return;
    }

    GoogleImage.search(query, filters, page, new ErrorResponseHandler() {
      @Override
      public void onSuccess(int page, ArrayList<GoogleImage> photos) {
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
