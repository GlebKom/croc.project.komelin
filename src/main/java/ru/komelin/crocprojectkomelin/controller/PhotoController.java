package ru.komelin.crocprojectkomelin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.komelin.crocprojectkomelin.dto.PhotoDto;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.FileUploadException;
import ru.komelin.crocprojectkomelin.service.PhotoService;
import ru.komelin.crocprojectkomelin.service.StorageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class PhotoController {
    private final StorageService storageService;

    private final PhotoService photoService;

    @Autowired
    public PhotoController(StorageService storageService, PhotoService photoService) {
        this.storageService = storageService;
        this.photoService = photoService;
    }

    @PostMapping()
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles) {
        List<String> fileNames;
        String link;
        try {
            fileNames = storageService.uploadFiles(multipartFiles);
            link = photoService.addPhotosAndGetLink(fileNames);

        } catch (FileUploadException | IOException | FileEmptyException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().body(link);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<?> getPhotos(@PathVariable("hash") String hash){
        List<PhotoDto> photos = photoService.getPhotosByHash(hash).stream().map(photo -> new PhotoDto(photo.getId(), photo.getName())).toList();
        if (photos.isEmpty()) {
            return ResponseEntity.badRequest().body("No photos found");
        }
        return ResponseEntity.ok().body(photos);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadPhoto(@PathVariable("id") String id) throws IOException, FileDownloadException {
        String fileName = photoService.getFileNameById(id);
        Object response = storageService.downloadFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(response);
    }
}
