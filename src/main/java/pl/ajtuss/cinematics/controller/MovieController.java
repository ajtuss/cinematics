package pl.ajtuss.cinematics.controller;

import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.services.MovieService;

@RestController()
@RequestMapping(value = "/api/movies", consumes = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public ResponseEntity<Page<Movie>> getAll(@RequestParam(value = "s", required = false) String filter, Pageable pageable) {
    Page<Movie> all = movieService.findAll(filter, pageable);

    return ResponseEntity.ok(all);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getOne(@PathVariable String id) {
    Optional<Movie> byId = movieService.findById(id);

    return ResponseEntity.of(byId);
  }

  @PostMapping
  public ResponseEntity<Movie> save(@RequestBody Movie movie) {
    Movie saved = movieService.save(movie);

    return ResponseEntity
        .created(URI.create("/api/movies/" + saved.getId()))
        .body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> update(@PathVariable String id, @RequestBody Movie movie) {
    Optional<Movie> updated = movieService.update(id, movie);

    return ResponseEntity.of(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    movieService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

}
