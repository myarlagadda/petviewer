package org.workflow.app.petviewer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Image")
public class Image {

  @Column private String imageUrl;
  @Column @Lob private byte[] imageData;
  @Column private String imageType;

  @Id @Column private String businessKey;

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

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }
}
