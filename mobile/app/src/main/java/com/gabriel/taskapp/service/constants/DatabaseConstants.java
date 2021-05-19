package com.gabriel.taskapp.service.constants;

public class DatabaseConstants {
    public static final long DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "todo_db_" + DATABASE_VERSION + ".realm";

    public static class TASK {
        public static final String ID = "id";
        public static final String DESCRIPTION = "description";
        public static final String COMPLETED = "completed";
        public static final String DATETIME = "datetime";
        public static final String IMAGES_PATHS = "imagePaths";
        public static final String REMOVED = "removed";
    }
}
