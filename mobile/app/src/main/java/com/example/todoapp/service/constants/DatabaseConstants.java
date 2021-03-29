package com.example.todoapp.service.constants;

public class DatabaseConstants {
    public static final long DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "todo_db_" + DATABASE_VERSION + ".realm";

    public static class TODO {
        public static final String ID = "id";
        public static final String DESCRIPTION = "description";
        public static final String COMPLETED = "completed";
    }
}
