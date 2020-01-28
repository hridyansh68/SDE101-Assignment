package com.sde101.notificationservice.models.responses;

import com.sde101.notificationservice.models.entity.SmsRequest;
import lombok.Data;

@Data
public class SmsRequestByIdResponse {

    SmsRequest Data;

    public SmsRequestByIdResponse(SmsRequest data) {
        Data = data;
    }

    public SmsRequest getData() {
        return Data;
    }

    public void setData(SmsRequest data) {
        Data = data;
    }
}
