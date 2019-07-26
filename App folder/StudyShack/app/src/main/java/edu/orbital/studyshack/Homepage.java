package edu.orbital.studyshack;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    HouseDbHelper dbHspecific;
    SQLiteDatabase dbspecific;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
//        resetDatabase();
    }

    public void openStatistics(View view) {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    public void openHouseCardview(View view) {
        Intent intent = new Intent (this, HouseCardview.class);
        startActivity(intent);
    }

    public void openInfoPage(View view) {
        Intent intent = new Intent(this, infoPage.class);
        startActivity(intent);
    }

    public void resetDatabase() {
        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        dbHspecific = new HouseDbHelper(this);
        dbspecific = dbHspecific.getWritableDatabase();

        this.deleteDatabase(HouseLevelDbHelper.TABLE_NAME);
        this.deleteDatabase(HouseDbHelper.TABLE_NAME);
    }
}
