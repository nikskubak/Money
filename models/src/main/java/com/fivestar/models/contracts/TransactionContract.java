package com.fivestar.models.contracts;

import android.net.Uri;

import com.artjoker.core.database.constants.DatabaseTypes;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.utils.ContentProviderHelper;

/**
 * Created by skuba on 06.03.2016.
 */
public interface TransactionContract extends TransactionColumns, DatabaseTypes {

    String TABLE_NAME = ContentProviderHelper.tableName("transaction");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT , "
            + DESCRIPTION + " " + TEXT + ", "
            + CATEGORY + " " + INTEGER + ", "
            + MONEY + " " + REAL + ", "
            + DATE + " " + TEXT + ", "
            + TYPE + " " + INTEGER + ", "
            + "FOREIGN KEY(" + CATEGORY + ") REFERENCES " + CategoryContract.TABLE_NAME + "(" + CategoryColumns.ID + ")"
            + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);
    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
