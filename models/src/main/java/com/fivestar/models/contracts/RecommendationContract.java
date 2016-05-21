package com.fivestar.models.contracts;

import android.net.Uri;

import com.artjoker.core.database.constants.DatabaseTypes;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.RecommendationColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.utils.ContentProviderHelper;

/**
 * Created by skuba on 15.05.2016.
 */
public interface RecommendationContract extends RecommendationColumns, DatabaseTypes {

    String TABLE_NAME = ContentProviderHelper.tableName("recommendation");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT , "
            + DESCRIPTION + " " + TEXT + ", "
            + DATE + " " + TEXT + ", "
            + NAME_CATEGORY_FIRST + " " + TEXT + ", "
            + NAME_CATEGORY_SECOND + " " + TEXT + ", "
            + DATA_CATEGORY_FIRST + " " + TEXT + ", "
            + DATA_CATEGORY_SECOND + " " + TEXT + ", "
            + VIEWED + " " + INTEGER
            + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);
    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

}
