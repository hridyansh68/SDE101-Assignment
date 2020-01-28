package com.sde101.notificationservice.models.requests;

public class DateRequestBody {
    private String phoneNumber;
    private int fyear;
    private int fmonth;
    private int fday;
    private int tyear;
    private int tmonth;
    private int tday;

    public DateRequestBody() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getFyear() {
        return fyear;
    }

    public void setFyear(int fyear) {
        this.fyear = fyear;
    }

    public int getFmonth() {
        return fmonth;
    }

    public void setFmonth(int fmonth) {
        this.fmonth = fmonth;
    }

    public int getFday() {
        return fday;
    }

    public void setFday(int fday) {
        this.fday = fday;
    }

    public int getTyear() {
        return tyear;
    }

    public void setTyear(int tyear) {
        this.tyear = tyear;
    }

    public int getTmonth() {
        return tmonth;
    }

    public void setTmonth(int tmonth) {
        this.tmonth = tmonth;
    }

    public int getTday() {
        return tday;
    }

    public void setTday(int tday) {
        this.tday = tday;
    }
}
