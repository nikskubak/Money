package com.fivestar.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by android on 11.01.2016.
 */
public class DatabaseModel implements Parcelable {

    public static final int ID_ONE = 1;
    public static final int ID_TWO = 2;
    public static final int ID_THREE = 3;
    public static final int ID_FOUR = 4;
    public static final int ID_FIVE = 5;
    public static final int EMPTY_OFFLINE_DATA = -1;

    public DatabaseModel() {
        values = new ContentValues();
        rating = EMPTY_OFFLINE_DATA;
        answers = new ArrayList<>();
        setDate(String.valueOf(System.currentTimeMillis()));
    }

    private ContentValues values;
    private String jsonAnswers;

    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String date;
    @Expose
    private Integer rating;
    @Expose
    private ArrayList<Answer> answers;


    protected DatabaseModel(Parcel in) {
        values = in.readParcelable(ContentValues.class.getClassLoader());
        name = in.readString();
        jsonAnswers = in.readString();
        phone = in.readString();
        date = in.readString();
        answers = in.createTypedArrayList(Answer.CREATOR);
        rating = in.readInt();
    }

    public static final Creator<DatabaseModel> CREATOR = new Creator<DatabaseModel>() {
        @Override
        public DatabaseModel createFromParcel(Parcel in) {
            return new DatabaseModel(in);
        }

        @Override
        public DatabaseModel[] newArray(int size) {
            return new DatabaseModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        values.put(RatingColumns.DATE, date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        values.put(RatingColumns.NAME, name);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        values.put(RatingColumns.PHONE, phone);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        values.put(RatingColumns.RATING, rating);
    }

    public ContentValues getValues() {
        return values;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public String getJsonAnswers() {
        return jsonAnswers;
    }

    public void setJsonAnswers(String jsonAnswers) {
        this.jsonAnswers = jsonAnswers;
        values.put(RatingColumns.FORM, jsonAnswers);
        answers = jsonToObject(jsonAnswers);
    }

    private ArrayList<Answer> jsonToObject(String json) {
        ArrayList<Answer> answers;
        Type type = new TypeToken<ArrayList<Answer>>() {
        }.getType();
        Gson gson = new Gson();
        answers = gson.fromJson(json, type);
        return answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(values, flags);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(date);
        dest.writeString(jsonAnswers);
        dest.writeTypedList(answers);
        dest.writeInt(rating);
    }
}
