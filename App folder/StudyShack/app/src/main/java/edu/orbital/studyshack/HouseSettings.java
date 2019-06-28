package edu.orbital.studyshack;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class HouseSettings extends AppCompatActivity {

    EditText mHouseNameEditText;
    EditText mHouseDescEditText;
    Button mEditButton;
    Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings);

        mHouseNameEditText = findViewById(R.id.house_settings_name_edit_text);
        mHouseDescEditText = findViewById(R.id.house_settings_description_edit_text);
        mEditButton = findViewById(R.id.house_settings_edit_button);
        mDeleteButton = findViewById(R.id.house_settings_delete_button);

        Intent intent = getIntent();
        mHouseNameEditText.setText(intent.getStringExtra("HOUSE_NAME"));
        mHouseDescEditText.setText(intent.getStringExtra("HOUSE_DESC"));


    }
}
