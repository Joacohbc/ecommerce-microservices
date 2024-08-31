package com.microecommerce.fileservice.models;

import java.io.Serializable;

import org.apache.http.entity.ContentType;
import org.springframework.util.MimeType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoredFile implements Serializable, Video, Image, Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String originalFilename;
    String extension;
    String fileName;
    String filePath;
    MimeType mimeType;
    ContentType contentType;
    String base64Content;
    String hash;

    @Lob
    byte[] bytes;
    long size;
    
    @Override
    public String getAltText() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAltText'");
    }

    @Override
    public Double getDuration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDuration'");
    }
}
