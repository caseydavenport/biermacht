package com.biermacht.brews.ingredient;
import android.os.Parcel;

import com.biermacht.brews.utils.Units;

public class Yeast extends Ingredient {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	// Name - Inherited
	// Version - Inherited
	private String type;								// Ale, Lager, etc
	private String form;								// Liquid, Dry, etc
	private double amount;								// Amount in liters
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private double minTemp;
	private double maxTemp;
	private double attenuation;							// Percentage: x / 100
	private String notes;
	private String bestFor;
    private String productid;
    private String lab;
	
	// Custom Fields ==================================================
	// ================================================================

	
	// Static values =================================================
	// ===============================================================
	public static final String TYPE_ALE = "Ale";
	public static final String TYPE_LAGER = "Lager";
	public static final String TYPE_WHEAT = "Wheat";
	public static final String TYPE_WINE = "Wine";
	public static final String TYPE_CHAMPAGNE = "Champagne";
	
	public static final String FORM_LIQUID = "Liquid";
	public static final String FORM_DRY = "Dry";
	public static final String FORM_SLANT = "Slant";
	public static final String FORM_CULTURE = "Culture";

	public Yeast(String name)
	{
		super(name);
		this.type = TYPE_ALE;
		this.form = FORM_LIQUID;
		this.amount = 0;
		this.minTemp = 0;
		this.maxTemp = 0;
		this.attenuation = 75;
		this.notes = "";
		this.bestFor = "";
        this.productid = "";
        this.lab = "";
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

    }

    @Override
    public String toString()
    {
        return this.getName();
    }

	@Override
	public String getType() {
		return Ingredient.YEAST;
	}

	@Override
	public String getShortDescription() {
        String s = this.getLaboratory();
        s += " " + this.getProductId();

        if (s.length() > 3)
            s += ": ";
         s += this.notes;
		return s;
	}

	public String getBeerXmlStandardUnits()
	{
		return Units.LITERS;
	}

	public double getDisplayAmount()
	{
		// TODO: We just assume a single yeast pkg
		return 1;
	}

	public double getBeerXmlStandardAmount()
	{
		return this.amount;
	}

	public void setDisplayAmount(double amt)
	{
		this.amount = amt;
	}

	public void setBeerXmlStandardAmount(double amt)
	{
		this.amount = amt;
	}
	
	@Override
	public String getDisplayUnits() {
		return Units.PACKAGES;
	}

	@Override
	public int hashCode() {
        int hc = this.getName().hashCode();
        hc = hc ^ (int) (this.getAttenuation() * 1234);
        return hc;
	}

	@Override
	public boolean equals(Object o) {
        if (o instanceof Yeast)
            if (this.hashCode() == o.hashCode())
                return true;
        return false;
	}

	@Override
	public void setStartTime(int startTime) {
		// Start time to come later?
	}

	@Override
	public void setEndTime(int endTime) {
		// Yeasts shouldn't have an end time...
	}

	@Override
	public int getStartTime() {
		return 0;
	}

	@Override
	public int getEndTime() {
		return 0;
	}

	@Override
	public int getTime() {
		return 0;
	}

	@Override
	public void setShortDescription(String description) {
		this.notes = description;
	}

	@Override
	public void setDisplayUnits(String units) {

	}

	/**
	 * @return the type
	 */
	public String getYeastType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the form
	 */
	public String getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}

	/**
	 * @return the minTemp
	 */
	public double getMinTemp() {
		return minTemp;
	}

	/**
	 * @param minTemp the minTemp to set
	 */
	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	/**
	 * @return the maxTemp
	 */
	public double getMaxTemp() {
		return maxTemp;
	}

	/**
	 * @param maxTemp the maxTemp to set
	 */
	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}
	
	public int getBeerXmlStandardFermentationTemp()
	{
		return (int) ((maxTemp + minTemp) / 2);
	}
	
	public int getDisplayFermentationTemp()
	{
		return (int) Units.celsiusToFarenheit(((maxTemp + minTemp) / 2));
	}

	/**
	 * @return the attenuation
	 */
	public double getAttenuation() {
		return attenuation;
	}

	/**
	 * @param attenuation the attenuation to set
	 */
	public void setAttenuation(double attenuation) {
		this.attenuation = attenuation;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the bestFor
	 */
	public String getBestFor() {
		return bestFor;
	}

	/**
	 * @param bestFor the bestFor to set
	 */
	public void setBestFor(String bestFor) {
		this.bestFor = bestFor;
	}

    public void setProductId(String s)
    {
        this.productid = s;
    }

    public void setLaboratory(String s)
    {
        this.lab = s;
    }

    public String getLaboratory()
    {
        return this.lab;
    }

    public String getProductId()
    {
        return this.productid;
    }
}
