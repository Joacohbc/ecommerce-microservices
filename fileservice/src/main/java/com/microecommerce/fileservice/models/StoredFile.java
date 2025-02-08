package com.microecommerce.fileservice.models;

import java.io.Serializable;

import com.microecommerce.utilitymodule.models.TimeStamped;
import org.apache.http.entity.ContentType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class StoredFile extends TimeStamped implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hash;
    private String extension;
    private String filePath;
    private String originalFilename;
    private ContentType contentType;
    private Long size;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "base64Content", columnDefinition = "LONGBLOB")
    private String base64Content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "bytes", columnDefinition = "LONGBLOB")
    private byte[] bytes;
}
