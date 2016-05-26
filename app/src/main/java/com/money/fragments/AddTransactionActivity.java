package com.money.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.artjoker.core.activities.AbstractLauncher;
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
import java.util.Calendar;

/**
 * Created by skuba on 19.03.2016.
 */
public class AddTransactionActivity extends Activity implements LoaderManager.LoaderCallbacks, CategoryRecyclerAdapter.OnItemCLickListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button buttonCategory;
    EditText editTextSum;
    EditText editTextDescription;
    ArrayList<Category> categories;
    RecyclerView recyclerViewCategory;
    CategoryRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    AlertDialog alertDailog;
    CustomKeyboardView keyboardView;
    ImageView buttonCalendar;
    ImageView buttonBack;
    String enteredDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction_fragment);
        initViews();
        initListeners();
    }

    protected void initViews() {
        buttonCalendar = (ImageView)findViewById(R.id.add_transaction_button_date);
        buttonBack = (ImageView)findViewById(R.id.back_button);
        buttonCategory = (Button)findViewById(R.id.add_transaction_buuton_category);
        editTextSum = (EditText)findViewById(R.id.add_transaction_edit_text_sum);
        editTextDescription = (EditText) findViewById(R.id.add_transaction_edit_text_description);
        recyclerViewCategory = (RecyclerView) findViewById(R.id.add_transaction_recycler_categoty);
        keyboardView = (CustomKeyboardView) findViewById(R.id.keyboardview);
        keyboardView.setKeyboard(R.xml.number_keyboard);
        keyboardView.setListeners();
        keyboardView.showCustomKeyboard(editTextSum);
        editTextSum.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    protected void initListeners() {
        buttonCategory.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonCalendar.setOnClickListener(this);
        editTextSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyboardView.showCustomKeyboard(v);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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




    void startRecyclerView() {
        adapter = new CategoryRecyclerAdapter(categories);
        adapter.setListener(this);
        layoutManager = new LinearLayoutManager(this);
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
//                startRecyclerView();
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
        adapter = new CategoryRecyclerAdapter(categories);
        adapter.setListener(this);
        layoutManager = new LinearLayoutManager(this);
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
//        onCommit(new OperationFragment(), null);
    }

    @Override
    public void onLongItemClick(Category category) {

    }

    void insertTransaction(Category category) {
        ContentValues values = new ContentValues();
        values.put(TransactionColumns.CATEGORY, category.getId());
        values.put(TransactionColumns.MONEY, editTextSum.getText().toString());
        values.put(TransactionColumns.DATE, enteredDate == null ? String.valueOf(System.currentTimeMillis()) : enteredDate);
        values.put(TransactionColumns.TYPE, category.getType());
        values.put(TransactionColumns.DESCRIPTION, editTextDescription.getText().toString());
        getContentResolver().insert(TransactionContract.CONTENT_URI, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_transaction_buuton_category:
                if (!android.text.TextUtils.isEmpty(editTextSum.getText().toString())) {
                    getLoaderManager().initLoader(Constants.LoadersID.LOADER_CATEGORIES, null, this);
                } else {
                    Toast.makeText(this, "Введите сумму", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_transaction_button_date:
                showDateDialog();
                break;
            case R.id.back_button:
                onBackPressed();
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

    void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        enteredDate = String.valueOf(calendar.getTimeInMillis());
        buttonCalendar.setImageResource(R.drawable.ic_calendar_multiple_check_black_enabled);
    }

}
