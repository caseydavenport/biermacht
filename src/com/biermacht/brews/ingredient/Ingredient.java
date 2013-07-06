package com.biermacht.brews.ingredient;

public abstract class Ingredient {
	
	// Beer XML 1.0 Required Fields (To be inherited) =================
	// ================================================================
	private String name;                    // Ingredient name
	private int version;					// bXML Version being used
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                        // Lookup ID for database                  
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
		this.id = -1;
		this.ownerId = -1;
	}
	
	// Abstract methods of Ingredient
	public abstract String getType();
	public abstract String getShortDescription();
	public abstract void setShortDescription(String description);
	
	// Get beerXML 1.0 Units
	public abstract String getBeerXmlStandardUnits();
	
	// Get display units
	public abstract String getDisplayUnits();
	
	// Set display units
	public abstract void setDisplayUnits(String s);
	
	// Returns display amount
	public abstract double getDisplayAmount();
	
	// Takes display amount
	public abstract void setDisplayAmount(double amt);
	
	// Returns amount in units specified by beerXML 1.0
	public abstract double getBeerXmlStandardAmount();

	// Takes amount in beerXML 1.0 units
	public abstract void setBeerXmlStandardAmount(double amt);
	
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	public abstract void setStartTime(int startTime);
	public abstract void setEndTime(int endTime);
	public abstract int getStartTime();
	public abstract int getEndTime();
	public abstract int getTime();
	
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

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
