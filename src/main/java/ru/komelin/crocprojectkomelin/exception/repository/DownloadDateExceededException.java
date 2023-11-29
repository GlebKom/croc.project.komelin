package ru.komelin.crocprojectkomelin.exception.repository;

public class DownloadDateExceededException extends Exception{
    public DownloadDateExceededException(String message) {
        super(message);
    }
}
