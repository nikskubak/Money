package com.fivestar.utils;

import android.provider.BaseColumns;

import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;

/**
 * Created by skuba on 24.03.2016.
 */
public class SQLiteHelper {
    public static String selectJoinCategoryAndTransaction() {
        return
                TransactionContract.TABLE_NAME +
                        " JOIN " +
                        CategoryContract.TABLE_NAME +
                        " ON " +
                        addTransactionPrefix(TransactionColumns.CATEGORY) +
                        " = " +
                        addCategoryPrefix(CategoryColumns.ID);
    }

    public static String addTransactionPrefix(String column) {
        return TransactionContract.TABLE_NAME + "." + column;
    }

    public static String addCategoryPrefix(String column) {
        return CategoryContract.TABLE_NAME + "." + column;
    }

    public static String getCategoryColumnWithPrefix(String column) {
        return CategoryContract.TABLE_NAME + "_" + column;
    }
}
