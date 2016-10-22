package com.maksim88.diffutilsadvrecycler;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by maksim on 22.10.16.
 */
public class AdvRecyclerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
