package edu.orbital.studyshack;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Homepage extends AppCompatActivity {

    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    HouseDbHelper dbHspecific;
    SQLiteDatabase dbspecific;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
//
//        dbH = new HouseLevelDbHelper(this);
//        db = dbH.getWritableDatabase();
//        dbHspecific = new HouseDbHelper(this);
//        dbspecific = dbHspecific.getWritableDatabase();
//
//        this.deleteDatabase(HouseLevelDbHelper.TABLE_NAME);
//        this.deleteDatabase(HouseDbHelper.TABLE_NAME);
    }

    public void openStatistics(View view) {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
        // not implemented yet, will navigate to Statistics activity later
        //Toast.makeText(this, "Not ready yet!", Toast.LENGTH_LONG).show();
    }

    public void openHouseCardview(View view) {
        Intent intent = new Intent (this, HouseCardview.class);
        startActivity(intent);
    }
}
