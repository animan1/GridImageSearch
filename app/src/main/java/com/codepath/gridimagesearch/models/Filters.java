package com.codepath.gridimagesearch.models;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Filters implements Serializable {
  public static final LinkedHashMap<String, String> SIZES = new LinkedHashMap<>();
  public static final ArrayList<String> SIZE_DISPLAY;
  public static final ArrayList<String> SIZE_VALUES;
  public static final LinkedHashMap<String, String> COLORS = new LinkedHashMap<>();
  public static final ArrayList<String> COLOR_DISPLAY;
  public static final ArrayList<String> COLOR_VALUES;
  public static final LinkedHashMap<String, String> TYPES = new LinkedHashMap<>();
  public static final ArrayList<String> TYPE_DISPLAY;
  public static final ArrayList<String> TYPE_VALUES;

  static {
    SIZES.put("Any", "");
    SIZES.put("Small", "small");
    SIZES.put("Medium", "medium");
    SIZES.put("Large", "large");
    SIZES.put("Extra Large", "xlarge");
    SIZE_DISPLAY = new ArrayList<>(SIZES.keySet());
    SIZE_VALUES = new ArrayList<>(SIZES.values());

    COLORS.put("Any", "");
    COLORS.put("Black", "black");
    COLORS.put("Blue", "blue");
    COLORS.put("Brown", "brown");
    COLORS.put("Gray", "gray");
    COLORS.put("Green", "green");
    COLORS.put("Orange", "orange");
    COLORS.put("Pink", "pink");
    COLORS.put("Purple", "purple");
    COLORS.put("Red", "red");
    COLORS.put("Teal", "teal");
    COLORS.put("White", "white");
    COLORS.put("Yellow", "yellow");
    COLOR_DISPLAY = new ArrayList<>(COLORS.keySet());
    COLOR_VALUES = new ArrayList<>(COLORS.values());

    TYPES.put("Any", "");
    TYPES.put("Face", "face");
    TYPES.put("Photo", "photo");
    TYPES.put("Clip Art", "clipart");
    TYPES.put("Line Art", "lineart");
    TYPE_DISPLAY = new ArrayList<>(TYPES.keySet());
    TYPE_VALUES = new ArrayList<>(TYPES.values());
  }

  public final String size;
  public final String color;
  public final String type;
  public final String site;

  public Filters() {
    this("", "", "", "");
  }

  public Filters(String size, String color, String type, String site) {
    this.size = size.isEmpty() ? null : size;
    this.color = color.isEmpty() ? null : color;
    this.type = type.isEmpty() ? null : type;
    this.site = site.isEmpty() ? null : site;
  }

  public RequestParams getParams() {
    RequestParams params = new RequestParams();
    if (size != null) {
      params.put("imgsz", size);
    }
    if (color != null) {
      params.put("imgcolor", color);
    }
    if (type != null) {
      params.put("imgtype", type);
    }
    if (site != null) {
      params.put("as_sitesearch", site);
    }
    return params;
  }
}
