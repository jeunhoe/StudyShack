package edu.orbital.studyshack;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.LinkedList;

public class Statistics extends AppCompatActivity {

    BarChart barChart;
    ImageView upButton;

    // Database Variables
    HouseLevelDbHelper dbHouses;
    HouseDbHelper dbSpecific;
    SQLiteDatabase dbHousesSQL;
    SQLiteDatabase dbSpecificSQL;

    private LinkedList<String> houseNameList;

    Spinner houseNameDropdown;
    Spinner timeFilterDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        upButton = findViewById(R.id.statistics_up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        barChart = findViewById(R.id.barGraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(30f, 1));
        barEntries.add(new BarEntry(20f, 2));
        barEntries.add(new BarEntry(10f, 3));
        barEntries.add(new BarEntry(5f, 4));
        barEntries.add(new BarEntry(28f, 5));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.setData(barData);
        barChart.invalidate();

        setupDatabase();
        readDatabaseBasic();

        updateDropDown();
    }

    public void setupDatabase() {
        this.dbHouses = new HouseLevelDbHelper(this);
        this.dbHousesSQL = dbHouses.getReadableDatabase();

        this.dbSpecific = new HouseDbHelper(this);
        this.dbSpecificSQL = dbSpecific.getReadableDatabase();
    }

    public void readDatabaseBasic() {
        // Clears initial list
        this.houseNameList = new LinkedList<>();
        houseNameList.add("All Houses");

        // Read from dbHousesSQL (Read basic data into list of houses)
        Cursor c = dbHousesSQL.rawQuery("select " + HouseLevelDbHelper.KEY_NAME + " from " + HouseLevelDbHelper.TABLE_NAME, null);

        // Basic house info
        while (c.moveToNext()) {
            String name = c.getString(0);
            houseNameList.add(name);
        }
    }

    public void updateDropDown() {
        //get the spinner from the xml.
        houseNameDropdown = findViewById(R.id.spinner_house_name);
        timeFilterDropdown = findViewById(R.id.spinner_time_filter);

        //create a list of items for the spinner.
        String[] timeFilter = new String[]{"Day", "Week", "Month", "Year"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeFilter);
        ArrayAdapter<String> adapterName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, houseNameList);

        //set the spinners adapter to the previously created one.
        houseNameDropdown.setAdapter(adapterName);
        timeFilterDropdown.setAdapter(adapterTime);
    }
}
