package com.money.activities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.artjoker.core.activities.AbstractLauncher;
import com.crashlytics.android.Crashlytics;
import com.fivestar.models.contracts.CategoryContract;
import com.fivestar.models.contracts.TransactionContract;
import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

/**
 * Created by skuba on 28.02.2016.
 */
public class LauncherActivity extends AbstractLauncher implements View.OnClickListener, LoaderManager.LoaderCallbacks {

    Drawer drawer;
//    Toolbar toolbar;
    ImageView filterButton;
    FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton buttonAddTransaction;
    PieChart pieChart;

    @Override
    protected void initDependencies() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
//        FirebaseCrash.report(new Exception("Idi nahuy"));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
//        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        buttonAddTransaction = (FloatingActionButton) findViewById(R.id.main_fragment_add_transaction_button);
        pieChart = (PieChart) findViewById(R.id.chart);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        buttonAddTransaction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fragment_add_transaction_button:
                startActivity(new Intent(this, AddTransactionActivity.class));
                break;
        }
    }

    @Override
    protected void initContent() {
        super.initContent();
        new DrawerBuilder().withActivity(this).build();
        initDrawer();
        startRecomandationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLoader();
    }

    void initLoader(){
        getLoaderManager().restartLoader(
                Constants.LoadersID.LOADER_TRANSACTIONS,
                DatabaseUtils.getTransactionsFromDB(null, String.valueOf(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS*30), String.valueOf(System.currentTimeMillis()), null),
                this
        ).forceLoad();
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.launcher_activity;
    }

    @Override
    protected int getContentViewContainerId() {
        return 0;
    }

    @Override
    protected Fragment getInitFragment() {
        return null;
    }

    @Override
    public void onChange(Fragment fragment) {

    }

    @Override
    public void onEvent(int type, Bundle data) {

    }

    @Override
    protected void initSocialNetworks() {

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
                HashMap<String, Double> categoriesSum = DatabaseUtils.getSumTransactionsByCategories((Cursor) data);
                Log.e("getSumTransactions", DatabaseUtils.getSumTransactionsByCategories((Cursor)data).toString());
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> labels = getCategoriesName(categoriesSum);
                int index = 0;

                for(Map.Entry<String, Double> entry : categoriesSum.entrySet()) {
                    entries.add(new Entry(entry.getValue().floatValue(), index));
                    index++;
                }
                PieDataSet dataset = new PieDataSet(entries, " category");
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData pieData = new PieData(labels, dataset); // initialize Piedata
                pieData.setValueTextSize(15);
                pieChart.setData(pieData);
                pieChart.animateY(500);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private ArrayList<String> getCategoriesName(HashMap<String, Double> categoriesSum){
        Set<String> keys = categoriesSum.keySet();
        ArrayList<String> result = new ArrayList<>();
            for(String key : keys){
                result.add(key);
            }
            return result;
    }


}
