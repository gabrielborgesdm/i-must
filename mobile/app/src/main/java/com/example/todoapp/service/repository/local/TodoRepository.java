package com.example.todoapp.service.repository.local;

import android.util.Log;

import com.example.todoapp.service.constants.DatabaseConstants;
import com.example.todoapp.service.model.TodoModel;

import java.util.List;

import io.realm.Realm;

import static com.example.todoapp.service.constants.TodoConstants.TODO_TAG;
import static com.example.todoapp.service.repository.local.RealmHelpers.getRealm;

public class TodoRepository {
    private static TodoRepository repository = null;

    private TodoRepository() {
    }

    public static TodoRepository getRealmRepository() {
        if (repository == null) {
            repository = new TodoRepository();
        }
        return repository;
    }

    public TodoModel get(final String id) {
        TodoModel todo;
        Realm realm = null;
        try {
            realm = getRealm();
            todo = realm
                    .where(TodoModel.class)
                    .equalTo(DatabaseConstants.TODO.ID, id)
                    .findFirst();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return todo;
    }

    public List<TodoModel> getAllFiltered(boolean completed) {
        List<TodoModel> todo = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            todo = realm.where(TodoModel.class).equalTo(DatabaseConstants.TODO.COMPLETED, completed).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TODO_TAG, "getAll: " + todo);
        return todo;
    }

    public boolean saveOrUpdate(final TodoModel todo) {
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(todo));
        } catch (Exception e) {
            Log.d(TODO_TAG, "saveOrUpdate: " + e.getLocalizedMessage());
            success = false;
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
        return success;
    }

    public void delete(final String id) {
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(inRealm ->
                    inRealm.where(TodoModel.class)
                            .equalTo(DatabaseConstants.TODO.ID, id)
                            .findFirst()
                            .deleteFromRealm());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public List<TodoModel> getTodo() {
        return getAllFiltered(false);
    }

    public List<TodoModel> getCompleted() {
        return getAllFiltered(true);
    }

    public boolean complete(TodoModel todo) {
        boolean success = true;
        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(realm1 -> {
                todo.setCompleted(true);
                realm1.insertOrUpdate(todo);
            });
        } catch (Exception e) {
            Log.d(TODO_TAG, "saveOrUpdate: " + e.getLocalizedMessage());
            success = false;
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
        return success;
    }
}
