package pl.ajtuss.cinematics.repository;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.ajtuss.cinematics.MongoInitialize;
import pl.ajtuss.cinematics.model.Movie;

@DataMongoTest
public class MovieRepositoryTests extends MongoInitialize {

  @Autowired
  private MovieRepository movieRepository;

  @Test
  void saveShouldPersistObject() {
    Movie movie = new Movie();
    movie.setName("Title");
    movie.setDescription("Description");

    Movie saved = movieRepository.save(movie);
    Optional<Movie> byId = movieRepository.findById(saved.getId());
    Assertions.assertThat(byId.isPresent()).isTrue();
    Assertions.assertThat(byId.get()).isEqualTo(saved);
  }

  @Test
  void findAllByNameContainsShouldFind() {
    String searched = "vilizati";
    PageRequest pageable = PageRequest.of(0, 100);

    Page<Movie> found = movieRepository.findAllByNameContains(searched, pageable);

    Assertions.assertThat(found.getTotalElements()).isEqualTo(1);
    Assertions.assertThat(found).allMatch(m -> m.getName().contains(searched));
  }


}
