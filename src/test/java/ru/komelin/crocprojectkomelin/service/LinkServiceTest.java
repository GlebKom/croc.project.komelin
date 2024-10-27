package ru.komelin.crocprojectkomelin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import ru.komelin.crocprojectkomelin.dao.UniqueNumberDao;
import ru.komelin.crocprojectkomelin.exception.request.DownloadDateExceededException;
import ru.komelin.crocprojectkomelin.exception.request.DownloadLimitExceededException;
import ru.komelin.crocprojectkomelin.exception.request.RequestPerSecondExceededException;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.repository.LinkRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LinkServiceTest {

	private LinkService linkService;
	@Mock
	private HashService hashService;
	@Mock
	private UniqueNumberDao uniqueNumberDao;
	@Mock
	private LinkRepository linkRepository;
	@Mock
	private RateLimitService rateLimitService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.linkService = new LinkServiceImpl(
				uniqueNumberDao,
				hashService,
				linkRepository,
				rateLimitService
		);
	}

	@Test
	public void getLinkByAddress_success() {
		//given
		String address = "testAddress";
		Link link = new Link();
		link.setLifetime(LocalDateTime.MAX);
		link.setDownloadLimit(100);
		link.setLinkAddress("testAddress");

		Link expectedLink = new Link();
		expectedLink.setLifetime(LocalDateTime.MAX);
		expectedLink.setDownloadLimit(99);
		expectedLink.setLinkAddress("testAddress");


		//when
		when(rateLimitService.isAccessGained(any())).thenReturn(true);
		when(linkRepository.findByLinkAddress(address)).thenReturn(link);

		//then
		Link resultLink = linkService.getLinkByAddress(address);
		verify(linkRepository, times(1)).save(expectedLink);
		assertEquals(resultLink, expectedLink);
	}

	@Test
	public void getLinkByAddressWhenLinkIsOutdated_failure() {
		//given
		String address = "testAddress";
		Link link = new Link();
		link.setLifetime(LocalDateTime.MIN);
		link.setDownloadLimit(100);
		link.setLinkAddress("testAddress");

		//when
		when(rateLimitService.isAccessGained(any())).thenReturn(true);
		when(linkRepository.findByLinkAddress(address)).thenReturn(link);

		//then
		assertThrows(
				DownloadDateExceededException.class,
				() -> linkService.getLinkByAddress(address),
				"Expected getLinkByAddress(address) to throw, but it didn't"
		);
	}

	@Test
	public void getLinkByAddressWhenDownloadLimitIsExceeded_failure() {
		//given
		String address = "testAddress";
		Link link = new Link();
		link.setLifetime(LocalDateTime.MIN);
		link.setDownloadLimit(0);
		link.setLinkAddress("testAddress");

		//when
		when(rateLimitService.isAccessGained(any())).thenReturn(true);
		when(linkRepository.findByLinkAddress(address)).thenReturn(link);

		//then
		assertThrows(
				DownloadLimitExceededException.class,
				() -> linkService.getLinkByAddress(address),
				"Expected getLinkByAddress(address) to throw, but it didn't"
		);
	}

	@Test
	public void getLinkByAddressWhenAccessIsNotGained_failure() {
		//given
		String address = "testAddress";
		Link link = new Link();
		link.setLifetime(LocalDateTime.MIN);
		link.setDownloadLimit(0);
		link.setLinkAddress("testAddress");

		//when
		when(rateLimitService.isAccessGained(any())).thenReturn(false);
		when(linkRepository.findByLinkAddress(address)).thenReturn(link);

		//then
		assertThrows(
				RequestPerSecondExceededException.class,
				() -> linkService.getLinkByAddress(address),
				"Expected getLinkByAddress(address) to throw, but it didn't"
		);
	}
}
