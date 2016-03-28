package com.money;

/**
 * Created by skuba on 19.03.2016.
 */
public class Constants {
    public static final int ITEM_DRAWER_GAINS = 1;
    public static final int ITEM_DRAWER_COSTS = 0;
    public static final int ITEM_DRAWER_CATEGORIES = 2;

    public static final int TYPE_TRANSACTION_GAIN = 1;
    public static final int TYPE_TRANSACTION_COST = 0;

    public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
    public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL


    public interface LoadersID{
        int LOADER_CATEGORIES = 0;
        int LOADER_TRANSACTIONS = 1;
    }
}
