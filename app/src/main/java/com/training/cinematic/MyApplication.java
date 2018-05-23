package com.training.cinematic;

import android.app.Application;


import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dhruvisha on 5/18/2018.
 */

public class MyApplication extends Application {
    public void OnCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }
}
