package com.money.recomandation;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.fivestar.models.Category;
import com.fivestar.models.Transaction;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.fivestar.models.converters.TransactionCursorConverter;
import com.google.common.primitives.Doubles;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.models.CategoriesPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by skuba on 11.04.2016.
 */
public class CorrelationRecomandationGenerator {

    Context context;
    long startDate, endDate;
    ArrayList<Double> firstCategoryDaySumValues;
    ArrayList<Double> secondCategoryDaySumValues;
    ArrayList<Category> categories;
    int countOfDay;
    int indexOfCurrentDay;

    public CorrelationRecomandationGenerator(Context context, long startDate, long endDate) {
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
        countOfDay = (int) ((endDate - startDate) / DateUtils.DAY_IN_MILLIS);
        indexOfCurrentDay = 0;

        firstCategoryDaySumValues = new ArrayList<>(countOfDay);
        secondCategoryDaySumValues = new ArrayList<>(countOfDay);
        categories = new ArrayList<>();
    }

    ArrayList<Integer> getCategoryIds() {
        ArrayList<Integer> categoryIds = new ArrayList<>();
        Cursor categoryCursor = context.getContentResolver().query(CategoryContract.CONTENT_URI, null, null, null, null);
        if (categoryCursor != null) {
            CategoryCursorConverter converter = new CategoryCursorConverter();
            for (categoryCursor.moveToFirst(); !categoryCursor.isAfterLast(); categoryCursor.moveToNext()) {
                converter.setCursor(categoryCursor);
                categories.add(converter.getObject());
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

    public void getCorrelationRecomandation() {
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
//            convertCursor(firstCategoryTransactionsCursor);
            secondCategoryTransactionsCursor = context.getContentResolver().query(
                    TransactionContract.CONTENT_URI,
                    null,
                    querySecondCategoryBundle.getString(Constants.TRANSACTION_SELECTION),
                    querySecondCategoryBundle.getStringArray(Constants.TRANSACTION_SELECTION_ARGS),
                    null
            );
            if (iterator.getFirstCategory() == 1 && iterator.getSecondCategory() == 2) {
                DatabaseUtils.logCursor(firstCategoryTransactionsCursor);
                firstCategoryDaySumValues = convertCursor(firstCategoryTransactionsCursor);
                secondCategoryDaySumValues = convertCursor(secondCategoryTransactionsCursor);

                DatabaseUtils.logCursor(secondCategoryTransactionsCursor);
                Log.e("coef correlation", " " + MathHelper.getCorrelationCoefficient(firstCategoryDaySumValues, secondCategoryDaySumValues));
                Log.e("picks first values", " " + MathHelper.findPickValues(firstCategoryDaySumValues).toString());
                Log.e("picks second values", " " + MathHelper.findPickValues(secondCategoryDaySumValues).toString());

                if (matchingPicks(MathHelper.findPickValues(firstCategoryDaySumValues), MathHelper.findPickValues(secondCategoryDaySumValues)) >= Constants.PICK_CONSTANT) {
                    Log.e("matchingPicks", " " + matchingPicks(MathHelper.findPickValues(firstCategoryDaySumValues), MathHelper.findPickValues(secondCategoryDaySumValues)));
                    //нужно создать табоицу рекомендаций и заносить туда данные
                    ContentValues values = new ContentValues();
                    values.put(RecommendationContract.DESCRIPTION, "Существует зависимость между категорией " + iterator.getFirstCategory() +
                    " и категорией" + iterator.getSecondCategory());
                    values.put(RecommendationContract.TYPE, "Matching picks");
                    values.put(RecommendationContract.DATE, System.currentTimeMillis());
                    values.put(RecommendationContract.VIEWED, 1);
                    context.getContentResolver().insert(RecommendationContract.CONTENT_URI, values);
                }

            }
        }
    }

    private int matchingPicks(ArrayList<Integer> firstPicks, ArrayList<Integer> secondPicks) {
        int countOfConcurrency = 0;
        for (int i = 0; i < firstPicks.size(); i++) {
            for (int j = 0; j < secondPicks.size(); j++) {
                if (firstPicks.get(i).intValue() == secondPicks.get(j).intValue()) {
                    countOfConcurrency++;
                }
            }
        }
        return countOfConcurrency;
    }

    ArrayList<Double> convertCursor(Cursor cursor) {
        ArrayList<Double> resultArrayList = new ArrayList<>();
        double[] categoryDaySum = new double[countOfDay];
        for (int i = 0; i < categoryDaySum.length; i++) {
            categoryDaySum[i] = 0;
        }
        if (cursor != null) {
            TransactionCursorConverter converter = new TransactionCursorConverter();
            Transaction currentTransaction;

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                converter.setCursor(cursor);
                currentTransaction = converter.getObject();
//                isConsistedDay(currentTransaction.getDate());
//                categoryDaySum[indexOfCurrentDay] = categoryDaySum[indexOfCurrentDay] + currentTransaction.getMoney();
                Date date = new Date(currentTransaction.getDate());
                Log.e("Correlation", date.toString());
                calculateMoneyForOneDay(currentTransaction, categoryDaySum);

                /////////////// тут нужно сконвертирловать транзакции для определенной категории, чтобы потом посчтить сумму транзакций за каждый день
            }

            for (int i = 0; i < categoryDaySum.length; i++) {
                Log.e("Correlation", "categoryDaySum" + "[" + i + "] = " + categoryDaySum[i]);
            }

            for (int i = 0; i < categoryDaySum.length; i++) {
                resultArrayList.add(categoryDaySum[i]);
            }
        }
        return resultArrayList;
    }

    boolean calculateMoneyForOneDay(Transaction transaction, double[] categoryDaySum) {
        //проверка на то, что транзакция совершенна в конкретный день. Нужно для нахождения общей суммы за день
        for (int i = 0; i < countOfDay; i++) {
            long startDateInterval = startDate + i * DateUtils.DAY_IN_MILLIS;
            long finishDateInterval = startDateInterval + DateUtils.DAY_IN_MILLIS;
            if (transaction.getDate() > startDateInterval && transaction.getDate() < finishDateInterval) {
                categoryDaySum[i] += transaction.getMoney();
                return true;
            }
        }
        return false;
    }

}
