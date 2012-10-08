package com.biermacht.brews.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.InstructionComparator;
import com.biermacht.brews.utils.Utils;

public class Recipe {
	private long id;
	private String name;
	private String description;
	private String beerType;
	private int batchTime; // Batch time in weeks
	private int boilTime; // Total boil time for this recipe
	private float volume;
	private float gravity;
	private float ABV;
	private float bitterness;
	private float color;
	private float efficiency; // TODO: ADD THIS TO DATABASE
	private ArrayList<Ingredient> ingredientList; 
	private ArrayList<Instruction> instructionList;
	
	// Public constructors
	public Recipe(String s)
	{
		this.name = s;
		
		// Default values
		this.id = -1;
		this.volume = 0;
		this.ingredientList = new ArrayList<Ingredient>();
		this.instructionList = new ArrayList<Instruction>();
		this.description = "No description provided";
		this.beerType = Utils.BEERTYPE_OTHER.toString();
		this.ABV = 0;
		this.bitterness = 0;
		this.color = 0;
		this.gravity = 0;
		this.batchTime = 0;
		this.boilTime = 0;
		
	}
	
	// Public methods
	public void update()
	{
		setColor(BrewCalculator.calculateColorFromRecipe(this));
		setGravity(BrewCalculator.calculateGravityFromRecipe(this));
		setBitterness(BrewCalculator.calculateIbuFromRecipe(this));
	}
	
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
		update();
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
		update();
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
	
	public void setBeerType(BeerStyle beertype) {
		this.beerType = beertype.toString();
	}
	
	public ArrayList<Ingredient> getIngredientList()
	{
		return ingredientList;
	}
	
	public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
		this.ingredientList = ingredientsList;
		update();
	}
	
	public ArrayList<Instruction> getInstructionList()
	{
		return generateInstructionsFromIngredients(); //instructionList;
	}
	
	// Comparator for sorting ingredients list
	private class ingredientComparator implements Comparator<Ingredient>
	{

		public int compare(Ingredient i1, Ingredient i2) {
			return i1.getType().compareTo(i2.getType());
		}	
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		gravity = (float) Math.round(gravity * 1000) / 1000;
		this.gravity = gravity;
	}

	public float getBitterness() {
		bitterness = (float) Math.round(bitterness * 10) / 10;
		return bitterness;
	}

	public void setBitterness(float bitterness) {
		bitterness = (float) Math.round(bitterness * 10) / 10;
		this.bitterness = bitterness;
	}

	public float getColor() {
		color = (float) Math.round(color * 10) / 10;
		return color;
	}

	public void setColor(float color) {
		color = (float) Math.round(color * 10) / 10;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getVolume() {
		return this.volume;
	}
	
	public void setVolume(float v)
	{
		this.volume = v;
	}

	public int getBoilTime() {
		// TODO: CALCULATE BOIL TIME BASED OFF INGREDIENTS
		return boilTime;
	}

	public void setBoilTime(int boilTime) {
		this.boilTime = boilTime;
	}
	
	@Override
	public String toString()
	{
		return this.getRecipeName();
	}
	
	private ArrayList<Instruction> generateInstructionsFromIngredients()
	{
		ArrayList<Instruction> list = new ArrayList<Instruction>();

		
		for (Ingredient i : getIngredientList())
		{
			Instruction inst = new Instruction();
			
			// Gain case
			if (i.getType().equals(Ingredient.GRAIN) || i.getType().equals(Ingredient.HOP))
			{
				inst.setInstructionText("Boil " + i.getName());
				inst.setStartTime(i.getBoilStartTime());
				inst.setEndTime(i.getBoilEndTime());
				list.add(inst);
			}
		}
		
		// Sort based on start time
		Collections.sort(list, new InstructionComparator<Instruction>());
		
		return list;
	}

	public float getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
	}
}
