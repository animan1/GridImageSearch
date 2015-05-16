package com.codepath.gridimagesearch.models;

import java.io.Serializable;

public class ImageModel implements Serializable {
  public final String url;
  public final int width;
  public final int height;

  public ImageModel(String url, int width, int height) {
    this.url = url;
    this.width = width;
    this.height = height;
  }
}
