package com.money.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.fivestar.models.Category;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.R;
import com.money.activities.BaseActivity;
import com.money.adapters.CostsRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by skuba on 24.03.2016.
 */
public class OperationActivity extends BaseActivity implements LoaderManager.LoaderCallbacks,
        CompoundButton.OnCheckedChangeListener, CategoryRecyclerAdapter.OnItemCLickListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {
    RecyclerView recyclerView;
    CostsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private RadioButton radioCost;
    private RadioButton radioGain;
    private RadioButton radioALL;
    private RadioGroup radioGroup;
    ArrayList<Category> categories;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    AlertDialog alertDailog;
    ImageView imageButtonCategoryFilter;
    ImageView imageButtonDateFilter;
    int currentCategoryId;

    @Override
    protected void initViews() {
        radioCost = (RadioButton) findViewById(R.id.add_category_radio_button_cost);
        radioGain = (RadioButton) findViewById(R.id.add_category_radio_button_gain);
        radioALL = (RadioButton) findViewById(R.id.add_category_radio_button_all);
        recyclerView = (RecyclerView) findViewById(R.id.cost_fragment_recycler_view);
        imageButtonCategoryFilter = (ImageView) findViewById(R.id.operation_button_category_filter);
        imageButtonDateFilter = (ImageView) findViewById(R.id.operation_button_date_filter);
        radioGroup = (RadioGroup) findViewById(R.id.operation_radio_group);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        radioALL.setOnCheckedChangeListener(this);
        radioGain.setOnCheckedChangeListener(this);
        radioCost.setOnCheckedChangeListener(this);
        imageButtonCategoryFilter.setOnClickListener(this);
        imageButtonDateFilter.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
        currentCategoryId = Constants.CATEGORY_NOT_ENTERED;
        categories = new ArrayList<>();
        getLoaderManager().initLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                DatabaseUtils.getTransactionsFromDB(null, null, null, null),
                this
        ).forceLoad();

//        DatabaseUtils.getTransactionsFromDB(null, null, null, null);
//        DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_gain), null, null, 2);
//        DatabaseUtils.getTransactionsFromDB(null, "123654", "457896", 3);
//        DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_gain), "123654", "457896", null);
//        DatabaseUtils.getTransactionsFromDB(null, null, null, 3);
//        DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_gain), null, null, null);
//        DatabaseUtils.getTransactionsFromDB(null, "123654", "457896", null);
//        DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_gain), "123654", "457896", 5);

    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.operations_fragment;
    }

    void startRecycler(Cursor cursor) {
        layoutManager = new LinearLayoutManager(this);
        adapter = new CostsRecyclerAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.LoadersID.LOADER_TRANSACTIONS:
                return new CursorLoader(
                        this,
                        TransactionContract.CONTENT_URI,
                        null,
                        args.getString(Constants.TRANSACTION_SELECTION),
                        args.getStringArray(Constants.TRANSACTION_SELECTION_ARGS),
                        null
                );
            case Constants.LoadersID.LOADER_CATEGORIES:
                return new CursorLoader(this, CategoryContract.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor = (Cursor) data;
        DatabaseUtils.logCursor(cursor);
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_TRANSACTIONS:
                startRecycler(cursor);
                break;
            case Constants.LoadersID.LOADER_CATEGORIES:
                categories = new ArrayList<>();
                CategoryCursorConverter converter = new CategoryCursorConverter();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    converter.setCursor(cursor);
                    categories.add(converter.getObject());
                }
                showDialogWithCategory();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    void showDialogWithCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.dialog_recycler_view, null);
        RecyclerView dialogRecycler = (RecyclerView) dialogView.findViewById(R.id.dialog_recycler_view_categoty);
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(categories);
        categoryRecyclerAdapter.setListener(this);
        layoutManager = new LinearLayoutManager(this);
        dialogRecycler.setAdapter(categoryRecyclerAdapter);
        dialogRecycler.setLayoutManager(layoutManager);
        builder.setView(dialogView);
        builder.setTitle("Выберите категорию");
        alertDailog = builder.create();
        alertDailog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.add_category_radio_button_all:
                    imageButtonCategoryFilter.setImageResource(R.drawable.ic_checkbox_multiple_marked_black_48dp);
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            DatabaseUtils.getTransactionsFromDB(null, null, null, null),
                            this
                    ).forceLoad();
                    break;
                case R.id.add_category_radio_button_cost:
                    imageButtonCategoryFilter.setImageResource(R.drawable.ic_checkbox_multiple_marked_black_48dp);
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_cost), null, null, null),
                            this
                    ).forceLoad();
                    break;
                case R.id.add_category_radio_button_gain:
                    imageButtonCategoryFilter.setImageResource(R.drawable.ic_checkbox_multiple_marked_black_48dp);
                    getLoaderManager().restartLoader(
                            Constants.LoadersID.LOADER_TRANSACTIONS,
                            DatabaseUtils.getTransactionsFromDB(getResources().getString(R.string.category_type_gain), null, null, null),
                            this
                    ).forceLoad();
                    break;
            }
        }
    }

    @Override
    public void onItemClick(int position) {
//        radioGroupIsChecked(false);
        radioGroup.clearCheck();
        imageButtonCategoryFilter.setImageResource(R.drawable.ic_checkbox_multiple_marked_black_enabled);
        alertDailog.dismiss();
        getLoaderManager().restartLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                DatabaseUtils.getTransactionsFromDB(null, null, null, categories.get(position).getId()),
                this
        ).forceLoad();
    }

    @Override
    public void onLongItemClick(Category category) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operation_button_category_filter:
                getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
                break;
            case R.id.operation_button_date_filter:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(year, monthOfYear, dayOfMonth);
        endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);

        getLoaderManager().restartLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                DatabaseUtils.getTransactionsFromDB(null, String.valueOf(startDate.getTimeInMillis()), String.valueOf(endDate.getTimeInMillis()), null),
                this
        ).forceLoad();

        Log.e("datePicker", "start = " + startDate.getTimeInMillis() + " end = " + endDate.getTimeInMillis());
    }

    void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog();
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show(getFragmentManager(), getResources().getString(R.string.operation_calendar_title));
    }
}
