package com.biermacht.brews.recipe;

import com.biermacht.brews.utils.Utils;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private double startTime;
	private double endTime;
	private double duration;
	private String duration_units;
	
	public static String TYPE_OTHER = "";
	public static String TYPE_ADD = "Add";
	public static String TYPE_STEEP = "Steep";
	public static String TYPE_BOIL = "Boil";
	public static String TYPE_COOL = "Cool";
	public static String TYPE_FERMENT = "Wait";
	public static String TYPE_DRY_HOP = "Hop";
	
	public Instruction()
	{
		this.setInstructionText("Blank Instruction");
		this.startTime = 0;
		this.endTime = 0;
		this.duration = 0;
		this.duration_units = "mins";
		this.instructionType = TYPE_OTHER;
	}

	public String getInstructionText() {
		return instructionText;
	}
	
	public void addToText(String s)
	{
		instructionText += s;
	}
	
	public Instruction concatInstruction(Instruction i)
	{
		Instruction iret = new Instruction();
		iret.setDuration(i.getDuration());
		iret.setDuration_units(i.getDuration_units());
		iret.setEndTime(i.getEndTime());
		iret.setStartTime(i.getStartTime());
		iret.setInstructionText(getInstructionText() + "\n" + i.getInstructionText());
		return iret;
	}
	
	@Override
	public String toString()
	{
		return this.getInstructionText();
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

	public void setDuration(double d)
	{
		this.duration = d;
	}
	
	public double getDuration() {
		return duration;
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
