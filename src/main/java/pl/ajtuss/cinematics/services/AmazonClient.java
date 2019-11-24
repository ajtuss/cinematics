package pl.ajtuss.cinematics.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ajtuss.cinematics.model.Image;

@Slf4j
@Service
public class AmazonClient {

  @Value("${amazonProperties.endpointUrl}")
  private String endpointUrl;
  @Value("${amazonProperties.bucketName}")
  private String bucketName;

  private final AmazonS3 s3client;

  @Autowired
  public AmazonClient(AmazonS3 s3client) {
    this.s3client = s3client;
  }

  public Optional<Image> uploadFile(MultipartFile multipartFile) {
    try {
      File file = convertMultiPartToFile(multipartFile);
      String fileName = generateFileName(file.getName());
      uploadFileTos3bucket(fileName, file);
      Image image = new Image(getURL(fileName), file.getName());
      return Optional.of(image);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

  public void deleteFileFromS3Bucket(Image image) {
    String fileName = getFilenameFromURL(image.getUrl());
    s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
  }

  private String getURL(String fileName) {
    return endpointUrl + "/" + bucketName + "/" + fileName;
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  private String generateFileName(String filename) {
    return new Date().getTime() + "-" + filename.replace(" ", "_");
  }

  private void uploadFileTos3bucket(String fileName, File file) {
    s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
        .withCannedAcl(CannedAccessControlList.PublicRead));
  }

  private String getFilenameFromURL(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }
}
