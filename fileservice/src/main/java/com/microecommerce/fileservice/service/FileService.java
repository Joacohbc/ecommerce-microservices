package com.microecommerce.fileservice.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import com.microecommerce.fileservice.validations.FileValidations;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microecommerce.fileservice.models.MetadataFile;
import com.microecommerce.fileservice.models.StoredFile;
import com.microecommerce.fileservice.repositories.MetadataFileRepository;
import com.microecommerce.fileservice.repositories.StoredFileRepository;

import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;


@Service
public class FileService {

    @Autowired
    private StoredFileRepository fileRepository;

    @Autowired
    private MetadataFileRepository metadataFileRepository;

    @Autowired
    private HashService hashService;

    @Autowired
    private FileValidations fileValidations;

    /**
     * Loads file data from a MultipartFile into a StoredFile and return it.
     * - Don't calculate the hash. 
     * - Don't insert the StoredFile into the database. 
     *
     * @param multipartFile The MultipartFile to load data from.
     * @return A StoredFile object populated with data from the MultipartFile.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws InvalidEntityException If the file data is invalid.
     */
    private StoredFile loadStoredFile(MultipartFile multipartFile) throws IOException, InvalidEntityException {
        StoredFile file = new StoredFile();

        // Validate file data
        fileValidations.validateFileExtension(multipartFile.getOriginalFilename());
        fileValidations.validateFileName(multipartFile.getOriginalFilename());
        fileValidations.validateFileContentType(multipartFile.getContentType());
        fileValidations.validateFileContent(multipartFile.getBytes());

        // Populate file data
        file.setOriginalFilename(multipartFile.getOriginalFilename());
        file.setContentType(ContentType.parse(multipartFile.getContentType()));
        file.setSize(multipartFile.getSize());
        file.setBytes(multipartFile.getBytes());
        file.setBase64Content(Base64.getEncoder().encodeToString(multipartFile.getBytes()));

        // Extract extension from original filename
        int lastDotIndex = file.getOriginalFilename().lastIndexOf('.');
        if (lastDotIndex > 0) {
            file.setExtension(file.getOriginalFilename().substring(lastDotIndex + 1));
        }

        return file;
    }
    
    /**
     * Load a MetadataFile information from a StoredFile object. Don't insert the MetadataFile into the database.
     * @param storeId The ID of the StoredFile object to create the MetadataFile from.
     * @param name The name of the file.
     * @param parentId The ID of the parent MetadataFile object. If null, the file is a root file.
     * @return The MetadataFile object created from the StoredFile object.
     * @throws EntityNotFoundException If no file with the given ID is found.
     * @throws EntityNotFoundException If no parent file with the given ID is found.
     * @throws InvalidEntityException If the file name is invalid.
     */
    private MetadataFile loadMetadataInfo(Long storeId, String name, Long parentId) throws EntityNotFoundException, InvalidEntityException {
        fileValidations.validateFileName(name);

        MetadataFile metadata = new MetadataFile();
        metadata.setFile(fileRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("File not found")));
        metadata.setFileName(name);

        if (parentId != null) {
            metadata.setParent(metadataFileRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent file not found")));
        }
        metadata.setDir(false);
        return metadata;
    }

    /**
     * Stores a temporary file in the database. If the file already exists, it will be returned.
     * @param multipartFile The MultipartFile to store.
     * @return The StoredFile object representing the stored file.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public StoredFile createStoreFile(MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException, InvalidEntityException {
        String fileHash = hashService.calculateFileHash(multipartFile.getBytes());
        Optional<StoredFile> existingFile = fileRepository.findByHash(fileHash);
        if (existingFile.isPresent()) {
            return existingFile.get();
        }

        StoredFile file = loadStoredFile(multipartFile);
        file.setHash(fileHash);
        return fileRepository.save(file);
    }

    /**
     * Creates a new file with the provided metadata and stores it in the database.
     *
     * @param multipartFile The MultipartFile to be stored.
     * @param name The name of the file.
     * @param parentId The ID of the parent directory. If null, the file is a root file.
     * @return The MetadataFile object representing the stored file.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If the file or parent directory is not found.
     * @throws InvalidEntityException If the file name is invalid.
     */
    public MetadataFile createFile(MultipartFile multipartFile, String name, Long parentId) throws IOException, NoSuchAlgorithmException, EntityNotFoundException, InvalidEntityException {
        fileValidations.validateFileName(name);

        StoredFile storedFile = createStoreFile(multipartFile);
        MetadataFile metadataFile = loadMetadataInfo(
            storedFile.getId(),
            name,
            parentId
        );

        return metadataFileRepository.save(metadataFile);
    }

    /**
     * Updates an existing file with new data from a MultipartFile.
     *
     * @param id The ID of the file to update.
     * @param multipartFile The new MultipartFile data to update the file with.
     * @return The updated MetadataFile object.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If the file with the given ID is not found.
     * @throws InvalidEntityException If the file data is invalid or the file is empty.
     */
    public MetadataFile updateFile(Long id, MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException, EntityNotFoundException, InvalidEntityException {
        if (multipartFile == null || multipartFile.isEmpty()) throw new InvalidEntityException("File cannot be empty");
        MetadataFile metadataFile = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        metadataFile.setFile(createStoreFile(multipartFile));
        return metadataFileRepository.save(metadataFile);
    }

    /**
     * Renames an existing file.
     *
     * @param id The ID of the file to rename.
     * @param name The new name for the file.
     * @return The updated MetadataFile object.
     * @throws EntityNotFoundException If the file with the given ID is not found.
     * @throws InvalidEntityException If the new name is empty.
     */
    public MetadataFile renameFile(Long id, String name) throws EntityNotFoundException, InvalidEntityException {
        fileValidations.validateFileName(name);
        MetadataFile file = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        file.setFileName(name);
        return metadataFileRepository.save(file);
    }
    
    /**
     * Deletes a file or directory.
     *
     * @param id The ID of the file or directory to delete.
     * @return The deleted MetadataFile object.
     * @throws EntityNotFoundException If the file or directory with the given ID is not found.
     * @throws InvalidActionException If the directory is not empty.
     */
    public MetadataFile deleteFileOrDir(Long id) throws EntityNotFoundException, InvalidActionException {
        MetadataFile file = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));

        if(file.isDir() && !file.getChildren().isEmpty()) {
            throw new InvalidActionException("Directory is not empty");
        }

        metadataFileRepository.delete(file);
        return file;
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
        metadata.setParent(metadataFileRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent file not found")));
        metadata.setDir(true);
        return metadataFileRepository.save(metadata);
    }

    /**
     * Sets the parent directory for a file.
     *
     * @param id The ID of the file to set the parent directory for.
     * @param parentId The ID of the parent directory.
     * @return The updated MetadataFile object.
     * @throws EntityNotFoundException If the file or parent directory with the given ID is not found.
     * @throws InvalidActionException If the file is a directory or the parent is not a directory.
     */
    public MetadataFile setDir(Long id, Long parentId) throws EntityNotFoundException, InvalidActionException {
        MetadataFile file = metadataFileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        MetadataFile parent = metadataFileRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        if(file.isDir()) {
            throw new InvalidActionException("Cannot move a directory");
        }

        if(!parent.isDir()) {
            throw new InvalidActionException("Parent must be a directory");
        }

        metadataFileRepository.save(file);
        return file;
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
