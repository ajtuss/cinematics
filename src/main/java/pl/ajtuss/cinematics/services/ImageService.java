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

  private final AmazonClient amazonClient;

  @Autowired
  public ImageService(MovieRepository movieRepository,
      AmazonClient amazonClient) {
    this.movieRepository = movieRepository;
    this.amazonClient = amazonClient;
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
    return amazonClient.uploadFile(multipartFile);
  }

  public void deleteImage(Image image) {
    amazonClient.deleteFileFromS3Bucket(image);
  }


}
