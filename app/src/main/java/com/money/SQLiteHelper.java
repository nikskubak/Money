package com.money;

import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;

/**
 * Created by skuba on 24.03.2016.
 */
public class SQLiteHelper {
    public static String selectJoinCategoryAndTransaction(){
        return "SELECT * FROM " +
                TransactionContract.TABLE_NAME +
                " JOIN " +
                CategoryContract.TABLE_NAME +
                " ON " +
                TransactionColumns.CATEGORY +
                " = " +
                CategoryColumns.ID;
    }
}
