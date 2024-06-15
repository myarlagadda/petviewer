package org.workflow.app.petviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageController {

  private String imageUrl;

  @Autowired private ImageService imageService;

  @GetMapping("/animal/{type}/{businessKey}")
  public ResponseEntity<AnimalImage> getRandomAnimalImage(
      @PathVariable String type, @PathVariable String businessKey) throws IOException {
    String imageUrl;

    int minWidth = 200;
    int maxWidth = 1024;
    int minHeight = 300;
    int maxHeight = 1024;

 // Generate random width and height within the specified ranges
    Random random = new Random();
    int width = random.nextInt(maxWidth - minWidth + 1) + minWidth;
    int height = random.nextInt(maxHeight - minHeight + 1) + minHeight;

    switch (type.toLowerCase()) {
      case "cat":
        imageUrl = "https://placekitten.com/" + width + "/" + height;
        break;
      case "dog":
        imageUrl = "https://place.dog/" + width + "/" + height;
        break;
      case "bear":
        imageUrl = "https://placebear.com/" + width + "/" + height;
        break;
      default:
        return ResponseEntity.badRequest().body(null);
    }

    URL url = new URL(imageUrl);
    System.out.println("Invoking the URL - " + url);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET"); // Ensure GET request for downloading image
    connection.connect();

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("Error downloading image: HTTP " + responseCode);
    }

    try (InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      int bytesRead;
      byte[] buffer = new byte[1024];
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      byte[] imageData = outputStream.toByteArray();

      // Create AnimalImage object
      AnimalImage savedImage = new AnimalImage();
      savedImage.setType(type);
      savedImage.setImageUrl(imageUrl);
      savedImage.setImageData(imageData);

      System.out.println(savedImage);
      Image image = new Image();
      image.setImageData(imageData);
      image.setImageType(type);
      image.setImageUrl(imageUrl);
      image.setBusinessKey(businessKey);
      imageService.create(image);
      Image image2 = imageService.findByBusinessKey(businessKey);
      return ResponseEntity.ok(savedImage);

    } finally {
      connection.disconnect(); // Close the connection (optional)
    }
  }

  private int getRandomMultipleOf100(int min, int max) {
    // TODO Auto-generated method stub
    min = (min % 100 == 0) ? min : (min / 100 + 1) * 100;
    int randomInt = new Random().nextInt((max - min) / 100 + 1);
    return randomInt * 100;
  }
}
