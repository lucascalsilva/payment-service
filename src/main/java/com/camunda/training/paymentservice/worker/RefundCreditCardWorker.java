package com.camunda.training.paymentservice.worker;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RefundCreditCardWorker implements CommandLineRunner {

    private final ExternalTaskClient externalTaskClient;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    public RefundCreditCardWorker(ExternalTaskClient externalTaskClient) {
        log.info("Creating refund worker...");
        this.externalTaskClient = externalTaskClient;
    }

    @Override
    public void run(String... args) throws Exception {
        externalTaskClient
                .subscribe("credit-refunding")
                .handler(this::runRefundCredit).open();
    }

    private void runRefundCredit(ExternalTask externalTask, ExternalTaskService externalTaskService){
        log.info("Completing the task {}, in topic {} for process instance id {}", externalTask.getActivityId(), externalTask.getTopicName(), externalTask.getProcessInstanceId());
        externalTaskService.complete(externalTask);
    }
}
