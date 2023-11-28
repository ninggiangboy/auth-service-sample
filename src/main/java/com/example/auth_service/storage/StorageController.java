package com.example.auth_service.storage;

import com.example.auth_service.base.BaseController;
import com.example.auth_service.base.ResultResponse;
import com.example.auth_service.exception.NotFoundException;
import com.example.auth_service.util.StringHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Slf4j
public class StorageController extends BaseController {

    private final StorageService storageService;

    @GetMapping("/{location}/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @PathVariable String location,
            @PathVariable String fileName
    ) throws IOException {
        Resource file = storageService.loadAsResource(location + "/" + fileName);
        String contentType = Files.probeContentType(Path.of(file.getFile().getAbsolutePath()));
        try {
            MediaType type = MediaType.parseMediaType(contentType);
            return ResponseEntity.ok()
                    .contentType(type)
                    .body(file);
        } catch (InvalidMediaTypeException e) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + StringHelper.getCleanFileName(file.getFilename()) + "\"").body(file);
        }
    }

    @PostMapping("/{location}/{fileName:.+}")
    public ResponseEntity<ResultResponse> handleFileUpload(
            @PathVariable String location,
            @PathVariable String fileName,
            @RequestParam("file") MultipartFile file
    ) {
        FileType type = FileType.fromLocation(location);
        if (type == null) {
            throw new NotFoundException("File type not found");
        }
        String filePath = storageService.storeFile(file, fileName, type);
        return buildResponse("Upload file successfully", List.of(filePath), HttpStatus.CREATED);
    }
}
