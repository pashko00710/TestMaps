package me.uptop.testmaps2.data.managers;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.uptop.testmaps2.data.storage.dto.PointsDto;
import me.uptop.testmaps2.data.storage.realm.PointsRealm;

public class RealmManager {
    private Realm mRealmInstance;
    private static RealmManager manager = new RealmManager();

    public void saveProductResponseToRealm(PointsDto pointsDto) {
        Realm realm = Realm.getDefaultInstance();

        PointsRealm pointsRealm =  new PointsRealm(pointsDto);

        Log.e("aq", "saveProductResponseToRealm: "+pointsRealm.getId());

        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(pointsRealm));

        Log.e("aq", "saveProductResponseToRealm: "+getAllQuotesFromRealm());

        realm.close();
    }

    public List<PointsRealm> getAllQuotesFromRealm() {
        RealmResults<PointsRealm> manageProduct = getQueryRealmInstance().where(PointsRealm.class).findAllAsync();
        Log.e("lul", "getAllQuotesFromRealm: "+manageProduct.size());
//        List<PointsRealm> points = mRealmInstance.copyFromRealm(manageProduct);
        return manageProduct;
    }




    public static RealmManager getInstance() {
        return manager;
    }

    private Realm getQueryRealmInstance() {
        if(mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }
}
