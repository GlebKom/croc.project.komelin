package ru.komelin.crocprojectkomelin.service;

import ru.komelin.crocprojectkomelin.exception.repository.LinkNotFoundException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.request.RequestPerSecondExceededException;
import ru.komelin.crocprojectkomelin.model.Link;

import java.time.LocalDateTime;

public interface LinkService {

    Link getLinkByAddress(String address) throws DownloadLimitExceededException, DownloadDateExceededException,
            LinkNotFoundException, RequestPerSecondExceededException;

    Link createLink(int downloadLimit, LocalDateTime lifetime, int requestsPerSecond);
}
