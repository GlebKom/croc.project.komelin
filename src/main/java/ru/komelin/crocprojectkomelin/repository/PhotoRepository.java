package ru.komelin.crocprojectkomelin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.komelin.crocprojectkomelin.model.Photo;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
    List<Photo> findAllByHash(String link);
}
