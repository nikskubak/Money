package com.money.recomandation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.fivestar.models.Category;
import com.fivestar.models.DayCount;
import com.fivestar.models.Transaction;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.fivestar.models.converters.TransactionCursorConverter;
import com.google.gson.Gson;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.models.CategoriesPair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by skuba on 11.04.2016.
 */
public class CorrelationRecomandationGenerator {

    Context context;
    long startDate, endDate;
    ArrayList<Double> firstCategoryDaySumValues;
    ArrayList<Double> secondCategoryDaySumValues;
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
    }

    ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor categoryCursor = context.getContentResolver().query(CategoryContract.CONTENT_URI, null, null, null, null);
        if (categoryCursor != null) {
            CategoryCursorConverter converter = new CategoryCursorConverter();
            for (categoryCursor.moveToFirst(); !categoryCursor.isAfterLast(); categoryCursor.moveToNext()) {
                converter.setCursor(categoryCursor);
                categories.add(converter.getObject());
            }
        }
        return categories;
    }

    ArrayList<CategoriesPair> getCategoriesPairs() {
        ArrayList<Category> categories = getCategories();
        ArrayList<CategoriesPair> categoriesPairs = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = i + 1; j < categories.size(); j++) {
                categoriesPairs.add(new CategoriesPair(categories.get(i), categories.get(j)));
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
            queryFirstCategoryBundle = DatabaseUtils.getTransactionsFromDB(null, String.valueOf(startDate), String.valueOf(endDate), iterator.getFirstCategory().getId());
            querySecondCategoryBundle = DatabaseUtils.getTransactionsFromDB(null, String.valueOf(startDate), String.valueOf(endDate), iterator.getSecondCategory().getId());
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
            if (iterator.getFirstCategory().getId() == 1 && iterator.getSecondCategory().getId() == 2) {
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
                    values.put(RecommendationContract.DESCRIPTION, "Существует зависимость между категорией " + iterator.getFirstCategory().getName() +
                            " и категорией " + iterator.getSecondCategory().getName());
                    values.put(RecommendationContract.DATE, System.currentTimeMillis());
                    values.put(RecommendationContract.VIEWED, 1);

                    values.put(RecommendationContract.NAME_CATEGORY_FIRST, iterator.getFirstCategory().getName());
                    values.put(RecommendationContract.NAME_CATEGORY_SECOND, iterator.getSecondCategory().getName());
                    values.put(RecommendationContract.DATA_CATEGORY_FIRST, initCategoryData(firstCategoryDaySumValues));
                    values.put(RecommendationContract.DATA_CATEGORY_SECOND, initCategoryData(secondCategoryDaySumValues));
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

    private String initCategoryData(ArrayList<Double> values) {
        ArrayList<DayCount> result = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < values.size(); i++) {
            result.add(new DayCount(values.get(i), startDate + i * DateUtils.DAY_IN_MILLIS));
        }
        return gson.toJson(result);
    }

}
