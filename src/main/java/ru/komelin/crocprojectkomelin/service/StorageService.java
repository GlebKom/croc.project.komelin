package ru.komelin.crocprojectkomelin.service;

import org.springframework.web.multipart.MultipartFile;
import ru.komelin.crocprojectkomelin.exception.storage.FileDownloadException;
import ru.komelin.crocprojectkomelin.exception.storage.FileEmptyException;
import ru.komelin.crocprojectkomelin.exception.storage.FileUploadException;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> uploadFiles(MultipartFile[] multipartFiles) throws FileUploadException, IOException, FileEmptyException;

    Object downloadFile(String fileName) throws FileDownloadException, IOException;
}
