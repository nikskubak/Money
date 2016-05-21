package com.fivestar.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by skuba on 15.05.2016.
 */
public class Recommendation {
    int id;
    String description;
    boolean isViewed;
    long date;
    String firstCategoryName;
    String secondCategoryName;
    ArrayList<DayCount> firstCategoryData;
    ArrayList<DayCount> secondCategoryData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    public void setIsViewed(int viewed) {
        this.isViewed = viewed == 1;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }

    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName;
    }

    public ArrayList<DayCount> getFirstCategoryData() {
        return firstCategoryData;
    }

    public String getFirstCategoryDataToJSON() {
        Gson gson = new Gson();
        return gson.toJson(firstCategoryData);
    }

    public void setFirstCategoryData(ArrayList<DayCount> firstCategoryData) {
        this.firstCategoryData = firstCategoryData;
    }

    public void setFirstCategoryDataFromJSON(String json) {
        Type listType = new TypeToken<ArrayList<DayCount>>() {}.getType();
        Gson gson = new Gson();
        this.firstCategoryData = gson.fromJson(json, listType);
    }

    public ArrayList<DayCount> getSecondCategoryData() {
        return secondCategoryData;
    }

    public String getSecondCategoryDataToJSON() {
        Gson gson = new Gson();
        return gson.toJson(secondCategoryData);
    }

    public void setSecondCategoryData(ArrayList<DayCount> secondCategoryData) {
        this.secondCategoryData = secondCategoryData;
    }

    public void setSecondCategoryDataFromJSON(String json) {
        Type listType = new TypeToken<ArrayList<DayCount>>() {}.getType();
        Gson gson = new Gson();
        this.secondCategoryData = gson.fromJson(json, listType);
    }
}
