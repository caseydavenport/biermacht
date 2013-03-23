package com.biermacht.brews.ingredient;

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
	private String units;
	private int startTime;
	private int endTime;
	
	public Misc(String name)
	{
		super(name);
		setUnits("");
		setAmountIsWeight(false);
		setAmount(0);
		setUse("");
		setVersion(1);
		setTime(0);
	}

	public void setMiscType(String type)
	{
		this.miscType = type;
	}
	
	public String getMiscType()
	{
		return this.miscType;
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
	
	public boolean getAmountIsWeight()
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
	public double getAmount() {
		return amount;	
	}

	@Override
	public void setAmount(double amt) {
		this.amount = amt;
	}

	@Override
	public String getUnits() {
		return units;
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
	public void setUnits(String units) {
		this.units = units;
	}
}
