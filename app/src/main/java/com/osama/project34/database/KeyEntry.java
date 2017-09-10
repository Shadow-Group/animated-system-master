package com.osama.project34.database;

import android.provider.BaseColumns;

/**
 * Created by bullhead on 9/5/17.
 */

public final class KeyEntry implements BaseColumns {
    public static final String COLUMN_EMAIL = "email_address";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALID = "valid";
    public static final String TABLE_NAME = "keys";

    public static final class Indices {
        public static final int COLUMN_EMAIL = 1;
        public static final int COLUMN_KEY = 2;
        public static final int COLUMN_VALID = 3;
    }
}
