package com.gabriel.taskapp.service.repositories.local;

import android.util.Log;

import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.models.local.AlarmModel;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static com.gabriel.taskapp.service.repositories.local.RealmHelpers.getRealm;

public class LocalAlarmsRepository {
    private static LocalAlarmsRepository mRepository = null;

    private LocalAlarmsRepository() {
    }

    public static LocalAlarmsRepository getRealmRepository() {
        if (mRepository == null) {
            mRepository = new LocalAlarmsRepository();
        }
        return mRepository;
    }

    public List<AlarmModel> getAlarms() {
        List<AlarmModel> alarms = null;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.beginTransaction();
            alarms = realm.where(AlarmModel.class).findAll();
            alarms = realm.copyFromRealm(alarms);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return alarms;
    }

    public AlarmModel get(final String id) {
        AlarmModel alarm;
        try (Realm realm = getRealm()) {
            realm.refresh();
            alarm = realm
                    .where(AlarmModel.class)
                    .equalTo(DatabaseConstants.ALARM.ID, id)
                    .findFirst();
            if(alarm != null) {
                alarm = realm.copyFromRealm(alarm);
            }
        }
        return alarm;
    }

    public int getAlarmsLength() {
        List<AlarmModel> alarms = new ArrayList<>();
        Realm realm = null;
        try {
            realm = getRealm();
            realm.beginTransaction();
            alarms = realm.where(AlarmModel.class).findAll();
            alarms = realm.copyFromRealm(alarms);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        Log.d(TASK_TAG, "getAlarmsLength: " + alarms.size());
        return alarms.size();
    }

    public boolean saveOrUpdate(final AlarmModel alarm) {
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(alarm));
        } catch (Exception e) {
            Log.d(TASK_TAG, "saveOrUpdate: " + e.getLocalizedMessage());
            success = false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return success;
    }

    public void delete(final String id) {
        Realm realm = getRealm();
        try {
            realm.executeTransaction(inRealm -> {
                AlarmModel alarm = inRealm.where(AlarmModel.class)
                        .equalTo(DatabaseConstants.ALARM.ID, id)
                        .findFirst();
                if (alarm == null) return;
                Log.d(TASK_TAG, "delete: " + alarm.getAlarmId());
                alarm.deleteFromRealm();
            });
        } catch (Exception e) {
            Log.d(TASK_TAG, "delete: " + e.getLocalizedMessage());
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    public void deleteAll() {
        Realm realm = getRealm();
        try {
            realm.executeTransaction(inRealm -> {
                inRealm.where(AlarmModel.class)
                        .findAll()
                        .deleteAllFromRealm();
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}
