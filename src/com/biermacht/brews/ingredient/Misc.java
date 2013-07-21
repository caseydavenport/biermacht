package com.biermacht.brews.ingredient;
import com.biermacht.brews.utils.Units;

public class Misc extends Ingredient {
	
	// Beer XML 1.0 Required Fields (To be inherited) =================
	// ================================================================
	private String miscType;
	private String use;
	private Double amount;
	private boolean amountIsWeight;
	private String useFor;
	private String notes;
	
	// Custom Fields ==================================================
	// ================================================================
	private String displayUnits;
	private int startTime;
	private int endTime;
	private Double displayAmount;
	
	// Static values =================================================
	// ===============================================================
	public static String TYPE_SPICE = "Spice";
	public static String TYPE_FINING = "Fining";
	public static String TYPE_WATER_AGENT = "Water Agent";
	public static String TYPE_HERB = "Herb";
	public static String TYPE_FLAVOR = "Flavor";
	public static String TYPE_OTHER = "Other";
	
	public Misc(String name)
	{
		super(name);
		setDisplayUnits("");
		setAmountIsWeight(false);
		setBeerXmlStandardAmount(0);
		setShortDescription("");
		setDisplayAmount(0);
		setUse("");
		setVersion(1);
		setTime(0);
	}
	
	@Override
	public String toString()
	{
		String s = getName();
		s += getUse() + ", " + getUseFor() + ", " + getDisplayAmount() + ", ";
		s += getShortDescription() + ", " + getMiscType();
		
		return s;
	}

	public void setMiscType(String type)
	{
		this.miscType = type;
	}
	
	public String getMiscType()
	{
		if (!miscType.isEmpty() || this.miscType == null)
		    return this.miscType;
		else
			return Misc.TYPE_OTHER;
	}
	
	public void setUse(String u)
	{
		this.use = u;
	}
	
	public String getUse()
	{
		return this.use;
	}
	
	public void setAmountIsWeight(boolean b)
	{
		this.amountIsWeight = b;
	}
	
	public boolean amountIsWeight()
	{
		return this.amountIsWeight;
	}
	
	public void setUseFor(String s)
	{
		this.useFor = s;
	}
	
	public String getUseFor()
	{
		return this.useFor;
	}
	
	@Override
	public String getType() {
		return Ingredient.MISC;
	}

	@Override
	public String getShortDescription() {
		return notes;
	}

	@Override
	public double getDisplayAmount() {
		
		String unit = this.getDisplayUnits();
		
		if (unit.equalsIgnoreCase(Units.GALLONS))
			return Units.litersToGallons(this.amount);
		if (unit.equalsIgnoreCase(Units.GRAMS))
			return Units.kilosToGrams(this.amount);
		if (unit.equalsIgnoreCase(Units.TEASPOONS))
			return Units.litersToTeaspoons(this.amount);
		if (unit.equalsIgnoreCase(Units.OUNCES))
			if (this.amountIsWeight())
			    return Units.kilosToOunces(this.amount);
			else
			    return Units.litersToOunces(this.amount);
		if (unit.equalsIgnoreCase(Units.POUNDS))
			return Units.kilosToPounds(this.amount);
		if (unit.equalsIgnoreCase(Units.CUP) || unit.equalsIgnoreCase(Units.CUPS))
			return Units.litersToCups(this.amount);
		if (unit.equalsIgnoreCase(Units.ITEMS))
			return this.amount;
		else
			return -1; // Should show that we couldnt compute
	}
	
	@Override
	public double getBeerXmlStandardAmount(){
		return this.amount;
	}

	@Override
	public void setDisplayAmount(double amt)
	{
		// Not implemented - use other method
	}
	
	public void setDisplayAmount(double amt, String unit) {

		if (unit.equalsIgnoreCase(Units.GALLONS))
			this.amount = Units.gallonsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.GRAMS))
			this.amount = Units.gramsToKilos(amt);
		else if (unit.equalsIgnoreCase(Units.TEASPOONS))
			this.amount = Units.teaspoonsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.OUNCES))
			if (this.amountIsWeight())
			    this.amount = Units.ouncesToKilos(amt);
			else
			    this.amount = Units.ouncesToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.POUNDS))
			this.amount = Units.poundsToKilos(amt);
		else if (unit.equalsIgnoreCase(Units.CUP) || unit.equalsIgnoreCase(Units.CUPS))
			this.amount = Units.cupsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.ITEMS))
			this.amount = amt;
		else
			this.amount = -1.0; // Should show that we couldnt compute
	}
	
	@Override
	public void setBeerXmlStandardAmount(double amt){
		this.amount = amt;
	}

	@Override
	public String getDisplayUnits() {
		if (!this.displayUnits.equals(""))
			return this.displayUnits;

		if (this.amountIsWeight())
			if (this.amount > .113)// .25lbs
				return Units.POUNDS;
			else
				return Units.OUNCES;
		else
		    if (this.amount > .946) // .25gal
		    	return Units.GALLONS;
		    else
			    return Units.OUNCES;
	}

	@Override
	public String getBeerXmlStandardUnits()
	{
		if (this.amountIsWeight())
			return Units.KILOGRAMS;
		else
			return Units.LITERS;
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
		return endTime - startTime;
	}
	
	public void setTime(int i) {
		this.startTime = 0;
		this.endTime = i;
	}

	@Override
	public void setShortDescription(String description) {
		if (description.isEmpty())
			description = "No description provided";
		this.notes = description;
	}

	@Override
	public void setDisplayUnits(String units) {
		this.displayUnits = units;
	}
}
