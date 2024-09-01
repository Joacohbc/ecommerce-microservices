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

@Service
public class FileService {

    @Autowired
    private StoredFileRepository fileRepository;

    @Autowired
    private MetadataFileRepository metadataFileRepository;

    @Autowired
    private HashService hashService;

    /**
     * Loads file data from a MultipartFile. Don't calculate the hash.
     *
     * @param multipartFile The MultipartFile to load data from.
     * @return A StoredFile object populated with data from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     */
    private StoredFile createStoredFile(MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException {
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

    public MetadataFile storeFile(MultipartFile multipartFile, String name) throws IOException, NoSuchAlgorithmException {
        String fileHash = hashService.calculateFileHash(multipartFile);
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);

        if (existingFile.isPresent()) {
            MetadataFile file = new MetadataFile();
            file.setFile(existingFile.get());
            file.setFileName(name);
            return metadataFileRepository.save(file);
        } else {
            StoredFile file = createStoredFile(multipartFile);
            file.setHash(fileHash);
            fileRepository.save(file);

            MetadataFile metadataFile = new MetadataFile();
            metadataFile.setFile(file);
            metadataFile.setFileName(name);
            return metadataFileRepository.save(metadataFile); 
        }
    }

    public MetadataFile updateFile(Long id, MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        if(multipartFile == null || multipartFile.isEmpty()) return null;

        MetadataFile metadataFile = metadataFileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("File not found"));
        
        // Check if have a file identical already saved (so change the reference to that file)
        String fileHash = hashService.calculateFileHash(multipartFile);
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);
        if(existingFile.isPresent()) {
            metadataFile.setFile(existingFile.get());
            return metadataFileRepository.save(metadataFile); 
        }
        
        StoredFile newInfo = createStoredFile(multipartFile); 
        newInfo.setOriginalFilename(newInfo.getOriginalFilename());
        newInfo.setExtension(newInfo.getExtension());
        newInfo.setContentType(newInfo.getContentType());
        newInfo.setSize(newInfo.getSize());
        newInfo.setBytes(newInfo.getBytes());
        newInfo.setBase64Content(newInfo.getBase64Content());
        newInfo.setHash(fileHash);
        fileRepository.save(newInfo);

        metadataFile.setFile(newInfo);
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
