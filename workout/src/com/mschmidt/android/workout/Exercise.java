package com.mschmidt.android.workout;

public class Exercise implements IWorkoutComponent {

	private String name;
	private String weight;
	private String reps;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getReps() {
		return reps;
	}
	public void setReps(String reps) {
		this.reps = reps;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if(weight!=null) {
			sb.append(" ");
			sb.append(weight);
		}
		if(reps!=null) {
			sb.append(" x");
			sb.append(reps);
		}
		return sb.toString();
	}
	
}
