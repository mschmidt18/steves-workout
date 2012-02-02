package com.mschmidt.android.workout.activity;

import com.mschmidt.android.workout.R;
import com.mschmidt.android.workout.Rest;
import com.mschmidt.android.workout.WorkoutSession;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRestActivity extends Activity {
	public static final String TAG = "AddRestActivity";
	private Button addRestButton;
	private Button cancelButton;
	private EditText minutes;
	private EditText seconds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rest);
		
		addRestButton = (Button) findViewById(R.id.addButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		minutes = (EditText) findViewById(R.id.minutesText);
		seconds = (EditText) findViewById(R.id.secondsText);
		
		addRestButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                Log.d(TAG, "addRestButton clicked");
	                Editable sMins = minutes.getText();
	                Editable sSecs = seconds.getText();
	                
	                int iMins = Integer.parseInt(sMins.toString());
	                int iSecs = Integer.parseInt(sSecs.toString());
	                Rest rest = new Rest();
	                rest.setMinutes(iMins);
	                rest.setSeconds(iSecs);
	                WorkoutSession.getInstance().addWorkoutItem(rest);
	                finish();
	            }
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Log.d(TAG, "cancelButton clicked");
				finish();
			}
		});
	                
	}
}
