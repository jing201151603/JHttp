package com.jing.jhttp;

import android.app.Application;

/**
 * Created by bmc on 2016/10/18.
 */

public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());*/
    }
}
