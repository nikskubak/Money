package com.money.fragments;

import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.Category;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.R;

import java.util.ArrayList;

/**
 * Created by skuba on 19.03.2016.
 */
public class AddTransactionFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks{

    Button buttonCategory;
    EditText editTextSum;
    ArrayList<Category> categories;
    RecyclerView recyclerViewCategory;
    CategoryRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.add_transaction_fragment;
    }

    @Override
    protected void initViews(View view) {
        buttonCategory = (Button) view.findViewById(R.id.add_transaction_buuton_category);
        editTextSum = (EditText) view.findViewById(R.id.add_transaction_edit_text_sum);
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.add_transaction_recycler_categoty);
    }

    @Override
    protected void initContent() {
        super.initContent();
        getActivity().getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    void startRecyclerView(){
        adapter = new CategoryRecyclerAdapter(categories);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(adapter);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_CATEGORIES:
                return new CursorLoader(getActivity(), CategoryContract.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor = (Cursor)data;
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_CATEGORIES:
                categories = new ArrayList<>();
                CategoryCursorConverter converter = new CategoryCursorConverter();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    converter.setCursor(cursor);
                    categories.add(converter.getObject());
                }
                startRecyclerView();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
