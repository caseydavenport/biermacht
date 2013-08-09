package com.biermacht.brews.ingredient;
import android.os.Parcel;

import com.biermacht.brews.utils.Units;

public class Water extends Ingredient
{
	public Water(String name)
	{
		super(name);
	}


    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * THIS IS HOW WE SERIALIZE THIS OBJECT INTO
     * A PARCEL
     * @param p
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel p, int flags)
    {

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

	public double getDisplayAmount()
	{
		// TODO: Implement this method
		return 0;
	}

	public double getBeerXmlStandardAmount()
	{
		// TODO: Implement this method
		return 0;
	}

	public void setDisplayAmount(double amt)
	{
		// TODO: Implement this method
	}

	public void setBeerXmlStandardAmount(double amt)
	{
		// TODO: Implement this method
	}
	
	@Override
	public String getDisplayUnits() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setDisplayUnits(String units) {
		// TODO Auto-generated method stub

	}
	
	public String getBeerXmlStandardUnits()
	{
		// TODO: Implement this method
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
}
