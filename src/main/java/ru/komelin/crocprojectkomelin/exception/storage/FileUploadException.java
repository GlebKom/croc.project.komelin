package ru.komelin.crocprojectkomelin.exception.storage;

public class FileUploadException extends SpringBootStorageException {
    public FileUploadException(String message) {
        super(message);
    }
}
