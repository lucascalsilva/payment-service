package com.camunda.training.paymentservice;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

public class PaymentProcessTest {

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    @Before
    public void setup(){
        init(rule.getProcessEngine());
    }

    @Deployment(resources = "payment_process.bpmn")
    @Test
    public void testHappyPath(){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("creditSufficient", false);

        ProcessInstance processInstance = runtimeService().startProcessInstanceByMessage("PaymentRequestedMessage", variables);
        assertThat(processInstance).isStarted().isWaitingAt("ChargeCreditTask");

        complete(externalTask());

        assertThat(processInstance).isWaitingAt("ChargeCreditCardTask");

        complete(externalTask());

        assertThat(processInstance).isWaitingAt("PaymentFinishedEndEvent").externalTask().hasTopicName("payment-finishing");

        complete(externalTask());

        assertThat(processInstance).isEnded();
    }
}
