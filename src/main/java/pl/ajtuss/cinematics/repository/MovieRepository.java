package pl.ajtuss.cinematics.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.ajtuss.cinematics.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

  Page<Movie> findAllByNameContains(String name, Pageable pageable);

}
