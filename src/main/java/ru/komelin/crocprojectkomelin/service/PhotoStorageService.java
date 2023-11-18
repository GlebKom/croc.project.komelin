package ru.komelin.crocprojectkomelin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.FileUploadException;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PhotoStorageService implements StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Autowired
    public PhotoStorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> uploadFiles(MultipartFile[] multipartFiles) throws FileUploadException, IOException, FileEmptyException {
        List<String> fileNames = new ArrayList<>();

        // photos count check
        if (multipartFiles.length > 10) {
            throw new FileUploadException("File count should be less or equals 10");
        }

        for (MultipartFile multipartFile : multipartFiles) {
            // check is multipart file is empty
            checkIsEmpty(multipartFile);

            // check is multipart file is an image
            checkIsImage(multipartFile);

            checkIsEmpty(multipartFile);

            // generate filename
            String fileName = generateFileName(multipartFile);

            // upload file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
            metadata.addUserMetadata("Title", "File Upload - " + fileName);
            metadata.setContentLength(multipartFile.getSize());
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(), metadata);
            s3Client.putObject(request);
            fileNames.add(fileName);
        }

        return fileNames;
    }

    @Override
    public Object downloadFile(String fileName) throws FileDownloadException, IOException {
        if (bucketIsEmpty()) {
            throw new FileDownloadException("Requested bucket does not exist or is empty");
        }
        S3Object object = s3Client.getObject(bucketName, fileName);
        try (S3ObjectInputStream s3is = object.getObjectContent()) {
            Resource resource = new InputStreamResource(new ByteArrayInputStream(s3is.readAllBytes()));
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileDownloadException("Could not find the file!");
            }
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void checkIsImage(MultipartFile file) throws FileUploadException, IOException {
        if (ImageIO.read(file.getInputStream()) == null) {
            throw new FileUploadException("File should be an image");
        }
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Result result = s3Client.listObjectsV2(this.bucketName);
        if (result == null){
            return false;
        }
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        return objects.isEmpty();
    }

    private void checkIsEmpty(MultipartFile multipartFile) throws FileEmptyException {
        if (multipartFile.isEmpty()) {
            throw new FileEmptyException("Cannot save empty file");
        }
    }
}
