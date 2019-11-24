package pl.ajtuss.cinematics.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

  @Value("${amazonProperties.region}")
  private String region;
  @Value("${amazonProperties.accessKey}")
  private String accessKey;
  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  @Bean
  public AmazonS3 amazonS3client() {
    return AmazonS3ClientBuilder.standard()
                                .withRegion(region)
                                .withCredentials(awsCredentialsProvider())
                                .build();
  }

  private AWSCredentialsProvider awsCredentialsProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey));
  }
}
