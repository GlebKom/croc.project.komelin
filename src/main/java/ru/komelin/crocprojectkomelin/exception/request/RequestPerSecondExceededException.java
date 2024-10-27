package ru.komelin.crocprojectkomelin.exception.request;

public class RequestPerSecondExceededException extends RuntimeException{

    public RequestPerSecondExceededException(String message) {
        super(message);
    }
}
