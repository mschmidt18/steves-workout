package com.mschmidt.android.workout.activity;

import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.mschmidt.android.workout.Rest;
import com.mschmidt.android.workout.WorkoutSession;

/**
 * Adding javadoc to this class
 * 
 * @author Matt
 * 
 */
public class EnterWorkoutActivity extends Activity {
	public static final String TAG = "EnterWorkoutActivity";
	private static final int ADD_REST_REQUEST_CODE = 1;
	private static final int ADD_EXERCISE_REQUEST_CODE = 2;
	private static final int DO_WORKOUT_REQUEST_CODE = 3;

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
						launchAddRest(null);
						// if exercise, launch add_exercise
					} else if (((String) selectedValue).equals("Exercise")) {
						launchAddExercise(null);
					}
				}
			}
		});

		startWorkoutButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG, "startWorkoutButton clicked");
				if (WorkoutSession.getInstance().getWorkoutItems().isEmpty()) {
					new AlertDialog.Builder(EnterWorkoutActivity.this)
							.setTitle("Can't Do That!")
							.setMessage("No items in your workout!")
							.setNeutralButton("Close", null).show();
				} else {
					// launch execute workout activity
					launchDoWorkout();
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
	 * 
	 * @param edit
	 *            the index of workoutItemList that we are editing
	 */
	private void launchAddRest(Integer edit) {
		Intent i = new Intent(this, AddRestActivity.class);
		if (edit != null) {
			i.putExtra("edit", edit + 1);
		}
		startActivityForResult(i, ADD_REST_REQUEST_CODE);
	}

	/**
	 * Launches the Execute Workout activity to play back the workout
	 * 
	 */
	private void launchDoWorkout() {
		Intent i = new Intent(this, ExecuteWorkoutActivity.class);
		startActivityForResult(i, DO_WORKOUT_REQUEST_CODE);
	}

	/**
	 * Launches the Add Exercise activity to add a new exercise to the workout
	 * 
	 * @param edit
	 *            the index of workoutItemList that we are editing
	 */
	private void launchAddExercise(Integer edit) {
		Intent i = new Intent(this, AddExerciseActivity.class);
		if (edit != null) {
			i.putExtra("edit", edit + 1);
		}
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

			// Remove it from the list
			WorkoutSession.getInstance().removeWorkoutItem(menuInfo.position);

			listViewAdapter.notifyDataSetChanged();
			return true;
		case R.id.edit:
			if (WorkoutSession.getInstance().getWorkoutItems()
					.get(menuInfo.position) instanceof Rest) {
				launchAddRest(menuInfo.position);
			} else {
				launchAddExercise(menuInfo.position);
			}

			return true;
		case R.id.moveup:
			if (menuInfo.position > 0) {
				Collections.swap(
						WorkoutSession.getInstance().getWorkoutItems(),
						menuInfo.position, menuInfo.position - 1);
				listViewAdapter.notifyDataSetChanged();
				return true;
			} else {
				new AlertDialog.Builder(this).setTitle("Can't Do That!")
						.setMessage("Already at the top!")
						.setNeutralButton("Close", null).show();
				return false;
			}
		case R.id.movedown:
			if (menuInfo.position < WorkoutSession.getInstance()
					.getWorkoutItems().size() - 1) {
				Collections.swap(
						WorkoutSession.getInstance().getWorkoutItems(),
						menuInfo.position, menuInfo.position + 1);
				listViewAdapter.notifyDataSetChanged();
				return true;
			} else {
				new AlertDialog.Builder(this).setTitle("Can't Do That!")
						.setMessage("Already at the bottom!")
						.setNeutralButton("Close", null).show();
				return false;
			}
		default:
			return super.onContextItemSelected(item);
		}
	}
}