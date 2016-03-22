package com.money.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.Category;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.R;
import com.money.adapters.CursorRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by skuba on 22.03.2016.
 */
public class AddCategoryFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks, View.OnClickListener, CategoryRecyclerAdapter.OnItemCLickListener {

    private RadioButton radioCost;
    private RadioButton radioGain;
    private EditText editTextCategoryName;
    private Button buttonAddCategory;
    private ArrayList<Category> categories;
    private RecyclerView categoryRecyclerView;
    private CategoryRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.add_category_fragment;
    }

    @Override
    protected void initViews(View view) {
        radioCost = (RadioButton) view.findViewById(R.id.add_category_radio_button_cost);
        radioGain = (RadioButton) view.findViewById(R.id.add_category_radio_button_gain);
        editTextCategoryName = (EditText) view.findViewById(R.id.add_category_edit_text_name);
        buttonAddCategory = (Button) view.findViewById(R.id.add_category_button_add);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.add_category_recycler_view);
    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonAddCategory.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
        categories = new ArrayList<>();
        getActivity().getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
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
        Cursor cursor = (Cursor) data;
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_CATEGORIES:
                categories = new ArrayList<>();
                CategoryCursorConverter converter = new CategoryCursorConverter();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    converter.setCursor(cursor);
                    categories.add(converter.getObject());
                }
                startRecycler();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_category_button_add:
                insertCategory();
                break;
        }
    }

    void insertCategory() {
        if (!TextUtils.isEmpty(editTextCategoryName.getText().toString())) {
            String categoryType;
            if (radioCost.isChecked()) {
                categoryType = getResources().getString(R.string.category_type_cost);
            } else {
                categoryType = getResources().getString(R.string.category_type_gain);
            }
            Category newCategory = new Category();
            ContentValues values = new ContentValues();
            values.put(CategoryColumns.NAME, editTextCategoryName.getText().toString());
            values.put(CategoryColumns.TYPE, categoryType);
            newCategory.setName(editTextCategoryName.getText().toString());
            newCategory.setType(categoryType);
            String selection = CategoryColumns.NAME + "=? AND " + CategoryColumns.TYPE + "=?";
            String[] selectionArgs = new String[]{editTextCategoryName.getText().toString(),categoryType};
            getActivity().getContentResolver().update(CategoryContract.CONTENT_URI, values, selection, selectionArgs);
            getActivity().getLoaderManager().restartLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
            editTextCategoryName.setText("");
//            editTextCategoryName.setFocusable(false);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.add_category_alert), Toast.LENGTH_SHORT).show();
        }
    }

    void startRecycler() {
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CategoryRecyclerAdapter(categories);
        adapter.setListener(this);
        categoryRecyclerView.setAdapter(adapter);
        categoryRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(Category category) {
        showDialog(category);
    }

    void showDialog(final Category category) {
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(getActivity());
        ad.setMessage("Удалить категорию?"); // сообщение
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selection = CategoryColumns.NAME + "=? AND " + CategoryColumns.TYPE + "=?";
                String[] selectionArgs = new String[]{category.getName(),category.getType()};
                getActivity().getContentResolver().delete(CategoryContract.CONTENT_URI,selection,selectionArgs);
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        ad.show();
    }
}
