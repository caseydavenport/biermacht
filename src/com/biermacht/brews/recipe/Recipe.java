package com.biermacht.brews.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Water;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.InstructionComparator;
import com.biermacht.brews.utils.Utils;

public class Recipe {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;		     // Recipe name
	private int version;			 // XML Version -- 1
	private String type;             // Extract, Grain, Mash
	private String style;         // Stout, Pilsner, etc.
	private String brewer;		     // Brewer's name
	private float batchSize;         // Target size (L)
	private float boilSize;		     // Pre-boil vol (L)
	private int boilTime;		     // In Minutes
	private float efficiency;	     // 100 for extract
	private ArrayList<Hop> hops;     // Hops used
	private ArrayList<Fermentable> fermentables;  // Fermentables used
	private ArrayList<Yeast> yeasts; // Yeasts used
	private ArrayList<Misc> miscs;   // Misc ingredients used
	private ArrayList<Water> waters; // Waters used
	// TODO: ADD MASH PROFILES
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private float OG;			      // Original Gravity
	private float FG;			      // Final Gravity
	private int fermentationStages;   // # of Fermentation stages
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                  // id for use in database
	private String description;       // User input description
	private int batchTime;            // Total length in weeks
	private float ABV;                // Alcohol by volume
	private float bitterness;         // Bitterness in IBU
	private float color;              // Color - SRM
	private ArrayList<Ingredient> ingredientList; 
	private ArrayList<Instruction> instructionList;
	
	// Static values =================================================
	// ===============================================================
	public static final String EXTRACT = "Extract";
	public static final String ALL_GRAIN = "All Grain";
	public static final String PARTIAL_MASH = "Partial Mash";
	
	// Public constructors
	public Recipe(String s)
	{
		// Beer XML 1.0 Required Fields ===================================
		// ================================================================
		this.name = s;	     
		this.version = 1;			
		this.type = EXTRACT;            
		this.style = Utils.BEERTYPE_OTHER.toString();     
		this.brewer = "Unknown Brewer";		     
		this.batchSize = -1;        
		this.boilSize = -1;		     
		this.boilTime = -1;	     
		this.efficiency = 100;	     
		this.hops = new ArrayList<Hop>();   
		this.fermentables = new ArrayList<Fermentable>();  
		this.yeasts = new ArrayList<Yeast>(); 
		this.miscs = new ArrayList<Misc>();
		this.waters = new ArrayList<Water>(); 
		
		// Beer XML 1.0 Optional Fields ===================================
		// ================================================================
		this.OG = 1;
		this.FG = 1;
		this.fermentationStages = 1;
		
		// Custom Fields ==================================================
		// ================================================================
		this.id = -1;
		this.description = "No description provided";
		this.batchTime = 60;
		this.ABV = 0;
		this.bitterness = 0;
		this.color = 0;
		this.ingredientList = new ArrayList<Ingredient>(); 
		instructionList = new ArrayList<Instruction>();
	}
	
	// Public methods
	public void update()
	{
		setColor(BrewCalculator.calculateColorFromRecipe(this));
		setOG(BrewCalculator.calculateGravityFromRecipe(this));
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String beerStyle) {
		this.style = beerStyle;
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

	public float getOG() {
		return OG;
	}

	public void setOG(float gravity) {
		gravity = (float) Math.round(gravity * 1000) / 1000;
		this.OG = gravity;
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

	public float getBatchSize() {
		return this.batchSize;
	}
	
	public void setBatchSize(float v)
	{
		this.batchSize = v;
	}

	public int getBoilTime() {
		return boilTime;
	}

	public void setBoilTime(int boilTime) {
		this.boilTime = boilTime;
	}
	
	public float getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
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
			if (i.getType().equals(Ingredient.FERMENTABLE) || i.getType().equals(Ingredient.HOP))
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
}
