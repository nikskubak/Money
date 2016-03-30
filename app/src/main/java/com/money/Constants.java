package com.money;

/**
 * Created by skuba on 19.03.2016.
 */
public class Constants {
    public static final int ITEM_DRAWER_OPERATIONS = 0;
    public static final int ITEM_DRAWER_CATEGORIES = 2;
    public static final int ITEM_DRAWER_MAIN = 3;

    public static final int TYPE_TRANSACTION_GAIN = 1;
    public static final int TYPE_TRANSACTION_COST = 0;

    public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
    public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL

    public static final String TRANSACTION_SELECTION = "TRANSACTION_SELECTION";
    public static final String TRANSACTION_SELECTION_ARGS = "TRANSACTION_SELECTION_ARGS";

;

    public interface LoadersID{
        int LOADER_CATEGORIES = 0;
        int LOADER_TRANSACTIONS = 1;
    }
}
