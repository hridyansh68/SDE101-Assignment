package com.sde101.notificationservice.models.requests;

public class SmsRequestPostBody {
    String phone_number;
    String message;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
