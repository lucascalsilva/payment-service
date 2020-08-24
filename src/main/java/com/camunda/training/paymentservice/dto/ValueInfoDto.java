package com.camunda.training.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValueInfoDto {

    private String objectTypeName;
    private String serializationDataFormat;
    private String filename;
    private String mimetype;
    private String encoding;

    @JsonProperty("transient")
    private Boolean isTransient;

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    public String getSerializationDataFormat() {
        return serializationDataFormat;
    }

    public void setSerializationDataFormat(String serializationDataFormat) {
        this.serializationDataFormat = serializationDataFormat;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Boolean getTransient() {
        return isTransient;
    }

    public void setTransient(Boolean aTransient) {
        isTransient = aTransient;
    }
}
