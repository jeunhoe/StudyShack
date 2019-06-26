package edu.orbital.studyshack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HouseLevelDbHelper extends SQLiteOpenHelper {
    private String TableName = "HouseLevel";
    private String KEY_ID = "id";
    private String KEY_NAME = "housename";
    private String KEY_DESC = "description";
    private String KEY_LEVEL = "level";


    public HouseDbHelper(Context context) {
        super(context, TableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "create table " + TableName + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                + KEY_NAME + " TEXT," + KEY_DESC + " TEXT" + KEY_LEVEL + " INTEGER)";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TableName);
        onCreate(db);
    }
}
