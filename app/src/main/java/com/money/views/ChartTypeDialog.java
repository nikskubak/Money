package com.money.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.money.Constants;
import com.money.R;

/**
 * Created by skuba on 13.06.2016.
 */
public class ChartTypeDialog extends DialogFragment implements OnClickListener, View.OnClickListener {

    final String LOG_TAG = "myLogs";
    ChangeTypeListener listener;
    CheckedTextView pieChart;
    CheckedTextView williamBarChart;
    CheckedTextView mpBarChart;
    int type = Constants.PIE_CHART;


    public ChangeTypeListener getListener() {
        return listener;
    }

    public void setListener(ChangeTypeListener listener) {
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_chart_type, null);
        initViews(view);
        initListenets();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this);

        return adb.create();
    }

    private void initViews(View view) {
        pieChart = (CheckedTextView) view.findViewById(R.id.text_pie_chart);
        williamBarChart = (CheckedTextView) view.findViewById(R.id.text_bar_chart_william);
        mpBarChart = (CheckedTextView) view.findViewById(R.id.text_bar_chart_mp);
    }

    void initListenets() {
        pieChart.setOnClickListener(this);
        williamBarChart.setOnClickListener(this);
        mpBarChart.setOnClickListener(this);
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                listener.onChangeType(type);
                dismiss();
                break;
            case Dialog.BUTTON_NEUTRAL:
                dismiss();
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_pie_chart:
                pieChart.setChecked(true);
                williamBarChart.setChecked(false);
                mpBarChart.setChecked(false);
                type = Constants.PIE_CHART;
                break;
            case R.id.text_bar_chart_william:
                pieChart.setChecked(false);
                williamBarChart.setChecked(true);
                mpBarChart.setChecked(false);
                type = Constants.BAR_CHART_WILLIAM;
                break;
            case R.id.text_bar_chart_mp:
                pieChart.setChecked(false);
                williamBarChart.setChecked(false);
                mpBarChart.setChecked(true);
                type = Constants.BAR_CHART_MP;
                break;
        }
    }

    public interface ChangeTypeListener {
        void onChangeType(int type);
    }
}