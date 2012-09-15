package com.biermacht.brews.recipe;

import com.biermacht.brews.utils.Utils;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private int duration;
	private String duration_units;

	public Instruction(String i)
	{
		this.setInstructionText(i);
		this.duration = 0;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setDuration(int duration, String units)
	{
		this.duration = duration;
		this.duration_units = units;
	}

	public String getDuration_units() {
		return duration_units;
	}

	public void setDuration_units(String duration_units) {
		this.duration_units = duration_units;
	}

}
