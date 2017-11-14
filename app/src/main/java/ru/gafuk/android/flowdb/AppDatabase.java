package ru.gafuk.android.flowdb;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Александр on 25.09.2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "gafuk";
    public static final int VERSION = 1;
}
