package edu.orbital.studyshack;

import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

}
