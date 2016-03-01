package com.money;

import android.app.Fragment;
import android.os.Bundle;

import com.artjoker.core.activities.AbstractLauncher;

/**
 * Created by skuba on 28.02.2016.
 */
public class Launcher extends AbstractLauncher {
    @Override
    protected void initDependencies() {

    }

    @Override
    protected int getContentViewLayoutResId() {
        return 0;
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
}
