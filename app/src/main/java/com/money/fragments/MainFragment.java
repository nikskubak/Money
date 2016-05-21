package com.money.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.columns.CategoryColumns;
import com.google.firebase.crash.FirebaseCrash;
import com.money.Constants;
import com.money.R;

/**
 * Created by skuba on 06.03.2016.
 */
public class MainFragment extends AbstractBasic implements View.OnClickListener {

    ImageButton buttonAddTransaction;

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initViews(View view) {
        buttonAddTransaction = (ImageButton) view.findViewById(R.id.main_fragment_add_transaction_button);
    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonAddTransaction.setOnClickListener(this);
        FirebaseCrash.log("SQL database failed to initialize");
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
