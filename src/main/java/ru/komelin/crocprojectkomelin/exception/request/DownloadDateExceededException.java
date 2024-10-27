package ru.komelin.crocprojectkomelin.exception.request;

public class DownloadDateExceededException extends RuntimeException{
    public DownloadDateExceededException(String message) {
        super(message);
    }
}
