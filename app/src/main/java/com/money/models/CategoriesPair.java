package com.money.models;

import com.fivestar.models.Category;

/**
 * Created by skuba on 12.04.2016.
 */
public class CategoriesPair {

    Category firstCategory;
    Category secondCategory;

    public CategoriesPair(Category firstCategory, Category secondCategory) {
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
    }

    public Category getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(Category firstCategory) {
        this.firstCategory = firstCategory;
    }

    public Category getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(Category secondCategory) {
        this.secondCategory = secondCategory;
    }
}
