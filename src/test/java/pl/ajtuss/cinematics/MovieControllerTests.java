package pl.ajtuss.cinematics;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.ajtuss.cinematics.model.Movie;

@SpringBootTest
public class MovieControllerTests {

  private MockMvc mockMvc;

  private JsonMapper mapper = new JsonMapper();

  @BeforeEach
  void setup(WebApplicationContext wac) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  void getMovies() throws Exception {
    this.mockMvc.perform(get("/api/movies")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void postMovie() throws Exception {
    Movie movie = new Movie();
    movie.setName("Title");
    movie.setDescription("Description");
    String movieJson = mapper.writeValueAsString(movie);

    this.mockMvc.perform(post("/api/movies")
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

  }
}
