package com.money.fragments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.contracts.TransactionContract;
import com.money.Constants;
import com.money.R;
import com.money.adapters.CostsRecyclerAdapter;

/**
 * Created by skuba on 24.03.2016.
 */
public class CostaFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks {
    RecyclerView recyclerView;
    CostsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.costs_fragment;
    }

    @Override
    protected void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.cost_fragment_recycler_view);
    }

    @Override
    protected void initContent() {
        super.initContent();
        getLoaderManager().initLoader(Constants.LoadersID.LOADER_TRANSACTIONS, null, this).forceLoad();
    }

    void startRecycler(Cursor cursor) {
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CostsRecyclerAdapter(cursor,getActivity());
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
                return new CursorLoader(getActivity(), TransactionContract.CONTENT_URI, null, null, null, null);
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
}
