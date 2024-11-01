package com.microecommerce.fileservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microecommerce.fileservice.models.MetadataFile;

@Repository
public interface MetadataFileRepository extends JpaRepository<MetadataFile, Long> {
    
}
