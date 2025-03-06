package com.microecommerce.fileservice.validations;

import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import jakarta.inject.Singleton;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Singleton
@Service
public class FileValidations {

    /**
     * Validates the file extension.
     *
     * @param extension the file extension to validate
     * @throws InvalidEntityException if the file extension is null or empty
     */
    public void validateFileExtension(String extension) throws InvalidEntityException {
        if (extension == null || extension.isEmpty()) {
            throw new InvalidEntityException("File extension must not be null or empty.");
        }
    }

    /**
     * Validates the file name.
     *
     * @param name the file name to validate
     * @throws InvalidEntityException if the file name is null or empty
     */
    public void validateFileName(String name) throws InvalidEntityException {
        if (name == null || name.isEmpty()) {
            throw new InvalidEntityException("File name must not be null or empty.");
        }
    }

    /**
     * Validates the file content type.
     *
     * @param contentType the file content type to validate
     * @throws InvalidEntityException if the file content type is null or empty
     */
    public void validateFileContentType(String contentType) throws InvalidEntityException {
        if (contentType == null || contentType.isEmpty()) {
            throw new InvalidEntityException("File content type must not be null or empty.");
        }
    }

    /**
     * Validates the file content.
     *
     * @param content the file content to validate
     * @throws InvalidEntityException if the file content is null or empty
     */
    public void validateFileContent(byte[] content) throws InvalidEntityException {
        if (content == null || content.length == 0) {
            throw new InvalidEntityException("File content must not be null or empty.");
        }
    }
}
