package com.fivestar.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by android on 23.11.2015.
 */
public class SpinnerAddresses {
    @Expose
    private ArrayList<Address> items;

    public ArrayList<Address> getItems() {
        return items;
    }

    public void setItems(ArrayList<Address> items) {
        this.items = items;
    }

    public Address getItem (int position){
        return items.get(position);
    }

    @Override
    public String toString() {
        return "SpinnerAddresses{" +
                "items=" + items +
                '}';
    }
}
