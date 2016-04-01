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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artjoker.core.fragments.AbstractBasic;
import com.fivestar.models.Category;
import com.fivestar.models.columns.TransactionColumns;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.fivestar.models.converters.CategoryCursorConverter;
import com.money.CategoryRecyclerAdapter;
import com.money.Constants;
import com.money.Launcher;
import com.money.R;
import com.money.views.CustomKeyboardView;

import java.util.ArrayList;

/**
 * Created by skuba on 19.03.2016.
 */
public class AddTransactionFragment extends AbstractBasic implements LoaderManager.LoaderCallbacks, CategoryRecyclerAdapter.OnItemCLickListener, View.OnClickListener {

    Button buttonCategory;
    EditText editTextSum;
    EditText editTextDescription;
    ArrayList<Category> categories;
    RecyclerView recyclerViewCategory;
    CategoryRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    AlertDialog alertDailog;
    CustomKeyboardView keyboardView;

    @Override
    protected int getLayoutId() {
        return R.layout.add_transaction_fragment;
    }

    @Override
    protected void initViews(View view) {
        buttonCategory = (Button) view.findViewById(R.id.add_transaction_buuton_category);
        editTextSum = (EditText) view.findViewById(R.id.add_transaction_edit_text_sum);
        editTextDescription = (EditText) view.findViewById(R.id.add_transaction_edit_text_description);
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.add_transaction_recycler_categoty);
        keyboardView = (CustomKeyboardView) view.findViewById(R.id.keyboardview);
        keyboardView.setKeyboard(R.xml.number_keyboard);
        keyboardView.setListeners();
        keyboardView.showCustomKeyboard(editTextSum);
        editTextSum.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonCategory.setOnClickListener(this);
        editTextSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyboardView.showCustomKeyboard(v);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
        editTextSum.setOnClickListener(this);
        editTextSum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
//        editTextDescription.setOnClickListener(this);
//        editTextDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                } else {
//                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                }
//            }
//        });
//        editTextDescription.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                EditText edittext = (EditText) v;
//                int inType = edittext.getInputType();       // Backup the input type
//                edittext.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT); // Disable standard keyboard
//                edittext.onTouchEvent(event);               // Call native handler
//                edittext.setInputType(inType);              // Restore input type
//                return true; // Consume touch event
//            }
//        });
    }

    @Override
    protected void initContent() {
        super.initContent();
        ((Launcher)getActivity()).getDrawer().deselect();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    void startRecyclerView() {
        adapter = new CategoryRecyclerAdapter(categories);
        adapter.setListener(this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.dialog_recycler_view, null);
        RecyclerView dialogRecycler = (RecyclerView) dialogView.findViewById(R.id.dialog_recycler_view_categoty);
        adapter = new CategoryRecyclerAdapter(categories);
        adapter.setListener(this);
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
//        Toast.makeText(getActivity(), categories.get(position).getName(), Toast.LENGTH_SHORT).show();
        alertDailog.dismiss();
        insertTransaction(categories.get(position));
        commit(new OperationFragment(), null);
    }

    @Override
    public void onLongItemClick(Category category) {

    }

    void insertTransaction(Category category) {
        ContentValues values = new ContentValues();
        values.put(TransactionColumns.CATEGORY, category.getId());
        values.put(TransactionColumns.MONEY, editTextSum.getText().toString());
        values.put(TransactionColumns.DATE, System.currentTimeMillis());
        values.put(TransactionColumns.TYPE, category.getType());
        values.put(TransactionColumns.DESCRIPTION, editTextDescription.getText().toString());
        getActivity().getContentResolver().insert(TransactionContract.CONTENT_URI, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_transaction_buuton_category:
                if (!android.text.TextUtils.isEmpty(editTextSum.getText().toString())) {
                    getActivity().getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
                } else {
                    Toast.makeText(getActivity(), "Введите сумму", Toast.LENGTH_SHORT).show();
                }
                break;

//            case R.id.add_transaction_edit_text_description:
//                editTextSum.setFocusable(false);
//                editTextDescription.setFocusable(true);
////                editTextDescription.requestFocus();
////                editTextDescription.setKeyListener(originalKeyListener);
////                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
////                imm.showSoftInput(editTextDescription, InputMethodManager.SHOW_IMPLICIT);
//                break;

        }
    }
}
