package com.biermacht.brews.recipe;

import com.biermacht.brews.utils.Utils;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private double startTime;
	private double endTime;
	private String duration_units;

	public Instruction()
	{
		this.setInstructionText("Blank Instruction");
		this.startTime = 0;
		this.endTime = 0;
		this.duration_units = "mins";
		this.instructionType = Utils.INSTRUCTION_TYPE_OTHER;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public String getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}

	public double getDuration() {
		return getEndTime() - getStartTime();
	}

	public String getDuration_units() {
		return duration_units;
	}

	public void setDuration_units(String duration_units) {
		this.duration_units = duration_units;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

}
