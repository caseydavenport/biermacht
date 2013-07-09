package com.biermacht.brews.ingredient;
import com.biermacht.brews.utils.Units;

public class Misc extends Ingredient {

	// Defines for type of misc ingredient
	public static String TYPE_SPICE = "Spice";
	public static String TYPE_FINING = "Fining";
	public static String TYPE_WATER_AGENT = "Water Agent";
	public static String TYPE_HERB = "Herb";
	public static String TYPE_FLAVOR = "Flavor";
	public static String TYPE_OTHER = "Other";

	// Defines for use of misc ingredient
	public static String USE_BOIL = "Boil";
	public static String USE_MASH = "Mash";
	public static String USE_PRIMARY = "Primary";
	public static String USE_SECONDARY = "Secondary";
	public static String USE_BOTTLING = "Bottling";
	
	// Required by beerXML
	private String miscType;
	private String use;
	private Double amount;
	private boolean amountIsWeight;
	private String useFor;
	private String notes;
	
	// Others
	private String displayUnits;
	private int startTime;
	private int endTime;
	private Double displayAmount;
	
	public Misc(String name)
	{
		super(name);
		setDisplayUnits("");
		setAmountIsWeight(false);
		setBeerXmlStandardAmount(0);
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
		if (this.displayAmount > 0)
			return this.displayAmount;
			
		String unit = this.getDisplayUnits();
		if (unit.equals(Units.GALLONS))
			return Units.litersToGallons(this.amount);
		if (unit.equals(Units.OUNCES))
			return Units.kilosToOunces(this.amount);
		if (unit.equals(Units.POUNDS))
			return Units.kilosToPounds(this.amount);
		else
			return this.amount;
	}
	
	@Override
	public double getBeerXmlStandardAmount(){
		return this.amount;
	}

	@Override
	public void setDisplayAmount(double amt) {
		this.displayAmount = amt;
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
			return Units.LITERS;
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
		this.notes = description;
	}

	@Override
	public void setDisplayUnits(String units) {
		this.displayUnits = units;
	}
}
