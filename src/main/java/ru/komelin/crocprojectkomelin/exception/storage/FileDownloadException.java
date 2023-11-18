package ru.komelin.crocprojectkomelin.exception.storage;

public class FileDownloadException extends SpringBootFileUploadException {
    public FileDownloadException(String message) {
        super(message);
    }
}
