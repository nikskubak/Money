package com.fivestar.tools;

import android.content.ContentValues;


/**
 * Created by alexsergienko on 24.03.15.
 */
public interface ContentValuesConverter<T> extends Validator {
    ContentValues getContentValues();

    String getUpdateWhere();

    String[] getUpdateArgs();

    void setObjectModel(T model);
}
