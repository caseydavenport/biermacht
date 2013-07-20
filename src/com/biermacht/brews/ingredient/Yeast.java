package com.biermacht.brews.ingredient;
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
	
	// Custom Fields ==================================================
	// ================================================================

	
	// Static values =================================================
	// ===============================================================
	public static final String ALE = "Ale";
	public static final String LAGER = "Lager";
	public static final String WHEAT = "Wheat";
	public static final String WINE = "Wine";
	public static final String CHAMPAGNE = "Champagne";
	
	public static final String LIQUID = "Liquid";
	public static final String DRY = "Dry";
	public static final String SLANT = "Slant";
	public static final String CULTURE = "Culture";

	public Yeast(String name)
	{
		super(name);
		this.type = ALE;
		this.form = LIQUID;
		this.amount = 0;
		this.minTemp = 0;
		this.maxTemp = 0;
		this.attenuation = 75;
		this.notes = "";
		this.bestFor = "";
	}

	@Override
	public String getType() {
		return Ingredient.YEAST;
	}

	@Override
	public String getShortDescription() {
		return this.notes;
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
		// TODO: Implement this method
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
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
}
