package com.mschmidt.android.workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {

	private static WorkoutSession instance = null;

	private List<IWorkoutComponent> workoutItems;

	protected WorkoutSession() {
		setWorkoutItems(new ArrayList<IWorkoutComponent>());
	}

	public static WorkoutSession getInstance() {
		if (instance == null) {
			instance = new WorkoutSession();
		}
		return instance;
	}

	public List<IWorkoutComponent> getWorkoutItems() {
		return workoutItems;
	}

	public void setWorkoutItems(List<IWorkoutComponent> workoutItems) {
		this.workoutItems = workoutItems;
	}

	public void addWorkoutItem(IWorkoutComponent item) {
		this.workoutItems.add(item);
	}

	public void removeWorkoutItem(int i) {
		this.workoutItems.remove(i);
	}
}
