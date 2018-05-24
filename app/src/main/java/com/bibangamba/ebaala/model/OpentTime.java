package com.bibangamba.ebaala.model;

/**
 * Created by davy on 4/20/2018.
 */

public class OpentTime {

    private String day;
    private String open;

    public OpentTime(String day, String open) {
        this.day = day;
        this.open = open;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
