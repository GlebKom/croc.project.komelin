package ru.komelin.crocprojectkomelin.exception.request;

public class RequestPerSecondExceededException extends Exception{

    public RequestPerSecondExceededException(String message) {
        super(message);
    }
}
