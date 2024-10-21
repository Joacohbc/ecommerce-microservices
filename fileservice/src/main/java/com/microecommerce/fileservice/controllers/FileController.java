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

import com.microecommerce.fileservice.mapper.FileMapper;
import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.service.FileService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;

@RestController
@RequestMapping("/files")
public class FileController {
    
    @Autowired
    private FileService fileService;

    @Autowired
    private FileMapper fileMapper;

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
    public ResponseEntity<Object> uploadFile(
        @RequestParam(value = "file", required = true) MultipartFile file,
        @RequestParam(value = "name", required = true) String name) throws IOException, NoSuchAlgorithmException {
        
        try {
            MetadataFile savedFile = fileService.createFile(file, name);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
    @PutMapping("/{id}/binary")
    public ResponseEntity<Object> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            MetadataFile savedFile = fileService.updateFile(id, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Renames an existing file.
     *
     * @param id The ID of the file to rename.
     * @param updatedFileInfo The new filename for the file.
     * @return The updated StoredFile object.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @PutMapping("/{id}/metadata")
    public ResponseEntity<Object> uploadFile(@PathVariable Long id, @RequestBody Map<String, String> updatedFileInfo) {
        try {
            MetadataFile savedFile = fileService.renameFile(id, updatedFileInfo.get("name"));
            return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
        } catch (EntityNotFoundException | InvalidEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return The StoredFile object with the given ID.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @GetMapping("/{id}/metadata")
    public ResponseEntity<Object> getFileInfo(@PathVariable Long id)  {
        try {
            return ResponseEntity.ok(fileMapper.toDto(fileService.getFileById(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/binary")
    public ResponseEntity<Object> getFileBinary(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fileService.getFileById(id).getFile().getBytes());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/base64")
    public ResponseEntity<Object> getFileBase64(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fileService.getFileById(id).getFile().getBase64Content());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
