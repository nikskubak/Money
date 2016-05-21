package com.fivestar.models;

import com.google.gson.annotations.Expose;

/**
 * Created by skuba on 21.05.2016.
 */
public class DayCount {

    public DayCount() {

    }

    public DayCount(double moneySum, long date) {
        this.moneySum = moneySum;
        this.date = date;
    }

    @Expose
    private long date;

    @Expose
    private double moneySum;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }
}
