package edu.orbital.studyshack;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddHouse extends AppCompatActivity {

    private ImageView upButton;

    EditText mNameEditText;
    EditText mDescEditText;
    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    //SQLiteDatabase dbread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        upButton = findViewById(R.id.add_house_up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mNameEditText = findViewById(R.id.houseNameEditText);
        mDescEditText = findViewById(R.id.houseDescriptionEditText);

        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        //dbread = dbH.getReadableDatabase();

    }

    public void createHouse(View view) {
        String name = mNameEditText.getText().toString();
        String desc = mDescEditText.getText().toString();

//        String[] columnName = {HouseLevelDbHelper.KEY_NAME};
////        String[] checkName = {name};
////        Cursor c = dbread.rawQuery("SELECT * FROM " + HouseLevelDbHelper.TABLE_NAME +  " WHERE " +  HouseLevelDbHelper.KEY_NAME
////                + " = " + name, null);
////        if(c.moveToFirst()){
////            Toast.makeText(getApplicationContext(), "A House with this name already exists.", Toast.LENGTH_LONG).show();
////            return;
////        }

        if (name.equals("") || desc.equals("")) {
            Toast.makeText(getApplicationContext(), "Please input a valid name/description", Toast.LENGTH_LONG).show();
            return;
        } else if (name.length() > 20) {
            Toast.makeText(getApplicationContext(), "Please shorten name to max 20 characters", Toast.LENGTH_LONG).show();
            return;
        } else if (desc.length() > 50) {
            Toast.makeText(getApplicationContext(), "Please shorten description to max 50 characters", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(HouseLevelDbHelper.KEY_NAME, name);
        values.put(HouseLevelDbHelper.KEY_DESC, desc);
        values.put(HouseLevelDbHelper.KEY_LEVEL, 1);

        // make sure to error correct, dun allow duplicates (not done yet)
        long row = db.insert(HouseLevelDbHelper.TABLE_NAME, null, values);
        if (row == -1) {
            Toast.makeText(getApplicationContext(), "Error! Please try again.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "House added!", Toast.LENGTH_LONG).show();
        }
    }
}
