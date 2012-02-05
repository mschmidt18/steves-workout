package com.mschmidt.android.workout.activity;

import java.text.DecimalFormat;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mschmidt.android.workout.IWorkoutComponent;
import com.mschmidt.android.workout.R;
import com.mschmidt.android.workout.Rest;
import com.mschmidt.android.workout.WorkoutSession;

public class ExecuteWorkoutActivity extends Activity {

	public static final String TAG = "ExecuteWorkoutActivity";

	private int currentItem = 0;

	private Button nextButton;
	private TextView previousText;
	private TextView nextText;
	private TextView currentText;
	private CountDownTimer timer;
	private MediaPlayer mp;
	private Chronometer chrono;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.do_workout);

		nextButton = (Button) findViewById(R.id.nextButton);
		previousText = (TextView) findViewById(R.id.previousText);
		nextText = (TextView) findViewById(R.id.nextText);
		currentText = (TextView) findViewById(R.id.currentText);

		populateDisplay();

		nextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "nextButton clicked");

				// if a countdowntimer is currently running, stop it
				if (timer != null) {
					timer.cancel();
				}

				if (mp != null) {
					mp.release();
				}

				if (chrono != null) {
					chrono.stop();
					chrono.setVisibility(View.INVISIBLE);
				}

				currentItem++;
				if (currentItem >= WorkoutSession.getInstance()
						.getWorkoutItems().size()) {
					finish();
				} else {
					populateDisplay();
				}
			}
		});

	}

	private void populateDisplay() {

		IWorkoutComponent curr = WorkoutSession.getInstance().getWorkoutItems()
				.get(currentItem);

		// set current item text
		if (curr instanceof Rest) {
			final int min = ((Rest) curr).getMinutes();
			final int sec = ((Rest) curr).getSeconds();

			long time = (sec * 1000) + (min * 60000);

			timer = new CountDownTimer(time, 100) {

				@Override
				public void onTick(long millisUntilFinished) {

					currentText.setText("Rest Remaining: "
							+ calcTime(millisUntilFinished));
				}

				@Override
				public void onFinish() {
					// update text to say completed
					currentText.setText(String.format(
							"%d minute %d second rest is over!", min, sec));

					// play rest complete sound
					mp = MediaPlayer.create(ExecuteWorkoutActivity.this,
							R.raw.failure);
					mp.start();

					// start post rest timer
					chrono = (Chronometer) findViewById(R.id.rest_chrono);
					chrono.setBase(SystemClock.elapsedRealtime());
					chrono.start();
					chrono.setVisibility(View.VISIBLE);
				}
			};
			timer.start();
		} else {
			currentText.setText(curr.toString());
		}

		// set text of previous item
		if (currentItem > 0) {
			IWorkoutComponent prev = WorkoutSession.getInstance()
					.getWorkoutItems().get(currentItem - 1);
			previousText.setText(prev.toString());
		}

		// set text of next item
		if (currentItem < WorkoutSession.getInstance().getWorkoutItems().size() - 1) {
			IWorkoutComponent nex = WorkoutSession.getInstance()
					.getWorkoutItems().get(currentItem + 1);
			nextText.setText(nex.toString());
		} else {
			nextText.setText("DONE!");
		}

	}

	/**
	 * turns milliseconds into MM:SS
	 * 
	 * @param ms
	 * @return
	 */
	private String calcTime(long ms) {
		double minutes = 0, seconds = 0;
		StringBuilder sb = new StringBuilder();
		if (ms > 60000) {
			minutes = Math.floor((double) ms / (double) 60000);
		}
		seconds = ((double) ms / (double) 1000) - (minutes * 60);

		DecimalFormat minDf = new DecimalFormat("00");
		DecimalFormat secDf = new DecimalFormat("00.000");

		sb.append(minDf.format(minutes));
		sb.append(":");
		sb.append(secDf.format(seconds));
		return sb.toString();
	}
}
