package com.biermacht.brews.recipe;

import java.util.HashMap;

import android.util.Log;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private int order;
	private double startTime;
	private double endTime;
	private double duration;
	private String duration_units;
	private HashMap<String, Integer> typeToOrder;
	
	public static String TYPE_STEEP = "Steep";
	public static String TYPE_BOIL = "Boil";
	public static String TYPE_PRIMARY = "Prim";
	public static String TYPE_SECONDARY = "Sec";
	public static String TYPE_DRY_HOP = "Hop";
	public static String TYPE_MASH = "Mash";
	public static String TYPE_YEAST = "Yeast";
	public static String TYPE_COOL = "Cool";
	public static String TYPE_OTHER = "";
	
	public Instruction()
	{
		this.setInstructionText("Blank Instruction");
		this.startTime = 0;
		this.endTime = 0;
		this.duration = 0;
		this.duration_units = "mins";
		this.order = -1;
		this.instructionType = TYPE_OTHER;
		
		typeToOrder = new HashMap<String, Integer>();
		this.configureHashMap();
	}

	public String getInstructionText() {
		return instructionText;
	}
	
	public void addToText(String s)
	{
		instructionText += s;
	}
	
	/**
	 * Used for ordering of instruction 
	 */
	public int getOrder()
	{
		if (this.order >= 100)
		{
			Log.d("recipe.Instruction", "Instruction order out of bounds");
			return -1;
		}
		
		try
		{
			return this.typeToOrder.get(this.getInstructionType());
		} 
		catch (Exception e)
		{
			Log.d("recipe.Instruction", "Failed to get instruction order");
			return -1;
		}
	}
	
	/*
	 * Sets order within 
	 */
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
	
	private void configureHashMap()
	{
		int i = 0;
		this.typeToOrder.put(TYPE_OTHER, i);	    i +=100;
		this.typeToOrder.put(TYPE_STEEP, i);	    i +=100;
		this.typeToOrder.put(TYPE_MASH, i);	   		i +=100;
		this.typeToOrder.put(TYPE_BOIL, i);	    	i +=100;
		this.typeToOrder.put(TYPE_COOL, i);	    	i +=100;
		this.typeToOrder.put(TYPE_YEAST, i);		i +=100;
		this.typeToOrder.put(TYPE_PRIMARY, i);		i +=100;
		this.typeToOrder.put(TYPE_SECONDARY, i);	i +=100;
		this.typeToOrder.put(TYPE_DRY_HOP, i);		i +=100;
	}
}
