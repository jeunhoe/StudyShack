package edu.orbital.studyshack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.barGraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(30f,1));
        barEntries.add(new BarEntry(20f,2));
        barEntries.add(new BarEntry(10f,3));
        barEntries.add(new BarEntry(5f,4));
        barEntries.add(new BarEntry(28f,5));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.setData(barData);
        barChart.invalidate();


    }
}
