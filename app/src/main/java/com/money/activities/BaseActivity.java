package com.money.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by skuba on 27.05.2016.
 */
public abstract class BaseActivity extends AppCompatActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(getContentViewLayoutResId());
            initViews();
            initListeners();
            initContent();
        }

        protected void initViews() {

        }

        protected void initListeners() {

        }

        protected void initContent() {

        }

        protected abstract int getContentViewLayoutResId();

}
