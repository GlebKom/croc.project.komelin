package ru.komelin.crocprojectkomelin.integration.web;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import ru.komelin.crocprojectkomelin.model.Link;
import ru.komelin.crocprojectkomelin.service.LinkService;
import ru.komelin.crocprojectkomelin.service.PhotoService;
import ru.komelin.crocprojectkomelin.service.RateLimitService;
import ru.komelin.crocprojectkomelin.service.StorageService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PhotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private StorageService storageService;
    @Mock
    private PhotoService photoService;
    @Mock
    private LinkService linkService;
    @Mock
    private RateLimitService rateLimitService;

    @SneakyThrows
    @Test
    public void whenUploadFile_thenGetLink() {
        // given
        MultipartFile[] files = new MultipartFile[3];
        List<String> fileNames = List.of("test1", "test2", "test3");
        int downloadLimit = 10;
        long secondsOfLife = 1000;
        int requestsPerSecond = 5;
        Link link = new Link();
        link.setLinkAddress("test");

        // when
        when(storageService.uploadFiles(files)).thenReturn(fileNames);
        when(linkService.createLink(downloadLimit, LocalDateTime.now().plusSeconds(secondsOfLife), requestsPerSecond))
                .thenReturn(link);

        // then
        MvcResult result = mockMvc.perform(post("/photo"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), link.getLinkAddress());
    }

    @SneakyThrows
    @Test
    public void whenUploadFileAndWrongParams_thenGetErrorCode() {
        // given
        MultipartFile[] files = new MultipartFile[3];
        List<String> fileNames = List.of("test1", "test2", "test3");
        int downloadLimit = 10;
        long secondsOfLife = 1000;
        int requestsPerSecond = -10;
        Link link = new Link();
        link.setLinkAddress("test");

        // when
        when(storageService.uploadFiles(files)).thenReturn(fileNames);
        when(linkService.createLink(downloadLimit, LocalDateTime.now().plusSeconds(secondsOfLife), requestsPerSecond))
                .thenReturn(link);

        // then
        mockMvc.perform(post("/photo"))
                .andExpect(status().isBadRequest());
    }

}
