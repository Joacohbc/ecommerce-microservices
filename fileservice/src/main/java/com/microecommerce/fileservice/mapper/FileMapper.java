package com.microecommerce.fileservice.mapper;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.dtoslibrary.storage_service.FileDTO;

@Service
public class FileMapper {

    public FileDTO toDto(MetadataFile file) {
        StoredFile storedFile = file.getFile();
        return FileDTO.builder()
            .id(file.getId())
            .fileName(file.getFileName())
            .hash(storedFile.getHash())
            .extension(storedFile.getExtension())
            .filePath(storedFile.getFilePath())
            .originalFilename(storedFile.getOriginalFilename())
            .contentType(storedFile.getContentType().toString())
            .size(storedFile.getSize())
            .base64Content(storedFile.getBase64Content())
            .bytes(storedFile.getBytes())
            .build();
    }

    public MetadataFile toEntity(FileDTO fileDTO) {
        StoredFile storedFile = new StoredFile();
        storedFile.setHash(fileDTO.getHash());
        storedFile.setExtension(fileDTO.getExtension());
        storedFile.setFilePath(fileDTO.getFilePath());
        storedFile.setOriginalFilename(fileDTO.getOriginalFilename());
        storedFile.setContentType(ContentType.parse(fileDTO.getContentType()));
        storedFile.setSize(fileDTO.getSize());
        storedFile.setBase64Content(fileDTO.getBase64Content());
        storedFile.setBytes(fileDTO.getBytes());

        return new MetadataFile(
            fileDTO.getId(),
            fileDTO.getFileName(),
            storedFile
        );
    }
}
