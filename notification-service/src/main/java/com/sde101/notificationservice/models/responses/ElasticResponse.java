package com.sde101.notificationservice.models.responses;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class ElasticResponse {
    ArrayList<Map<String, Object>> data;
    String scrollID;

    public ArrayList<Map<String, Object>> getData() {
        return data;
    }

    public void setData(ArrayList<Map<String, Object>> data) {
        this.data = data;
    }

    public String getScrollID() {
        return scrollID;
    }

    public void setScrollID(String scrollID) {
        this.scrollID = scrollID;
    }

    public ElasticResponse(ArrayList<Map<String, Object>> data, String scrollID) {
        this.data = data;
        this.scrollID = scrollID;
    }
}
