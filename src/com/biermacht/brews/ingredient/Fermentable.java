package com.biermacht.brews.ingredient;

import com.biermacht.brews.utils.Measurements;

// Grain subclass of Ingredient
public class Fermentable extends Ingredient {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	
	// Name - Inherited
	// Version - Inherited
	private String type;                           // Grain, Extract, Adjunct
	private double amount;                         // Amount of fermentable used
	private double yield;                          // Dry yeild / raw yield
	private double color;                          // Color in Lovibond (SRM)
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private boolean addAfterBoil;                  // True if added after boil
	
	// Custom Fields ==================================================
	// ================================================================
	private double gravity;                        // OG Contribution
	private float efficiency;                      // Efficiency
	private int startTime;						   // Boil start time
	private int endTime;						   // Boil end time
	
	// Static values =================================================
	// ===============================================================
	public static final String GRAIN = "Grain";
	public static final String EXTRACT = "Extract";
	public static final String ADJUNCT = "Adjunct";

	public Fermentable(String name)
	{
		super(name);
		this.type = GRAIN;
		this.amount = 0;
		this.yield = 1;
		this.color = 0;
		this.addAfterBoil = false;
		this.gravity = 1;
		this.efficiency = 1;
	}
	
	public Fermentable(String name, String units, double color, double grav, String type)
	{
		// TODO - FIX THIS CONSTRUCTOR -- REMOVE UNITS
		super(name);
		this.type = type;
		this.amount = 0;
		this.yield = 1;
		this.color = color;
		this.addAfterBoil = false;
		this.gravity = grav;
		this.efficiency = 1;
	}

	@Override
	public String getType() {
		return Ingredient.FERMENTABLE;
	}

	public double getLovibondColor() {
		color = (float) Math.round(color * 10) / 10;
		return color;
	}

	public void setLovibondColor(double color) {
		color = (float) Math.round(color * 10) / 10;
		this.color = color;
	}

	public double getGravity() {
		gravity = (double) Math.round(gravity * 1000) / 1000;
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public String getFermentableType() {
		return type;
	}	
	
	public void setFermentableType(String type){
		this.type = type;
	}
	
	public void setEfficiency(float e)
	{
		this.efficiency = e;
	}
	
	public float getEfficiency()
	{
		return this.efficiency;
	}

	public float getPpg() {
		
		float adjPPG = (float) (efficiency * gravity);
		
		return adjPPG;
	}
	
	@Override
	public double getAmount()
	{
		return this.amount;
	}
	
	@Override 
	public void setAmount(double amt)
	{
		this.amount = amt;
	}

	@Override
	public String getShortDescription() {
		// TODO
		return "";
	}

	@Override
	public String getUnits() {
		if (Measurements.useMetric())
			return Measurements.KILOGRAMS;
		else
			return Measurements.POUNDS;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Fermentable)
			return true;
		else
			return false;
	}

	@Override
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	@Override
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	@Override
	public int getStartTime() {
		return startTime;
	}

	@Override
	public int getEndTime() {
		return endTime;
	}

	@Override
	public int getTime() {
		return getEndTime() - getStartTime();
	}

	/**
	 * @return the yield
	 */
	public double getYield() {
		return yield;
	}

	/**
	 * @param yield the yield to set
	 */
	public void setYield(double yield) {
		this.yield = yield;
	}

	/**
	 * @return the addAfterBoil
	 */
	public boolean isAddAfterBoil() {
		return addAfterBoil;
	}

	/**
	 * @param addAfterBoil the addAfterBoil to set
	 */
	public void setAddAfterBoil(boolean addAfterBoil) {
		this.addAfterBoil = addAfterBoil;
	}

	@Override
	public void setShortDescription(String description) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setUnits(String units) {
		// TODO This does nothign for now
	}
}

