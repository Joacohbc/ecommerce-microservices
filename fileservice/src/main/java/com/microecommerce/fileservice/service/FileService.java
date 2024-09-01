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

    /**
     * Creates a dummy StoredFile instance.
     *
     * A dummy file is a file that doesn't store the actual file content, but instead
     * references another StoredFile (the original file) using its ID and hash.
     *
     * @param originalId The ID of the original StoredFile that this dummy file references.
     * @param name The desired filename for the dummy file.
     * @param fileHash The hash of the original file content.
     * @param insertDummy Whether to directly insert the created dummy file into the database.
     * @return The created StoredFile instance, marked as a dummy file.
     */
    private StoredFile createDummy(Long originalId, String name, String fileHash, boolean insertDummy) {
        StoredFile file = new StoredFile();
        file.setFileName(name);
        file.markAsDummy(originalId, fileHash);
        return insertDummy ? fileRepository.save(file) : file;
    }

    /**
     * Stores a file or creates a dummy reference if an identical file already exists.
     *
     * This method first calculates the SHA-256 hash of the provided MultipartFile. 
     * It then checks if a file with the same hash already exists in the database.
     * 
     * If an identical file is found:
     *   - A new "dummy" StoredFile is created. This dummy file references the existing file 
     *     using its ID and hash, but doesn't store the actual file content again.
     *   - The dummy file is saved to the database.
     * 
     * If no identical file is found:
     *   - A new StoredFile is created and populated with data from the MultipartFile.
     *   - The file hash is calculated and stored in the StoredFile.
     *   - The new StoredFile is saved to the database.
     *
     * @param multipartFile The MultipartFile containing the file data to store.
     * @param name The desired filename for the stored file.
     * @return The StoredFile object representing the stored file (either a new file or a dummy reference).
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
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


    /**
     * Updates an existing StoredFile with new data from a MultipartFile.
     *
     * If the provided MultipartFile has the same hash as an existing file in the database,
     * the existing file's information will be used to update the file with the given ID.
     * Otherwise, the file with the given ID will be updated with the new data from the MultipartFile.
     *
     * The FileName can't be update in this method.
     * 
     * @param id The ID of the StoredFile to update.
     * @param multipartFile The MultipartFile containing the new file data.
     * @return The updated StoredFile object.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws EntityNotFoundException If no StoredFile with the given ID is found.
     */
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

    /**
     * Renames an existing StoredFile.
     *
     * @param id The ID of the StoredFile to rename.
     * @param name The new filename for the StoredFile.
     * @return The updated StoredFile object.
     * @throws EntityNotFoundException If no StoredFile with the given ID is found.
     */
    public StoredFile renameFile(Long id, String name) throws EntityNotFoundException {
        StoredFile file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        file.setFileName(name);
        return fileRepository.save(file);
    }
    
    /**
     * Retrieves a StoredFile by its ID.
     *
     * @param id The ID of the StoredFile to retrieve.
     * @return The StoredFile object with the given ID.
     * @throws EntityNotFoundException If no StoredFile with the given ID is found.
     */
    public StoredFile getFileById(Long id) throws EntityNotFoundException {
        return fileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("File not found"));
    }
}
