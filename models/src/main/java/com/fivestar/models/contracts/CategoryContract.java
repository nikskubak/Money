package com.fivestar.models.contracts;

import android.net.Uri;

import com.fivestar.DatabaseTypes;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.utils.ContentProviderHelper;

/**
 * Created by android on 11.01.2016.
 */
public interface CategoryContract extends CategoryColumns, DatabaseTypes {

    String TABLE_NAME = ContentProviderHelper.tableName("category");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT , "
            + NAME + " " + TEXT + " ,"
            + TYPE + " " + TEXT
            + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);
    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

}
