package ru.komelin.crocprojectkomelin.exception.request;

public class DownloadLimitExceededException extends RuntimeException{
    public DownloadLimitExceededException(String message) {
        super(message);
    }
}
