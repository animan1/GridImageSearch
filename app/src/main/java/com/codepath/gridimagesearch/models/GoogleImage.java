package com.codepath.gridimagesearch.models;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class GoogleImage {

  public interface ResponseHandler {
    void onSuccess(int page, ArrayList<GoogleImage> photos);
    void onFailure(int page, String error);
  }

  private static AsyncHttpClient client = new AsyncHttpClient();
  private static final int PAGE_SIZE = 8;
  private static final Random TEST_FAILURE_MODES = null;

  public final ImageModel full;
  public final ImageModel thumb;

  public GoogleImage(ImageModel full, ImageModel thumb) {
    this.full = full;
    this.thumb = thumb;
  }

  public static void search(String query, Filters filters, final int page, final ResponseHandler handler) {
    String url = "https://ajax.googleapis.com/ajax/services/search/images";
    RequestParams params = filters.getParams();
    params.put("q", query);
    params.put("v", "1.0");
    params.put("rsz", PAGE_SIZE);
    params.put("start", page * PAGE_SIZE);

    if (TEST_FAILURE_MODES != null && TEST_FAILURE_MODES.nextBoolean()) {
      Log.e("failure_mode_test", "Error response failure mode");
      params = new RequestParams();
    }

    client.get(url, params, new JsonHttpResponseHandler() {
      final JSONObject EMPTY_OBJECT = new JSONObject();
      final JSONArray EMPTY_ARRAY = new JSONArray();

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        int responseStatus = response.optInt("responseStatus", 0);
        if (responseStatus != 200) {
          handler.onFailure(page, response.optString("responseDetails"));
          return;
        }
        JSONObject responseDataObject = getObject(response, "responseData");
        JSONArray resultsArray = getArray(responseDataObject, "results");
        final ArrayList<GoogleImage> imageArray = new ArrayList<>(resultsArray.length());
        for (int index = 0; index < resultsArray.length(); index++) {
          JSONObject resultObject = resultsArray.optJSONObject(index);
          if (resultObject == null) {
            continue;
          }

          String url = resultObject.optString("url");
          int width = resultObject.optInt("width");
          int height = resultObject.optInt("height");
          ImageModel full = new ImageModel(url, width, height);

          String thumbUrl = resultObject.optString("tbUrl");
          int thumbHeight = resultObject.optInt("tbHeight");
          int thumbWidth = resultObject.optInt("tbWidth");
          ImageModel thumb = new ImageModel(thumbUrl, thumbWidth, thumbHeight);

          imageArray.add(new GoogleImage(full, thumb));
        }
        if (TEST_FAILURE_MODES != null && TEST_FAILURE_MODES.nextBoolean()) {
          Log.e("failure_mode_test", "Delayed response failure mode");
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        handler.onSuccess(page, imageArray);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        handler.onFailure(page, null);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        handler.onFailure(page, null);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        handler.onFailure(page, null);
      }

      JSONObject getObject(JSONObject object, String name) {
        JSONObject child = object.optJSONObject(name);
        return child == null ? EMPTY_OBJECT : child;
      }

      JSONArray getArray(JSONObject object, String name) {
        JSONArray array = object.optJSONArray(name);
        return array == null ? EMPTY_ARRAY : array;
      }
    });
  }
}
