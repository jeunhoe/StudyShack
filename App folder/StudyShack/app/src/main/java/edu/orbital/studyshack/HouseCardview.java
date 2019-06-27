package edu.orbital.studyshack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class HouseCardview extends AppCompatActivity {

    private ImageView upButton;
    private ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_cardview);
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
    }
}
