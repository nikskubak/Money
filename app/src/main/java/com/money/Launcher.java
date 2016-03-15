package com.money;

import android.app.Fragment;
import android.os.Bundle;

import com.artjoker.core.activities.AbstractLauncher;
import com.money.fragments.MainFragment;

/**
 * Created by skuba on 28.02.2016.
 */
public class Launcher extends AbstractLauncher {
    @Override
    protected void initDependencies() {

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
}
