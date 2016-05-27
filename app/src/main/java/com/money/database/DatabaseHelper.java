package com.money.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.models.contracts.TransactionContract;
import com.money.R;

import java.util.Calendar;


/**
 * Created by android on 11.01.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;

    DatabaseHelper(Context context) {
        super(context, DatabaseConfig.NAME, null, DatabaseConfig.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("onCreate", "onCreate");
        db.execSQL(CategoryContract.CREATE_TABLE);
        db.execSQL(TransactionContract.CREATE_TABLE);
        db.execSQL(RecommendationContract.CREATE_TABLE);
        initDefaultCategories(db,
                context.getResources().getStringArray(R.array.default_name_categories_costs),
                context.getResources().getString(R.string.category_type_cost));
        initDefaultCategories(db,
                context.getResources().getStringArray(R.array.default_name_categories_gains),
                context.getResources().getString(R.string.category_type_gain));
//        initDefaultTransactions(db,
//                1,
//                context.getResources().getString(R.string.category_type_cost));
//        initDefaultTransactions(db,
//                2,
//                context.getResources().getString(R.string.category_type_cost));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("onCreate", "onUpgrade");
        if(oldVersion < newVersion) {
            db.execSQL(CategoryContract.DROP_TABLE);
            db.execSQL(TransactionContract.DROP_TABLE);
            db.execSQL(RecommendationContract.DROP_TABLE);
            onCreate(db);
        }
    }

    void initDefaultCategories(SQLiteDatabase db, String names[], String type) {
        ContentValues values;
        for (int i = 0; i < names.length; i++) {
            values = new ContentValues();
            values.put(CategoryColumns.NAME, names[i]);
            values.put(CategoryColumns.TYPE, type);
            db.insert(CategoryContract.TABLE_NAME, null, values);
        }
    }

    void initDefaultTransactions(SQLiteDatabase db, int category_id, String type) {
        ContentValues values;
        for (int i = 0; i < 4; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016,4,8 + 2*i,0,0,1 + i);
            values = new ContentValues();
//            long date = System.currentTimeMillis() + ((i * DateUtils.DAY_IN_MILLIS)/8);
            values.put(TransactionColumns.CATEGORY, category_id);
            values.put(TransactionColumns.MONEY, i % 3 == 0 ? 10 : 1);
            values.put(TransactionColumns.DATE, calendar.getTime().getTime());
            values.put(TransactionColumns.TYPE, type);
            values.put(TransactionColumns.DESCRIPTION, "description #" + i);
            db.insert(TransactionContract.TABLE_NAME, null, values);
        }
    }
}
