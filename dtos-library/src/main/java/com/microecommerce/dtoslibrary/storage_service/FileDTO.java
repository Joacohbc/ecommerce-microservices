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
    String extension;
    String contentType;
    Long size;
    Long parentId;
    String parentFileName;
    boolean isDir;
}
