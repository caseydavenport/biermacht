package com.biermacht.brews;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
	private String type;
	private String name;
	private String unit;
	private float amount;
	
	// Types
	public static String TYPE_HOPS = "hops";
	public static String TYPE_YEAST = "yeast";
	public static String TYPE_MALT = "malt";
	public static String TYPE_SPICE = "spice";
	public static String TYPE_OTHER = "other";
	
	// Public constructors
	public Ingredient(String type, String name, float amount, String unit)
	{
		this.type = type;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}
	
	public Ingredient(Parcel parcel)
	{
		type = parcel.readString();
		name = parcel.readString();
		unit = parcel.readString();
		amount = parcel.readFloat();
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
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(type);
		parcel.writeString(name);
		parcel.writeString(unit);
		parcel.writeFloat(amount);
	}
	
    public static Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
