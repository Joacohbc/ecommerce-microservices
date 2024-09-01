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

    @PostMapping
    public ResponseEntity<StoredFile> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "name", required = true) String name) throws IOException, NoSuchAlgorithmException {
        
        StoredFile savedFile = fileService.storeFile(file, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoredFile> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        StoredFile savedFile = fileService.updateFile(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoredFile> uploadFile(@PathVariable Long id, @RequestBody Map<String, String> file) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        StoredFile savedFile = fileService.renameFile(id, file.get("name"));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoredFile> getFile(@PathVariable Long id) throws EntityNotFoundException {
        StoredFile storedFile = fileService.getFileById(id);
        return ResponseEntity.ok(storedFile);
    }
}
