package com.camunda.training.paymentservice.dto;

import org.camunda.bpm.engine.variable.value.TypedValue;

public class VariableDto {

    private Object value;
    private String type;
    private ValueInfoDto valueInfo;

    public VariableDto() {
    }

    public VariableDto(Object value, String type) {
        this.value = value;
        this.type = type;
    }

    public VariableDto(Object value, String type, ValueInfoDto valueInfo) {
        this.value = value;
        this.type = type;
        this.valueInfo = valueInfo;
    }

    public static VariableDto fromVariableTyped(TypedValue typedValue){
        VariableDto variableDto = new VariableDto();
        variableDto.setValue(typedValue.getValue());
        variableDto.setType(typedValue.getType().getName());

        ValueInfoDto valueInfoDto = new ValueInfoDto();
        valueInfoDto.setTransient(typedValue.isTransient());

        variableDto.setValueInfo(valueInfoDto);

        return variableDto;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ValueInfoDto getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(ValueInfoDto valueInfo) {
        this.valueInfo = valueInfo;
    }
}
