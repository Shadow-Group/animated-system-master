package com.osama.project34.database;

import android.provider.BaseColumns;

/**
 * Created by bullhead on 9/5/17.
 */

public final class FolderEntry implements BaseColumns {
    public static final String TABLE_NAME = "folders";
    public static final String COLUMN_TITLE = "title";

    public static final class Indices {
        public static final int COLUMN_TITLE = 1;
    }
}
