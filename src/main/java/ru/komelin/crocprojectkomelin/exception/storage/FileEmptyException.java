package ru.komelin.crocprojectkomelin.exception.storage;

public class FileEmptyException extends SpringBootFileUploadException {
    public FileEmptyException(String message) {
        super(message);
    }
}
