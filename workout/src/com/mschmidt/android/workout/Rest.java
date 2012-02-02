package com.mschmidt.android.workout;

public class Rest implements IWorkoutComponent {

	private int minutes;
	private int seconds;
	
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	@Override
	public String toString() {
		return "Rest: " + minutes + " minutes " + seconds + " seconds";
	}
	
}
