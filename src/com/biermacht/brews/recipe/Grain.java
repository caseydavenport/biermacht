package com.biermacht.brews.recipe;

// Grain subclass of Ingredient
public class Grain extends Ingredient {
	private double weight;
	private double color;
	private double gravity;

	public Grain(String name)
	{
		super(name);
		this.weight = 0;
		this.color = 7;
		this.gravity = 0;
	}
	
	public Grain(String name, double amount, String unit, float time, float weight, float gravity) {
		super(name, amount, unit, time);
		this.weight = weight;
		this.color = 10;
		this.gravity = gravity;
	}
	
	public Grain(String name, String units, double colour, double grav)
	{
		super(name);
		setUnit(units);
		color = colour;
		gravity = grav;
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

	public double getLovibondColor() {
		return color;
	}

	public void setLovibondColor(double color) {
		this.color = color;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}		
}

