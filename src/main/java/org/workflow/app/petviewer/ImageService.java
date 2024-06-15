package org.workflow.app.petviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  @Autowired private ImageRepository imageRepo;

  public String create(Image image) {
    return imageRepo.save(image).getBusinessKey();
  }

  public Image findByBusinessKey(String businessKey) {
    return imageRepo.getById(businessKey);
  }
}
