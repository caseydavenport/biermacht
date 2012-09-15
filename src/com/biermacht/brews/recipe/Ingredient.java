package com.biermacht.brews.recipe;

public abstract class Ingredient {
	private String name;
	private String unit;
	private double amount;
	private double time;
	
	// Public constructors
	public Ingredient(String name)
	{
		this.name = name;
		this.amount = 0;
		this.unit = "";
		this.time = 0;
	}
	public Ingredient(String name, double amount, String unit, float time)
	{
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.time = time;
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
	public void setAmount(float amount) {
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

	// Hop subclass of Ingredient
	public class Hop extends Ingredient {

		public Hop(String name)
		{
			super(name);
		}
		
		public Hop(String name, double amount, String unit, float time) {
			super(name, amount, unit, time);
		}

		@Override
		public String getType() {
			return "Hops";
		}	
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
	
	// Grain subclass of Ingredient
	public class Grain extends Ingredient {
		private double weight;
		private double color;

		public Grain(String name)
		{
			super(name);
			this.weight = 0;
			this.color = 7;
		}
		
		public Grain(String name, double amount, String unit, float time, float weight) {
			super(name, amount, unit, time);
			this.weight = weight;
			this.color = 10;
		}

		@Override
		public String getType() {
			return "Grain";
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public double getColor() {
			return color;
		}

		public void setColor(double color) {
			this.color = color;
		}		
	}
	
//====================================================================================
//====================================================================================
	
	// Grain subclass of Ingredient
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
