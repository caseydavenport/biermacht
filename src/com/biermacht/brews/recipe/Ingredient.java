package com.biermacht.brews.recipe;

public abstract class Ingredient {
	private long id;
	private String name;
	private String unit;
	private double amount;
	private double boilStartTime;
	private double boilEndTime;
	private long ownerId;
	
	public static final String GRAIN = "grain";
	public static final String HOP = "hop";
	
	// Public constructors
	public Ingredient(String name)
	{
		this.name = name;
		this.amount = 0;
		this.unit = "";
		this.boilStartTime = -1;
		this.setOwnerId(-1);
	}
	public Ingredient(String name, double amount, String unit, float time)
	{
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.boilStartTime = time;
		this.setOwnerId(-1);
	}
	
	// Public Methods
	@Override 
	public String toString() {
		return name;
	}
	
	// Setters and getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getAmount() {
		return (float) Math.round(amount * 100) / 100.0;
	}
	public void setAmount(double amount) {
		this.amount = (float) Math.round(amount * 100) / 100;
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
	
	// Abstract methods of Ingredient
	public abstract String getType();
	public abstract String getShortDescription();
}
