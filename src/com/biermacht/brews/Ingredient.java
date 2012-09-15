package com.biermacht.brews;

import com.biermacht.brews.utils.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient {
	private String type;
	private String name;
	private String unit;
	private float color;
	private float gravity;
	private double amount;
	
	// Public constructors
	public Ingredient(String type, String name, double amount, String unit)
	{
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.type = Utils.TYPE_OTHER;
	}
	
	// Public Methods
	@Override 
	public String toString()
	{
		return name + " " + type;
	}
	
	// Setters and getters
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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

	public float getColor() {
		return color;
	}

	public void setColor(float color) {
		this.color = color;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
}
