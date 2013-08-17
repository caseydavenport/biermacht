package com.biermacht.brews.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Units;

import android.util.Log;

public class Instruction {
	
	private String instructionText;
	private String instructionType;
	private int order;
	private double duration;
	private String durationUnits;
    private double nextDuration;        // Time the next instruction starts
    private boolean lastInType;       // Last of this type
	private HashMap<String, Integer> typeToOrder;
	private ArrayList<Ingredient> relevantIngredients;
    private MashStep mashStep;     // Used for mash step instructions ONLY
	
	public static String TYPE_STEEP = "Steep";
	public static String TYPE_BOIL = "Boil";
	public static String TYPE_PRIMARY = "1st";
	public static String TYPE_SECONDARY = "2nd";
    public static String TYPE_TERTIARY = "3rd";
	public static String TYPE_DRY_HOP = "Hop";
	public static String TYPE_MASH = "Mash";
    public static String TYPE_RAMP = "Ramp";
	public static String TYPE_YEAST = "Yeast";
	public static String TYPE_COOL = "Cool";
	public static String TYPE_BOTTLING = "Bottle";
    public static String TYPE_CALENDAR = "Calendar";
	public static String TYPE_OTHER = "";

    // Defines for timer types
    public static String TIMER_TYPE_STEEP = "Steep Timer";
    public static String TIMER_TYPE_BOIL = "Boil Timer";
    public static String TIMER_TYPE_MASH = "Mash Timer";
    public static String TIMER_TYPE_NONE = "No Timer";

    public static int FIRST_INSTRUCTION_IN_SET = 0;
    public static int LAST_INSTRUCTION_IN_SET = -1;
	
