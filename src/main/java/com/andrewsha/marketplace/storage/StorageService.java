package com.andrewsha.marketplace.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
	
	@Value("${server.address}")
	String serverAddress;
	@Value("${server.port}")
	String serverPort;
	@Value("${upload.path.products}")
	String uploadPath;

	public File initUploadDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public String store(MultipartFile file, String uploadDirPath)
			throws IllegalStateException, IOException {
		this.initUploadDir(uploadDirPath);
		String newFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
		file.transferTo(new File(uploadDirPath + "/" + newFileName));
		return newFileName;
	}

	public Collection<String> storeAll(Collection<MultipartFile> files, String uploadDirPath) {
		this.initUploadDir(uploadDirPath);
		return files.stream().map(e -> {
			String newFileName = UUID.randomUUID().toString() + "." + e.getOriginalFilename();
			try {
				e.transferTo(new File(uploadDirPath + "/" + newFileName));
			} catch (IOException ex) {
				throw new IllegalStateException("cannot save file");
			}
			return newFileName;
		}).collect(Collectors.toList());
	}
}
