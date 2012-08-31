package com.biermacht.brews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	private String name;
	private ArrayList<Ingredient> ingredientList;
	
	// Public contructors
	public Recipe(String name)
	{
		this.name = name;
		this.ingredientList = new ArrayList<Ingredient>();
	}
	
	public Recipe(Parcel parcel)
	{
		name = parcel.readString();
		parcel.readList(ingredientList, null);
	}
	
	// Public methods
	public void setRecipeName(String name)
	{
		this.name = name;
	}
	
	public String getRecipeName()
	{
		return this.name;
	}
	
	public void addIngredient(Ingredient i)
	{
		ingredientList.add(i);
		Collections.sort(ingredientList, new ingredientComparator());
	}
	
	public void removeIngredient(String i)
	{
		for (Ingredient ingredient : ingredientList)
		{
			if(i.equals(ingredient.toString()));
			{
				ingredientList.remove(ingredient);
			}
		}
	}
	
	public ArrayList<Ingredient> getIngredientList()
	{
		return ingredientList;
	}
	
	// Comparator for sorting ingredients list
	private class ingredientComparator implements Comparator<Ingredient>
	{

		public int compare(Ingredient i1, Ingredient i2) {
			return i1.getType().compareTo(i2.getType());
		}	
	}

	// Parcelable methods so that we can send via an intent
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 public static Creator<Recipe> CREATOR = new Creator<Recipe>() {
	        public Recipe createFromParcel(Parcel parcel) {
	            return new Recipe(parcel);
	        }

	        public Recipe[] newArray(int size) {
	            return new Recipe[size];
	        }
	    };

	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeList(ingredientList);
	}
}
