package com.sde101.notificationservice.models.requests;

public class ElasticRequest {
    String text;
    String scrollId;

    public ElasticRequest(String text, String scrollId) {
        this.text = text;
        this.scrollId = scrollId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}
