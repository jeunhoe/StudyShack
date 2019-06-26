package edu.orbital.studyshack;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class HouseDbHelper extends SQLiteOpenHelper {
    private String TableName = "HouseInputs";
    private String KEY_ID = "id";
    private String KEY_DAY = "day";
    private String KEY_MONTH = "month";
    private String KEY_YEAR = "year";
    private String KEY_HOUR = "hour";
    private String KEY_MINS = "minutes";
    private String KEY_NAME = "housename";
    private String KEY_INPUT = "input";


    public HouseDbHelper(Context context) {
        super(context, TableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "create table " + TableName + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DAY + " INTEGER," + KEY_MONTH + " INTEGER," + KEY_YEAR + " INTEGER," +
                KEY_HOUR+ " INTEGER," + KEY_MINS + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_INPUT + " TEXT)";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TableName);
        onCreate(db);
    }
}
