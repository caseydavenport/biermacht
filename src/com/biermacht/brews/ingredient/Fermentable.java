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
	
	// Static values =================================================
	// ===============================================================
	public static final String GRAIN = "Grain";
	public static final String EXTRACT = "Extract";
	public static final String ADJUNCT = "Adjunct";

	public Fermentable(String name)
	{
		super(name);
		setName(name);
		this.amount = 0;
		this.color = 0;
		this.gravity = 1;
		this.type = GRAIN;
		this.efficiency = 1;
	}
	
	public Fermentable(String name, String units, double colour, double grav, String type)
	{
		super(name);
		this.color = colour;
		this.gravity = grav;
		this.type = type;
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
		// TODO Auto-generated method stub
		return false;
	}
}

