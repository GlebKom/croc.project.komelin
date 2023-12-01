package ru.komelin.crocprojectkomelin.exception.storage;

public class FileEmptyException extends SpringBootStorageException {
    public FileEmptyException(String message) {
        super(message);
    }
}
