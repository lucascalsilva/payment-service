package com.camunda.training.paymentservice.worker;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChargeCreditCardWorker implements CommandLineRunner {

    private final ExternalTaskClient externalTaskClient;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    public ChargeCreditCardWorker(ExternalTaskClient externalTaskClient) {
        this.externalTaskClient = externalTaskClient;
    }

    @Override
    public void run(String... args) throws Exception {
        externalTaskClient
                .subscribe("credit-card-charging")
                .handler(this::runChargeCreditTask).open();
    }

    private void runChargeCreditTask(ExternalTask externalTask, ExternalTaskService externalTaskService){
        Boolean doError = (Boolean)  externalTask.getVariable("doError");
        if(doError != null && doError){
            log.info("Handling bpmn error to the task {}, in topic {} for process instance id {}", externalTask.getActivityId(), externalTask.getTopicName(), externalTask.getProcessInstanceId());
            externalTaskService.handleBpmnError(externalTask, "ChargeCreditCardFailed");
        }
        else {
            log.info("Completing the task {}, in topic {} for process instance id {}", externalTask.getActivityId(), externalTask.getTopicName(), externalTask.getProcessInstanceId());
            externalTaskService.complete(externalTask);
        }
    }
}
