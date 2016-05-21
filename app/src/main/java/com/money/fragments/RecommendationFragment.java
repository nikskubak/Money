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
import com.fivestar.models.Recommendation;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.models.converters.RecommendationCursorConverter;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.R;
import com.money.adapters.RecommendRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by skuba on 21.05.2016.
 */
public class RecommendationFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks {

    RecyclerView recommendRecyclerView;
    ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
    RecommendRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.recommendation_fragment;
    }

    @Override
    protected void initViews(View view) {
        recommendRecyclerView = (RecyclerView) view.findViewById(R.id.recommendation_recycler_view);
    }

    @Override
    protected void initContent() {
        super.initContent();
        getLoaderManager().restartLoader(Constants.LoadersID.LOADER_RECOMMENDATIONS, null, this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_RECOMMENDATIONS:
                return new CursorLoader(getActivity(), RecommendationContract.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor = (Cursor) data;
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_RECOMMENDATIONS:
                if (DatabaseUtils.isValid(cursor)) {
                    Recommendation currentRecommendation = null;
                    RecommendationCursorConverter converter = new RecommendationCursorConverter();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        converter.setCursor(cursor);
                        recommendationArrayList.add(converter.getObject());
                    }
                    startRecycler(recommendationArrayList);
                }
                break;
        }
    }

    private void startRecycler(ArrayList<Recommendation> recommendationArrayList) {
        adapter = new RecommendRecyclerAdapter(recommendationArrayList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recommendRecyclerView.setLayoutManager(layoutManager);
        recommendRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
