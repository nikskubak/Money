package com.money.models;

/**
 * Created by skuba on 12.04.2016.
 */
public class CategoriesPair {

    int firstCategory;
    int secondCategory;

    public CategoriesPair(int firstCatrgory, int secondCatrgory) {
        this.firstCategory = firstCatrgory;
        this.secondCategory = secondCatrgory;
    }

    public int getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(int firstCategory) {
        this.firstCategory = firstCategory;
    }

    public int getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(int secondCategory) {
        this.secondCategory = secondCategory;
    }
}
