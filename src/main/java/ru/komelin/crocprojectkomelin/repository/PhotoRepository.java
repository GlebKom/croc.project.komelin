package ru.komelin.crocprojectkomelin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.model.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByLink(Link link);
}
