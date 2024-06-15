package org.workflow.app.petviewer;

public class AnimalImage {

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public byte[] getImageData() {
    return imageData;
  }

  public void setImageData(byte[] imageData) {
    this.imageData = imageData;
  }

  private String type;
  private String imageUrl;
  private byte[] imageData;

  // Getters and setters (optional)

  // ... (other methods specific to AnimalImage)
}
