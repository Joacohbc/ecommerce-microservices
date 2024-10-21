package com.microecommerce.fileservice.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashService {

    /**
     * Calculates the SHA-256 hash of the provided MultipartFile.
     *
     * @param bytesFromFile The MultipartFile to calculate the hash for.
     * @return The calculated SHA-256 hash as a hexadecimal string.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException If an I/O error occurs while reading from the MultipartFile.
     */
    public String calculateFileHash(byte[] bytesFromFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytesFromFile);
        
        byte[] hashBytes = md.digest();

        // Convert byte array to hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
