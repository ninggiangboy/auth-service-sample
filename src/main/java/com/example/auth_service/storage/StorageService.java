package com.example.auth_service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    String storeFile(MultipartFile file, String fileName, FileType type);

    List<String> storeFileAll(MultipartFile[] files, String[] fileNames, FileType type);

    Stream<Path> loadAllPaths();

    Path loadPath(String filePath);

    Resource loadAsResource(String filePath);

    void deleteFile(String filePath);

    void deleteAll();
}
