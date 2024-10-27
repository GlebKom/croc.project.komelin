package ru.komelin.crocprojectkomelin.exception.repository;

public class LinkNotFoundException extends RuntimeException{
    private final String link;

    public LinkNotFoundException(String link) {
        this.link = link;
    }

    @Override
    public String getMessage() {
        return "Link '" + link + "' not found";
    }
}
