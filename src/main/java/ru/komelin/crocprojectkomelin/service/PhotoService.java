package ru.komelin.crocprojectkomelin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.exception.repository.PhotoNotFoundException;
import ru.komelin.crocprojectkomelin.model.Photo;
import ru.komelin.crocprojectkomelin.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;

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

    public String addPhotosAndGetLink(List<String> fileNames){
        String link = hashService.generateHash(uniqueNumberDao.getUniqueNumber());

        for (String fileName : fileNames) {
            Photo photo = new Photo(fileName, link);
            photoRepository.save(photo);
        }

        return link;
    }

    public String getFileNameById(String fileId) {
        long id;
        try {
            id = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new PhotoNotFoundException(fileId);
        }
        Optional<Photo> photo = photoRepository.findById(id);
        if (photo.isPresent()) {
            return photo.get().getName();
        } else {
            throw new PhotoNotFoundException(fileId);
        }
    }

    public List<Photo> getPhotosByHash(String hash) {
        return photoRepository.findAllByHash(hash);
    }
}
