package com.example.auth_service.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum FileType {
    PROFILE_IMAGE("profile_image", 5_000_000L, Set.of("png", "jpg", "jpeg", "bmp"));
    private final String location;
    private final long maxFileSize;
    private final Set<String> allowedExtensions;

    public static FileType fromLocation(String location) {
        for (FileType type : FileType.values()) {
            if (type.getLocation().equals(location)) {
                return type;
            }
        }
        return null;
    }
}
