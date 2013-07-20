package com.biermacht.brews.recipe;

import com.biermacht.brews.utils.Units;
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
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.utils.comparators.InstructionComparator;
import java.util.*;

public class Recipe {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;		     // Recipe name
	private int version;			 // XML Version -- 1
	private String type;             // Extract, Grain, Mash
	private BeerStyle style;         // Stout, Pilsner, etc.
	private String brewer;		     // Brewer's name
	private double batchSize;         // Target size (L)
	private double boilSize;		     // Pre-boil vol (L)
	private int boilTime;		     // In Minutes
	private double efficiency;	     // 100 for extract
	private ArrayList<Hop> hops;     // Hops used
	private ArrayList<Fermentable> fermentables;  // Fermentables used
	private ArrayList<Yeast> yeasts; // Yeasts used
	private ArrayList<Misc> miscs;   // Misc ingredients used
	private ArrayList<Water> waters; // Waters used
    private MashProfile mashProfile; // Mash profile for non-extracts	
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private double OG;			      // Original Gravity
	private double FG;			      // Final Gravity
	private int fermentationStages;   // # of Fermentation stages
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                  // id for use in database
	private String description;       // User input description
	private int batchTime;            // Total length in weeks
	private double ABV;                // Alcohol by volume
	private double bitterness;         // Bitterness in IBU
	private double color;              // Color - SRM
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
		this.setVersion(1);			
		this.setType(EXTRACT);            
		this.style = Utils.BEERSTYLE_OTHER;     
		this.setBrewer("Unknown Brewer");		     
		this.batchSize = -1;        
		this.setBeerXmlStandardBoilSize(-1);		     
		this.boilTime = -1;	     
		this.efficiency = 100;	     
		this.hops = new ArrayList<Hop>();   
		this.fermentables = new ArrayList<Fermentable>();  
		this.yeasts = new ArrayList<Yeast>(); 
		this.miscs = new ArrayList<Misc>();
		this.waters = new ArrayList<Water>(); 
		this.mashProfile = new MashProfile();
		
		// Beer XML 1.0 Optional Fields ===================================
		// ================================================================
		this.OG = 1;
		this.setFG(1);
		this.setFermentationStages(1);
		
