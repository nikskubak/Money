package com.fivestar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by android on 25.11.2015.
 */
public class Answer implements Parcelable {
    @Expose
    int id;

    @Expose
    int answer;

    public Answer(int idQuestion, int mark) {
        this.id = idQuestion;
        this.answer = mark;
    }

    protected Answer(Parcel in) {
        id = in.readInt();
        answer = in.readInt();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public int getIdQuestion() {
        return id;
    }

    public void setIdQuestion(int idQuestion) {
        this.id = idQuestion;
    }

    public int getMsrk() {
        return answer;
    }

    public void setMsrk(int msrk) {
        this.answer = msrk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(answer);
    }
}
