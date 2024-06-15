package org.workflow.app.petviewer;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Deployment(resources = "classpath*:/models/*.*")
public class PetViewerAppApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(PetViewerAppApplication.class, args);
    JobWorkerCreator.CreateWorker(null);
  }
}
