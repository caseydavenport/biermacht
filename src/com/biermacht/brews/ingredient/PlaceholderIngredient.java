package com.biermacht.brews.ingredient;
import android.os.Parcel;
import android.os.Parcelable;

import com.biermacht.brews.utils.Units;

// Grain subclass of Ingredient
public class PlaceholderIngredient extends Ingredient implements Parcelable {
	
	private String description;
	
	public PlaceholderIngredient(String name)
	{
		super(name);
		this.description = "";
	}

    public PlaceholderIngredient(Parcel p)
    {
        super(p);
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        super.writeToParcel(p, flags);
    }

    public static final Parcelable.Creator<PlaceholderIngredient> CREATOR =
            new Parcelable.Creator<PlaceholderIngredient>() {
                @Override
                public PlaceholderIngredient createFromParcel(Parcel p)
                {
                    return new PlaceholderIngredient(p);
                }

                @Override
                public PlaceholderIngredient[] newArray(int size) {
                    return null;
                }
            };

	@Override
	public String getType() {
		return Ingredient.PLACEHOLDER;
	}

	@Override
	public String getShortDescription() {
		return this.description;
	}

	@Override
	public void setShortDescription(String description) {
		this.description = description;
	}

	@Override
	public String getBeerXmlStandardUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayUnits(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getDisplayAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDisplayAmount(double amt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getBeerXmlStandardAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBeerXmlStandardAmount(double amt) {
		// TODO Auto-generated method stub
		
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
	public void setTime(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}