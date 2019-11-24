package pl.ajtuss.cinematics.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Movie {

  @Id
  @JsonProperty(access = Access.READ_ONLY)
  private String id;

  private String name;

  private String description;

  @With
  @JsonProperty(access = Access.READ_ONLY)
  private Image image;

}

