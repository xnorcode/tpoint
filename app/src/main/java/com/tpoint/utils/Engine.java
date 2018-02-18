package com.tpoint.utils;

import android.app.Application;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class Engine extends Application {


    private static boolean VISIBLE;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /*
    App visibility flag methods
     */
    public static boolean isVisible() {
        return VISIBLE;
    }

    public static void activityPaused() {
        VISIBLE = false;
    }

    public static void activityResumed() {
        VISIBLE = true;
    }
}