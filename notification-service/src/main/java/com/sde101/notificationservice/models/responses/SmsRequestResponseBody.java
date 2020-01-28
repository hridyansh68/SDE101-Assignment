package com.sde101.notificationservice.models.responses;

public class SmsRequestResponseBody {
    Integer request_id;
    String comments;

    public SmsRequestResponseBody(Integer id) {
        this.request_id = id;
        this.comments = "Successfully Sent";
    }

    public Integer getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Integer request_id) {
        this.request_id = request_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
