package com.biermacht.brews.ingredient;

import java.util.ArrayList;

import com.biermacht.brews.utils.Measurements;

public class Hop extends Ingredient {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	
	// Name - Inherited
	// Version - Inherited
	private double amount;                      // Weight in kg
	private double alpha;                       // Alpha in %
	private String use;                         // Boil, Dry Hop, etc
	private int time;                           // Time (mins) - variable based on use
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private String type;                        // Bittering, Armoa, Both
	private String form;                        // Pellet, plug, whole
	private String origin;                      // Place of origin
	private ArrayList<String> substitutes;      // Substitute options
	
	// Custom Fields ==================================================
	// ================================================================
	private String description;
	private String hopType;
	
	// Static values =================================================
	// ===============================================================
	public static final String FORM_PELLET = "Pellet";
	public static final String FORM_WHOLE = "Whole";
	public static final String FORM_PLUG = "Plug";
	public static final String USE_BOIL = "Boil";
	public static final String USE_DRY_HOP = "Dry Hop";
	public static final String USE_MASH = "Mash";
	public static final String USE_FIRST_WORT = "First Wort";
	public static final String USE_AROMA = "Aroma";

	public Hop(String name)
	{
		super(name);
		this.setAlphaAcidContent(0);
		this.setDescription("No Description");
		this.setBoilStartTime(0);
		this.setBoilEndTime(60);
		this.setForm(FORM_PELLET);
	}
	
	public Hop(String name, double aAcid, String desc) {
		super(name);
		this.setAlphaAcidContent(aAcid);
		this.setDescription(desc);
		this.setBoilStartTime(0);
		this.setBoilEndTime(60);
		this.setForm(FORM_PELLET);
	}

	@Override
	public String getType() {
		return Ingredient.HOP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAlphaAcidContent() {
		return alpha;
	}

	public void setAlphaAcidContent(double alpha) {
		this.alpha = alpha;
	}

	@Override
	public String getShortDescription() {
		return this.description;
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public void setAmount(double amt) {
		this.amount = amt;
	}

	public String getForm() {
		return form;
	}
	
	public void setForm(String form) {
		this.form = form;
	}

	@Override
	public String getUnits() {
		if(Measurements.useMetric())
			return "g";
		else
			return "oz";
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