package com.example.auth_service.storage;

import com.example.auth_service.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@Slf4j
public class StorageConfig {

    public StorageConfig(StorageProperties properties) {
        init(Paths.get(properties.getRootLocation()));
    }

    private void init(Path rootLocation) {
        try {
            Files.createDirectories(rootLocation);
            log.info("Storage root location: {}", rootLocation.toAbsolutePath());
            for (FileType type : FileType.values()) {
                Path path = Files.createDirectories(rootLocation.resolve(type.getLocation()));
                log.info("Storage {} location: {}", type.getLocation(), path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }
}
