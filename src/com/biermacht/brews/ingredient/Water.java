package com.biermacht.brews.ingredient;

public class Water extends Ingredient {

	public Water(String name)
	{
		super(name);
	}

	@Override
	public String getType() {
		return Ingredient.WATER;
	}

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAmount(double amt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUnits() {
		// TODO Auto-generated method stub
		return "";
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndTime(int endTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStartTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEndTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setShortDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUnits(String units) {
		// TODO Auto-generated method stub
		
	}
}