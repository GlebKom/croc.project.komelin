package ru.komelin.crocprojectkomelin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.model.Photo;
import ru.komelin.crocprojectkomelin.repository.PhotoRepository;

import java.util.List;

@Service
@Transactional
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final HashService hashService;
    private final UniqueNumberDao uniqueNumberDao;

    @Autowired
    public PhotoService(PhotoRepository photoRepository,
                        HashService hashService,
                        UniqueNumberDao uniqueNumberDao) {
        this.photoRepository = photoRepository;
        this.hashService = hashService;
        this.uniqueNumberDao = uniqueNumberDao;
    }

    public void savePhoto(String name, Link link) {
        Photo photo = new Photo(name, link);
        photoRepository.save(photo);
    }

    public void savePhotos(List<String> names, Link link) {

        for (String name : names) {
            Photo photo = new Photo(name, link);
            photoRepository.save(photo);
        }
    }
}
