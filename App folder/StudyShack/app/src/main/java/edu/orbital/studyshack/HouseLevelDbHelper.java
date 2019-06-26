package edu.orbital.studyshack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HouseLevelDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "HouseLevel";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "housename";
    public static final String KEY_DESC = "description";
    public static final String KEY_LEVEL = "level";


    public HouseLevelDbHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "create table " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," + KEY_DESC + " TEXT" + KEY_LEVEL + " INTEGER)";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
