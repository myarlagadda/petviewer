package org.workflow.app.petviewer;

import io.camunda.zeebe.client.ZeebeClient;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// Optional for custom JSON response
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/process")
public class ProcessController {
  @Autowired private ImageRepository imageRepo;

  private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);
  private final ZeebeClient zeebe;

  public ProcessController(ZeebeClient client) {
    this.zeebe = client;
  }

  @PostMapping("/start")
  public ResponseEntity<StartProcessResponse> startProcessInstance(
      @RequestBody ProcessVariables variables) {

    LOG.info(
        "Starting process `" + ProcessConstants.BPMN_PROCESS_ID + "` with variables: " + variables);

    String businessKey = variables.getBusinessKey();
    String encodedImageData = null;
    StartProcessResponse processResponse = new StartProcessResponse(businessKey, encodedImageData);

    zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
        .latestVersion()
        .variables(variables)
        .send();
    return ResponseEntity.ok(processResponse);
  }

  @PostMapping("/message/{messageName}/{correlationKey}")
  public ResponseEntity<StartProcessResponse> publishMessage(
      @PathVariable String messageName,
      @PathVariable String correlationKey,
      @RequestBody ProcessVariables variables) {

    LOG.info(
        "Publishing message `{}` with correlation key `{}` and variables: {}",
        messageName,
        correlationKey,
        variables);

    String message = correlationKey;

    zeebe
        .newPublishMessageCommand()
        .messageName(messageName)
        .correlationKey(correlationKey)
        .variables(variables)
        .send();

    // Add a one-second wait using Thread.sleep(milliseconds)
    try {
      Thread.sleep(1000); // 1000 milliseconds = 1 second
    } catch (InterruptedException e) {
      // Handle interruption (optional)
      e.printStackTrace();
    }

    Image image = imageRepo.getById(correlationKey);
    byte[] imageData = image.getImageData();

    String encodedImageData = Base64.getEncoder().encodeToString(imageData);

    StartProcessResponse processResponse = new StartProcessResponse(message, encodedImageData);
    System.out.println("UI Image to Load" + encodedImageData);
    System.out.println("processResponse" + processResponse);

    return ResponseEntity.ok(processResponse);
  }
}
