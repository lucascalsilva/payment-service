package com.camunda.training.paymentservice.worker;

import com.camunda.training.paymentservice.dto.MessageRequestDto;
import com.camunda.training.paymentservice.dto.VariableDto;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.impl.TypedValueField;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.camunda.training.paymentservice.dto.VariableDto.fromVariableTyped;

@Component
public class SendPaymentWorker implements CommandLineRunner {

    private final ExternalTaskClient externalTaskClient;
    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private static String CAMUNDA_REST_API_URL = "http://localhost:8080/engine-rest";
    private static String CAMUNDA_MESSAGE_API = "/message";


    public SendPaymentWorker(ExternalTaskClient externalTaskClient, RestTemplate restTemplate) {
        this.externalTaskClient = externalTaskClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        externalTaskClient
                .subscribe("confirmation-sending")
                .handler(this::sendPayment).open();
    }

    private void sendPayment(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.info("Completing the task {}, in topic {} for process instance id {}", externalTask.getActivityId(), externalTask.getTopicName(), externalTask.getProcessInstanceId());
        MessageRequestDto messageRequestDto = new MessageRequestDto();
        Map<String, VariableDto> variables = new HashMap<String, VariableDto>();

        VariableDto orderId = new VariableDto(externalTask.getBusinessKey(), "String");

        variables.put("orderId", orderId);
        variables.put("paymentAmount", fromVariableTyped(externalTask.getVariableTyped("paymentAmount")));
        variables.put("doError", fromVariableTyped(externalTask.getVariableTyped("doError")));
        variables.put("doFailure", fromVariableTyped(externalTask.getVariableTyped("doFailure")));
        variables.put("resolvable", fromVariableTyped(externalTask.getVariableTyped("resolvable")));

        messageRequestDto.setBusinessKey(UUID.randomUUID().toString());
        messageRequestDto.setProcessVariables(variables);
        messageRequestDto.setMessageName("PaymentRequestedMessage");
        ResponseEntity<String> response = restTemplate.postForEntity(CAMUNDA_REST_API_URL + CAMUNDA_MESSAGE_API, messageRequestDto, String.class);

        if(response.getStatusCode().equals(HttpStatus.NO_CONTENT) || response.getStatusCode().equals(HttpStatus.OK)){
            externalTaskService.complete(externalTask);
        }

    }
}
