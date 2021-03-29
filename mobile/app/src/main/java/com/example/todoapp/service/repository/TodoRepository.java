package com.example.todoapp.service.repository;

import com.example.todoapp.service.constants.DatabaseConstants;
import com.example.todoapp.service.model.TodoModel;

import java.util.List;

import io.realm.Realm;

import static com.example.todoapp.service.repository.RealmHelpers.getRealm;

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
        TodoModel todo = null;
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

    public List<TodoModel> getAll() {
        List<TodoModel> todo = null;
        Realm realm = null;
        try {
            realm = getRealm();
            todo = realm.where(TodoModel.class).findAll();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return todo;
    }

    public void saveOrUpdate(final TodoModel todo) {

        Realm realm = null;
        try {
            realm = getRealm();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(todo);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
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
}
