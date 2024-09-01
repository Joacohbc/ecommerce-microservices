package com.microecommerce.fileservice.models;

import java.io.Serializable;

import org.apache.http.entity.ContentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoredFile implements Serializable, Video, Image, Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fileName;
    String extension;
    String filePath;
    String originalFilename;
    ContentType contentType;
    String base64Content;
    String hash;

    // Identify the File as "Dummy", because is reference to other file (have the same hash
    // so are the same file)
    @Setter(AccessLevel.NONE)
    boolean dummyFile;
    @Setter(AccessLevel.NONE)
    Long dummyId;
    
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

    @Override
    public byte[] getOptimized() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOptimized'");
    }

    @Override
    public byte[] getThumbnail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThumbnail'");
    }

    @Override
    public String getOptimizedBase64() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOptimizedBase64'");
    }

    @Override
    public String getThumbnailBase64() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThumbnailBase64'");
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWidth'");
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHeight'");
    }

    public void markAsDummy(Long originalId, String hash) {
        this.dummyFile = true;
        this.dummyId = originalId;
        this.hash = hash;
    }

    public void unmarkAsDummy() {
        this.dummyFile = false;
        this.dummyId = null;
        this.hash = null;
    }
}
