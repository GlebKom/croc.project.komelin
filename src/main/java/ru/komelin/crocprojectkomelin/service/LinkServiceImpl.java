package ru.komelin.crocprojectkomelin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.request.RequestPerSecondExceededException;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.repository.LinkRepository;

import java.time.LocalDateTime;

@Service
public class LinkServiceImpl implements LinkService{

    private final UniqueNumberDao uniqueNumberDao;
    private final HashService hashService;
    private final LinkRepository linkRepository;

    private final RateLimitService rateLimitService;

    public LinkServiceImpl(UniqueNumberDao uniqueNumberDao, HashService hashService, LinkRepository linkRepository, RateLimitService rateLimitService) {
        this.uniqueNumberDao = uniqueNumberDao;
        this.hashService = hashService;
        this.linkRepository = linkRepository;
        this.rateLimitService = rateLimitService;
    }

    @Transactional
    public Link getLinkByAddress(String address) throws DownloadLimitExceededException, DownloadDateExceededException,
            LinkNotFoundException, RequestPerSecondExceededException {
        Link link = linkRepository.findByLinkAddress(address);
        if (link == null) {
            throw new LinkNotFoundException(address);
        }

        if (!rateLimitService.isAccessGained(link)) {
            throw new RequestPerSecondExceededException("Too many requests");
        }

        if (link.getDownloadLimit() > 0) {
            link.setDownloadLimit(link.getDownloadLimit() - 1);
            linkRepository.save(link);
        } else {
            throw new DownloadLimitExceededException("Download limit by this link '" + link.getLinkAddress() + "' is exceeded");
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
