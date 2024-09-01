package com.microecommerce.fileservice.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.fileservice.repositories.FileRepository;

import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;


@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private HashService hashService;

    /**
     * Loads file data from a MultipartFile.
     *
     * @param multipartFile The MultipartFile to load data from.
     * @return A StoredFile object populated with data from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     */
    private StoredFile loadFileData(MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException {
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

    private StoredFile createDummy(Long originalId, String name, String fileHash, boolean insertDummy) {
        StoredFile file = new StoredFile();
        file.setFileName(name);
        file.markAsDummy(originalId, fileHash);
        return insertDummy ? fileRepository.save(file) : file;
    }

    public StoredFile storeFile(MultipartFile multipartFile, String name) throws IOException, NoSuchAlgorithmException {
        String fileHash = hashService.calculateFileHash(multipartFile);
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);

        if (existingFile.isPresent()) {
            return createDummy(existingFile.get().getId(), name, fileHash, true);
        } else {
            StoredFile file = loadFileData(multipartFile);
            file.setFileName(name);
            file.setHash(fileHash);
            return fileRepository.save(file);
        }
    }

    public StoredFile updateFile(Long id, MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        if(multipartFile == null || multipartFile.isEmpty()) return null;

        String fileHash = hashService.calculateFileHash(multipartFile);
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);
        if(existingFile.isPresent()) {
            return fileRepository.save(createDummy(id, fileHash, existingFile.get().getFileName(), false));
        }

        StoredFile savedFile = fileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("File not found"));
        
        StoredFile newInfo = loadFileData(multipartFile);
        // Override the specific info 
        savedFile.setOriginalFilename(newInfo.getOriginalFilename());
        savedFile.setExtension(newInfo.getExtension());
        savedFile.setContentType(newInfo.getContentType());
        savedFile.setSize(newInfo.getSize());
        savedFile.setBytes(newInfo.getBytes());
        savedFile.setBase64Content(newInfo.getBase64Content());
        savedFile.setHash(fileHash);

        return fileRepository.save(savedFile);
    }

    public StoredFile renameFile(Long id, String name) throws EntityNotFoundException {
        StoredFile file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        file.setFileName(name);
        return fileRepository.save(file);
    }

    public StoredFile getFileById(Long id) throws EntityNotFoundException {
        return fileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("File not found"));
    }
}
