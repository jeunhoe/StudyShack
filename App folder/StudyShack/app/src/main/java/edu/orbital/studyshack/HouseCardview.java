package edu.orbital.studyshack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.LinkedList;

public class HouseCardview extends AppCompatActivity {

    private ImageView upButton;
    private ImageView addButton;
    private LinkedList<House> houses;

    private RecyclerView mRecyclerView;
    private HouseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    HouseDbHelper dbSpecific;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Homepage.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_cardview);

        dbH = new HouseLevelDbHelper(this);
        dbSpecific = new HouseDbHelper(this);
        db = dbH.getWritableDatabase();

        upButton = findViewById(R.id.house_cardview_up_button);
        addButton = findViewById(R.id.house_cardview_add_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHouse.class);
                startActivity(intent);
            }
        });
        houses = new LinkedList<>();

        Cursor c = db.rawQuery("select * from " + HouseLevelDbHelper.TABLE_NAME, null);

        // Basic house info
        while(c.moveToNext()){
            String name = c.getString(1);
            String desc = c.getString(2);
            int lvl = c.getInt(3);
            houses.add(new House(name, desc, lvl, 0));
        }

        // Summary statistics
        db = dbSpecific.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        for (int i = 0; i < houses.size(); i++) {
            House house = houses.get(i);
            String houseName = house.getName();

            // House total time
            String query = "select " + HouseDbHelper.KEY_INPUT + " from " + HouseDbHelper.TABLE_NAME
                    + " where " + HouseDbHelper.KEY_NAME + " = " + "\"" + houseName + "\"";
            Cursor cc = db.rawQuery(query, null);

            while (cc.moveToNext()) {
                house.addTotalTime(cc.getInt(0));
            }

            // House weekly time
            String weekQuery = "select " + HouseDbHelper.KEY_INPUT + " from " +
                    HouseDbHelper.TABLE_NAME + " where " + HouseDbHelper.KEY_NAME + " = " + "\"" +
                    houseName + "\"" + " AND " + HouseDbHelper.KEY_WEEK + " = " + "\"" + week + "\"";

            Cursor ccc = db.rawQuery(weekQuery, null);
            while(ccc.moveToNext()) {
                int time = ccc.getInt(0);
                house.addWeekTime(time);
            }
        }


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HouseAdapter(houses);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new HouseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                House toPass = houses.get(position);
                Intent intent = new Intent(getApplicationContext(), HouseView.class);
                intent.putExtra("HOUSE_NAME", toPass.getName());
                intent.putExtra("HOUSE_LEVEL", toPass.getLevel());
                intent.putExtra("HOUSE_DESC", toPass.getDesc());
                //Intent intent = new Intent(getApplicationContext(), Statistics.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        db = dbH.getWritableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = dbH.getWritableDatabase();
        this.updateRecyclerView();
        mAdapter.notifyDataSetChanged();
    }

    private void updateRecyclerView() {
        houses.clear();
        Cursor c = db.rawQuery("select * from " + HouseLevelDbHelper.TABLE_NAME, null);

        while(c.moveToNext()){
            String name = c.getString(1);
            String desc = c.getString(2);
            int lvl = c.getInt(3);
            houses.add(new House(name, desc, lvl, 0));
        }
    }
}
