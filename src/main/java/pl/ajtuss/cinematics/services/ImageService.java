package pl.ajtuss.cinematics.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ajtuss.cinematics.model.Image;
import pl.ajtuss.cinematics.model.Movie;
import pl.ajtuss.cinematics.repository.MovieRepository;

@Service
public class ImageService {

  private final MovieRepository movieRepository;

  private final AmazonS3Service amazonS3Service;

  @Autowired
  public ImageService(MovieRepository movieRepository,
      AmazonS3Service amazonS3Service) {
    this.movieRepository = movieRepository;
    this.amazonS3Service = amazonS3Service;
  }

  public Optional<Movie> updateImage(String id, MultipartFile multipartFile) {
    Optional<Movie> movie = movieRepository.findById(id);

    movie.map(Movie::getImage).ifPresent(this::deleteImage);

    return movie.flatMap(m -> {
      Optional<Image> saved = uploadImage(multipartFile);
      return saved.map(image -> movieRepository.save(m.withImage(image)));
    });
  }

  public Optional<Image> uploadImage(MultipartFile multipartFile) {
    return amazonS3Service.uploadFile(multipartFile);
  }

  public void deleteImage(Image image) {
    amazonS3Service.deleteFileFromS3Bucket(image);
  }


}
