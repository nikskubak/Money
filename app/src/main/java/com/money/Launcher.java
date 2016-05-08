package com.money;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.artjoker.core.activities.AbstractLauncher;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.money.fragments.AddCategoryFragment;
import com.money.fragments.OperationFragment;
import com.money.fragments.MainFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by skuba on 28.02.2016.
 */
public class Launcher extends AbstractLauncher {

    private PowerManager.WakeLock wakeLock;
    Drawer drawer;

    @Override
    protected void initDependencies() {

    }

    @Override
    protected void initContent() {
        super.initContent();
        new DrawerBuilder().withActivity(this).build();
        initDrawer();
        startRecomandationService();

    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.launcher;
    }

    @Override
    protected int getContentViewContainerId() {
        return R.id.main_container;
    }

    @Override
    protected Fragment getInitFragment() {
        return new MainFragment();
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
        SecondaryDrawerItem itemMain = new SecondaryDrawerItem().withName(R.string.item_drawer_main).withIdentifier(Constants.ITEM_DRAWER_MAIN);
        SecondaryDrawerItem itemCosts = new SecondaryDrawerItem().withName(R.string.item_drawer_operations).withIdentifier(Constants.ITEM_DRAWER_OPERATIONS);
        SecondaryDrawerItem itemCategories = new SecondaryDrawerItem().withName(R.string.item_drawer_categories).withIdentifier(Constants.ITEM_DRAWER_CATEGORIES);

//create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
//                .withToolbar(toolbar)
                .withAccountHeader(initDrawerHeader())
                .addDrawerItems(
                        itemMain,
                        itemCosts,
                        itemCategories
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case Constants.ITEM_DRAWER_OPERATIONS:
                                drawer.closeDrawer();
                                onCommit(new OperationFragment(), null);
                                break;
                            case Constants.ITEM_DRAWER_CATEGORIES:
                                onCommit(new AddCategoryFragment(), null);
                                drawer.closeDrawer();
                                break;
                            case Constants.ITEM_DRAWER_MAIN:
                                onCommit(new MainFragment(), null);
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
        calendar.set(2016,4,8,0,0,0);
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
}
