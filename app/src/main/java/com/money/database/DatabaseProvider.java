package com.money.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.database.SelectionBuilder;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.utils.ContentProviderConfig;
import com.fivestar.utils.SQLiteHelper;

import java.util.HashMap;


/**
 * Created by android on 11.01.2016.
 */
public class DatabaseProvider extends SecureDatabaseProvider {
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, CategoryContract.TABLE_NAME, Config.CATEGORY_DIR_ID, Config.CATEGORY_ITEM_ID);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, TransactionContract.TABLE_NAME, Config.TRANSACTION_DIR_ID, Config.TRANSACTION_ITEM_ID);
    }

    @Override
    protected SQLiteOpenHelper getNewDatabaseHelper() {
        Log.e("onCreate", "getNewDatabaseHelper");
        return new DatabaseHelper(getContext());
    }

    @Override
    protected SelectionBuilder getSimpleSelectionBuilder(Uri uri, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                appendSelection(builder, CategoryContract.TABLE_NAME, selection, selectionArgs);
                return builder;
            case Config.TRANSACTION_DIR_ID:
                appendSelection(builder, TransactionContract.TABLE_NAME, selection, selectionArgs);
                return builder;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase database = getInstance();
        final Cursor cursor;
        if (getType(uri).equals(TransactionContract.CONTENT_TYPE)) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(SQLiteHelper.selectJoinCategoryAndTransaction());
            queryBuilder.setProjectionMap(buildColumnMap());
            cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        } else {
            cursor = getSimpleSelectionBuilder(uri, selection, selectionArgs).query(database, projection, sortOrder);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {

            case Config.CATEGORY_DIR_ID:
                return CategoryContract.CONTENT_TYPE;

            case Config.CATEGORY_ITEM_ID:
                return CategoryContract.CONTENT_ITEM_TYPE;

            case Config.TRANSACTION_DIR_ID:
                return TransactionContract.CONTENT_TYPE;

            case Config.TRANSACTION_ITEM_ID:
                return TransactionContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = getInstance();
        Uri contentUri;
        String tableName;
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                contentUri = CategoryContract.CONTENT_URI;
                tableName = CategoryContract.TABLE_NAME;
                final Uri insertedUriEventsCategory = buildInsertUri(database, contentUri, tableName, values);
                getContext().getContentResolver().notifyChange(insertedUriEventsCategory, null);
                return insertedUriEventsCategory;
            case Config.TRANSACTION_DIR_ID:
                contentUri = TransactionContract.CONTENT_URI;
                tableName = TransactionContract.TABLE_NAME;
                final Uri insertedUriEventsTransaction = buildInsertUri(database, contentUri, tableName, values);
                getContext().getContentResolver().notifyChange(insertedUriEventsTransaction, null);
                return insertedUriEventsTransaction;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = getInstance();
        final int count = getSimpleSelectionBuilder(uri, selection, selectionArgs).delete(database);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case Config.CATEGORY_DIR_ID:
                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;
            case Config.TRANSACTION_ITEM_ID:
                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);
                }
                break;
            default:
                Log.w(DatabaseProvider.class.getSimpleName(), "unknown uri: " + uri);
        }
        final int count = insertOrUpdate(uri, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int insertOrUpdate(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return super.insertOrUpdate(uri, contentValues, selection, selectionArgs);
    }

    public interface Config {
        int CATEGORY_ITEM_ID = 0x1000;
        int CATEGORY_DIR_ID = 0x1001;
        int TRANSACTION_ITEM_ID = 0x2002;
        int TRANSACTION_DIR_ID = 0x2003;

    }

    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String transactionColumns[] = TransactionColumns.COLUMNS;
        for (String col : transactionColumns) {

            String qualifiedCol = SQLiteHelper.addTransactionPrefix(col);
            map.put(qualifiedCol, qualifiedCol + " as " + col);
        }

        String categorycolumns[] = CategoryColumns.COLUMNS;
        for (String col : categorycolumns) {

            String qualifiedCol = SQLiteHelper.addCategoryPrefix(col);
            String alias = qualifiedCol.replace(".", "_");
            map.put(qualifiedCol, qualifiedCol + " AS " + alias);
        }
        return map;
    }
}
