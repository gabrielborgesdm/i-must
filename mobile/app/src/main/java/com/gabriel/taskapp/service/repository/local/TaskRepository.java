package com.gabriel.taskapp.service.repository.local;

import android.util.Log;

import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;

import java.util.List;

import io.realm.Realm;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_FILTER_ALL;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_FILTER_COMPLETED;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_FILTER_OPEN;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static com.gabriel.taskapp.service.repository.local.RealmHelpers.getRealm;

public class TaskRepository {
    private static TaskRepository repository = null;

    private TaskRepository() {
    }

    public static TaskRepository getRealmRepository() {
        if (repository == null) {
            repository = new TaskRepository();
        }
        return repository;
    }

    public TaskModel get(final String id) {
        TaskModel task;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.refresh();
            task = realm
                    .where(TaskModel.class)
                    .equalTo(DatabaseConstants.TASK.ID, id)
                    .findFirst();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return task;
    }

    public List<TaskModel> getAllFiltered(int filter) {
        List<TaskModel> task = null;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.beginTransaction();
            switch (filter){
                case TASK_FILTER_ALL:
                    task = realm.where(TaskModel.class)
                            .equalTo(DatabaseConstants.TASK.REMOVED, false)
                            .findAll();
                    break;
                case TASK_FILTER_COMPLETED:
                case TASK_FILTER_OPEN:
                    Boolean completed = filter == TASK_FILTER_COMPLETED;
                    task = realm.where(TaskModel.class)
                            .equalTo(DatabaseConstants.TASK.COMPLETED, completed)
                            .equalTo(DatabaseConstants.TASK.REMOVED, false)
                            .findAll();
                    break;
                default:
                    task = realm.where(TaskModel.class)
                            .equalTo(DatabaseConstants.TASK.REMOVED, true)
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

    public boolean saveOrUpdate(final TaskModel task) {
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
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm inRealm) {
                    TaskModel task = inRealm.where(TaskModel.class)
                            .equalTo(DatabaseConstants.TASK.ID, id)
                            .findFirst();
                    if (task == null) return;
                    if (removeFromDatabase) {
                        task.deleteFromRealm();
                    } else {
                        task.setRemoved(true);
                        inRealm.insertOrUpdate(task);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public List<TaskModel> getOpenTasks() {
        return getAllFiltered(TASK_FILTER_OPEN);
    }

    public List<TaskModel> getCompleted() {
        return getAllFiltered(TASK_FILTER_COMPLETED);
    }

    public boolean complete(TaskModel task) {
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> {
                task.setCompleted(true);
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
}
