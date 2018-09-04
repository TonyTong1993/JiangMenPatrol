package com.ecity.cswatersupply.model;

public class StatusInfoModel {
    private String state;
    private boolean isDisplay;
    private int number;

    public StatusInfoModel(String status, boolean isDisplay, int number) {
        this.state = status;
        this.isDisplay = isDisplay;
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String status) {
        this.state = status;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}