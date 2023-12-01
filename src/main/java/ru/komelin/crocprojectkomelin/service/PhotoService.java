package ru.komelin.crocprojectkomelin.service;

import ru.komelin.crocprojectkomelin.model.Link;

import java.util.List;

public interface PhotoService {
    void savePhoto(String name, Link link);

    void savePhotos(List<String> names, Link link);
}
