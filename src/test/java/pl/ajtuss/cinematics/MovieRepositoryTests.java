package pl.ajtuss.cinematics;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.repository.MovieRepository;

@DataMongoTest
public class MovieRepositoryTests {


  @Autowired
  private MovieRepository movieRepository;

  @Test
  public void testSaveAndFind() {

    Movie movie = new Movie();
    movie.setName("Title");
    movie.setDescription("Description");

    Movie saved = movieRepository.save(movie);
    Optional<Movie> byId = movieRepository.findById(saved.getId());
    Assertions.assertThat(byId.isPresent()).isTrue();
    Assertions.assertThat(byId.get()).isEqualTo(saved);
  }


}
