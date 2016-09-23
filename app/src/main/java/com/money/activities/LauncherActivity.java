package com.money.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.ChartView;
import com.db.chart.view.HorizontalBarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.CubicEase;
import com.fivestar.models.contracts.TransactionContract;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.money.Constants;
import com.money.DatabaseUtils;
import com.money.R;
import com.money.RecomandationIntentSerice;
import com.money.views.ChartTypeDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

//import com.github.mikephil.charting.data.Entry;

/**
 * Created by skuba on 28.02.2016.
 */
public class LauncherActivity extends BaseActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks, ChartTypeDialog.ChangeTypeListener {

    Drawer drawer;
    //    Toolbar toolbar;
    ImageView filterButton;
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton buttonAddTransaction;
    PieChart pieChart;
    HorizontalBarChart barChart;
    HorizontalBarChartView williamBarChart;
    ImageView chartTypeButton;
    HashMap<String, Double> categoriesSum;
    int chartType = Constants.PIE_CHART;

    @Override
    protected void initViews() {
        super.initViews();
//        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        buttonAddTransaction = (FloatingActionButton) findViewById(R.id.main_fragment_add_transaction_button);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        barChart = (HorizontalBarChart) findViewById(R.id.bar_chart);
        williamBarChart = (HorizontalBarChartView) findViewById(R.id.william_barchart);
        chartTypeButton = (ImageView) findViewById(R.id.chart_type_button);
        Log.e("time", " " + System.currentTimeMillis());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        buttonAddTransaction.setOnClickListener(this);
        chartTypeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fragment_add_transaction_button:
                startActivity(new Intent(this, AddTransactionActivity.class));
                break;
            case R.id.chart_type_button:
                ChartTypeDialog dialog = new ChartTypeDialog();
                dialog.setListener(this);
                dialog.show(getFragmentManager(), "TAG");
                break;
        }
    }

    @Override
    protected void initContent() {
        super.initContent();
        new DrawerBuilder().withActivity(this).build();
//        Cursor cursor = getContentResolver().query(TransactionContract.CONTENT_URI, null, null, new String[]{String.valueOf(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS * 30), String.valueOf(System.currentTimeMillis())}, null);
//        cursor.moveToFirst();
//        Log.e("sum123456", " " + cursor.getDouble(0));
        startrAnalytic();
        initDrawer();
        startRecomandationService();
    }

    private void startrAnalytic() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        logAnalyticsEvent();
    }

    private void logAnalyticsEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.click_event_add_transaction_text));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLoader();
    }

    void initLoader() {
        getLoaderManager().restartLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                DatabaseUtils.getTransactionsFromDB(null, String.valueOf(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS * 60), String.valueOf(System.currentTimeMillis()), null),
                this
        ).forceLoad();
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.launcher_activity;
    }


    void initDrawer() {
//        SecondaryDrawerItem itemMain = new SecondaryDrawerItem().withName(R.string.item_drawer_main).withIdentifier(Constants.ITEM_DRAWER_MAIN);
        SecondaryDrawerItem itemCosts = new SecondaryDrawerItem().withName(R.string.item_drawer_operations).withIdentifier(Constants.ITEM_DRAWER_OPERATIONS);
        SecondaryDrawerItem itemCategories = new SecondaryDrawerItem().withName(R.string.item_drawer_categories).withIdentifier(Constants.ITEM_DRAWER_CATEGORIES);
        SecondaryDrawerItem itemRecommendations = new SecondaryDrawerItem().withName(R.string.item_drawer_recommendations).withIdentifier(Constants.ITEM_RECOMMENDATIONS);


//create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
//                .withToolbar(toolbar)
                .withAccountHeader(initDrawerHeader())
                .addDrawerItems(
//                        itemMain,
                        itemCosts,
                        itemCategories,
                        itemRecommendations
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case Constants.ITEM_DRAWER_OPERATIONS:
                                startActivity(new Intent(getBaseContext(), OperationActivity.class));
                                drawer.closeDrawer();
                                break;
                            case Constants.ITEM_DRAWER_CATEGORIES:
                                startActivity(new Intent(getBaseContext(), AddCategoryActivity.class));
                                drawer.closeDrawer();
                                break;
//                            case Constants.ITEM_DRAWER_MAIN:
////                                onCommit(new MainFragment(), null);
//                                drawer.closeDrawer();
//                                break;
                            case Constants.ITEM_RECOMMENDATIONS:
                                startActivity(new Intent(getBaseContext(), RecommendationActivity.class));
                                drawer.closeDrawer();
                                break;
                        }
                        return true;
                    }
                })
                .build();
