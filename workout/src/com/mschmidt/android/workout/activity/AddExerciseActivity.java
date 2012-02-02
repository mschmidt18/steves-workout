package com.mschmidt.android.workout.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mschmidt.android.workout.Exercise;
import com.mschmidt.android.workout.R;
import com.mschmidt.android.workout.WorkoutSession;

public class AddExerciseActivity extends Activity {

	public static final String TAG = "AddExerciseActivity";
	private Button addExerciseButton;
	private Button cancelButton;
	private EditText name;
	private EditText weight;
	private EditText reps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_exercise);

		addExerciseButton = (Button) findViewById(R.id.addButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		name = (EditText) findViewById(R.id.exerciseText);
		weight = (EditText) findViewById(R.id.weightText);
		reps = (EditText) findViewById(R.id.repsText);

		addExerciseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "addExerciseButton clicked");

				Exercise exercise = new Exercise();
				exercise.setName(name.getText().toString());
				exercise.setWeight(weight.getText().toString());
				exercise.setReps(reps.getText().toString());

				WorkoutSession.getInstance().addWorkoutItem(exercise);
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
