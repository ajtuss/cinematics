package pl.ajtuss.cinematics.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.repository.MovieRepository;
import pl.ajtuss.cinematics.services.ImageService;

@RestController
@RequestMapping("/api/movies")
public class ImageController {

  private final MovieRepository movieRepository;

  private final ImageService imageService;

  @Autowired
  ImageController(MovieRepository movieRepository,
      ImageService imageService) {
    this.movieRepository = movieRepository;
    this.imageService = imageService;
  }

  @PostMapping("/{id}/image")
  public ResponseEntity<?> uploadFile(@PathVariable("id") String id,
      @RequestPart(value = "file") MultipartFile file) {

    Optional<Movie> movie = imageService.updateImage(id, file);

    return ResponseEntity.of(movie);


  }

  @DeleteMapping("/{id}/image")
  public void deleteFile(@PathVariable("id") String id) {
    Optional<Movie> movie = movieRepository.findById(id);
    movie.map(Movie::getImage)
        .ifPresent(imageService::deleteImage);
  }
}
