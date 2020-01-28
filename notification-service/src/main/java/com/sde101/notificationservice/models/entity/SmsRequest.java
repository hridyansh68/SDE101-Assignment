package com.sde101.notificationservice.models.entity;


import com.sde101.notificationservice.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="SMS_REQUEST")
public class SmsRequest extends BaseEntity {
    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Integer id;

    @Column(nullable = false)
    private String phoneNumber;

    String message;

    Integer status;

    Integer failureCode;

    String failureComments;

    public SmsRequest(String phone_number, String message) {
        super();
        this.phoneNumber = phone_number;
        this.message = message;
    }

    public SmsRequest(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(Integer failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureComments() {
        return failureComments;
    }

    public void setFailureComments(String failureComments) {
        this.failureComments = failureComments;
    }
}
