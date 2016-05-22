package com.money.fragments;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.artjoker.core.fragments.AbstractBasic;
import com.google.firebase.crash.FirebaseCrash;
import com.money.R;

/**
 * Created by skuba on 06.03.2016.
 */
public class MainFragment extends AbstractBasic implements View.OnClickListener {

    FloatingActionButton buttonAddTransaction;

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initViews(View view) {
        buttonAddTransaction = (FloatingActionButton) view.findViewById(R.id.main_fragment_add_transaction_button);
    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonAddTransaction.setOnClickListener(this);

    }

    @Override
    protected void initContent() {
        super.initContent();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fragment_add_transaction_button:
                commit(new AddTransactionFragment(), null);
                break;
        }
    }
}
