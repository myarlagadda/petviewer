package org.workflow.app.petviewer;

public class StartProcessResponse {

  private final String businessKey;
  private final String encodedImageData;

  public StartProcessResponse(String businessKey, String encodedImageData) {
    this.encodedImageData = encodedImageData; // Set to null for original behavior
    this.businessKey = businessKey;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public String getEncodedImageData() {
    return encodedImageData;
  }
}
