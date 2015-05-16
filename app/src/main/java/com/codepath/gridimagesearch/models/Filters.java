package com.codepath.gridimagesearch.models;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Filters implements Serializable {
  public static final LinkedHashMap<String, String> SIZES = new LinkedHashMap<>();
  public static final ArrayList<String> SIZE_DISPLAY;
  public static final ArrayList<String> SIZE_VALUES;

  static {
    SIZES.put("Any", "");
    SIZES.put("Small", "small");
    SIZES.put("Medium", "medium");
    SIZES.put("Large", "large");
    SIZES.put("Extra Large", "xlarge");
    SIZE_DISPLAY = new ArrayList<>(SIZES.keySet());
    SIZE_VALUES = new ArrayList<>(SIZES.values());
  }

  public final String size;

  public Filters() {
    this("");
  }

  public Filters(String size) {
    this.size = size.isEmpty() ? null : size;
  }

  public RequestParams getParams() {
    RequestParams params = new RequestParams();
    if (size != null) {
      params.put("imgsz", size);
    }
    return params;
  }
}
