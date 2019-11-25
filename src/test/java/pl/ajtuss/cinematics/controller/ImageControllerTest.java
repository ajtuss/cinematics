package pl.ajtuss.cinematics.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adobe.testing.s3mock.junit5.S3MockExtension;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.nio.file.Files;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import pl.ajtuss.cinematics.MongoInitialize;
import pl.ajtuss.cinematics.controller.ImageControllerTest.S3MockConfig;
import pl.ajtuss.cinematics.model.Movie;


@SpringBootTest
@Import(S3MockConfig.class)
class ImageControllerTest extends MongoInitialize {

  @RegisterExtension
  static final S3MockExtension S3_MOCK = S3MockExtension.builder().silent()
                                                        .withSecureConnection(false).build();

  @TestConfiguration
  static class S3MockConfig {

    @Bean
    @Primary
    public AmazonS3 amazonS3client() {
      AmazonS3 s3Client = S3_MOCK.createS3Client();
      s3Client.createBucket("cinematics-bucket");
      return s3Client;
    }
  }

  @Value("${amazonProperties.endpointUrl}")
  private String urlS3;

  private static JsonMapper mapper = new JsonMapper();
  private static String API_URL = "/api/movies/%s/image";
  private static String IMAGE_NAME = "image.jpg";

  private MockMvc mockMvc;

  @BeforeEach
  void setup(WebApplicationContext wac) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void uploadFile() throws Exception {
    Movie movie = new Movie();
    movie.setName("Title");
    movie.setDescription("Description");

    Movie saved = mongoTemplate.save(movie, MOVIE);
    Assertions.assertThat(saved.getImage()).isNull();

    File image = ResourceUtils.getFile("classpath:" + IMAGE_NAME);
    byte[] bytes = Files.readAllBytes(image.toPath());

    MockMultipartFile file = new MockMultipartFile("file", IMAGE_NAME, MediaType.IMAGE_JPEG.toString(), bytes);

    mockMvc.perform(multipart(String.format(API_URL, saved.getId()))
        .file(file))
           .andExpect(status().isOk())
           .andExpect(content().contentType("application/json"))
           .andExpect(jsonPath("$.id").value(saved.getId()))
           .andExpect(jsonPath("$.name").value(saved.getName()))
           .andExpect(jsonPath("$.description").value(saved.getDescription()))
           .andExpect(jsonPath("$.image.filename").value(IMAGE_NAME))
           .andExpect(jsonPath("$.image.url", allOf(startsWith(urlS3), endsWith(IMAGE_NAME))));
  }

}
