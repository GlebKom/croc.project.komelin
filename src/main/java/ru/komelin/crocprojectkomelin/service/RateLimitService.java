package ru.komelin.crocprojectkomelin.service;

import ru.komelin.crocprojectkomelin.model.Link;

public interface RateLimitService {

    boolean isAccessGained(Link link);
}
