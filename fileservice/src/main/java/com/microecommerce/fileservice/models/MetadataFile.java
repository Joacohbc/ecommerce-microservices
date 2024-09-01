package com.microecommerce.fileservice.models;

import java.io.Serializable;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataFile implements Serializable, Video, Image, Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fileName;

    @ManyToOne
    StoredFile file;
    
    @Override
    public Double getDuration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDuration'");
    }

    @Override
    public String getAltText() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAltText'");
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
    
}
