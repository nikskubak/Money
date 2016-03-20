package com.money.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artjoker.core.fragments.AbstractBasic;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.fivestar.models.Category;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by skuba on 19.03.2016.
 */
public class AddTransactionFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks, CategoryRecyclerAdapter.OnItemCLickListener, View.OnClickListener {

    Button buttonCategory;
    EditText editTextSum;
    ArrayList<Category> categories;
    RecyclerView recyclerViewCategory;
    CategoryRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    AlertDialog alertDailog;

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
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonCategory.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        super.initContent();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    void startRecyclerView() {
        adapter = new CategoryRecyclerAdapter(categories, this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(adapter);
        recyclerViewCategory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
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
//                startRecyclerView();
                showDialogWithCategory();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    void showDialogWithCategory() {
        String names[] = {"A", "B", "C", "D"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.dialog_recycler_view, null);
        RecyclerView dialogRecycler = (RecyclerView) dialogView.findViewById(R.id.dialog_recycler_view_categoty);
        adapter = new CategoryRecyclerAdapter(categories, this);
        layoutManager = new LinearLayoutManager(getActivity());
        dialogRecycler.setAdapter(adapter);
        dialogRecycler.setLayoutManager(layoutManager);
        builder.setView(dialogView);
        builder.setTitle("Выберите категорию");

        alertDailog = builder.create();
        alertDailog.show();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), categories.get(position).getName(), Toast.LENGTH_SHORT).show();
        alertDailog.dismiss();
        insertTransaction(categories.get(position));
    }

    void insertTransaction(Category category) {
        ContentValues values = new ContentValues();
        values.put(TransactionColumns.CATEGORY, category.getId());
        values.put(TransactionColumns.MONEY, editTextSum.getText().toString());
        values.put(TransactionColumns.DATE, System.currentTimeMillis());
        values.put(TransactionColumns.TYPE, category.getType());
        getActivity().getContentResolver().insert(TransactionContract.CONTENT_URI, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_transaction_buuton_category:
                if(!android.text.TextUtils.isEmpty(editTextSum.getText().toString())) {
                    getActivity().getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
                }else{
                    Toast.makeText(getActivity(), "Введите сумму", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
