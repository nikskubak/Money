package com.money.recomandation;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.fivestar.models.Category;
import com.fivestar.models.Transaction;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.fivestar.models.converters.TransactionCursorConverter;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.models.CategoriesPair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by skuba on 11.04.2016.
 */
public class CorrelationRecomandationGenerator {

    Context context;
    long startDate, endDate;
    ArrayList<Double> firstCategoryDaySumValues;
    ArrayList<Double> secondCategoryDaySumValues;

    public CorrelationRecomandationGenerator(Context context, long startDate, long endDate) {
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    void startAnalysis() {

    }

    ArrayList<Integer> getCategoryIds() {
        ArrayList<Integer> categoryIds = new ArrayList<>();
        Cursor categoryCursor = context.getContentResolver().query(CategoryContract.CONTENT_URI, null, null, null, null);
        if (categoryCursor != null) {
            CategoryCursorConverter converter = new CategoryCursorConverter();
            for (categoryCursor.moveToFirst(); !categoryCursor.isAfterLast(); categoryCursor.moveToNext()) {
                converter.setCursor(categoryCursor);
                categoryIds.add(converter.getObject().getId());
            }
        }
        return categoryIds;
    }

    ArrayList<CategoriesPair> getCategoriesPairs() {
        ArrayList<Integer> categoryIds = getCategoryIds();
        ArrayList<CategoriesPair> categoriesPairs = new ArrayList<>();
        for (int i = 0; i < categoryIds.size(); i++) {
            for (int j = i + 1; j < categoryIds.size(); j++) {
                categoriesPairs.add(new CategoriesPair(categoryIds.get(i), categoryIds.get(j)));
            }
        }
        return categoriesPairs;
    }

    void getCorrelationRecomandation() {
        ArrayList<CategoriesPair> categoriesPairs = getCategoriesPairs();
        Cursor firstCategoryTransactionsCursor;
        Cursor secondCategoryTransactionsCursor;
        Bundle queryFirstCategoryBundle;
        Bundle querySecondCategoryBundle;
        ArrayList<Transaction> transactionsFirstCategory = new ArrayList<>();
        ArrayList<Transaction> transactionsSecondCategory = new ArrayList<>();
        for (CategoriesPair iterator : categoriesPairs) {
            queryFirstCategoryBundle = DatabaseUtils.getTransactionsFromDB(null, String.valueOf(startDate), String.valueOf(endDate), iterator.getFirstCategory());
            querySecondCategoryBundle = DatabaseUtils.getTransactionsFromDB(null, String.valueOf(startDate), String.valueOf(endDate), iterator.getSecondCategory());
            firstCategoryTransactionsCursor = context.getContentResolver().query(
                    TransactionContract.CONTENT_URI,
                    null,
                    queryFirstCategoryBundle.getString(Constants.TRANSACTION_SELECTION),
                    queryFirstCategoryBundle.getStringArray(Constants.TRANSACTION_SELECTION_ARGS),
                    null
            );
            secondCategoryTransactionsCursor = context.getContentResolver().query(
                    TransactionContract.CONTENT_URI,
                    null,
                    querySecondCategoryBundle.getString(Constants.TRANSACTION_SELECTION),
                    querySecondCategoryBundle.getStringArray(Constants.TRANSACTION_SELECTION_ARGS),
                    null
            );

        }
    }

    void convertCursor(Cursor cursor){
        if (cursor != null) {
            TransactionCursorConverter converter = new TransactionCursorConverter();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                converter.setCursor(cursor);
                converter.getObject().getMoney();
                /////////////// тут нужно сконвертирловать транзакции для определенной категории, чтобы потом посчтить сумму транзакций за каждый день
            }
        }

    }

}
