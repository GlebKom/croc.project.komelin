package ru.komelin.crocprojectkomelin.dto;

public class PhotoDto {

    private long id;

    private String fileName;

    public PhotoDto(long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
