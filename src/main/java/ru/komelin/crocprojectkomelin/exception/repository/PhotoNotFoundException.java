package ru.komelin.crocprojectkomelin.exception.repository;

public class PhotoNotFoundException extends RuntimeException{
    private String id;

    public PhotoNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Photo with id " + id + " not found";
    }
}
