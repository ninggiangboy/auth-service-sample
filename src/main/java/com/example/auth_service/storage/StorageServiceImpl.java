package com.example.auth_service.storage;

import com.example.auth_service.exception.FileException;
import com.example.auth_service.exception.NotFoundException;
import com.example.auth_service.exception.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getRootLocation());
    }

    @Override
    public String storeFile(MultipartFile file, String fileName, FileType type) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        checkInvalidFile(file, type, fileName, fileExtension);
        String generatedFilename = UUID.randomUUID().toString().replace("-", "")
                + "-" + fileName + "." + fileExtension;
        Path destinationFile = rootLocation.resolve(Paths.get(type.getLocation()).resolve(generatedFilename))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(rootLocation.resolve(type.getLocation()).toAbsolutePath())) {
            throw new StorageException(
                    "Cannot store file outside current directory.");
        }
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return type.getLocation() + "/" + generatedFilename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.");
        }
    }

    private void checkInvalidFile(MultipartFile file, FileType type, String fileName, String fileExtension) throws FileException {
        if (file == null || file.isEmpty()) {
            throw new FileException("Failed to storeFile empty file.");
        }

        if (fileName == null || fileName.isEmpty()) {
            throw new FileException("File has no valid name.");
        }

        if (!type.getAllowedExtensions().contains(fileExtension)) {
            throw new FileException(
                    String.format("File extension %s is not allowed, only accept %s", fileExtension, type.getAllowedExtensions())
            );
        }

        if (file.getSize() > type.getMaxFileSize()) {
            throw new FileException("File must be <= " + type.getMaxFileSize() / 1_000_000L + "Mb");
        }
    }

    @Override
    public List<String> storeFileAll(MultipartFile[] files, String[] fileNames, FileType type) {
        List<String> generatedFileNames = new ArrayList<>();
        if (files.length != fileNames.length) {
            throw new StorageException("Number of files and file names must be equal");
        }
        for (int i = 0; i < files.length; i++) {
            generatedFileNames.add(storeFile(files[i], fileNames[i], type));
        }
        return generatedFileNames;
    }

    @Override
    public Stream<Path> loadAllPaths() {
        try {
            return Files.walk(this.rootLocation)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new FileException("Failed to read stored files");
        }
    }

    @Override
    public Path loadPath(String filePath) {
        return rootLocation.resolve(filePath);
    }

    @Override
    public Resource loadAsResource(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new NotFoundException("Not found file: " + filePath);
        }
        try {
            Path file = loadPath(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("Not found file: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new FileException("Could not read file: " + filePath);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        Path file = rootLocation.resolve(filePath);
        if (!Files.exists(file)) {
            throw new NotFoundException("Could not find file: " + filePath);
        }
        if (!FileSystemUtils.deleteRecursively(file.toFile())) {
            throw new FileException("Failed to deleteFile file: " + filePath);
        }
    }

    @Override
    public void deleteAll() {
        if (!FileSystemUtils.deleteRecursively(rootLocation.toFile())) {
            throw new FileException("Failed to deleteFile all files");
        }
    }
}
