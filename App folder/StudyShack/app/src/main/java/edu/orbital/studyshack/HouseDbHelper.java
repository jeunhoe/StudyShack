package edu.orbital.studyshack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HouseDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "HouseInputs";
    public static final String KEY_ID = "id";
    public static final String KEY_DAY = "day";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_MINS = "minutes";
    public static final String KEY_NAME = "housename";
    public static final String KEY_INPUT = "input";


    public HouseDbHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "create table " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DAY + " INTEGER," +
                KEY_DATE + " INTEGER," + KEY_MONTH + " INTEGER," + KEY_YEAR + " INTEGER," +
                KEY_HOUR + " INTEGER," + KEY_MINS + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_INPUT + " INTEGER)";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
