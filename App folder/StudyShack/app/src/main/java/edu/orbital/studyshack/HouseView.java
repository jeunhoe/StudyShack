package edu.orbital.studyshack;

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

import java.util.Locale;

public class HouseView extends AppCompatActivity {

    private ImageView upButton;

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
    String houselevel;
    int housetiming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //instantiate both database
        dbH = new HouseLevelDbHelper(this);
        db = dbH.getWritableDatabase();
        dbHspecific = new HouseDbHelper(this);
        dbspecific = dbHspecific.getWritableDatabase();
        //pull needed data  from input
        //housename = getIntent().getStringExtra( key_of_house_name);
        //houselevel = getIntent().getExtras().getInt(key_of_the_integer);


        //query max level
        //make house object using levledatabase

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);
        upButton = findViewById(R.id.house_view_up_button);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountdown = findViewById(R.id.text_view_countdown);

        mButtonStartStop = findViewById(R.id.button_start_stop);
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
                Toast.makeText(getApplicationContext(), "Congratulations! Good study session :)", Toast.LENGTH_LONG).show();
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

//    private int getTotaltime(SQLiteDatabase db, String name){
//        Cursor c = db.rawQuery("select input from ")
//
//    }

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

}
