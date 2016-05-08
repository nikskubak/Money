package com.money;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.utils.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by android on 11.01.2016.
 */
public class DatabaseUtils {
    public static void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.e(DatabaseUtils.class.getSimpleName(), str);
                } while (c.moveToNext());
            }
        } else
            Log.e(DatabaseUtils.class.getSimpleName(), "Cursor is null");
    }

    public static Bundle getTransactionsFromDB(String type, String startDate, String endDate, Integer categoryId) {
        StringBuilder selection = new StringBuilder();
        Bundle bundle = new Bundle();
        ArrayList<String> selectionArgs = new ArrayList<>();
        if (!TextUtils.isEmpty(type)) {
            selectionArgs.add(type);
            selection.append(TransactionColumns.TYPE).append("=?");
            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                selection
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append(">?")
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append("<?");
                selectionArgs.add(startDate);
                selectionArgs.add(endDate);
            }
            if (categoryId != null) {
                selection
                        .append(" AND ")
                        .append(SQLiteHelper.getCategoryColumnWithPrefix(CategoryColumns.ID)).append("=?");
                selectionArgs.add(String.valueOf(categoryId));
            }

        } else {
            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                selection
                        .append(TransactionColumns.DATE).append(">?")
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append("<?");
                selectionArgs.add(startDate);
                selectionArgs.add(endDate);
                if (categoryId != null) {
                    selection
                            .append(" AND ")
                            .append(SQLiteHelper.getCategoryColumnWithPrefix(CategoryColumns.ID)).append("=?");
                    selectionArgs.add(String.valueOf(categoryId));
                }
            } else {
                if (categoryId != null) {
                    selection
                            .append(SQLiteHelper.getCategoryColumnWithPrefix(CategoryColumns.ID)).append("=?");
                    selectionArgs.add(String.valueOf(categoryId));
                }
            }
        }
        bundle.putString(Constants.TRANSACTION_SELECTION, selection.toString());
        bundle.putStringArray(Constants.TRANSACTION_SELECTION_ARGS, selectionArgs.toArray(new String[selectionArgs.size()]));
//        Log.e("getTransactionsFromDB", "selection = " + selection.toString() + " selectionArgs = " + selectionArgs.toString());
        return bundle;
    }
}
