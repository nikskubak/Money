package com.money.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.database.SelectionBuilder;
import com.fivestar.models.RatingContract;
import com.fivestar.utils.ContentProviderConfig;


/**
 * Created by android on 11.01.2016.
 */
public class DatabaseProvider extends SecureDatabaseProvider {
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        addUri(URI_MATCHER, ContentProviderConfig.AUTHORITY_CONTENT, RatingContract.TABLE_NAME, Config.RATING_DIR_ID, Config.RATING_ITEM_ID);
    }

    @Override
    protected SQLiteOpenHelper getNewDatabaseHelper() {
        return new DatabaseHelper(getContext());
    }

    @Override
    protected SelectionBuilder getSimpleSelectionBuilder(Uri uri, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (URI_MATCHER.match(uri)) {
            case Config.RATING_DIR_ID:
                appendSelection(builder, RatingContract.TABLE_NAME, selection, selectionArgs);
                return builder;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase database = getInstance();
        final Cursor cursor = getSimpleSelectionBuilder(uri, selection, selectionArgs).query(database, projection, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {

            case Config.RATING_DIR_ID:
                return RatingContract.CONTENT_TYPE;

            case Config.RATING_ITEM_ID:
                return RatingContract.CONTENT_ITEM_TYPE;

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
            case Config.RATING_DIR_ID:
                contentUri = RatingContract.CONTENT_URI;
                tableName = RatingContract.TABLE_NAME;
                final Uri insertedUriEvents = buildInsertUri(database, contentUri, tableName, values);
                getContext().getContentResolver().notifyChange(insertedUriEvents, null);
                return insertedUriEvents;

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
            case Config.RATING_DIR_ID:
                if (TextUtils.isEmpty(selection)) {
                    throw new IllegalArgumentException("specify selection for location dir update: " + uri);

                }
                break;

            case Config.RATING_ITEM_ID:
                break;

            default:
                Log.w(DatabaseProvider.class.getSimpleName(), "unknown uri: " + uri);

        }
        final int count = insertOrUpdate(uri, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public interface Config {
        int RATING_ITEM_ID = 0x1000;
        int RATING_DIR_ID = 0x1001;
    }
}
