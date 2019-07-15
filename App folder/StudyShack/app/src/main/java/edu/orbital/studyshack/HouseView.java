package edu.orbital.studyshack;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HouseView extends AppCompatActivity {

    private ImageView upButton;
    private ImageView upgradeButton;
    private ImageView settingsButton;
    private TextView houseViewHeader;
    private ImageView houseImage;
    private CircularSeekBar seekBar;

    private TextView mTextViewCountdown;
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
    String housedesc;
    int houselevel;
    int housetiming;

    int currentDay;
    int currentDate;
    int currentWeek;
    int currentMonth;
    int currentYear;
    int currentHour;
    int currentMinute;

    int seekBarProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);


        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                String message = String.format("%.0f", progress);
                seekBarProgress = Integer.parseInt(message);
                mTextViewCountdown.setText(message + ":00");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStopTrackingTouch");
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStartTrackingTouch");
            }
        });


        // Tie layout files to java code

        upButton = findViewById(R.id.house_view_up_button);
        upgradeButton = findViewById(R.id.house_view_upgrade_button);
        settingsButton = findViewById(R.id.house_view_settings_button);
        houseViewHeader = findViewById(R.id.house_view_header);
        houseImage = findViewById(R.id.houseview_house);

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
                Intent intent = new Intent(getApplicationContext(), HouseSettings.class);
                intent.putExtra("HOUSE_NAME", housename);
                intent.putExtra("HOUSE_DESC", housedesc);
                startActivity(intent);
            }
        });

        //instantiate both database
        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        dbHspecific = new HouseDbHelper(this);
        dbspecific = dbHspecific.getWritableDatabase();

        //getting extras from the intent
        housename = getIntent().getStringExtra("HOUSE_NAME");
        housedesc = getIntent().getStringExtra("HOUSE_DESC");
        houselevel = getIntent().getExtras().getInt("HOUSE_LEVEL");

        //setting upgrade button visibility
        housetiming = getTotaltime(dbspecific, housename);
        checkUpgrade();

        //setting house image
        houseImage.setImageResource(House.HOUSE_IMAGES[(houselevel - 1)]);
        houseViewHeader.setText(housename);

        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(HouseView.this);
                    alert.setTitle("Confirmation");
                    alert.setMessage("Are you sure you want to stop the timer? All progress will be lost!");
                    alert.setNegativeButton("NO", null);
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mTimerRunning) {
                                stopTimer();
                            } else {
                                return;
                            }
                        }
                    });
                    alert.show();
                } else {
                    if (seekBarProgress == 0) {
                        Toast.makeText(getApplicationContext(), "Please input a time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mStartTimeInMillis = ((long) seekBarProgress) * 60 * 1000;
                    mTimeLeftInMillis = mStartTimeInMillis;
                    startTimer();
                }
            }
        });

        updateCountDownText();
    }

    public void startTimer() {

        seekBar.setVisibility(View.INVISIBLE);
        long millis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        currentDay = cal.get(Calendar.DAY_OF_WEEK);
        currentDate = cal.get(Calendar.DAY_OF_MONTH);
        currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);

        upgradeButton.setVisibility(View.INVISIBLE);


        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                seekBar.setVisibility(View.VISIBLE);

                mTimerRunning = false;
                mButtonStartStop.setText("START");
                mTextViewCountdown.setText("00:00");

                //specific database add input
                ContentValues input = new ContentValues();
                input.put(HouseDbHelper.KEY_DAY, currentDay);
                input.put(HouseDbHelper.KEY_DATE, currentDate);
                input.put(HouseDbHelper.KEY_WEEK, currentWeek);
                input.put(HouseDbHelper.KEY_MONTH, currentMonth);
                input.put(HouseDbHelper.KEY_YEAR, currentYear);
                input.put(HouseDbHelper.KEY_HOUR, currentHour);
                input.put(HouseDbHelper.KEY_MINS, currentMinute);
                input.put(HouseDbHelper.KEY_NAME, housename);

                long timeInputlong = mStartTimeInMillis / (60 * 1000);
                int timeInput = (int) (long) timeInputlong;
                input.put(HouseDbHelper.KEY_INPUT, timeInput);
                long row = dbspecific.insert(HouseDbHelper.TABLE_NAME, null, input);

                if (row == -1) {
                    Toast.makeText(getApplicationContext(), "Sorry, an error occurred and your study session was not recorded :(", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Congratulations! Good study session :)", Toast.LENGTH_LONG).show();
                }

                //update the timeLeft and upgradebutton visibility
                if (houselevel == 5) {
                    Log.d("HouseView", "House is already max level");
                } else {
                    housetiming = getTotaltime(dbspecific, housename);
                    checkUpgrade();
                }
            }
        }.start();

        mTimerRunning = true;
        mButtonStartStop.setText("STOP");
    }

    public void stopTimer() {
        seekBar.setVisibility(View.VISIBLE);
        
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        mButtonStartStop.setText("START");
        mTextViewCountdown.setText("00:00");

        checkUpgrade();
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

        if (houselevel == 5) {
            Log.d("HouseView", "House is already max level");
        } else {
            housetiming = getTotaltime(dbspecific, housename);
            checkUpgrade();
        }
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

        while (c.moveToNext()) {
            result += c.getInt(0);
        }

        return result;
    }

}

