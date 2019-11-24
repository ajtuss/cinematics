package pl.ajtuss.cinematics.services;

import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.repository.MovieRepository;

@Service
public class MovieService {

  private final MovieRepository movieRepository;

  @Autowired
  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Page<Movie> findAll(String filter, Pageable pageable) {
    return Optional.ofNullable(filter)
                   .map(s -> movieRepository.findAllByNameContains(s, pageable))
                   .orElse(movieRepository.findAll(pageable));

  }

  public Optional<Movie> findById(String id) {
    return movieRepository.findById(id);
  }

  public Movie save(Movie movie) {
    return movieRepository.save(movie);
  }

  public void deleteById(String id) {
    movieRepository.deleteById(id);
  }

  public Optional<Movie> update(String id, Movie movie) {
    Optional<Movie> byId = movieRepository.findById(id);
    return byId.map(updateMovie(movie))
               .map(movieRepository::save);
  }

  private Function<Movie, Movie> updateMovie(Movie movie) {
    return m -> {
      m.setName(movie.getName());
      m.setDescription(movie.getDescription());
      return m;
    };
  }
}
