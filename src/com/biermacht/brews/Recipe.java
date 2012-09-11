package com.biermacht.brews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	private String name;
	private String description;
	private String beerType;
	private int batchTime; // Batch time in weeks
	private float gravity;
	private float ABV;
	private float bitterness;
	private float color;
	private ArrayList<Ingredient> ingredientList;
	private ArrayList<Instruction> instructionList;
	
	
	// Beer types - don't forget to update getBeerTypeList() as well
	public static String BEERTYPE_STOUT = "Stout";
	public static String BEERTYPE_HEFEWEIZEN = "Hefeweizen";
	public static String BEERTYPE_IPA = "India Pale Ale";
	public static String BEERTYPE_OTHER = "Other";
	
	// Public constructors
	public Recipe(String name)
	{
		this.name = name;
		
		// Default values
		this.ingredientList = new ArrayList<Ingredient>();
		this.instructionList = new ArrayList<Instruction>();
		this.description = "No description provided";
		this.beerType = BEERTYPE_OTHER;
		this.setABV(0);
		this.setBitterness(0);
		this.setColor(0);
		this.gravity = 0;
		this.setBatchTime(0);
	}
	
	public Recipe(Parcel parcel)
	{
		name = parcel.readString();
		parcel.readList(ingredientList, null);
		parcel.readList(instructionList, null);
	}
	
	public static ArrayList<String> getBeerTypeList()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(BEERTYPE_OTHER);
		list.add(BEERTYPE_HEFEWEIZEN);
		list.add(BEERTYPE_STOUT);
		list.add(BEERTYPE_IPA);
	
		return list;
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
	
	public void addInstruction(Instruction i)
	{
		instructionList.add(i);
		// TODO: Sort instructions somehow??
	}
	
	public void removeInstruction(String i)
	{

	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBeerType() {
		return beerType;
	}

	public void setBeerType(String beertype) {
		this.beerType = beertype;
	}
	
	public ArrayList<Ingredient> getIngredientList()
	{
		return ingredientList;
	}
	
	public ArrayList<Instruction> getInstructionList()
	{
		return instructionList;
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
		parcel.writeList(instructionList);
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public float getBitterness() {
		return bitterness;
	}

	public void setBitterness(float bitterness) {
		this.bitterness = bitterness;
	}

	public float getColor() {
		return color;
	}

	public void setColor(float color) {
		this.color = color;
	}

	public float getABV() {
		return ABV;
	}

	public void setABV(float aBV) {
		ABV = aBV;
	}

	public int getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(int batchTime) {
		this.batchTime = batchTime;
	}
}
