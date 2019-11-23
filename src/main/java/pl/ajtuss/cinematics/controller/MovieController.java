package pl.ajtuss.cinematics.controller;

import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.repository.MovieRepository;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

  private final MovieRepository movieRepository;


  public MovieController(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @GetMapping
  public Page<Movie> getAll(Pageable pageable) {
    return movieRepository.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getOne(@PathVariable String id) {
    Optional<Movie> byId = movieRepository.findById(id);

    return ResponseEntity.of(byId);
  }

  @PostMapping
  public ResponseEntity<Movie> save(@RequestBody Movie movie) {
    Movie saved = movieRepository.save(movie);

    return ResponseEntity
        .created(URI.create("/api/movies/" + saved.getId()))
        .body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> update(@PathVariable String id, @RequestBody Movie movie) {
    Optional<Movie> byId = movieRepository.findById(id);
    Optional<Movie> saved = byId.map(m -> {
      m.setName(movie.getName());
      m.setDescription(movie.getDescription());
      return m;
    }).map(movieRepository::save);

    return ResponseEntity.of(saved);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    movieRepository.deleteById(id);

    return ResponseEntity.noContent().build();
  }

}
