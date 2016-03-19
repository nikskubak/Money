package com.money.fragments;

import android.view.View;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.columns.CategoryColumns;
import com.money.R;

/**
 * Created by skuba on 06.03.2016.
 */
public class MainFragment extends AbstractBasic {
    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initContent() {
        super.initContent();
//        ContentValues values = new ContentValues();
//        values.put(CategoryColumns.NAME, "erery");
//        values.put(CategoryColumns.TYPE, "erery");
//        getActivity().getContentResolver().insert(CategoryContract.CONTENT_URI,values);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }
}
