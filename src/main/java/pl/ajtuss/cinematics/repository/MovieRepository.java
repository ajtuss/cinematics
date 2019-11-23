package pl.ajtuss.cinematics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import pl.ajtuss.cinematics.model.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

  Movie findByNameContains(@Param("s") String name);

}
