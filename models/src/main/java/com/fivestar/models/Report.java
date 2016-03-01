package com.fivestar.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by android on 12.01.2016.
 */
public class Report {

    @Expose
    long id;

    @Expose
    ArrayList<DatabaseModel> data;

    public Report(long id, ArrayList<DatabaseModel> data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<DatabaseModel> getData() {
        return data;
    }

    public void setData(ArrayList<DatabaseModel> data) {
        this.data = data;
    }
}
