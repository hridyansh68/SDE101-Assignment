package com.sde101.notificationservice.models.requests;


public class AddNumbertoBlacklistBody {
    private String[] phone_numbers;

    public String[] getPhone_numbers() {
        return phone_numbers;
    }

    public void setPhone_numbers(String[] phone_numbers) {
        this.phone_numbers = phone_numbers;
    }
}
