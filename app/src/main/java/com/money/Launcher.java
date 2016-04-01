package com.money;

import android.app.Fragment;
import android.os.Bundle;
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

/**
 * Created by skuba on 28.02.2016.
 */
public class Launcher extends AbstractLauncher {

    Drawer drawer;

    @Override
    protected void initDependencies() {

    }

    @Override
    protected void initContent() {
        super.initContent();
        new DrawerBuilder().withActivity(this).build();
        initDrawer();
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
                        switch ((int)drawerItem.getIdentifier()) {
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

    AccountHeader initDrawerHeader(){
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

        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        }else{
            super.onBackPressed();
        }
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }
}
