package org.workflow.app.petviewer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
  Image findByBusinessKey(String business_key);
}
