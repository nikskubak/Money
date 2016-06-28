package com.money.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fivestar.models.Category;
import com.fivestar.models.columns.CategoryColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.R;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;

/**
 * Created by skuba on 22.03.2016.
 */
public class AddCategoryActivity extends BaseActivity implements LoaderManager.LoaderCallbacks, View.OnClickListener, CategoryRecyclerAdapter.OnItemCLickListener {

    private RadioButton radioCost;
    private RadioButton radioGain;
    private EditText editTextCategoryName;
    private Button buttonAddCategory;
    private ArrayList<Category> categories;
    private RecyclerView categoryRecyclerView;
    private CategoryRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView buttonBack;
    private int categoryColor;


    @Override
    protected void initViews() {
        radioCost = (RadioButton)findViewById(R.id.add_category_radio_button_cost);
        radioGain = (RadioButton) findViewById(R.id.add_category_radio_button_gain);
        editTextCategoryName = (EditText)findViewById(R.id.add_category_edit_text_name);
        buttonAddCategory = (Button)findViewById(R.id.add_category_button_add);
        categoryRecyclerView = (RecyclerView)findViewById(R.id.add_category_recycler_view);
        buttonBack = (ImageView)findViewById(R.id.back_button);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        buttonAddCategory.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
        categories = new ArrayList<>();
        getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.add_category_fragment;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_CATEGORIES:
                return new CursorLoader(this, CategoryContract.CONTENT_URI, null, null, null, null);
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
            case R.id.back_button:
//                onBackPressed();
                showColorDialog();
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
            getContentResolver().update(CategoryContract.CONTENT_URI, values, selection, selectionArgs);
            getLoaderManager().restartLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
            editTextCategoryName.setText("");
//            editTextCategoryName.setFocusable(false);
        } else {
            Toast.makeText(this, getResources().getString(R.string.add_category_alert), Toast.LENGTH_SHORT).show();
        }
    }

    void startRecycler() {
        layoutManager = new LinearLayoutManager(this);
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
        ad = new AlertDialog.Builder(this);
        ad.setMessage("Удалить категорию?"); // сообщение
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selection = CategoryColumns.NAME + "=? AND " + CategoryColumns.TYPE + "=?";
                String[] selectionArgs = new String[]{category.getName(),category.getType()};
                getContentResolver().delete(CategoryContract.CONTENT_URI,selection,selectionArgs);
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



    private void showColorDialog() {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.full_palette)
                .setSelectedColorRes(R.color.Color_1)
                .setTitle("Выберите цвет категории")
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                           categoryColor = color;
                        }
                    }
                }).build().show(getSupportFragmentManager(), "tag");
    }
}
