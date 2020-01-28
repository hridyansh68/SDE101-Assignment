package com.sde101.notificationservice.models.responses;

public class AddNumbertoBlacklistResponseBody {

    private String data;

    public AddNumbertoBlacklistResponseBody(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
