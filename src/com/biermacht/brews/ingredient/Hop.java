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
	private ArrayList<String> substitutes;      // Substitute options TODO THIS!
	
	// Custom Fields ==================================================
	// ================================================================
	private String description;                 // Short description of flavor / use
	private int startTime;                      
	private int endTime;
	
	// Static values =================================================
	// ===============================================================
	public static final String FORM_PELLET = "Pellet";
	public static final String FORM_WHOLE = "Whole";
	public static final String FORM_PLUG = "Plug";
	
	public static final String TYPE_BITTERING = "Bittering";
	public static final String TYPE_AROMA = "Aromatic";
	public static final String TYPE_BOTH = "Bittering and Aromatic";
	
	public static final String USE_BOIL = "Boil";
	public static final String USE_DRY_HOP = "Dry Hop";
	public static final String USE_MASH = "Mash";
	public static final String USE_FIRST_WORT = "First Wort";
	public static final String USE_AROMA = "Aroma";

	public Hop(String name)
	{
		super(name);
		this.amount = 0;
		this.alpha = 0;
		this.use = USE_BOIL;
		this.type = TYPE_BITTERING;
		this.form = FORM_PELLET;
		this.origin = "";
		this.substitutes = new ArrayList<String>(); // TODO Get this from somewhere
		this.description = "No description";
	}
	
	public Hop(String name, double alpha, String desc) {
		super(name);
		this.amount = 0;
		this.alpha = alpha;
		this.use = USE_BOIL;
		this.type = TYPE_BITTERING;
		this.form = FORM_PELLET;
		this.origin = "";
		this.substitutes = new ArrayList<String>(); // TODO Get this from somewhere
		this.description = desc;
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
		if (o instanceof Hop)
			return true;
		else
			return false;
	}

	/**
	 * @return the use
	 */
	public String getUse() {
		return use;
	}

	/**
	 * @param use the use to set
	 */
	public void setUse(String use) {
		this.use = use;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return getEndTime() - getStartTime();
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the substitutes
	 */
	public ArrayList<String> getSubstitutes() {
		return substitutes;
	}

	/**
	 * @param substitutes the substitutes to set
	 */
	public void setSubstitutes(ArrayList<String> substitutes) {
		this.substitutes = substitutes;
	}

	/**
	 * @return the hopType
	 */
	public String getHopType() {
		return type;
	}

	/**
	 * @param hopType the hopType to set
	 */
	public void setHopType(String type) {
		this.type = type;
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

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public void setShortDescription(String description) {
		this.description = description;
	}

	@Override
	public void setUnits(String units) {
		// TODO Auto-generated method stub
		
	}
}