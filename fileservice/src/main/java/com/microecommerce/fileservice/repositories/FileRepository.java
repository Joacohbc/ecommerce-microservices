package com.microecommerce.fileservice.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.microecommerce.fileservice.models.StoredFile;

@Repository
public interface FileRepository extends JpaRepository<StoredFile, Long> {
    Optional<StoredFile> findByHash(String hash);
}
