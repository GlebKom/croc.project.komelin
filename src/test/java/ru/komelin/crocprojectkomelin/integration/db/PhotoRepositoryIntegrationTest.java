package ru.komelin.crocprojectkomelin.integration.db;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ru.komelin.crocprojectkomelin.Application;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.model.Photo;
import ru.komelin.crocprojectkomelin.repository.LinkRepository;
import ru.komelin.crocprojectkomelin.repository.PhotoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PhotoRepositoryIntegrationTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void whenFindByLink_thenReturnPhotos() {
        // given
        Link link = new Link();
        Photo photo1 = new Photo();
        Photo photo2 = new Photo();
        photo1.setLink(link);
        photo2.setLink(link);
        List<Photo> expected = List.of(photo1, photo2);

        entityManager.persist(link);
        entityManager.persist(photo1);
        entityManager.persist(photo2);
        entityManager.flush();

        // when
        List<Photo> foundPhotos = photoRepository.findAllByLink(link);

        // then
        assertEquals(foundPhotos, expected);
    }

    @Test
    public void whenInsertLinksWithSameAddress_thenThrowException() {
        // given
        Link link1 = new Link();
        Link link2 = new Link();

        link1.setLinkAddress("test");
        link2.setLinkAddress("test");

        // then
        assertThrows(RuntimeException.class, () -> {
            entityManager.persist(link1);
            entityManager.persist(link2);
            entityManager.flush();
        });

    }



    @Test
    public void whenFindByAddress_thenReturnLink() {
        // given
        String address = "test";
        Link link = new Link();
        link.setLinkAddress(address);

        entityManager.persist(link);
        entityManager.flush();

        // when
        Link foundLink = linkRepository.findByLinkAddress(address);

        // then
        assertEquals(link, foundLink);
    }
}
