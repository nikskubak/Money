package com.fivestar.models;

import com.google.gson.annotations.Expose;

/**
 * Created by android on 23.11.2015.
 */
public class Address {

    @Expose
    private long id;

    @Expose
    private String addr;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", addr='" + addr + '\'' +
                '}';
    }
}
