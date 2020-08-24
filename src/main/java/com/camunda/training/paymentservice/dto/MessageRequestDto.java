package com.camunda.training.paymentservice.dto;

import org.camunda.bpm.client.variable.impl.TypedValueField;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.Map;

public class MessageRequestDto{

	private String messageName;
	private String businessKey;
	protected Map<String, VariableDto> processVariables;
	protected Map<String, VariableDto> correlationKeys;

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Map<String, VariableDto> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(Map<String, VariableDto> processVariables) {
		this.processVariables = processVariables;
	}

	public Map<String, VariableDto> getCorrelationKeys() {
		return correlationKeys;
	}

	public void setCorrelationKeys(Map<String, VariableDto> correlationKeys) {
		this.correlationKeys = correlationKeys;
	}
}
