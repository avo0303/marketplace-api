package com.andrewsha.marketplace.storage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
    public String store(MultipartFile file, String uploadDirPath)
            throws IllegalStateException, IOException {
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String newFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadDirPath + "/" + newFileName));
        return newFileName;
    }
}
