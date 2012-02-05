package com.mschmidt.android.workout.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mschmidt.android.workout.IWorkoutComponent;
import com.mschmidt.android.workout.R;
import com.mschmidt.android.workout.Rest;
import com.mschmidt.android.workout.WorkoutSession;

public class AddRestActivity extends Activity {
	public static final String TAG = "AddRestActivity";
	private Button addRestButton;
	private Button cancelButton;
	private EditText minutes;
	private EditText seconds;

	private Integer editingItem = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rest);

		addRestButton = (Button) findViewById(R.id.addButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		minutes = (EditText) findViewById(R.id.minutesText);
		seconds = (EditText) findViewById(R.id.secondsText);

		Bundle editBundle = getIntent().getExtras();
		if (editBundle != null) {
			int itemToEdit = editBundle.getInt("edit");
			if (itemToEdit > 0
					&& itemToEdit <= WorkoutSession.getInstance()
							.getWorkoutItems().size()) {
				editingItem = itemToEdit - 1;
				IWorkoutComponent iItem = WorkoutSession.getInstance()
						.getWorkoutItems().get(editingItem);
				if (iItem instanceof Rest) {
					minutes.setText(Integer.toString(((Rest) iItem)
							.getMinutes()));
					seconds.setText(Integer.toString(((Rest) iItem)
							.getSeconds()));
				}
			}
		}

		addRestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "addRestButton clicked");
				Editable sMins = minutes.getText();
				Editable sSecs = seconds.getText();

				int iMins = Integer.parseInt(sMins.toString());
				int iSecs = Integer.parseInt(sSecs.toString());

				if (editingItem == null) {
					Rest rest = new Rest();
					rest.setMinutes(iMins);
					rest.setSeconds(iSecs);
					WorkoutSession.getInstance().addWorkoutItem(rest);
				} else {
					Rest rest = (Rest) WorkoutSession.getInstance()
							.getWorkoutItems().get(editingItem);
					rest.setMinutes(iMins);
					rest.setSeconds(iSecs);
				}
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
