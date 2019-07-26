package edu.orbital.studyshack;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class HouseSettings extends AppCompatActivity {

    ImageView mUpButton;
    EditText mHouseNameEditText;
    EditText mHouseDescEditText;
    Button mEditButton;
    Button mDeleteButton;

    String origHouseName;
    String origHouseDesc;

    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    HouseDbHelper dbHspecific;
    SQLiteDatabase dbspecific;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings);

        setupDatabase();
        setupLayout();

    }

    public void setupDatabase() {
        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        dbHspecific = new HouseDbHelper(this);
        dbspecific = dbHspecific.getWritableDatabase();
    }

    public void setupLayout() {
        // Receiving Intent
        Intent intent = getIntent();
        origHouseName = intent.getStringExtra("HOUSE_NAME");
        origHouseDesc = intent.getStringExtra("HOUSE_DESC");

        // Up Button
        mUpButton = findViewById(R.id.house_settings_up_button);
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // House Name Edit Text
        mHouseNameEditText = findViewById(R.id.house_settings_name_edit_text);
        mHouseNameEditText.setText(origHouseName);

        // House Description Edit Text
        mHouseDescEditText = findViewById(R.id.house_settings_description_edit_text);
        mHouseDescEditText.setText(origHouseDesc);

        // Edit Button
        mEditButton = findViewById(R.id.house_settings_edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((origHouseName.equals(mHouseNameEditText.getText().toString())) &&
                        (origHouseDesc.equals(mHouseDescEditText.getText().toString()))) {
                    return; //do nothing
                } else if (mHouseNameEditText.getText().toString().length() > 20) {
                    Toast.makeText(getApplicationContext(), "Please shorten name to max 20 characters", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String newHouseName = mHouseNameEditText.getText().toString();
                    String newHouseDesc = mHouseDescEditText.getText().toString();

                    // Change db (General database which only contains house info)
                    Cursor dbCursor = db.rawQuery("select " + HouseLevelDbHelper.KEY_LEVEL + " from " + HouseLevelDbHelper.TABLE_NAME + " where " +
                            HouseLevelDbHelper.KEY_NAME + " = ?", new String[]{origHouseName});
                    dbCursor.moveToFirst();

                    ContentValues changedRow = new ContentValues();
                    changedRow.put(HouseLevelDbHelper.KEY_NAME, newHouseName);
                    changedRow.put(HouseLevelDbHelper.KEY_DESC, newHouseDesc);
                    changedRow.put(HouseLevelDbHelper.KEY_LEVEL, dbCursor.getInt(0));
                    db.update(HouseLevelDbHelper.TABLE_NAME, changedRow, HouseLevelDbHelper.KEY_NAME + " = ?", new String[]{origHouseName});

                    // Change dbSpecific (Changes name of house in all study sessions)
                    Cursor dbSpecificCursor = dbspecific.rawQuery("select * from " + HouseDbHelper.TABLE_NAME + " where " +
                            HouseDbHelper.KEY_NAME + " = ?", new String[]{origHouseName});
                    while (dbSpecificCursor.moveToNext()) {
                        ContentValues editedRow = new ContentValues();
                        String chosenID = dbSpecificCursor.getInt(0) + "";
                        editedRow.put(HouseDbHelper.KEY_ID, dbSpecificCursor.getInt(0));
                        editedRow.put(HouseDbHelper.KEY_DAY, dbSpecificCursor.getInt(1));
                        editedRow.put(HouseDbHelper.KEY_DATE, dbSpecificCursor.getInt(2));
                        editedRow.put(HouseDbHelper.KEY_WEEK, dbSpecificCursor.getInt(3));
                        editedRow.put(HouseDbHelper.KEY_MONTH, dbSpecificCursor.getInt(4));
                        editedRow.put(HouseDbHelper.KEY_YEAR, dbSpecificCursor.getInt(5));
                        editedRow.put(HouseDbHelper.KEY_HOUR, dbSpecificCursor.getInt(6));
                        editedRow.put(HouseDbHelper.KEY_MINS, dbSpecificCursor.getInt(7));
                        editedRow.put(HouseDbHelper.KEY_NAME, newHouseName);
                        editedRow.put(HouseDbHelper.KEY_INPUT, dbSpecificCursor.getInt(9));
                        dbspecific.update(HouseDbHelper.TABLE_NAME, editedRow, HouseDbHelper.KEY_ID + " = ?",
                                new String[]{chosenID});

                    }

                    // Alert Dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(HouseSettings.this);
                    alert.setTitle("Success");
                    alert.setMessage("House successfully changed!");
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HouseSettings.this, HouseCardview.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    });
                    alert.setIcon(R.drawable.studyshacklogo);
                    alert.show();
                }
            }
        });

        mDeleteButton = findViewById(R.id.house_settings_delete_button);
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
                        String dbSelection = HouseLevelDbHelper.KEY_NAME + " LIKE ?";
                        String[] dbSelectionArg = {origHouseName};
                        db.delete(HouseLevelDbHelper.TABLE_NAME, dbSelection, dbSelectionArg);

                        String dbSpecificSelection = HouseDbHelper.KEY_NAME + " LIKE ?";
                        String[] dbSpecificSelectionArg = {origHouseName};
                        dbspecific.delete(HouseDbHelper.TABLE_NAME, dbSpecificSelection, dbSpecificSelectionArg);

                        // Navigate back to cardview
                        Intent intent = new Intent(HouseSettings.this, HouseCardview.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = dbH.getWritableDatabase();
        dbspecific = dbHspecific.getWritableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        dbHspecific.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = dbH.getWritableDatabase();
        dbspecific = dbHspecific.getWritableDatabase();
    }
}