//
//        //set the selection to the item with the identifier 1
//        result.setSelection(1);
////set the selection to the item with the identifier 2
//        result.setSelection(item2);
////set the selection and also fire the `onItemClick`-listener
//        result.setSelection(1, true);
    }

    AccountHeader initDrawerHeader() {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_drawer_header)
//                .addProfiles(
//                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
//                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        return false;
//                    }
//                })
                .build();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    void startRecomandationService() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 4, 8, 0, 0, 0);
        Date startDate = new Date(calendar.getTime().getTime());
        Date finisDate = new Date(startDate.getTime() + 30 * DateUtils.DAY_IN_MILLIS);
        Log.e("service_date", startDate.toString());
        Log.e("service_date", finisDate.toString());
        Intent serviceIntent = new Intent(this, RecomandationIntentSerice.class);
        serviceIntent.putExtra(Constants.RECOMANDATION_SERVICE_TAG, Constants.RECOMANDATION_SERVICE_CORRELATION_VALUE);
        serviceIntent.putExtra(Constants.START_DATE_TAG, startDate.getTime());
        serviceIntent.putExtra(Constants.FINISH_DATE_TAG, finisDate.getTime());
        startService(serviceIntent);
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
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case Constants.LoadersID.LOADER_TRANSACTIONS:
                categoriesSum = DatabaseUtils.getSumTransactionsByCategories((Cursor) data);
                Log.e("getSumTransactions", DatabaseUtils.getSumTransactionsByCategories((Cursor) data).toString());

                showChart(categoriesSum, chartType);
//                showBarChart(categoriesSum);
//                showWilliamBarChart(categoriesSum);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void showChart(HashMap<String, Double> categoriesSum, int typeOfChart) {
        switch (typeOfChart) {
            case Constants.PIE_CHART:
                pieChart.setVisibility(View.VISIBLE);
                williamBarChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                showPieChart(categoriesSum);
                break;
            case Constants.BAR_CHART_MP:
                pieChart.setVisibility(View.GONE);
                williamBarChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                showWilliamBarChart(categoriesSum);
                break;
            case Constants.BAR_CHART_WILLIAM:
                pieChart.setVisibility(View.GONE);
                williamBarChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.GONE);
                showBarChart(categoriesSum);
                break;
        }
    }

    private void showPieChart(HashMap<String, Double> categoriesSum) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = getCategoriesName(categoriesSum);
        int index = 0;
        for (Map.Entry<String, Double> entry : categoriesSum.entrySet()) {
            entries.add(new Entry(entry.getValue().floatValue(), index));
            index++;
        }
        PieDataSet dataset = new PieDataSet(entries, " category");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(labels, dataset); // initialize Piedata
        pieData.setValueTextSize(15);
        pieChart.setData(pieData);
        pieChart.animateY(500);
    }

    private void showBarChart(HashMap<String, Double> categoriesSum) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = getCategoriesName(categoriesSum);
        int index = 0;
        for (Map.Entry<String, Double> entry : categoriesSum.entrySet()) {
            entries.add(new BarEntry(entry.getValue().floatValue(), index));
            index++;
        }
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(ColorTemplate.LIBERTY_COLORS);
        dataset.setValueTextSize(12);
        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.setDescription("");
        barChart.animateY(500);
    }

    private void showWilliamBarChart(HashMap<String, Double> categoriesSum) {
        String[] labels = new String[categoriesSum.size()];
        float[] values = new float[categoriesSum.size()];
        int index = 0;
        for (Map.Entry<String, Double> entry : categoriesSum.entrySet()) {
            labels[index] = entry.getKey();
            values[index] = entry.getValue().floatValue();
            index++;
        }

        BarSet dataset = new BarSet(labels, values);
        williamBarChart.setBarSpacing(20.0f);
        williamBarChart.addData(dataset);

// Generic chart customization
// Paint object used to draw Grid
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#727272"));
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1.0f));
        williamBarChart.setGrid(ChartView.GridType.HORIZONTAL, gridPaint);
        williamBarChart.setLabelsFormat(new DecimalFormat("#"));

// Animation customization
        Animation anim = new Animation(500);
        anim.setEasing(new CubicEase());
        williamBarChart.show(anim);
    }

    private ArrayList<String> getCategoriesName(HashMap<String, Double> categoriesSum) {
        Set<String> keys = categoriesSum.keySet();
        ArrayList<String> result = new ArrayList<>();
        for (String key : keys) {
            result.add(key);
        }
        return result;
    }

    @Override
    public void onChangeType(int type) {
        chartType = type;
        initLoader();
    }
}
