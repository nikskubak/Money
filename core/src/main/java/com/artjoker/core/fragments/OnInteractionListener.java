package com.artjoker.core.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.artjoker.tool.fragments.collections.AnimationCollection;

public interface OnInteractionListener {

    public void onSetPrimary(Fragment fragment, String backStackTag);

    public void onCommit(Fragment fragment, String backStackTag, AnimationCollection animation);

    public void onCommit(Fragment fragment, AnimationCollection animation);

    public void onMatchCommit(Fragment fragment, String backStackTag, AnimationCollection animation);

    public void onPopBack(int type, String backStackTag);

    public void onEvent(int type, Bundle data);

}