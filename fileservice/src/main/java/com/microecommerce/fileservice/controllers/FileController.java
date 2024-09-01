package com.microecommerce.fileservice.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.fileservice.service.FileService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * Uploads a new file.
     *
     * @param file The file to upload.
     * @param name The desired filename for the uploaded file.
     * @return The StoredFile object representing the uploaded file.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    @PostMapping
    public ResponseEntity<StoredFile> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "name", required = true) String name) throws IOException, NoSuchAlgorithmException {
        
        StoredFile savedFile = fileService.storeFile(file, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    /**
     * Updates an existing file.
     *
     * @param id The ID of the file to update.
     * @param file The new file data.
     * @return The updated StoredFile object.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StoredFile> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        StoredFile savedFile = fileService.updateFile(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    /**
     * Renames an existing file.
     *
     * @param id The ID of the file to rename.
     * @param file The new filename for the file.
     * @return The updated StoredFile object.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StoredFile> uploadFile(@PathVariable Long id, @RequestBody Map<String, String> file) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        StoredFile savedFile = fileService.renameFile(id, file.get("name"));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return The StoredFile object with the given ID.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StoredFile> getFile(@PathVariable Long id) throws EntityNotFoundException {
        StoredFile storedFile = fileService.getFileById(id);
        return ResponseEntity.ok(storedFile);
    }
}
