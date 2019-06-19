package edu.orbital.studyshack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void openStatistics(View view) {
        // not implemented yet, will navigate to Statistics activity later
        Toast.makeText(this, "Not ready yet!", Toast.LENGTH_LONG).show();
    }

    public void openHouseCardview(View view) {
        Intent intent = new Intent (this, HouseCardview.class);
        startActivity(intent);
    }
}
