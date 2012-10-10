package com.biermacht.brews.ingredient;

public abstract class Ingredient {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;                    // Ingredient name
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                        // Lookup ID for database                  
	private double boilStartTime;           // Boil start time
	private double boilEndTime;             // Boil end time
	private long ownerId;                   // ID of recipe that contains this
	
	// Static values =================================================
	// ===============================================================
	public static final String FERMENTABLE = "Fermentable";
	public static final String HOP = "Hop";
	public static final String YEAST = "Yeast";
	public static final String MISC = "Misc";
	public static final String WATER = "Water";
	
	// Public constructors
	public Ingredient(String name)
	{
		this.name = name;
		this.boilStartTime = -1;
		this.setOwnerId(-1);
	}
	public Ingredient(String name, String unit, float time)
	{
		this.name = name;
		this.boilStartTime = time;
		this.setOwnerId(-1);
	}
	
	// Abstract methods of Ingredient
	public abstract String getType();
	public abstract String getShortDescription();
	public abstract String getUnits();
	public abstract double getAmount();
	public abstract void setAmount(double amt);
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	
	// Public Methods
	@Override 
	public String toString() {
		return name;
	}
	
	// Universal Setters and getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public double getBoilStartTime() {
		return boilStartTime;
	}

	public void setBoilStartTime(double time) {
		this.boilStartTime = time;
	}
	
	public double getBoilEndTime() {
		return boilEndTime;
	}
	public void setBoilEndTime(double boilEndTime) {
		this.boilEndTime = boilEndTime;
	}
	public double getBoilTime()
	{
		return this.boilEndTime - this.boilStartTime;
	}
	
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
