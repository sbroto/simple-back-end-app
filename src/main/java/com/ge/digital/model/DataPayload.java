package com.ge.digital.model;

public class DataPayload {
    private String tagName;
    private float value;

    public DataPayload() {
    }

    public DataPayload(String tagName, float value) {
        this.tagName = tagName;
        this.value = value;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
