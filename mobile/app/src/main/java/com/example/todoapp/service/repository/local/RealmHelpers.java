package com.example.todoapp.service.repository.local;

import android.content.Context;

import com.example.todoapp.service.constants.DatabaseConstants;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.annotations.RealmModule;

class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Long version = oldVersion;
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        // Changes from version 0 to 1: Adding lastName.
        // All properties will be initialized with the default value "".
        if (version == 0L) {
            schema.get("Person")
                    .addField("lastName", String.class, FieldAttribute.REQUIRED);
            version++;
        }

    }
};

@RealmModule(allClasses = true)
class Module {}

public class RealmHelpers {
    private static RealmConfiguration getRealmConfig(){
         return new RealmConfiguration.Builder()
                .modules(new Module())
                 .allowQueriesOnUiThread(true)
                 .allowWritesOnUiThread(true)
                .schemaVersion(DatabaseConstants.DATABASE_VERSION)
                .name(DatabaseConstants.DATABASE_NAME)
                .migration(new Migration())
                .build();
    }

    private RealmHelpers(){  }

    public static Realm getRealm(){
        return Realm.getDefaultInstance();
    }



    public static void startRealmContext(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(getRealmConfig());
    }
}
