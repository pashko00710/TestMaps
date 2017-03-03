package me.uptop.testmaps2;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        sContext = getApplicationContext();
    }

    public static Context getContext() {return sContext;}
}
