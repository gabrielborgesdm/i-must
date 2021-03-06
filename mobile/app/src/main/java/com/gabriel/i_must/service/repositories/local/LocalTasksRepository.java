package com.gabriel.i_must.service.repositories.local;

import android.util.Log;

import com.gabriel.i_must.service.constants.DatabaseConstants;
import com.gabriel.i_must.service.models.local.LocalTaskModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.gabriel.i_must.service.constants.TaskConstants.TASK_FILTER_ALL;
import static com.gabriel.i_must.service.constants.TaskConstants.TASK_FILTER_COMPLETED;
import static com.gabriel.i_must.service.constants.TaskConstants.TASK_FILTER_OPEN;
import static com.gabriel.i_must.service.constants.TaskConstants.TASK_TAG;
import static com.gabriel.i_must.service.repositories.local.RealmHelpers.getRealm;

public class LocalTasksRepository {
    private static LocalTasksRepository repository = null;

    private LocalTasksRepository() {
    }

    public static LocalTasksRepository getRealmRepository() {
        if (repository == null) {
            repository = new LocalTasksRepository();
        }
        return repository;
    }

    public LocalTaskModel get(final String id) {
        LocalTaskModel task;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.refresh();
            task = realm
                    .where(LocalTaskModel.class)
                    .equalTo(DatabaseConstants.TASK.ID, id)
                    .findFirst();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return task;
    }

    public List<LocalTaskModel> getAllFiltered(int filter) {
        List<LocalTaskModel> task = null;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.beginTransaction();
            switch (filter){
                case TASK_FILTER_ALL:
                    task = realm.where(LocalTaskModel.class)
                            .equalTo(DatabaseConstants.TASK.REMOVED, false)
                            .sort("lastUpdated", Sort.ASCENDING)
                            .findAll();
                    break;
                case TASK_FILTER_COMPLETED:
                case TASK_FILTER_OPEN:
                    Boolean completed = filter == TASK_FILTER_COMPLETED;
                    task = realm.where(LocalTaskModel.class)
                            .equalTo(DatabaseConstants.TASK.COMPLETED, completed)
                            .equalTo(DatabaseConstants.TASK.REMOVED, false)
                            .sort("lastUpdated", Sort.ASCENDING)
                            .findAll();
                    break;
                default:
                    task = realm.where(LocalTaskModel.class)
                            .equalTo(DatabaseConstants.TASK.REMOVED, true)
                            .sort("lastUpdated", Sort.ASCENDING)
                            .findAll();
                    break;

            }

            task = realm.copyFromRealm(task);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return task;
    }

    public boolean saveOrUpdate(final LocalTaskModel task) {
        task.updateLastUpdated();
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(task));
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

    public void delete(final String id, Boolean removeFromDatabase) {
        Realm realm = getRealm();
        try {
            realm.executeTransaction(inRealm -> {
                LocalTaskModel task = inRealm.where(LocalTaskModel.class)
                        .equalTo(DatabaseConstants.TASK.ID, id)
                        .findFirst();
                if (task == null) return;
                if (removeFromDatabase) {
                    task.deleteFromRealm();
                } else {
                    task.updateLastUpdated();
                    task.setRemoved(true);
                    inRealm.insertOrUpdate(task);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public List<LocalTaskModel> getOpenTasks() {
        return getAllFiltered(TASK_FILTER_OPEN);
    }

    public List<LocalTaskModel> getCompleted() {
        return getAllFiltered(TASK_FILTER_COMPLETED);
    }

    public boolean complete(LocalTaskModel task) {
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> {
                task.setCompleted(true);
                task.updateLastUpdated();
                realm1.insertOrUpdate(task);
            });
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

    public void removeAllTasks() {
        try (Realm realm = getRealm()) {
            realm.executeTransaction(inRealm -> {
                RealmResults<LocalTaskModel> tasks = inRealm.where(LocalTaskModel.class)
                        .findAll();
                if (tasks == null || tasks.size() == 0) return;
                tasks.deleteAllFromRealm();
            });
        }
    }
}
