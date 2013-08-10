package com.biermacht.brews.ingredient;
import android.os.Parcel;

import com.biermacht.brews.utils.Units;

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
	private double maxInBatch;					   // Max reccomended in this batch
	
	// Custom Fields ==================================================
	// ================================================================
	private String description;					   // Description of fermentable
	private int startTime;						   // Boil start time
	private int endTime;						   // Boil end time
	
	// Static values =================================================
	// ===============================================================
	public static final String TYPE_GRAIN = "Grain";
	public static final String TYPE_EXTRACT = "Extract";
	public static final String TYPE_ADJUNCT = "Adjunct";
	public static final String TYPE_SUGAR = "Sugar";
	
	public Fermentable(String name)
	{
		super(name);
		this.type = TYPE_GRAIN;
		this.amount = 0;
		this.yield = 1;
		this.color = 0;
		this.addAfterBoil = false;
		this.setMaxInBatch(0);
	}


    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * THIS IS HOW WE SERIALIZE THIS OBJECT INTO
     * A PARCEL
     * @param p
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        super.writeToParcel(p, flags);
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

	/**
	 * Gets the gravity
	 * @return
	 */
	public double getGravity() {
		return yieldToGravity(yield);
	}

	/**
	 * Sets gravity and yield accordingly
	 * @param gravity
	 */
	public void setGravity(double gravity) {
		this.yield = gravityToYield(gravity);
	}
	
	/**
	 * gets the fermentables points per pound per gallon
	 * @return
	 */
	public float getPpg() {		
		return (float) (yield * 46)/100;
	}

	public String getFermentableType() {
		return type;
	}	
	
	public void setFermentableType(String type){
		this.type = type;
	}

	@Override
	public double getDisplayAmount()
	{
		return Units.kilosToPounds(this.amount);
	}
	
	@Override 
	public double getBeerXmlStandardAmount(){
		return this.amount;
	}
	
	@Override 
	public void setDisplayAmount(double amt)
	{
		this.amount = Units.poundsToKilos(amt);
	}
	
	@Override
	public void setBeerXmlStandardAmount(double amt){
		this.amount = amt;
	}

	@Override
	public String getDisplayUnits() {
			return Units.POUNDS;
	}
	
	@Override
	public void setDisplayUnits(String s)
	{
		// TODO
	}
	
	@Override
	public String getBeerXmlStandardUnits() {
		return Units.KILOGRAMS;
	}

	@Override
    /**
     * Used for calculating if two fermentables are equal.
     * We want to match on the following fields:
     *         * Name
     *         * SRM Color
     *         * Gravity contribution
     */
	public int hashCode() {
		int hc = this.getName().hashCode();
        hc = hc ^ (int) this.getLovibondColor();
        hc = hc ^ (int) (this.getGravity() * 1234);
        return hc;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Fermentable)
			if (this.hashCode() == o.hashCode())
				return true;
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
		this.description = description;
	}
	
	@Override
	public String getShortDescription() {
		return this.description;
	}

	/**
	 * @return the maxInBatch
	 */
	public double getMaxInBatch() {
		return maxInBatch;
	}
	
	@Override
	public String getUse()
	{
		if (this.getFermentableType().equals(TYPE_EXTRACT))
			return Ingredient.USE_BOIL;
		else
			return Ingredient.USE_MASH;
	}

	/**
	 * @param maxInBatch the maxInBatch to set
	 */
	public void setMaxInBatch(double maxInBatch) {
		this.maxInBatch = maxInBatch;
	}
	
	/**
	 * Turns given gravity into a yield
	 * @param gravity
	 * @return
	 */
	public double gravityToYield(double gravity)
	{
		double yield = 0;
		
		yield = gravity - 1;
		yield = yield * 1000;
		yield = yield * 100 / 46;
		
		return yield;
	}
	
	/**
	 * turns given yield into a gravity
	 * @param yield
	 * @return
	 */
	public double yieldToGravity(double yield)
	{
		double gravity = 0;
		gravity = 1 + (((yield * 46)/100)/1000);
		
		return gravity;
	}
}

