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
    private int COLUMN_INDEX_TYPE;
    private int COLUMN_INDEX_VIEWED;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {

            COLUMN_INDEX_ID = cursor.getColumnIndex(RecommendationContract.ID);
            COLUMN_INDEX_DESCRIPTION = cursor.getColumnIndex(RecommendationContract.DESCRIPTION);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(RecommendationContract.DATE);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(RecommendationContract.TYPE);
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
            recommendation.setType(getCursor().getString(COLUMN_INDEX_TYPE));
            recommendation.setIsViewed(getCursor().getInt(COLUMN_INDEX_VIEWED));
        }
        return recommendation;
    }
}

