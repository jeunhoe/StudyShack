package edu.orbital.studyshack;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HouseSettings extends AppCompatActivity {

    EditText mHouseNameEditText;
    EditText mHouseDescEditText;
    Button mEditButton;
    Button mDeleteButton;

    String origHouseName;
    String origHouseDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings);

        mHouseNameEditText = findViewById(R.id.house_settings_name_edit_text);
        mHouseDescEditText = findViewById(R.id.house_settings_description_edit_text);
        mEditButton = findViewById(R.id.house_settings_edit_button);
        mDeleteButton = findViewById(R.id.house_settings_delete_button);

        Intent intent = getIntent();
        origHouseName = intent.getStringExtra("HOUSE_NAME");
        origHouseDesc = intent.getStringExtra("HOUSE_DESC");
        mHouseNameEditText.setText(origHouseName);
        mHouseDescEditText.setText(origHouseDesc);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((origHouseName.equals(mHouseNameEditText.getText().toString())) &&
                        (origHouseDesc.equals(mHouseDescEditText.getText().toString()))) {
                    return; //do nothing
                } else {
                    String newHouseName = mHouseNameEditText.getText().toString();
                    String newHouseDesc = mHouseDescEditText.getText().toString();

                    // Add code to edit both databases

                    // Cardview database

                    // Specific database

                    // Dialog pop up
                    AlertDialog.Builder alert = new AlertDialog.Builder(HouseSettings.this);
                    alert.setTitle("Success");
                    alert.setMessage("House successfully changed!");
                    alert.setNeutralButton("OK", null);
                    alert.setIcon(R.drawable.studyshacklogo);
                    alert.show();
                }
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HouseSettings.this);
                alert.setTitle("Delete house");
                alert.setMessage("Are you sure you want to delete your house? You will lose all records of your study sessions!");
                alert.setNegativeButton("NO", null);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete from database

                        // Navigate back to cardview
                        Intent intent = new Intent(getApplicationContext(), HouseCardview.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alert.show();
            }
        });

    }
}
