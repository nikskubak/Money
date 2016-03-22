package com.money.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.money.R;


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
        initDefaultCategories(db,
                context.getResources().getStringArray(R.array.default_name_categories_costs),
                context.getResources().getString(R.string.category_type_cost));
        initDefaultCategories(db,
                context.getResources().getStringArray(R.array.default_name_categories_gains),
                context.getResources().getString(R.string.category_type_gain));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("onCreate", "onUpgrade");
        db.execSQL(CategoryContract.DROP_TABLE);
        db.execSQL(TransactionContract.DROP_TABLE);
        onCreate(db);
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
}
