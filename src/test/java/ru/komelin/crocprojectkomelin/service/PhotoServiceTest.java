package ru.komelin.crocprojectkomelin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.model.Photo;
import ru.komelin.crocprojectkomelin.repository.PhotoRepository;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
class PhotoServiceTest {

	private PhotoService photoService;
	@Mock
	private HashService hashService;
	@Mock
	private UniqueNumberDao uniqueNumberDao;
	@Mock
	private PhotoRepository photoRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.photoService = new PhotoServiceImpl(
				photoRepository,
				hashService,
				uniqueNumberDao
		);
	}

	@Test
	public void savePhoto() {
		Link link = new Link();
		String testName = "testName";
		Photo photo = new Photo(testName, link);
		photoService.savePhoto(testName, link);
		verify(photoRepository).save(photo);
	}

}
