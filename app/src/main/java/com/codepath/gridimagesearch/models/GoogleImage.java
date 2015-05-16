package com.codepath.gridimagesearch.models;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleImage {

  public interface ResponseHandler {
    void onSuccess(ArrayList<GoogleImage> photos);
  }

  private static AsyncHttpClient client = new AsyncHttpClient();

  public final ImageModel full;
  public final ImageModel thumb;

  public GoogleImage(ImageModel full, ImageModel thumb) {
    this.full = full;
    this.thumb = thumb;
  }

  public static void search(String query, Filters filters, final ResponseHandler handler) {
    String url = "https://ajax.googleapis.com/ajax/services/search/images";
    RequestParams params = filters.getParams();
    params.put("q", query);
    params.put("v", "1.0");
    params.put("rsz", "8");
    client.get(url, params, new JsonHttpResponseHandler() {
      final JSONObject EMPTY_OBJECT = new JSONObject();
      final JSONArray EMPTY_ARRAY = new JSONArray();

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONObject responseDataObject = getObject(response, "responseData");
        JSONArray resultsArray = getArray(responseDataObject, "results");
        ArrayList<GoogleImage> imageArray = new ArrayList<>(resultsArray.length());
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
          ImageModel thumb = new ImageModel(thumbUrl, thumbWidth, thumbWidth);

          imageArray.add(new GoogleImage(full, thumb));
        }
        handler.onSuccess(imageArray);
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
