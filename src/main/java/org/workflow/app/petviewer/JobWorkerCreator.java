/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.1. You may not use this file
 * except in compliance with the Zeebe Community License 1.1.
 */
package org.workflow.app.petviewer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

public final class JobWorkerCreator {

  public static void CreateWorker(final String[] args) {

    final String jobType = "fetchpetimage";

    try (final ZeebeClient clientBuilder =
        ZeebeClient.newCloudClientBuilder()
            .withClusterId("c1607b7e-7d70-41ca-ac12-449e3e93258f")
            .withClientId("yL8jGqWioXLE34guXUql7uvipjpYD3ap")
            .withClientSecret("~wHhn8z4jkIlvB3O2RY_ovt1t_BTMicynT~BfitRr9jtoL72krO-0do5THwNvAM2")
            .withRegion("jfk-1")
            .build()) {

      System.out.println("Opening job worker.");

      try (final JobWorker workerRegistration =
          clientBuilder
              .newWorker()
              .jobType(jobType)
              .handler(new ExampleJobHandler())
              .timeout(Duration.ofSeconds(10))
              .open()) {
        System.out.println("Job worker opened and receiving jobs.");

        // run until System.in receives exit command
        waitUntilSystemInput("exit");
      }
    }
  }

  private static void waitUntilSystemInput(final String exitCode) {
    try (final Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        final String nextLine = scanner.nextLine();
        if (nextLine.contains(exitCode)) {
          return;
        }
      }
    }
  }

  private static class ExampleJobHandler implements JobHandler {
    @Override
    public void handle(final JobClient clientBuilder, final ActivatedJob job) {
      // here: business logic that is executed with every job

      String fetchimageresponse = null;

      System.out.println("Job received details" + job);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = null;

      try {
        jsonNode = objectMapper.readTree(job.getVariables());

      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        fetchimageresponse =
            callEndpoint(
                "http://localhost:8080/animal/"
                    + jsonNode.get("petType").asText()
                    + "/"
                    + jsonNode.get("businessKey").asText());

        System.out.println("Retrieved image URL: " + fetchimageresponse);

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      clientBuilder.newCompleteCommand(job.getKey()).variables(fetchimageresponse).send().join();

      // clientBuilder.newCompleteCommand(job.getKey()).send().join();
    }
  }

  public static String callEndpoint(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.connect();

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("Error: HTTP " + responseCode);
    }

    StringBuilder response = new StringBuilder();

    try (InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    }

    System.out.println(response.toString());

    return response.toString();
  }
}
