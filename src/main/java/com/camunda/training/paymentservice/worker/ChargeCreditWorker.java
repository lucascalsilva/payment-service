package com.camunda.training.paymentservice.worker;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChargeCreditWorker implements CommandLineRunner {

    private final ExternalTaskClient externalTaskClient;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    public ChargeCreditWorker(ExternalTaskClient externalTaskClient) {
        this.externalTaskClient = externalTaskClient;
    }

    @Override
    public void run(String... args) throws Exception {
        externalTaskClient
                .subscribe("credit-charging")
                .handler(this::runChargeCreditTask).open();
    }

    private void runChargeCreditTask(ExternalTask externalTask, ExternalTaskService externalTaskService){
        try {
            log.info("Completing the task {}, in topic {} for process instance id {}", externalTask.getActivityId(),
                    externalTask.getTopicName(), externalTask.getProcessInstanceId());
            Long paymentAmount = (Long) externalTask.getVariable("paymentAmount");
            Boolean doFailure = (Boolean) externalTask.getVariable("doFailure");
            Boolean creditSufficient = true;

            if(doFailure != null && doFailure){
                Integer retries = getRetries(externalTask);
                externalTaskService.handleFailure(externalTask, "You asked for a failure...", "", retries, 10000);
            }else{
                if (paymentAmount >= 1000) {
                    creditSufficient = false;
                }

                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("creditSufficient", creditSufficient);
                externalTaskService.complete(externalTask, variables);
            }
        }
        catch(Exception e){
            log.info("Issue when completing task {}, in topic {} for process instance id {}", externalTask.getActivityId(),
                    externalTask.getTopicName(), externalTask.getProcessInstanceId());

            Integer retries = getRetries(externalTask);

            externalTaskService.handleFailure(externalTask,
                    e.getMessage(), Arrays.toString(e.getStackTrace()), retries, 10000);
        }

    }

    private Integer getRetries(ExternalTask externalTask) {
        Integer retries = externalTask.getRetries();

        if(retries == null){
            retries = 3;
        }
        else{
            retries -= 1;
        }
        return retries;
    }
}
