package com.money.fragments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.TransactionContract;
import com.money.Constants;
import com.money.Launcher;
import com.money.R;
import com.money.adapters.CostsRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by skuba on 24.03.2016.
 */
public class CostaFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks, CompoundButton.OnCheckedChangeListener {
    RecyclerView recyclerView;
    CostsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private RadioButton radioCost;
    private RadioButton radioGain;
    private RadioButton radioALL;

    @Override
    protected int getLayoutId() {
        return R.layout.costs_fragment;
    }

    @Override
    protected void initViews(View view) {
        radioCost = (RadioButton) view.findViewById(R.id.add_category_radio_button_cost);
        radioGain = (RadioButton) view.findViewById(R.id.add_category_radio_button_gain);
        radioALL = (RadioButton) view.findViewById(R.id.add_category_radio_button_all);
        recyclerView = (RecyclerView) view.findViewById(R.id.cost_fragment_recycler_view);
    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        radioALL.setOnCheckedChangeListener(this);
        radioGain.setOnCheckedChangeListener(this);
        radioCost.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
        getLoaderManager().initLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                getTransactionsFromDB(null, null, null),
                this
        ).forceLoad();
    }

    void startRecycler(Cursor cursor) {
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CostsRecyclerAdapter(cursor, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_TRANSACTIONS:
                return new CursorLoader(
                        getActivity(),
                        TransactionContract.CONTENT_URI,
                        null,
                        args.getString(Constants.TRANSACTION_SELECTION),
                        args.getStringArray(Constants.TRANSACTION_SELECTION_ARGS),
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor = (Cursor) data;
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_TRANSACTIONS:
                startRecycler(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    Bundle getTransactionsFromDB(String type, String startDate, String endDate) {
        StringBuilder selection = new StringBuilder();
        Bundle bundle = new Bundle();
        ArrayList<String> selectionArgs = new ArrayList<>();
        if (!TextUtils.isEmpty(type)) {
            selectionArgs.add(type);
            selection.append(TransactionColumns.TYPE).append("=?");
            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                selection
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append(">?")
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append("<?");
                selectionArgs.add(startDate);
                selectionArgs.add(endDate);
            }
        } else {
            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                selection
                        .append(TransactionColumns.DATE).append(">?")
                        .append(" AND ")
                        .append(TransactionColumns.DATE).append("<?");
                selectionArgs.add(startDate);
                selectionArgs.add(endDate);
            }
        }
        bundle.putString(Constants.TRANSACTION_SELECTION, selection.toString());
        bundle.putStringArray(Constants.TRANSACTION_SELECTION_ARGS, selectionArgs.toArray(new String[selectionArgs.size()]));
        Log.e("getTransactionsFromDB", "selection = " + selection.toString() + " selectionArgs = " + selectionArgs.toString());
        return bundle;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.add_category_radio_button_all:
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            getTransactionsFromDB(null, null, null),
                            this
                    ).forceLoad();
                    break;
                case R.id.add_category_radio_button_cost:
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            getTransactionsFromDB(getResources().getString(R.string.category_type_cost), null, null),
                            this
                    ).forceLoad();
                    break;
                case R.id.add_category_radio_button_gain:
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            getTransactionsFromDB(getResources().getString(R.string.category_type_gain), null, null),
                            this
                    ).forceLoad();
                    break;
            }
        }
    }
}
