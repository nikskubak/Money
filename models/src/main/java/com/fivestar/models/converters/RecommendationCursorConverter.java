package com.fivestar.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.fivestar.models.Recommendation;
import com.fivestar.models.Transaction;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.utils.SQLiteHelper;

/**
 * Created by skuba on 15.05.2016.
 */
public class RecommendationCursorConverter extends AbstractCursorConverter<Recommendation> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_DESCRIPTION;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_VIEWED;
    private int COLUMN_INDEX_NAME_FIRST_CATEGORY;
    private int COLUMN_INDEX_NAME_SECOND_CATEGORY;
    private int COLUMN_INDEX_DATA_FIRST_CATEGORY;
    private int COLUMN_INDEX_DATA_SECOND_CATEGORY;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {

            COLUMN_INDEX_ID = cursor.getColumnIndex(RecommendationContract.ID);
            COLUMN_INDEX_DESCRIPTION = cursor.getColumnIndex(RecommendationContract.DESCRIPTION);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(RecommendationContract.DATE);
            COLUMN_INDEX_NAME_FIRST_CATEGORY = cursor.getColumnIndex(RecommendationContract.NAME_CATEGORY_FIRST);
            COLUMN_INDEX_NAME_SECOND_CATEGORY = cursor.getColumnIndex(RecommendationContract.NAME_CATEGORY_SECOND);
            COLUMN_INDEX_DATA_FIRST_CATEGORY = cursor.getColumnIndex(RecommendationContract.DATA_CATEGORY_FIRST);
            COLUMN_INDEX_DATA_SECOND_CATEGORY = cursor.getColumnIndex(RecommendationContract.DATA_CATEGORY_SECOND);
            COLUMN_INDEX_VIEWED = cursor.getColumnIndex(RecommendationContract.VIEWED);
        }
    }

    @Override
    public Recommendation getObject() {
        Recommendation recommendation = null;
        if (isValid()) {
            recommendation = new Recommendation();
            recommendation.setId(getCursor().getInt(COLUMN_INDEX_ID));
            recommendation.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            recommendation.setDescription(getCursor().getString(COLUMN_INDEX_DESCRIPTION));
            recommendation.setIsViewed(getCursor().getInt(COLUMN_INDEX_VIEWED));

            recommendation.setFirstCategoryName(getCursor().getString(COLUMN_INDEX_NAME_FIRST_CATEGORY));
            recommendation.setSecondCategoryName(getCursor().getString(COLUMN_INDEX_NAME_SECOND_CATEGORY));
            recommendation.setFirstCategoryDataFromJSON(getCursor().getString(COLUMN_INDEX_DATA_FIRST_CATEGORY));
            recommendation.setSecondCategoryDataFromJSON(getCursor().getString(COLUMN_INDEX_DATA_SECOND_CATEGORY));
        }
        return recommendation;
    }
}

