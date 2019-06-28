package edu.orbital.studyshack;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class HouseView extends AppCompatActivity {

    private ImageView upButton;
    private ImageView upgradeButton;
    private ImageView settingsButton;
    private TextView houseViewHeader;
    private ImageView houseImage;
    private TextView timeToUpgradeTextView;

    private TextView mTextViewCountdown;
    private EditText mEditTextInput;
    private Button mButtonStartStop;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;

    HouseLevelDbHelper dbH;
    SQLiteDatabase db;
    HouseDbHelper dbHspecific;
    SQLiteDatabase dbspecific;

    String housename;
    int houselevel;
    int housetiming;

    int currentDay;
    int currentDate;
    int currentMonth;
    int currentYear;
    int currentHour;
    int currentMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);

        // Tie layout files to java code

        upButton = findViewById(R.id.house_view_up_button);
        upgradeButton = findViewById(R.id.house_view_upgrade_button);
        settingsButton = findViewById(R.id.house_view_settings_button);
        houseViewHeader = findViewById(R.id.house_view_header);
        houseImage = findViewById(R.id.houseview_house);
        timeToUpgradeTextView = findViewById(R.id.time_to_upgrade_textview);

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountdown = findViewById(R.id.text_view_countdown);
        mButtonStartStop = findViewById(R.id.button_start_stop);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                return; // fill in later
            }
        });

        //instantiate both database
        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        dbHspecific = new HouseDbHelper(this);
        dbspecific = dbHspecific.getWritableDatabase();

        //pull needed data to set house image
        housename = getIntent().getStringExtra("HOUSE_NAME");
        houselevel = getIntent().getExtras().getInt("HOUSE_LEVEL");
        housetiming = getTotaltime(dbspecific, housename);
        checkUpgrade();
        houseImage.setImageResource(House.HOUSE_IMAGES[(houselevel - 1)]);
        houseViewHeader.setText(housename);

        //pull needed data to set the time left to upgrade
        int timeLeft = House.timeLimit(houselevel) - checkInputTiming(dbspecific);
        timeToUpgradeTextView.setText(timeLeft + " mins");


        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();
                    mEditTextInput.setVisibility(View.VISIBLE);
                } else {
                    String timeString = mEditTextInput.getText().toString();
                    if (timeString.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please input a time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mStartTimeInMillis = Long.parseLong(timeString) * 60 * 1000;
                    mTimeLeftInMillis = mStartTimeInMillis;
                    startTimer();
                    mEditTextInput.setVisibility(View.INVISIBLE);
                }
            }
        });

        updateCountDownText();
    }

    public void startTimer() {

        long millis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        currentDay = cal.get(Calendar.DAY_OF_WEEK);
        currentDate = cal.get(Calendar.DAY_OF_MONTH);
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);


        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartStop.setText("START");
                mTextViewCountdown.setText("00:00");
                mEditTextInput.setVisibility(View.VISIBLE);
                mEditTextInput.setText("");

                //specific database add input
                ContentValues input = new ContentValues();
                input.put(HouseDbHelper.KEY_DAY, currentDay);
                input.put(HouseDbHelper.KEY_DATE, currentDate);
                input.put(HouseDbHelper.KEY_MONTH, currentMonth);
                input.put(HouseDbHelper.KEY_YEAR, currentYear);
                input.put(HouseDbHelper.KEY_HOUR, currentHour);
                input.put(HouseDbHelper.KEY_MINS, currentMinute);
                long row = dbspecific.insert(HouseDbHelper.TABLE_NAME, null, input);

                if(row == -1) {
                    Toast.makeText(getApplicationContext(), "Sorry, an error occurred and your study session was not recorded :(", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Congratulations! Good study session :)", Toast.LENGTH_LONG).show();
                }
            }
        }.start();

        mTimerRunning = true;
        mButtonStartStop.setText("STOP");
    }

    public void stopTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        mButtonStartStop.setText("START");
        mTextViewCountdown.setText("00:00");
        mEditTextInput.setVisibility(View.VISIBLE);
        mEditTextInput.setText("");
    }

    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);
    }


    private int getTotaltime(SQLiteDatabase db, String name) {
        String query = "select " + HouseDbHelper.KEY_INPUT + " from " + HouseDbHelper.TABLE_NAME + " where " + HouseDbHelper.KEY_NAME + " = " + "\"" + name + "\"";
        Cursor c = db.rawQuery(query, null);
        int result = 0;

        while (c.moveToNext()) {
            result += c.getInt(0);
        }

        return result;

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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = dbH.getWritableDatabase();
        dbspecific = dbHspecific.getWritableDatabase();

    }

    //change house picture
    public void upgradeHouse(View view) {
        Cursor c = db.rawQuery("select * from " + HouseLevelDbHelper.TABLE_NAME + " where " + HouseLevelDbHelper.KEY_NAME + " = ?", new String[]{housename});
        c.moveToFirst();
        String name = c.getString(1);
        String desc = c.getString(2);
        int lvl = c.getInt(3);

        ContentValues values = new ContentValues();
        values.put(HouseLevelDbHelper.KEY_NAME, name);
        values.put(HouseLevelDbHelper.KEY_DESC, desc);
        values.put(HouseLevelDbHelper.KEY_LEVEL, lvl + 1);
        db.update(HouseLevelDbHelper.TABLE_NAME, values, "housename = ?", new String[]{housename});

            houselevel = lvl + 1;
            houseImage.setImageResource(House.HOUSE_IMAGES[(houselevel - 1)]);

            Toast.makeText(getApplicationContext(), "House Upgraded Successfully!", Toast.LENGTH_LONG).show();

            checkUpgrade();
        }

        public void checkUpgrade() {
            if (houselevel == 5) {
            upgradeButton.setVisibility(View.INVISIBLE);
        } else if (housetiming >= House.timeLimit(houselevel)) {
            upgradeButton.setVisibility(View.VISIBLE);
        } else {
            upgradeButton.setVisibility((View.INVISIBLE));
        }
    }

    public int checkInputTiming(SQLiteDatabase db) {
        int result = 0;

        String query = "select " + HouseDbHelper.KEY_INPUT + " from " + HouseDbHelper.TABLE_NAME +
                " where " + HouseDbHelper.KEY_NAME + " = ?";
        Cursor c = db.rawQuery(query, new String[]{housename});

        while(c.moveToNext()){
            result += c.getInt(0);
        }

        return result;
    }

}

