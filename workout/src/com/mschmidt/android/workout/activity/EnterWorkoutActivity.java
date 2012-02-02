package com.mschmidt.android.workout.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.mschmidt.android.workout.IWorkoutComponent;
import com.mschmidt.android.workout.R;
import com.mschmidt.android.workout.WorkoutSession;

public class EnterWorkoutActivity extends Activity {
	public static final String TAG = "EnterWorkoutActivity";
	private static final int ADD_REST_REQUEST_CODE = 1;
	private static final int ADD_EXERCISE_REQUEST_CODE = 2;

	private ListView existingItemsListView;
	private Button addItemButton;
	private Button startWorkoutButton;
	private Spinner workoutItemSpinner;
	private ArrayAdapter<IWorkoutComponent> listViewAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build_workout);

		// Obtain handles to UI objects
		existingItemsListView = (ListView) findViewById(R.id.existingItemsListView);
		addItemButton = (Button) findViewById(R.id.addItemButton);
		startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
		workoutItemSpinner = (Spinner) findViewById(R.id.workoutItemSpinner);

		// Register handler for UI elements
		addItemButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "addItemButton clicked");
				// get spinner value

				Object selectedValue = workoutItemSpinner.getSelectedItem();
				if (selectedValue != null) {
					Log.d(TAG, "selectedValue: " + selectedValue);
					// if rest, launch add_rest
					if (((String) selectedValue).equals("Rest")) {
						launchAddRest();
						// if exercise, launch add_exercise
					} else if (((String) selectedValue).equals("Exercise")) {
						launchAddExercise();
					}
				}
			}
		});

		// Populate the list of workout items
		populateListView();

		// Populate the spinner of workout item types
		populateSpinner();
	}

	/**
	 * Populate the contact list based on account currently selected in the
	 * account spinner.
	 */
	private void populateListView() {
		listViewAdapter = new ArrayAdapter<IWorkoutComponent>(this,
				android.R.layout.simple_list_item_1, WorkoutSession
						.getInstance().getWorkoutItems());
		existingItemsListView.setAdapter(listViewAdapter);

		registerForContextMenu(existingItemsListView);
		/* Add Context-Menu listener to the ListView. */
		// existingItemsListView
		// .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		//
		// public void onCreateContextMenu(ContextMenu menu, View v,
		// ContextMenuInfo menuInfo) {
		// menu.setHeaderTitle("Delete Item?");
		// menu.add(Menu.NONE, CONTEXTMENU_DELETEITEM, Menu.NONE,
		// "Delete this item!");
		// }
		// });
	}

	private void populateSpinner() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.workout_item_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		workoutItemSpinner.setAdapter(adapter);
	}

	/**
	 * Launches the Add Rest activity to add a new rest item to the workout
	 */
	private void launchAddRest() {
		Intent i = new Intent(this, AddRestActivity.class);
		startActivityForResult(i, ADD_REST_REQUEST_CODE);
	}

	/**
	 * Launches the Add Exercise activity to add a new exercise to the workout
	 */
	private void launchAddExercise() {
		Intent i = new Intent(this, AddExerciseActivity.class);
		startActivityForResult(i, ADD_EXERCISE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_EXERCISE_REQUEST_CODE
				|| requestCode == ADD_REST_REQUEST_CODE) {
			Log.d(TAG, "requestCode: " + requestCode);
			Log.d(TAG, "resultCode: " + resultCode);
			listViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.workoutitems_contextmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();

		/* Switch on the ID of the item, to get what the user selected. */
		switch (item.getItemId()) {
		case R.id.delete:

			/* Remove it from the list. */
			WorkoutSession.getInstance().removeWorkoutItem(menuInfo.position);

			listViewAdapter.notifyDataSetChanged();
			return true; /* true means: "we handled the event". */
		default:
			return super.onContextItemSelected(item);
		}
	}
}