package com.fivestar.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.fivestar.models.Category;
import com.fivestar.models.Transaction;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;

/**
 * Created by skuba on 19.03.2016.
 */
public class TransactionCursorConverter extends AbstractCursorConverter<Transaction> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_DESCRIPTION;
    private int COLUMN_INDEX_CATEGORY;
    private int COLUMN_INDEX_MONEY;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_TYPE;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(TransactionColumns.ID);
            COLUMN_INDEX_DESCRIPTION = cursor.getColumnIndex(TransactionColumns.DESCRIPTION);
            COLUMN_INDEX_CATEGORY = cursor.getColumnIndex(TransactionColumns.CATEGORY);
            COLUMN_INDEX_MONEY = cursor.getColumnIndex(TransactionColumns.MONEY);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(TransactionColumns.DATE);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(TransactionColumns.TYPE);
        }
    }

    @Override
    public Transaction getObject() {
        Transaction transaction = null;
        if (isValid()) {
            transaction = new Transaction();
            transaction.setId(getCursor().getInt(COLUMN_INDEX_ID));
            transaction.setCategory(getCursor().getInt(COLUMN_INDEX_CATEGORY));
            transaction.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            transaction.setDescription(getCursor().getString(COLUMN_INDEX_DESCRIPTION));
            transaction.setMoney(getCursor().getDouble(COLUMN_INDEX_MONEY));
            transaction.setType(getCursor().getString(COLUMN_INDEX_TYPE));
        }
        return transaction;
    }
}
