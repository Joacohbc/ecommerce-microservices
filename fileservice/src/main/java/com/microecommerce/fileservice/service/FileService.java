package com.microecommerce.fileservice.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.fileservice.repositories.MetadataFileRepository;
import com.microecommerce.fileservice.repositories.StoredFileRepository;

import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;

import jakarta.transaction.Transactional;

@Service
public class FileService {

    @Autowired
    private StoredFileRepository fileRepository;

    @Autowired
    private MetadataFileRepository metadataFileRepository;

    @Autowired
    private HashService hashService;

    /**
     * Loads file data from a MultipartFile. 
     * - Don't calculate the hash. 
     * - Don't insert the StoredFile into the database. 
     *
     * @param multipartFile The MultipartFile to load data from.
     * @return A StoredFile object populated with data from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     */
    private StoredFile loadStoredFile(MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException {
        StoredFile file = new StoredFile();
        file.setOriginalFilename(multipartFile.getOriginalFilename());
        file.setContentType(ContentType.parse(multipartFile.getContentType()));
        file.setSize(multipartFile.getSize());
        file.setBytes(multipartFile.getBytes());
        file.setBase64Content(Base64.getEncoder().encodeToString(multipartFile.getBytes()));

        // Extract extension from original filename
        if (file.getOriginalFilename() != null) {
            int lastDotIndex = file.getOriginalFilename().lastIndexOf('.');
            if (lastDotIndex > 0) {
                file.setExtension(file.getOriginalFilename().substring(lastDotIndex + 1));
            }
        }

        return file;
    }
    
    /**
     * Creates a MetadataFile object from a StoredFile object.
     * @param storeId The ID of the StoredFile object to create the MetadataFile from.
     * @param name The name of the file.
     * @param parentId The ID of the parent MetadataFile object.
     * @param insert Whether to insert the MetadataFile object into the database.
     * @return The MetadataFile object created from the StoredFile object.
     * @throws EntityNotFoundException If no file with the given ID is found.
     * @throws EntityNotFoundException If no parent file with the given ID is found.
     */
    private MetadataFile loadMetadataInfo(Long storeId, String name, Long parentId) throws EntityNotFoundException {
        MetadataFile metadata = new MetadataFile();
        metadata.setFile(fileRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("File not found")));
        metadata.setFileName(name);
        if (parentId != null) {
            metadata.setParentFile(metadataFileRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent file not found")));
        }
        metadata.setDir(false);
        return metadata;
    }

    /**
     * Creates a new MetadataFile object representing a directory.
     * @param name The name of the directory.
     * @param parentId The ID of the parent MetadataFile object.
     * @return The MetadataFile object representing the new directory.
     * @throws EntityNotFoundException If no parent file with the given ID is found.
     */
    public MetadataFile createDir(String name, Long parentId) throws EntityNotFoundException {
        MetadataFile metadata = new MetadataFile();
        metadata.setFileName(name);
        metadata.setParentFile(metadataFileRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent file not found")));
        metadata.setDir(true);
        return metadataFileRepository.save(metadata);
    }

    /**
     * Stores a temporary file in the database. If the file already exists, it will be returned.
     * @param multipartFile The MultipartFile to store.
     * @return The StoredFile object representing the stored file.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public StoredFile createStoreFile(MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException {
        String fileHash = hashService.calculateFileHash(multipartFile.getBytes());
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);
        if (existingFile.isPresent()) {
            return existingFile.get();
        }

        StoredFile file = loadStoredFile(multipartFile);
        file.setHash(fileHash);
        return fileRepository.save(file);
    }

    public MetadataFile createFile(MultipartFile multipartFile, String name) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        StoredFile storedFile = createStoreFile(multipartFile);
        MetadataFile metadataFile = loadMetadataInfo(
            storedFile.getId(), 
            name, 
            null
        );
        return metadataFileRepository.save(metadataFile);
    }

    public MetadataFile updateFile(Long id, MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        if(multipartFile == null || multipartFile.isEmpty()) return null;
        MetadataFile metadataFile = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        metadataFile.setFile(createStoreFile(multipartFile));
        return metadataFileRepository.save(metadataFile);
    }

    public MetadataFile renameFile(Long id, String name) throws EntityNotFoundException, InvalidEntityException {
        if(name.isEmpty()) throw new InvalidEntityException("Name cannot be empty"); 
        MetadataFile file = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        file.setFileName(name);
        return metadataFileRepository.save(file);
    }
    
    /**
     * Retrieves a StoredFile by its ID.
     *
     * @param id The ID of the StoredFile to retrieve.
     * @return The StoredFile object with the given ID.
     * @throws EntityNotFoundException If no StoredFile with the given ID is found.
     */
    public MetadataFile getFileById(Long id) throws EntityNotFoundException {
        return metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
    }
}
