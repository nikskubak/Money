package com.money.fragments;

import android.view.View;

import com.artjoker.core.fragments.AbstractBasic;
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
    protected int getHeaderIconsPolicy() {
        return 0;
    }
}
