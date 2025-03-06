package com.microecommerce.fileservice.controllers;

import java.io.IOException;

import com.microecommerce.dtoslibrary.storage_service.FileDTO;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
import org.springframework.http.HttpHeaders;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.mapper.FileMapper;
import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.service.FileService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;

@RestController
@RequestMapping("/files")
public class FileController {
    
    private final FileService fileService;
    private final FileMapper fileMapper;

    public FileController(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    /**
     * Uploads a new file.
     *
     * @param file The file to upload.
     * @param name The desired filename for the uploaded file.
     * @return The StoredFile object representing the uploaded file.
     */
    @PostMapping
    public ResponseEntity<Object> uploadFile(
        @RequestParam(value = "file") MultipartFile file,
        @RequestParam(value = "name") String name,
        @RequestParam(value = "parentId", required = false) Long parentId) throws IOException, NoSuchAlgorithmException, EntityNotFoundException, InvalidEntityException {

        MetadataFile savedFile = fileService.createFile(file, name, parentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
    }

    /**
     * Updates an existing file.
     *
     * @param id The ID of the file to update.
     * @param file The new file data.
     * @return The updated StoredFile object.
     */
    @PatchMapping("/{id}/binary")
    public ResponseEntity<Object> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException, EntityNotFoundException, InvalidEntityException {
        MetadataFile savedFile = fileService.updateFileContent(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
    }

    /**
     * Renames an existing file.
     *
     * @param id The ID of the file to rename.
     * @param updatedFileInfo The new filename for the file.
     * @return The updated StoredFile object.
     */
    @PatchMapping("/{id}/metadata")
    public ResponseEntity<Object> uploadFile(@PathVariable Long id, @RequestBody FileDTO updatedFileInfo) throws EntityNotFoundException, InvalidEntityException, InvalidActionException {
        MetadataFile savedFile = null;

        if(updatedFileInfo.getFileName() != null)
            savedFile = fileService.renameFile(id, updatedFileInfo.getFileName());

        if(updatedFileInfo.getParentId() != null)
            savedFile = fileService.setDir(id, updatedFileInfo.getParentId());

        if(savedFile == null)
            throw new InvalidActionException("No valid action was provided.");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(savedFile));
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return The StoredFile object with the given ID.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @GetMapping("/{id}/metadata")
    public ResponseEntity<Object> getFileInfo(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(fileMapper.toDto(fileService.getFileById(id)));
    }

    /**
     * Retrieves the binary content of a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return A ResponseEntity containing the binary content of the file.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @GetMapping("/{id}/binary")
    public ResponseEntity<Object> getFileBinary(@PathVariable Long id) throws EntityNotFoundException {
        MetadataFile file = fileService.getFileById(id);
        ByteArrayResource resource = new ByteArrayResource(file.getFile().getBytes());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFile().getContentType().getMimeType()))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getFileName())
                                .build()
                                .toString()
                )
                .body(resource);
    }

    /**
     * Retrieves the base64 encoded content of a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return A ResponseEntity containing the base64 encoded content of the file.
     * @throws EntityNotFoundException If no file with the given ID is found.
     */
    @GetMapping("/{id}/base64")
    public ResponseEntity<Object> getFileBase64(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(fileService.getFileById(id).getFile().getBase64Content());
    }

    /**
     * Deletes a file or directory.
     *
     * @param id The ID of the file or directory to delete.
     * @return The deleted MetadataFile object.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFileOrDir(@PathVariable Long id) throws InvalidActionException, EntityNotFoundException {
        MetadataFile deletedFile = fileService.deleteFileOrDir(id);
        return ResponseEntity.ok(fileMapper.toDto(deletedFile));
    }

    /**
     * Creates a new directory.
     *
     * @param name The name of the directory.
     * @param parentId The ID of the parent directory.
     * @return The created MetadataFile object representing the directory.
     */
    @PostMapping("/directory")
    public ResponseEntity<Object> createDir(@RequestParam("name") String name, @RequestParam(value = "parentId", required = false) Long parentId) throws EntityNotFoundException {
        MetadataFile createdDir = fileService.createDir(name, parentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileMapper.toDto(createdDir));
    }
}
