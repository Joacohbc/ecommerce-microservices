package com.microecommerce.fileservice.models;

import java.io.Serializable;

import org.apache.http.entity.ContentType;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class StoredFile implements Serializable {
    @Id
    String hash;
    String extension;
    String filePath;
    String originalFilename;
    ContentType contentType;
    Long size;

    @Basic(fetch = FetchType.LAZY)
    String base64Content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] bytes;
}
