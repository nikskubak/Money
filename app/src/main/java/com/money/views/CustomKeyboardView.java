package com.money.views;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.money.Constants;

/**
 * Created by android on 06.01.2016.
 */
public class CustomKeyboardView extends KeyboardView {
    private Keyboard keyboard;
    private Context context;
    private EditText focusView;
    private ViewGroup parentView;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
    }

    public void setKeyboard(int keyboardType) {
        keyboard = new Keyboard(context, keyboardType);
    }

    public void setListeners() {
        if (keyboard != null) {
            setOnKeyboardActionListener(keyboardActionListener);
            setKeyboard(keyboard);
            setPreviewEnabled(false);
        }
    }

    public void hideCustomKeyboard() {
        this.setVisibility(View.GONE);
        this.setEnabled(false);
    }

    public void showCustomKeyboard(View v) {
        this.setVisibility(View.VISIBLE);
        this.setEnabled(true);
        if (v != null)
            ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        focusView = (EditText)v;
        parentView = (ViewGroup)v.getParent();

    }

    public boolean isCustomKeyboardVisible() {
        return this.getVisibility() == View.VISIBLE;
    }

    private OnKeyboardActionListener keyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // Get the EditText and its Editable
//            View focusCurrent = ((LauncherActivity) context).getWindow().getCurrentFocus();
//            if (focusCurrent == null || focusCurrent.getClass() != EditText.class || focusCurrent.getId()!= R.id.add_transaction_edit_text_sum) return;
//            EditText edittext = (EditText) focusCurrent;
            if(parentView.getFocusedChild() != null) {
                if (parentView.getFocusedChild().getId() != focusView.getId()) {
                    parentView.getFocusedChild().clearFocus();
                    focusView.setFocusable(true);
                }
            }
            Editable editable = focusView.getText();
            int start = focusView.getSelectionStart();
            // Handle key
            if (primaryCode == Constants.CodeCancel) {
                hideCustomKeyboard();
            } else if (primaryCode == Constants.CodeDelete) {
                if (editable != null && start > 0) editable.delete(start - 1, start);
            } else {// Insert character
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
}
