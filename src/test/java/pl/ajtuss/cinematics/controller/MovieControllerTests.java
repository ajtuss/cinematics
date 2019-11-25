package pl.ajtuss.cinematics.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.ajtuss.cinematics.MongoInitialize;
import pl.ajtuss.cinematics.model.Movie;

@SpringBootTest
public class MovieControllerTests extends MongoInitialize {

  private static JsonMapper mapper = new JsonMapper();
  private static String API_URL = "/api/movies/";

  private MockMvc mockMvc;

  @BeforeEach
  void setup(WebApplicationContext wac) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void getMovies() throws Exception {
    this.mockMvc.perform(get(API_URL)
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(20)));
  }

  @Test
  void getMovie() throws Exception {
    Movie anyMovie = mongoTemplate.findOne(Query.query(new Criteria()), Movie.class);
    Assertions.assertThat(anyMovie).isNotNull();

    this.mockMvc.perform(get(API_URL + anyMovie.getId())
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(anyMovie.getId()))
                .andExpect(jsonPath("$.name").value(anyMovie.getName()))
                .andExpect(jsonPath("$.description").value(anyMovie.getDescription()))
                .andExpect(jsonPath("$.image.filename").value(anyMovie.getImage().getFilename()))
                .andExpect(jsonPath("$.image.url").value(anyMovie.getImage().getUrl()));
  }

  @Test
  void postMovie() throws Exception {
    Movie movie = new Movie();
    movie.setName("Title");
    movie.setDescription("Description");
    String movieJson = mapper.writeValueAsString(movie);

    this.mockMvc.perform(post(API_URL)
        .accept(MediaType.APPLICATION_JSON)
        .content(movieJson)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));
  }

  @Test
  void deleteMovie() throws Exception {

    Movie anyMovie = mongoTemplate.findOne(Query.query(new Criteria()), Movie.class);
    Assertions.assertThat(anyMovie).isNotNull();

    this.mockMvc.perform(delete(API_URL + anyMovie.getId())
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    Movie shouldBeNull = mongoTemplate.findById(anyMovie.getId(), Movie.class);
    Assertions.assertThat(shouldBeNull).isNull();
  }

  @Test
  void searchMovieContainsInTitle() throws Exception {

    Movie aMovie = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Civilization")), Movie.class);
    Assertions.assertThat(aMovie).isNotNull();

    this.mockMvc.perform(get(API_URL)
        .param("s", "vilizat")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id").value(aMovie.getId()))
                .andExpect(jsonPath("$.content.[0].name").value(aMovie.getName()))
                .andExpect(jsonPath("$.content.[0].description").value(aMovie.getDescription()))
                .andExpect(jsonPath("$.content.[0].image.filename").value(aMovie.getImage().getFilename()))
                .andExpect(jsonPath("$.content.[0].image.url").value(aMovie.getImage().getUrl()));
  }
}
