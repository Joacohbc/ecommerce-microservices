package com.microecommerce.fileservice.models;

public interface Video {
    Double getDuration();
    String getAltText();
    byte[] getOptimized();
    byte[] getThumbnail();
    String getOptimizedBase64();
    String getThumbnailBase64();
    int getWidth();
    int getHeight();
}