		// Custom Fields ==================================================
		// ================================================================
		this.id = -1;
		this.description = "No description provided";
		this.batchTime = 60;
		this.ABV = 0;
		this.bitterness = 0;
		this.color = 0; 
		instructionList = new ArrayList<Instruction>();
	}
	
	// Public methods
	public void update()
	{
		setColor(BrewCalculator.calculateColorFromRecipe(this));
		setOG(BrewCalculator.calculateOriginalGravityFromRecipe(this));
		setBitterness(BrewCalculator.calculateIbuFromRecipe(this));
		setFG(BrewCalculator.estimateFinalGravityFromRecipe(this));
		setABV(BrewCalculator.calculateAbvFromRecipe(this));
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
		if (i.getType().equals(Ingredient.HOP))
			addHop(i);
		else if (i.getType().equals(Ingredient.FERMENTABLE))
			addFermentable(i);
		else if (i.getType().equals(Ingredient.MISC))
			addMisc(i);
		else if (i.getType().equals(Ingredient.YEAST))
			addYeast(i);
		else if (i.getType().equals(Ingredient.WATER))
			addWater(i);
		
		update();
	}
	
	public void removeIngredient(String i)
	{
		for (Ingredient ingredient : getIngredientList())
		{
			if(i.equals(ingredient.toString()));
			{
				removeIngredient(ingredient);
			}
		}
		update();
	}
	
	private void removeIngredient(Ingredient i)
	{
		if (i.getType().equals(Ingredient.HOP))
			hops.remove((Hop) i);
		else if (i.getType().equals(Ingredient.FERMENTABLE))
			fermentables.remove((Fermentable) i);
		else if (i.getType().equals(Ingredient.MISC))
			miscs.remove((Misc) i);
		else if (i.getType().equals(Ingredient.YEAST))
			yeasts.remove((Yeast) i);
		else if (i.getType().equals(Ingredient.WATER))
			waters.remove((Water) i);
	}
	
	public MashProfile getMashProfile()
	{
		return this.mashProfile;
	}
	
	public void setMashProfile(MashProfile profile)
	{
		this.mashProfile = profile;
	}
	
	public void addInstruction(Instruction i)
	{
		instructionList.add(i);
	}
	
	public void removeInstruction(String i)
	{

	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) 
	{
		if (description.isEmpty())
			this.description = "No description provided.";
		else
			this.description = description;
	}

	public BeerStyle getStyle() {
		return style;
	}

	public void setStyle(BeerStyle beerStyle) {
		this.style = beerStyle;
	}
	
	public ArrayList<Ingredient> getIngredientList()
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		list.addAll(hops);
		list.addAll(fermentables);
		list.addAll(yeasts);
		list.addAll(miscs);
		list.addAll(waters);
		
		Collections.sort(list, new IngredientComparator());
		return list;
	}
	
	public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
		
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.HOP))
				addHop(i);
			else if (i.getType().equals(Ingredient.FERMENTABLE))
				addFermentable(i);
			else if (i.getType().equals(Ingredient.MISC))
				addMisc(i);
			else if (i.getType().equals(Ingredient.YEAST))
				addYeast(i);
			else if (i.getType().equals(Ingredient.WATER))
				addWater(i);
		}
		
		update();
	}
	
	private void addWater(Ingredient i) {
		Water w = (Water) i;
		waters.add(w);
	}

	private void addYeast(Ingredient i) {
		Yeast y = (Yeast) i;
		yeasts.add(y);
	}

	private void addMisc(Ingredient i) {
		Misc m = (Misc) i;
		miscs.add(m);
	}

	private void addFermentable(Ingredient i) {
		Fermentable f = (Fermentable) i;
		fermentables.add(f);
	}

	private void addHop(Ingredient i) {
		Hop h = (Hop) i;
		hops.add(h);
	}

	public ArrayList<Instruction> getInstructionList()
	{
		return generateInstructionsFromIngredients();
	}
	
	// Comparator for sorting ingredients list
	private class IngredientComparator implements Comparator<Ingredient>
	{

		public int compare(Ingredient i1, Ingredient i2) {
			return i1.getType().compareTo(i2.getType());
		}	
	}

	public double getOG() {
		return OG;
	}

	public void setOG(double gravity) {
		gravity = (double) Math.round(gravity * 1000) / 1000;
		this.OG = gravity;
	}

	public double getBitterness() {
		bitterness = (double) Math.round(bitterness * 10) / 10;
		return bitterness;
	}

	public void setBitterness(double bitterness) {
		bitterness = (double) Math.round(bitterness * 10) / 10;
		this.bitterness = bitterness;
	}

	public double getColor() {
		color = (double) Math.round(color * 10) / 10;
		return color;
	}

	public void setColor(double color) {
		color = (double) Math.round(color * 10) / 10;
		this.color = color;
	}

	public double getABV() {
		ABV = (double) Math.round(ABV * 10) / 10;
		return ABV;
	}

	public void setABV(double aBV) {
		ABV = (double) Math.round(ABV * 10) / 10;
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
	
	public double getDisplayBatchSize() {
		return Units.litersToGallons(this.batchSize);
	}
	
	public void setDisplayBatchSize(double size) {
		this.batchSize = Units.gallonsToLiters(size);
	}

	public double getBeerXmlStandardBatchSize() {
		return this.batchSize;
	}
	
	public void setBeerXmlStandardBatchSize(double v)
	{
		this.batchSize = v;
	}

	public int getBoilTime() {
		return boilTime;
	}

	public void setBoilTime(int boilTime) {
		this.boilTime = boilTime;
	}
	
	public double getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
	}
	
	@Override
	public String toString()
	{
		return this.getRecipeName();
	}
	
	private ArrayList<Instruction> generateInstructionsFromIngredients()
	{
		if (this.getType().equals(Recipe.EXTRACT))
			return generateExtractInstructions();
		if (this.getType().equals(Recipe.ALL_GRAIN))
			return generateAllGrainInstructions();
		else
			return generateAllGrainInstructions();
	}
	
	/**
	 * @return the brewer
	 */
	public String getBrewer() {
		return brewer;
	}

	/**
	 * @param brewer the brewer to set
	 */
	public void setBrewer(String brewer) {
		this.brewer = brewer;
	}
	
	public double getDisplayBoilSize()
	{
		return Units.litersToGallons(this.boilSize);
	}
	
	public void setDisplayBoilSize(double size) {
		this.boilSize = Units.gallonsToLiters(size);
	}

	/**
	 * @return the boilSize
	 */
	public double getBeerXmlStandardBoilSize() {
		return boilSize;
	}

	/**
	 * @param boilSize the boilSize to set
	 */
	public void setBeerXmlStandardBoilSize(double boilSize) {
		this.boilSize = boilSize;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the fG
	 */
	public double getFG() {
		this.FG = (double) Math.round(FG * 1000) / 1000;
		return this.FG;
	}

	/**
	 * @param fG the fG to set
	 */
	public void setFG(double fG) {
		fG = (double) Math.round(fG * 1000) / 1000;
		this.FG = fG;
	}

	/**
	 * @return the fermentationStages
	 */
	public int getFermentationStages() {
		return fermentationStages;
	}

	/**
	 * @param fermentationStages the fermentationStages to set
	 */
	public void setFermentationStages(int fermentationStages) {
		this.fermentationStages = fermentationStages;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	public ArrayList<Fermentable> getFermentablesList()
	{
		return fermentables;
	}
	
	public ArrayList<Hop> getHopsList()
	{
		return hops;
	}
	
	public ArrayList<Yeast> getYeastsList()
	{
		return yeasts;
	}
	private ArrayList<Instruction> generateAllGrainInstructions()
	{

		ArrayList<Instruction> list = new ArrayList<Instruction>();
		ArrayList<MashStep> mashSteps = getMashProfile().getMashStepList();
		HashMap<Integer, String> mashes = new HashMap<Integer, String>();
		HashMap<Integer, String> hopBoils = new HashMap<Integer, String>();
		HashMap<Integer, String> dryHops = new HashMap<Integer, String>();
		HashMap<Integer, String> extracts = new HashMap<Integer, String>();
		String yeasts = "";
		Instruction inst;

	    // Generate mash and extract add instructions
		for(Fermentable f : getFermentablesList())
		{
			// We build up a map with K = steep duration
			// and V = string of steeped grains at duration K
			if (f.getFermentableType().equals(Fermentable.GRAIN))
			{
				if (!mashes.containsKey(f.getTime()))
				{
					// Add a new entry for that duration
					mashes.put(f.getTime(), f.getName());	
				}
				else
				{
					// Append to existing duration
					String s = mashes.get(f.getTime());
					s += "\n";
					s += f.getName();
					mashes.put(f.getTime(), s);
				}

			}
			// Handle extracts here
			else if (f.getFermentableType().equals(Fermentable.EXTRACT))
			{
				if (!extracts.containsKey(f.getTime()))
				{
					// Add a new entry for that duration
					extracts.put(f.getTime(), f.getName());	
				}
				else
				{
					// Append to existing duration
					String s = extracts.get(f.getTime());
					s += "\n";
					s += f.getName();
					extracts.put(f.getTime(), s);
				}
			}
		}

		// Boil hops instructions
		for(Hop h : getHopsList())
		{
			// Boil Case
			if (h.getUse().equals(Hop.USE_BOIL))
			{
				if (!hopBoils.containsKey(h.getDisplayTime()))
				{
					hopBoils.put(h.getDisplayTime(), h.getName());
				}
				else
				{
					// Append to existing duration
					String s = hopBoils.get(h.getTime());
					s += "\n";
					s += h.getName();
					hopBoils.put(h.getTime(), s);
				}
			}
			// Dry hop case
			else if (h.getUse().equals(Hop.USE_DRY_HOP))
			{
				if (!dryHops.containsKey(h.getDisplayTime()))
				{
					dryHops.put(h.getDisplayTime(), h.getName());
				}
				else
				{
					// Append to existing duration
					String s = dryHops.get(h.getTime());
					s += "\n";
					s += h.getName();
					dryHops.put(h.getTime(), s);
				}
			}
		}
		for (Yeast y : getYeastsList())
		{
			yeasts += y.getName() + " yeast";
		}

		// Build up the add grain to mash instructions
		if (mashes.size() > 0)
		{
			// for each k=steep_duration
			for (Integer k : mashes.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(mashes.get(k));
				inst.setInstructionType(Instruction.TYPE_ADD);
				inst.setDuration(0);
				inst.setOrder(0); // Mash instructions go first
				inst.setStartTime(0);
				inst.setEndTime(k);
				list.add(inst);
			}
		}
		
		// Build up mashSteps
		if (mashSteps.size() > 0)
		{
			// for each k=steep_duration
			for (MashStep s : mashSteps)
			{
				if (s.getRampTime() != 0)
				{
					inst = new Instruction();
					String temp = String.format("%2.0f", s.getDisplayStepTemp());
					String text = "Ramp temperature to " + temp + "F";
					inst.setInstructionText(text);
					inst.setInstructionType(Instruction.TYPE_MASH);
					inst.setDuration(s.getRampTime());
					inst.setOrder(0); // Mash instructions go first
					inst.setStartTime(0);
					inst.setEndTime(s.getRampTime());
					list.add(inst);
				}
				
				inst = new Instruction();
				inst.setInstructionText(s.getName());
				inst.setInstructionType(Instruction.TYPE_MASH);
				inst.setDuration(s.getStepTime());
				inst.setOrder(0); // Mash instructions go first
				inst.setStartTime(0);
				inst.setEndTime(s.getStepTime());
				list.add(inst);
			}
		}
		
		// Build up the extracts instructions
		if (extracts.size() > 0)
		{
			// for each k=boil_duration
			for (Integer k : extracts.keySet())
			{
				inst = new Instruction();
				inst.setInstructionType(Instruction.TYPE_ADD);
				inst.setInstructionText(extracts.get(k));
				inst.setDuration(k);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());

				// Order gets set based on if it is a late addition or not
				if(inst.getStartTime() != 0)
					inst.setOrder(2); // Same as hop boils
				else
					inst.setOrder(1); // Second after steeps

				list.add(inst);
			}
		}
		// Build up the boil hops instructions
		if (hopBoils.size() > 0)
		{
			// for each k=steep_duration
			for (Integer k : hopBoils.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(hopBoils.get(k));
				inst.setInstructionType(Instruction.TYPE_BOIL);
				inst.setOrder(2); // Third
				inst.setDuration(k);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());
				list.add(inst);
			}
		}
		if (list.size() > 0)
		{
			// Add a cool wort stage
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_COOL);
			inst.setInstructionText("Cool wort to 70F");
			inst.setStartTime(getBoilTime());
			inst.setEndTime(getBoilTime());
			inst.setDuration(2);
			inst.setOrder(3);
			inst.setDurationUnits("hours");
			list.add(inst);
		}
		if (yeasts.length() > 0)
		{
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_ADD);
			inst.setInstructionText(yeasts);
			inst.setStartTime(getBoilTime() + 1);
			inst.setEndTime(getBoilTime() + 1);
			inst.setDuration(1);
			inst.setOrder(4);
			list.add(inst);
		}
		if (list.size() > 0)
		{
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_FERMENT);
			inst.setInstructionText("Until FG = " + getFG());
			inst.setStartTime(getBoilTime() + 1);
			inst.setEndTime(getBoilTime() + 1);
			inst.setDuration(5);
			inst.setOrder(5);
			inst.setDurationUnits("days");
			list.add(inst);
		}
		// Build up the dry hops instructions
		if (dryHops.size() > 0)
		{
			// for each k=dry hop duration
			for (Integer k : dryHops.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(dryHops.get(k));
				inst.setInstructionType(Instruction.TYPE_DRY_HOP);
				inst.setOrder(6);
				inst.setDuration(k);
				inst.setDurationUnits(Units.DAYS);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());
				list.add(inst);
			}
		}

		// Sort based on order and then start time
		Collections.sort(list, new InstructionComparator<Instruction>());

		return list;
	}
	
	private ArrayList<Instruction> generateExtractInstructions()
	{

		ArrayList<Instruction> list = new ArrayList<Instruction>();
		HashMap<Integer, String> steeps = new HashMap<Integer, String>();
		HashMap<Integer, String> hopBoils = new HashMap<Integer, String>();
		HashMap<Integer, String> dryHops = new HashMap<Integer, String>();
		HashMap<Integer, String> extracts = new HashMap<Integer, String>();
		String yeasts = "";
		Instruction inst;

	    // Generate steep and extract add instructions
		for(Fermentable f : getFermentablesList())
		{
			// We build up a map with K = steep duration
			// and V = string of steeped grains at duration K
			if (f.getFermentableType().equals(Fermentable.GRAIN))
			{
				if (!steeps.containsKey(f.getTime()))
				{
					// Add a new entry for that duration
					steeps.put(f.getTime(), f.getName());	
				}
				else
				{
					// Append to existing duration
					String s = steeps.get(f.getTime());
					s += "\n";
					s += f.getName();
					steeps.put(f.getTime(), s);
				}

			}
			// Handle extracts here
			else if (f.getFermentableType().equals(Fermentable.EXTRACT))
			{
				if (!extracts.containsKey(f.getTime()))
				{
					// Add a new entry for that duration
					extracts.put(f.getTime(), f.getName());	
				}
				else
				{
					// Append to existing duration
					String s = extracts.get(f.getTime());
					s += "\n";
					s += f.getName();
					extracts.put(f.getTime(), s);
				}
			}
		}

		// Boil hops instructions
		for(Hop h : getHopsList())
		{
			// Boil Case
			if (h.getUse().equals(Hop.USE_BOIL))
			{
				if (!hopBoils.containsKey(h.getTime()))
				{
					hopBoils.put(h.getTime(), h.getName());
				}
				else
				{
					// Append to existing duration
					String s = hopBoils.get(h.getTime());
					s += "\n";
					s += h.getName();
					hopBoils.put(h.getTime(), s);
				}
			}
			// Dry hop case
			else if (h.getUse().equals(Hop.USE_DRY_HOP))
			{
				if (!dryHops.containsKey(h.getTime()))
				{
					dryHops.put(h.getTime(), h.getName());
				}
				else
				{
					// Append to existing duration
					String s = dryHops.get(h.getTime());
					s += "\n";
					s += h.getName();
					dryHops.put(h.getTime(), s);
				}
			}
		}
		for (Yeast y : getYeastsList())
		{
			yeasts += y.getName() + " yeast";
		}

		// Build up the steep instructions
		if (steeps.size() > 0)
		{
			// for each k=steep_duration
			for (Integer k : steeps.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(steeps.get(k));
				inst.setInstructionType(Instruction.TYPE_STEEP);
				inst.setDuration(k);
				inst.setOrder(0); // Steep instructions go first
				inst.setStartTime(0);
				inst.setEndTime(k);
				list.add(inst);
			}
		}
		// Build up the extracts instructions
		if (extracts.size() > 0)
		{
			// for each k=boil_duration
			for (Integer k : extracts.keySet())
			{
				inst = new Instruction();
				inst.setInstructionType(Instruction.TYPE_ADD);
				inst.setInstructionText(extracts.get(k));
				inst.setDuration(k);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());

				// Order gets set based on if it is a late addition or not
				if(inst.getStartTime() != 0)
					inst.setOrder(2); // Same as hop boils
				else
					inst.setOrder(1); // Second after steeps

				list.add(inst);
			}
		}
		// Build up the boil hops instructions
		if (hopBoils.size() > 0)
		{
			// for each k=steep_duration
			for (Integer k : hopBoils.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(hopBoils.get(k));
				inst.setInstructionType(Instruction.TYPE_BOIL);
				inst.setOrder(2); // Third
				inst.setDuration(k);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());
				list.add(inst);
			}
		}
		if (list.size() > 0)
		{
			// Add a cool wort stage
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_COOL);
			inst.setInstructionText("Cool wort to 70F");
			inst.setStartTime(getBoilTime());
			inst.setEndTime(getBoilTime());
			inst.setDuration(2);
			inst.setOrder(3);
			inst.setDurationUnits("hours");
			list.add(inst);
		}
		if (yeasts.length() > 0)
		{
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_ADD);
			inst.setInstructionText(yeasts);
			inst.setStartTime(getBoilTime() + 1);
			inst.setEndTime(getBoilTime() + 1);
			inst.setDuration(1);
			inst.setOrder(4);
			list.add(inst);
		}
		if (list.size() > 0)
		{
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_FERMENT);
			inst.setInstructionText("Until FG = " + getFG());
			inst.setStartTime(getBoilTime() + 1);
			inst.setEndTime(getBoilTime() + 1);
			inst.setDuration(5);
			inst.setOrder(5);
			inst.setDurationUnits("days");
			list.add(inst);
		}
		// Build up the dry hops instructions
		if (dryHops.size() > 0)
		{
			// for each k=dry hop duration
			for (Integer k : dryHops.keySet())
			{
				inst = new Instruction();
				inst.setInstructionText(dryHops.get(k));
				inst.setInstructionType(Instruction.TYPE_DRY_HOP);
				inst.setOrder(6);
				inst.setDuration(k);
				inst.setDurationUnits(Units.DAYS);
				inst.setStartTime(getBoilTime()-k);
				inst.setEndTime(getBoilTime());
				list.add(inst);
			}
		}

		// Sort based on order and then start time
		Collections.sort(list, new InstructionComparator<Instruction>());

		return list;
	}
}
