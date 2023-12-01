package ru.komelin.crocprojectkomelin.service;

import org.springframework.stereotype.Service;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.repository.LinkRepository;

import java.time.LocalDateTime;

@Service
public class LinkService {

    private final PhotoService photoService;
    private final UniqueNumberDao uniqueNumberDao;
    private final HashService hashService;
    private final LinkRepository linkRepository;

    public LinkService(PhotoService photoService, UniqueNumberDao uniqueNumberDao, HashService hashService, LinkRepository linkRepository) {
        this.photoService = photoService;
        this.uniqueNumberDao = uniqueNumberDao;
        this.hashService = hashService;
        this.linkRepository = linkRepository;
    }

    public Link getLinkByAddress(String address) throws DownloadLimitExceededException, DownloadDateExceededException,
            LinkNotFoundException {
        Link link = linkRepository.findByLinkAddress(address);
        if (link == null) {
            throw new LinkNotFoundException(address);
        }

        if (link.getLifetime().isBefore(LocalDateTime.now())) {
            throw new DownloadDateExceededException("This link is already exceeded");
        }

        if (link.getDownloadLimit() > 0) {
            link.setDownloadLimit(link.getDownloadLimit() - 1);
            linkRepository.save(link);
        } else {
            throw new DownloadLimitExceededException("Download limit by this link is exceeded");
        }

        if (link.getLifetime().isAfter(LocalDateTime.now())) {
            return link;
        } else {
            throw new DownloadDateExceededException("This link is already expired");
        }
    }

    public Link createLink(int downloadLimit, LocalDateTime lifetime, int requestsPerSecond) {
        String address = hashService.generateHash(uniqueNumberDao.getUniqueNumber());
        Link link = new Link(address, downloadLimit, lifetime, requestsPerSecond);
        linkRepository.save(link);
        return link;
    }
}
