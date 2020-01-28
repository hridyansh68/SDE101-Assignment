package com.sde101.notificationservice.models.responses;


import lombok.Data;

@Data
public class IsBlaklistedResponse {
    String phoneNumber;
    Boolean isBlacklisted;

    public IsBlaklistedResponse(String phoneNumber, Boolean isBlacklisted) {
        this.phoneNumber = phoneNumber;
        this.isBlacklisted = isBlacklisted;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        isBlacklisted = blacklisted;
    }
}
