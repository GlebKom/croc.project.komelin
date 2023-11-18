package ru.komelin.crocprojectkomelin.exception.storage;

public class FileUploadException extends SpringBootFileUploadException {
    public FileUploadException(String message) {
        super(message);
    }
}
