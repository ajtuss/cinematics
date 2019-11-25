package pl.ajtuss.cinematics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ResourceUtils;

public abstract class MongoInitialize {

  public static final String MOVIE = "movie";

  @Autowired
  public MongoTemplate mongoTemplate;

  private static final JacksonJsonParser PARSER = new JacksonJsonParser();

  @BeforeEach
  public void initializeDb() throws IOException {
    File file = ResourceUtils.getFile("classpath:movies.json");
    byte[] bytes = Files.readAllBytes(file.toPath());
    String json = new String(bytes);
    List<Object> objects = PARSER.parseList(json);
    objects.forEach(i -> mongoTemplate.save(i, MOVIE));
  }

  @AfterEach
  public void cleanDb() {
    mongoTemplate.dropCollection(MOVIE);
  }

}
