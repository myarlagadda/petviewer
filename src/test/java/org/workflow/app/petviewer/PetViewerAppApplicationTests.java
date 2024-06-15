package org.workflow.app.petviewer;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.MessageAssert;
import io.camunda.zeebe.process.test.inspections.InspectionUtility;
import io.camunda.zeebe.process.test.inspections.model.InspectedProcessInstance;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = PetViewerAppApplication.class) // will deploy BPMN & DMN models
@ZeebeSpringTest
public class PetViewerAppApplicationTests {
  @Autowired private ZeebeClient client;

  @Autowired private ProcessController processController;

  @Autowired private ZeebeTestEngine engine;

  @MockBean private MyService myService;

  @Test
  public void testHappyPath() throws Exception {

    ProcessVariables variables = new ProcessVariables().setBusinessKey("23");
    processController.startProcessInstance(variables);

    // wait for process to be started
    engine.waitForIdleState(Duration.ofSeconds(1));

    InspectedProcessInstance processInstance =
        InspectionUtility.findProcessInstances().findLastProcessInstance().get();
    assertThat(processInstance).isStarted();
  }

  @Test
  public void testMessagepush() throws Exception {

    ProcessVariables variables = new ProcessVariables().setBusinessKey("23");
    variables.setPetType("bear");
    PublishMessageResponse response =
        client
            .newPublishMessageCommand()
            .messageName(variables.getPetType())
            .correlationKey(variables.getBusinessKey())
            .send()
            .join();
    MessageAssert assertions = BpmnAssert.assertThat(response);
  }
}
