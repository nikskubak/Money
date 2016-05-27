package com.money.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fivestar.models.Recommendation;
import com.fivestar.models.contracts.RecommendationContract;
import com.fivestar.models.converters.RecommendationCursorConverter;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.R;
import com.money.activities.BaseActivity;
import com.money.adapters.RecommendRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by skuba on 21.05.2016.
 */
public class RecommendationActivity extends BaseActivity implements LoaderManager.LoaderCallbacks, View.OnClickListener {

    RecyclerView recommendRecyclerView;
    ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
    RecommendRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView buttonBack;


    @Override
    protected void initViews() {
        recommendRecyclerView = (RecyclerView)findViewById(R.id.recommendation_recycler_view);
        buttonBack = (ImageView)findViewById(R.id.back_button);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        buttonBack.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
        getLoaderManager().restartLoader(Constants.LoadersID.LOADER_RECOMMENDATIONS, null, this);
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.recommendation_activity;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_RECOMMENDATIONS:
                return new CursorLoader(this, RecommendationContract.CONTENT_URI, null, null, null, null);
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
        adapter = new RecommendRecyclerAdapter(recommendationArrayList, this);
        layoutManager = new LinearLayoutManager(this);
        recommendRecyclerView.setLayoutManager(layoutManager);
        recommendRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }
}
