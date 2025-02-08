package com.microecommerce.fileservice.mapper;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.dtoslibrary.storage_service.FileDTO;

@Service
public class FileMapper {

    public FileDTO toDto(MetadataFile file) {
        if(file.isDir()) {
            return FileDTO.builder()
                .id(file.getId())
                .fileName(file.getFileName())
                .isDir(true)
                .size(0L)
                .extension(null)
                .contentType(null)
                .build();
        }

        StoredFile storedFile = file.getFile();
        return FileDTO.builder()
            .id(file.getId())
            .fileName(file.getFileName())
            .extension(storedFile.getExtension())
            .contentType(storedFile.getContentType().toString())
            .size(storedFile.getSize())
            .parentId(file.getParent() != null ? file.getParent().getId() : null)
            .parentFileName(file.getParent() != null ? file.getParent().getFileName() : null)
            .isDir(false)
            .build();
    }

    public MetadataFile toEntity(FileDTO fileDTO) {
        StoredFile storedFile = new StoredFile();
        storedFile.setId(fileDTO.getId());
        storedFile.setExtension(fileDTO.getExtension());
        storedFile.setContentType(ContentType.parse(fileDTO.getContentType()));
        storedFile.setSize(fileDTO.getSize());

        MetadataFile metadataFile = new MetadataFile();
        metadataFile.setId(fileDTO.getId());
        metadataFile.setFileName(fileDTO.getFileName());
        metadataFile.setFile(storedFile);

        if(fileDTO.getParentId() != null) {
            MetadataFile parent = new MetadataFile();
            parent.setId(fileDTO.getParentId());
            parent.setFileName(fileDTO.getParentFileName());
            metadataFile.setParent(parent);
        }
        
        metadataFile.setDir(fileDTO.isDir());
        return metadataFile;
    }
}
