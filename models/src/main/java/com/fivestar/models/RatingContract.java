package com.fivestar.models;

import android.net.Uri;

import com.artjoker.core.database.constants.DatabaseTypes;
import com.fivestar.utils.ContentProviderHelper;

/**
 * Created by android on 11.01.2016.
 */
public interface RatingContract extends RatingColumns, DatabaseTypes {

    String TABLE_NAME = ContentProviderHelper.tableName("fivestar");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT , "
            + NAME + " " + TEXT + " , "
            + PHONE + " " + TEXT + " , "
            + DATE + " " + TEXT + " , "
            + RATING + " " + INTEGER + " , "
            + FORM + " " + TEXT
            + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);
    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

}
