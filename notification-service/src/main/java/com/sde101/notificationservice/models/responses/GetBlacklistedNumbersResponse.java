package com.sde101.notificationservice.models.responses;

public class GetBlacklistedNumbersResponse {

    private String[] data;

    public String[] getData() {
        return data;
    }

    public GetBlacklistedNumbersResponse(String[] data) {
        this.data = data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
