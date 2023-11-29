package ru.komelin.crocprojectkomelin.exception.repository;

public class DownloadLimitExceededException extends Exception{
    public DownloadLimitExceededException(String message) {
        super(message);
    }
}
