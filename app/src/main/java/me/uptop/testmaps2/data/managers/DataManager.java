package me.uptop.testmaps2.data.managers;

import android.content.Context;

import me.uptop.testmaps2.App;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;
    private RealmManager mRealmManager = RealmManager.getInstance();

    public DataManager(){
        this.mContext = App.getContext();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public RealmManager getRealmManager() {
        return mRealmManager;
    }

    public Context getContext(){return mContext;}
}
