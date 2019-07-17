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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class Statistics extends AppCompatActivity {

    BarChart barChart;
    ImageView upButton;
    ImageView okButton;

    // Database Variables
    HouseLevelDbHelper dbHouses;
    HouseDbHelper dbSpecific;
    SQLiteDatabase dbHousesSQL;
    SQLiteDatabase dbSpecificSQL;

    private LinkedList<String> houseNameList;

    Spinner houseNameDropdown;
    Spinner timeFilterDropdown;

    // Filters
    String houseName = "ALL";
    String timePeriod = "Day";

    Calendar calendar = Calendar.getInstance();
    Cursor search;
    LinkedList<StudySession> studySessions;
    LinkedList<StudySessionDay> studySessionsDay;
    LinkedList<StudySessionMonth> studySessionsMonth;

    int[] graphOfStudySessions;
    LinkedList<BarEntry> sessionsToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        okButton = findViewById(R.id.ok_button);
        upButton = findViewById(R.id.statistics_up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupDatabase();
        readDatabaseBasic();

        updateDropDown();

        barChart = findViewById(R.id.barGraph);
        beginSearch();
        updateEntries();
        addToArrayGraph();
        addToEntryList();
        BarDataSet barDataSet = new BarDataSet(sessionsToDisplay, "STUDY SESSIONS");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.setData(barData);

        //removing legend
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //removing description
        barChart.getDescription().setEnabled(false);

        //removing grid behind
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        //Set axis colors
        barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.whiteText));
        barChart.getXAxis().setTextColor(getResources().getColor(R.color.whiteText));
        barChart.getAxisRight().setDrawLabels(false);

        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.invalidate();
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
        houseNameList.add("ALL");

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
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this, R.layout.spinner_items, timeFilter);
        ArrayAdapter<String> adapterName = new ArrayAdapter<>(this, R.layout.spinner_items, houseNameList);

        //set the spinners adapter to the previously created one.
        adapterName.setDropDownViewResource(R.layout.spinner_items);
        adapterTime.setDropDownViewResource(R.layout.spinner_items);
        houseNameDropdown.setAdapter(adapterName);
        timeFilterDropdown.setAdapter(adapterTime);
    }

//    public void updateGraph() {
//        changeFilters();
//        Calendar calendar = Calendar.getInstance();
//
//        if (houseName.equals("All")) {
//            Cursor c = dbSpecificSQL.rawQuery();
//
//        } else {
//            Cursor c = dbSpecificSQL.rawQuery();
//            if(timePeriod == "Day"){
//                timeperiodmethod
//            }else if (timePeriod == "Week"){
//                weekperiodmethod
//            }
//        }
//    }

    public void changeFilters(View view) {
        houseName = (String) houseNameDropdown.getSelectedItem();
        timePeriod = (String) timeFilterDropdown.getSelectedItem();
        beginSearch();
        updateEntries();
        addToArrayGraph();
        addToEntryList();
        BarDataSet barDataSet = new BarDataSet(sessionsToDisplay, "STUDY SESSIONS");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.setData(barData);

        //removing legend
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //removing description
        barChart.getDescription().setEnabled(false);

        //removing grid behind
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        //Set axis colors
        barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.whiteText));
        barChart.getXAxis().setTextColor(getResources().getColor(R.color.whiteText));
        barChart.getAxisRight().setDrawLabels(false);

        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.invalidate();
    }

    //just to set the cursor and the length of the array for the x-axis
    public void beginSearch() {
        String day = "" + calendar.get(Calendar.DAY_OF_WEEK);
        String date = "" + calendar.get(Calendar.DAY_OF_MONTH);
        String week = "" + calendar.get(Calendar.WEEK_OF_YEAR);
        String month = "" + calendar.get(Calendar.MONTH);
        String year = "" + calendar.get(Calendar.YEAR);


        if (timePeriod.equals("Day")) {
            String query = "select hour, minutes, input from HouseInputs WHERE date = " + "\"" + date + "\"" + " AND month = "
                    + "\"" + month + "\"" + " AND year = " + "\"" + year + "\"";
            if (!houseName.equals("ALL")) {
                query = query + " AND housename = ?" ;
                search = dbSpecificSQL.rawQuery(query, null); // SPECIFIC SEARCH
            } else {
                search = dbSpecificSQL.rawQuery(query, null); // ALL SEARCH
            }
                graphOfStudySessions = new int[24];
        } else if (timePeriod.equals("Week")) {
            String query = "select day, hour, minutes, input from HouseInputs WHERE week = " + "\"" + week + "\"" + " AND year = " + "\"" + year + "\"";
            if (!houseName.equals("ALL")) {
                query = query + " AND housename = ?" ;
                search = dbSpecificSQL.rawQuery(query, null); // SPECIFIC SEARCH
            } else {
                search = dbSpecificSQL.rawQuery(query, null); // ALL SEARCH
            }
            graphOfStudySessions = new int[7];
            //graphOfStudySessions = new int[calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
        }
    }


    public void updateEntries() {
        if (timePeriod.equals("Day")) {
            studySessions = new LinkedList<>();
            while (search.moveToNext()) {
                int hour = search.getInt(0);
                int min = search.getInt(1);
                int input = search.getInt(2);
                studySessions.add(new StudySession(hour, min, input));
            }
        } else if (timePeriod.equals("Week")) {
            studySessionsDay = new LinkedList<>();
            while (search.moveToNext()) {
                int day = search.getInt(0);
                int hour = search.getInt(1);
                int min = search.getInt(2);
                int input = search.getInt(3);
                studySessionsDay.add(new StudySessionDay(day, hour, min, input));

            }
        }
    }

    public void addToArrayGraph() {
        if (timePeriod.equals("Day")) {
            while (!studySessions.isEmpty()) {
                StudySession session = studySessions.poll();
                if (session.exceedsFirstHour()) {
                    int firstHourStudied = session.getHour();
                    graphOfStudySessions[firstHourStudied] += session.clockFirstHour();
                    if (session.exceedsSecondHour()) {
                        int secondHourStudied = session.getHour();
                        graphOfStudySessions[secondHourStudied] += session.clockSecondHour();
                        int thirdHourStudied = session.getHour();
                        graphOfStudySessions[thirdHourStudied] += session.clockRemaining();
                    } else {
                        int secondHourStudied = session.getHour();
                        graphOfStudySessions[secondHourStudied] += session.clockRemaining();
                    }
                } else {
                    int firstHourStudied = session.getHour();
                    graphOfStudySessions[firstHourStudied] += session.clockRemaining();
                }
            }
        } else if (timePeriod.equals("Week")) {
            while (!studySessionsDay.isEmpty()){
                StudySessionDay session = studySessionsDay.poll();
                if (session.exceedsDay()) {
                    int getFirstDay = session.getDay();
                    graphOfStudySessions[getFirstDay] += session.clockDay();
                    graphOfStudySessions[getFirstDay + 1] += session.clockRemaining();
                } else {
                    graphOfStudySessions[session.getDay()] += session.clockRemaining();
                }
            }
        }
    }

    public void addToEntryList() {
        sessionsToDisplay = new LinkedList<>();
        float xValue = 1;
        for (Integer timeSpent: graphOfStudySessions) {
            sessionsToDisplay.add(new BarEntry(xValue, (float) timeSpent));
            xValue++;
        }
    }

}
