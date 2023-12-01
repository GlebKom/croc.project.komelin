package ru.komelin.crocprojectkomelin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.komelin.crocprojectkomelin.model.Link;

public interface LinkRepository extends JpaRepository<Link, Integer> {
    Link findByLinkAddress(String linkAddress);
}
