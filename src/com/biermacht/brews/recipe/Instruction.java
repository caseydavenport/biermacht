package com.biermacht.brews.recipe;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private int order;
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
	public static String TYPE_MASH = "Mash";
	
	public Instruction()
	{
		this.setInstructionText("Blank Instruction");
		this.startTime = 0;
		this.endTime = 0;
		this.duration = 0;
		this.duration_units = "mins";
		this.order = -1;
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
		iret.setDurationUnits(i.getDuration_units());
		iret.setEndTime(i.getEndTime());
		iret.setStartTime(i.getStartTime());
		iret.setInstructionText(getInstructionText() + "\n" + i.getInstructionText());
		return iret;
	}
	
	public int getOrder()
	{
		return this.order;
	}
	
	public void setOrder(int o)
	{
		this.order = o;
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

	public void setDurationUnits(String duration_units) {
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
