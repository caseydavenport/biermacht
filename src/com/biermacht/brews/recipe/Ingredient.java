package com.biermacht.brews.recipe;

public abstract class Ingredient {
	private long id;
	private String name;
	private String unit;
	private double amount;
	private double time;
	private long ownerId;
	
	public static final String GRAIN = "grain";
	public static final String HOP = "hop";
	
	// Public constructors
	public Ingredient(String name)
	{
		this.name = name;
		this.amount = 0;
		this.unit = "";
		this.time = -1;
		this.ownerId = -1;
	}
	public Ingredient(String name, double amount, String unit, float time)
	{
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.time = time;
		this.ownerId = -1;
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
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	// Abstract methods of Ingredient
	public abstract String getType();
	
//====================================================================================
//====================================================================================

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


//====================================================================================
//====================================================================================
	
	// Yeast subclass of Ingredient
	public class Yeast extends Ingredient {

		public Yeast(String name)
		{
			super(name);
		}
		
		public Yeast(String name, double amount, String unit, float time) {
			super(name, amount, unit, time);
		}

		@Override
		public String getType() {
			return "Yeast";
		}	
	}
	

//====================================================================================
//====================================================================================
	
	// Other subclass of Ingredient
	public class OtherIngredient extends Ingredient {

		public OtherIngredient(String name)
		{
			super(name);
		}
		
		public OtherIngredient(String name, double amount, String unit, float time) {
			super(name, amount, unit, time);
		}

		@Override
		public String getType() {
			return "Other";
		}
	}
}
