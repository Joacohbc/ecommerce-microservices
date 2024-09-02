package com.microecommerce.dtoslibrary.storage_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    Long id;
    String fileName;
    String hash;
    String extension;
    String filePath;
    String originalFilename;
    String contentType;
    Long size;
    String base64Content;
    byte[] bytes;
}
