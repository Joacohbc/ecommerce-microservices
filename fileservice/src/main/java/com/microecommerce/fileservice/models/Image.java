package com.microecommerce.fileservice.models;

public interface Image {
    String getAltText();
    byte[] getOptimized();
    byte[] getThumbnail();
    String getOptimizedBase64();
    String getThumbnailBase64();
    int getWidth();
    int getHeight();
}
