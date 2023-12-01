package ru.komelin.crocprojectkomelin.exception.request;

public class DownloadLimitExceededException extends Exception{
    public DownloadLimitExceededException(String message) {
        super(message);
    }
}
