package com.microecommerce.fileservice.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.fileservice.repositories.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private HashService hashService;

    public StoredFile storeFile(MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException {
        String fileHash = hashService.calculateFileHash(multipartFile);

        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);

        if (existingFile.isPresent()) {
            return existingFile.get();
        } else {
            StoredFile file = new StoredFile();
            file.setOriginalFilename(multipartFile.getOriginalFilename());
            // file.setContentType(multipartFile.getContentType());
            file.setSize(multipartFile.getSize());
            file.setBytes(multipartFile.getBytes());
            file.setHash(fileHash);

            // Extract extension from original filename
            if (file.getOriginalFilename() != null) {
                int lastDotIndex = file.getOriginalFilename().lastIndexOf('.');
                if (lastDotIndex > 0) {
                    file.setExtension(file.getOriginalFilename().substring(lastDotIndex + 1));
                }
            }

            return fileRepository.save(file);
        }
    }

    public StoredFile getFileById(Long id) {
        return fileRepository.findById(id).orElseThrow();
    }
}
