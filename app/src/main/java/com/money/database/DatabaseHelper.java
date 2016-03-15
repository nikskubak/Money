package com.money.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fivestar.models.CategoryContract;
import com.fivestar.models.TransactionContract;


/**
 * Created by android on 11.01.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
        super(context, DatabaseConfig.NAME, null, DatabaseConfig.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategoryContract.CREATE_TABLE);
        db.execSQL(TransactionContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CategoryContract.DROP_TABLE);
        db.execSQL(TransactionContract.DROP_TABLE);
        onCreate(db);
    }
}
