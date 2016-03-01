package com.fivestar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 25.11.2015.
 */
public class ListAnswers {
    @Expose
    long id;

    @Expose
    @SerializedName("data")
    ArrayList<Answer> answers;

    public ListAnswers(long id, ArrayList<Answer> answers) {
        this.id = id;
        this.answers = answers;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