	public Instruction()
	{
		this.setInstructionText("Blank Instruction");
		this.duration = 0;
		this.durationUnits = Units.MINUTES;
		this.order = -1;
		this.instructionType = TYPE_OTHER;
		this.relevantIngredients = new ArrayList<Ingredient>();
        this.setLastInType(false);
        this.setNextDuration(0);
        this.mashStep = new MashStep();
		
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

    public void setMashStep(MashStep s)
    {
        this.mashStep = s;
    }

    public MashStep getMashStep()
    {
        return this.mashStep;
    }

    public String getBrewTimerTitle()
    {
        if (instructionType.equals(Instruction.TYPE_MASH))
            return mashStep.getName();

        return this.instructionType;
    }

    // Returns the correct text to show for each
    // type of step.
    public String getBrewTimerText()
    {
        String s = "";
        if (this.instructionType.equals(Instruction.TYPE_MASH))
        {
            if (!mashStep.getDescription().isEmpty())
                return mashStep.getDescription();

            if (mashStep.getDisplayInfuseAmount() != 0)
            {
                s = "Add " + String.format("%2.2f", mashStep.getDisplayInfuseAmount()) + " gal " + "of " +
                        "" + String.format("%2.0f", mashStep.getDisplayInfuseTemp()) + (char) 0x00B0 + "F" + "" +
                        " water.  ";
            }

            s += "Hold at " + String.format("%2.0f", mashStep.getDisplayStepTemp()) + (char) 0x00B0 + "F";
            s += " for " + String.format("%2.0f", mashStep.getStepTime()) + " minutes.\n\n";
        }

        else if (this.instructionType.equals(Instruction.TYPE_STEEP))
        {
            s += "Steep ingredients at TEMP"; // TODO
        }

        else if (this.instructionType.equals(Instruction.TYPE_BOIL))
        {
            s = "Add the ingredients shown below to the boil kettle.";
        }
        else if (this.instructionType.equals(Instruction.TYPE_PRIMARY))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_SECONDARY))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_DRY_HOP))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_RAMP))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_YEAST))
        {
            s = "Add yeast, close your fermenter, and secure airlock. " +
                    "Place your fermenter in a dark, temperature controlled " +
                    "environment.";
        }
        else if (this.instructionType.equals(Instruction.TYPE_COOL))
        {
            s = getInstructionText() + " as quickly as possible.  When cool, transfer wort into your " +
                    "primary fermenter, and move to the next step.";
        }
        else if (this.instructionType.equals(Instruction.TYPE_BOTTLING))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_OTHER))
        {
        }
        else if (this.instructionType.equals(Instruction.TYPE_OTHER))
        {
            s = "To add this recipe to your calendar, click below!";
        }

        return s;
    }
	
	/**
	 * Used for ordering of instruction 
	 */
	public int getOrder()
	{
        // If our order is over 100, mod it down so it fits within
        // the designated 100 orders per instruction type
		if (this.order >= 100 || this.order < 0)
            this.order = this.order % 100;
		
		try
		{
			return this.typeToOrder.get(this.getInstructionType()) + this.order;
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

    public void setNextDuration(double i)
    {
        this.nextDuration = i;
    }

    public double getNextDuration()
    {
        return this.nextDuration;
    }

    public void setLastInType(boolean b)
    {
        this.lastInType = b;
    }

    public boolean isLastInType()
    {
        return lastInType;
    }
	
	public void setRelevantIngredients(ArrayList<Ingredient> i)
	{
		this.relevantIngredients = i;
	}
	
	public ArrayList<Ingredient> getRelevantIngredients()
	{
		return this.relevantIngredients;
	}
	
	public void addRelevantIngredient(Ingredient i)
	{
		this.relevantIngredients.add(i);
	}
	
	@Override
	public String toString()
	{
		return this.getInstructionText();
	}

	/**
	 * Sets instruction text to the value given in instructionText
	 * @param instructionText
	 */
	public void setInstructionText(String instructionText) 
	{
		this.instructionText = instructionText;
	}
	
	/**
	 * Sets the instruction text based on the ingredients
	 * given in relevant ingredients list for this instruction
	 */
	public void setInstructionTextFromIngredients()
	{
		String s = "";
		for (Ingredient i : this.getRelevantIngredients())
		{
			if (s.isEmpty())
				s += i.getName();
			else
				s += "\n" + i.getName();
		}
		this.instructionText = s;
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

    public boolean showInBrewTimer()
    {
        if (this.instructionType.equals(TYPE_BOIL))
            return true;
        if (this.instructionType.equals(TYPE_STEEP))
            return true;
        if (this.instructionType.equals(TYPE_MASH))
            return true;
        if (this.instructionType.equals(TYPE_COOL))
            return true;
        if (this.instructionType.equals(TYPE_YEAST))
            return true;
        if (this.instructionType.equals(TYPE_CALENDAR))
            return true;

        return false;
    }
	
	public double getDuration() {
		return duration;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	public void setDurationUnits(String d) {
		this.durationUnits = d;
	}
	
	private void configureHashMap()
	{
		int i = 0;
		this.typeToOrder.put(TYPE_OTHER, i);	    i+=100;
		this.typeToOrder.put(TYPE_STEEP, i);	    i+=100;
		this.typeToOrder.put(TYPE_MASH, i);	   		i+=100;
		this.typeToOrder.put(TYPE_BOIL, i);	    	i+=100;
		this.typeToOrder.put(TYPE_COOL, i);	    	i+=100;
		this.typeToOrder.put(TYPE_YEAST, i);		i+=100;
		this.typeToOrder.put(TYPE_PRIMARY, i);		i+=100;
		this.typeToOrder.put(TYPE_SECONDARY, i);	i+=100;
        this.typeToOrder.put(TYPE_TERTIARY, i);	    i+=100;
		this.typeToOrder.put(TYPE_DRY_HOP, i);		i+=100;
		this.typeToOrder.put(TYPE_BOTTLING, i);     i+=100;
        this.typeToOrder.put(TYPE_CALENDAR, i);     i+=100;
	}
}
