package ru.komelin.crocprojectkomelin.controller;

import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.request.RequestPerSecondExceededException;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.FileUploadException;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.model.Photo;
import ru.komelin.crocprojectkomelin.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/photo")
public class PhotoController {
    private final StorageService storageService;
    private final PhotoService photoService;
    private final LinkService linkService;
    private final RateLimitService rateLimitService;

    @Autowired
    public PhotoController(StorageService storageService, PhotoServiceImpl photoService, LinkServiceImpl linkService, RateLimitServiceImpl rateLimitService) {
        this.storageService = storageService;
        this.photoService = photoService;
        this.linkService = linkService;
        this.rateLimitService = rateLimitService;
    }

    @PutMapping()
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles,
                                         @RequestParam("download_limit") int downloadLimit,
                                         @RequestParam("seconds_of_life") long secondsOfLife,
                                         @RequestParam("requests_per_second") int requestsPerSecond) throws FileEmptyException,
            IOException, FileUploadException {

        if (requestsPerSecond <= 0 || secondsOfLife <= 0 || downloadLimit <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
        }

        List<String> fileNames = storageService.uploadFiles(multipartFiles);
        Link link = linkService.createLink(downloadLimit, LocalDateTime.now().plusSeconds(secondsOfLife), requestsPerSecond);
        photoService.savePhotos(fileNames, link);

        return ResponseEntity.ok().body(link.getLinkAddress());
    }

    @GetMapping("/{hash}")
    public ResponseEntity<StreamingResponseBody> getPhotos(@PathVariable("hash") String hash) throws FileDownloadException,
            DownloadLimitExceededException, DownloadDateExceededException, RequestPerSecondExceededException,
            LinkNotFoundException {

        Link link = linkService.getLinkByAddress(hash);

        List<String> fileNames = link.getPhotos().stream().map(Photo::getName).toList();

        if (fileNames.isEmpty()) {
            throw new FileDownloadException("Link is invalid");
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment = Result_files.zip")
                .body(out -> {
                    ZipOutputStream zipOutputStream = new ZipOutputStream(out);
                    List<InputStream> fileInputStreams;
                    try {
                        fileInputStreams = storageService.downloadFiles(fileNames);
                    } catch (FileDownloadException e) {
                        throw new RuntimeException(e);
                    }

                    Iterator<String> fileNameIterator = fileNames.iterator();
                    fileInputStreams.forEach(inputStream -> {
                        try {
                            zipOutputStream.putNextEntry(new ZipEntry(fileNameIterator.next()));
                            IOUtils.copy(inputStream, zipOutputStream);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    zipOutputStream.close();
                });
    }
}
