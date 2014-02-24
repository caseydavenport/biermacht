package com.biermacht.brews.recipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import java.util.ArrayList;
import java.util.Collections;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Water;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.InstructionGenerator;
import com.biermacht.brews.utils.comparators.IngredientComparator;

public class Recipe implements Parcelable {
	
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
	private int primaryAge;			  // Time in primary in days
	private double primaryTemp;		  // Temp in primary in C
	private int secondaryAge;		  // Time in Secondary in days
	private double secondaryTemp;	  // Temp in secondary in C
	private int tertiaryAge;		  // Time in tertiary in days
	private double tertiaryTemp;	  // Temp in tertiary in C
    private String tasteNotes;        // Taste notes
    private int tasteRating;          // Taste score out of 50
    private int bottleAge;            // Bottle age in days
    private double bottleTemp;        // Bottle temp in C
    private boolean isForceCarbonated;// True if force carb is used
    private double carbonation;       // Volumes of carbonation
    private String brewDate;          // Date brewed
    private String primingSugarName;  // Name of sugar for priming
    private double primingSugarEquiv; // Equivalent amount of priming sugar to be used
    private double kegPrimingFactor;  // factor - use less sugar when kegging vs bottles
    private double carbonationTemp;   // Carbonation temperature in C
    private int calories;             // Calories (KiloCals)
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                  // id for use in database
	private String notes;       // User input notes
	private int batchTime;            // Total length in weeks
	private double ABV;                // Alcohol by volume
	private double bitterness;         // Bitterness in IBU
	private double color;              // Color - SRM
	private InstructionGenerator instructionGenerator; // Generates instructions
	private double measuredOG;         // Brew day stat: measured OG
	private double measuredFG;         // Brew stat: measured FG
	private double steepTemp;          // Temperature to steep grains.

    // Fields for auto-calculation ====================================
    // ================================================================
    private boolean calculateBoilVolume;            // Calculate the boil volume automatically
    private boolean calculateStrikeVolume;          // Calculate strike vol automatically
    private boolean calculateStrikeTemp;            // Calculate strike temp automatically

	
	// Static values =================================================
	// ===============================================================
	public static final String EXTRACT = "Extract";
	public static final String ALL_GRAIN = "All Grain";
	public static final String PARTIAL_MASH = "Partial Mash";
	
	public static final int STAGE_PRIMARY = 1;
	public static final int STAGE_SECONDARY = 2;
	public static final int STAGE_TERTIARY = 3;
	
	// Public constructors
	public Recipe(String s)
	{
		// Beer XML 1.0 Required Fields ===================================
		// ================================================================
		this.name = s;	     
		this.setVersion(1);			
		this.setType(EXTRACT);            
		this.style = Constants.BEERSTYLE_OTHER;
		this.setBrewer("Unknown Brewer");		     
		this.setDisplayBatchSize(5);
		this.setDisplayBoilSize(2.5);
        this.setBoilTime(60);
		this.setEfficiency(100);
		this.hops = new ArrayList<Hop>();   
		this.fermentables = new ArrayList<Fermentable>();  
		this.yeasts = new ArrayList<Yeast>(); 
		this.miscs = new ArrayList<Misc>();
		this.waters = new ArrayList<Water>(); 
		this.mashProfile = new MashProfile(this);
		
		// Beer XML 1.0 Optional Fields ===================================
		// ================================================================
		this.OG = 1;
		this.setFG(1);
		this.setFermentationStages(1);
		this.primaryAge = 14;
		this.secondaryAge = 0;
		this.tertiaryAge = 0;
		this.primaryTemp = 21;
		this.secondaryTemp = 0;
		this.tertiaryTemp = 0;
		
		// Custom Fields ==================================================
		// ================================================================
		this.id = -1;
		this.notes = "No notes provided";
		this.batchTime = 60;
		this.ABV = 0;
		this.bitterness = 0;
		this.color = 0; 
		this.instructionGenerator = new InstructionGenerator(this);
		this.measuredOG = 0;
		this.measuredFG = 0;
		this.steepTemp = Units.fahrenheitToCelsius(155);

        // Fields for auto-calculation ====================================
        // ================================================================
        calculateBoilVolume = false;
        calculateStrikeVolume = false;
        calculateStrikeTemp = false;

        update();
	}

    // Constructor with no arguments!
    public Recipe()
    {
        this("New Recipe");
    }

    public Recipe(Parcel p)
    {
        hops = new ArrayList<Hop>();
        fermentables = new ArrayList<Fermentable>();
        yeasts = new ArrayList<Yeast>();
        miscs = new ArrayList<Misc>();
        waters = new ArrayList<Water>();

        name = p.readString();		        // Recipe name
        version = p.readInt();			    // XML Version -- 1
        type = p.readString();                // Extract, Grain, Mash
        style = p.readParcelable(BeerStyle.class.getClassLoader());    // Stout, Pilsner, etc.
        brewer = p.readString();		        // Brewer's name
        batchSize = p.readDouble();
        boilSize = p.readDouble();
        boilTime = p.readInt();		        // In Minutes
        efficiency = p.readDouble();	        // 100 for extract
        p.readTypedList(hops, Hop.CREATOR);              // Hops used
        p.readTypedList(fermentables, Fermentable.CREATOR);      // Fermentables used
        p.readTypedList(yeasts, Yeast.CREATOR);            // Yeasts used
        p.readTypedList(miscs, Misc.CREATOR);           // Misc ingredients used
        p.readTypedList(waters, Water.CREATOR);           // Waters used
        mashProfile = p.readParcelable(MashProfile.class.getClassLoader()); // Mash profile for non-extracts
        mashProfile.setRecipe(this);

        // Beer XML 1.0 Optional Fields ===================================
        // ================================================================
        OG = p.readDouble();			      // Original Gravity
        FG = p.readDouble();			      // Final Gravity
        fermentationStages = p.readInt();   // # of Fermentation stages
        primaryAge = p.readInt();			  // Time in primary in days
        primaryTemp = p.readDouble();		  // Temp in primary in C
        secondaryAge = p.readInt();		  // Time in Secondary in days
        secondaryTemp = p.readDouble();	  // Temp in secondary in C
        tertiaryAge = p.readInt();		  // Time in tertiary in days
        tertiaryTemp = p.readDouble();	  // Temp in tertiary in C
        tasteNotes = p.readString();        // Taste notes
        tasteRating = p.readInt();          // Taste score out of 50
        bottleAge = p.readInt();            // Bottle age in days
        bottleTemp = p.readDouble();        // Bottle temp in C
        isForceCarbonated = p.readInt() > 0;  // True if force carb is used
        carbonation = p.readDouble();       // Volumes of carbonation
        brewDate = p.readString();          // Date brewed
        primingSugarName = p.readString();  // Name of sugar for priming
        primingSugarEquiv = p.readDouble(); // Equivalent amount of priming sugar to be used
        kegPrimingFactor = p.readDouble();  // factor - use less sugar when kegging vs bottles
        carbonationTemp = p.readDouble();   // Carbonation temperature in C
        calories = p.readInt();

        // Custom Fields ==================================================
        // ================================================================
        id = p.readLong();                  // id for use in database
        notes = p.readString();       // User input notes
        batchTime = p.readInt();            // Total length in weeks
        ABV = p.readDouble();                // Alcohol by volume
        bitterness = p.readDouble();         // Bitterness in IBU
        color = p.readDouble();              // Color - SRM
                                             // Instruction generator not included in parcel
        measuredOG = p.readDouble();         // Brew day stat: measured OG
        measuredFG = p.readDouble();         // Brew stat: measured FG
        steepTemp = p.readDouble();          // Temperature to steep grains for extract recipes.

        // Fields for auto-calculation ====================================
        // ================================================================
        calculateBoilVolume = p.readInt() > 0;
        calculateStrikeVolume = p.readInt() > 0;
        calculateStrikeTemp = p.readInt() > 0;

        // Create instruction generator
        instructionGenerator = new InstructionGenerator(this);
        update();
    }

    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        p.writeString(name);		        // Recipe name
        p.writeInt(version);			    // XML Version -- 1
        p.writeString(type);                // Extract, Grain, Mash
        p.writeParcelable(style, flags);    // Stout, Pilsner, etc.
        p.writeString(brewer);		        // Brewer's name
        p.writeDouble(batchSize);           // Target size (L)
        p.writeDouble(boilSize);		    // Pre-boil vol (L)
        p.writeInt(boilTime);		        // In Minutes
        p.writeDouble(efficiency);	        // 100 for extract
        p.writeTypedList(hops);             // Hops used
        p.writeTypedList(fermentables);     // Fermentables used
        p.writeTypedList(yeasts);           // Yeasts used
        p.writeTypedList(miscs);            // Misc ingredients used
        p.writeTypedList(waters);           // Waters used
        p.writeParcelable(mashProfile, flags); // Mash profile for non-extracts

        // Beer XML 1.0 Optional Fields ===================================
        // ================================================================
        p.writeDouble(OG);			      // Original Gravity
        p.writeDouble(FG);			      // Final Gravity
        p.writeInt(fermentationStages);   // # of Fermentation stages
        p.writeInt(primaryAge);			  // Time in primary in days
        p.writeDouble(primaryTemp);		  // Temp in primary in C
        p.writeInt(secondaryAge);		  // Time in Secondary in days
        p.writeDouble(secondaryTemp);	  // Temp in secondary in C
        p.writeInt(tertiaryAge);		  // Time in tertiary in days
        p.writeDouble(tertiaryTemp);	  // Temp in tertiary in C
        p.writeString(tasteNotes);        // Taste notes
        p.writeInt(tasteRating);          // Taste score out of 50
        p.writeInt(bottleAge);            // Bottle age in days
        p.writeDouble(bottleTemp);        // Bottle temp in C
        p.writeInt(isForceCarbonated ? 1 : 0);  // True if force carb is used
        p.writeDouble(carbonation);       // Volumes of carbonation
        p.writeString(brewDate);          // Date brewed
        p.writeString(primingSugarName);  // Name of sugar for priming
        p.writeDouble(primingSugarEquiv); // Equivalent amount of priming sugar to be used
        p.writeDouble(kegPrimingFactor);  // factor - use less sugar when kegging vs bottles
        p.writeDouble(carbonationTemp);   // Carbonation temperature in C
        p.writeInt(calories);             // Calories (KiloCals)

        // Custom Fields ==================================================
        // ================================================================
        p.writeLong( id);                  // id for use in database
        p.writeString(notes);       // User input notes
        p.writeInt(batchTime);            // Total length in weeks
        p.writeDouble(ABV);                // Alcohol by volume
        p.writeDouble(bitterness);         // Bitterness in IBU
        p.writeDouble(color);              // Color - SRM
                                           // Instruction generator not included in parcel
        p.writeDouble(measuredOG);         // Brew day stat: measured OG
        p.writeDouble(measuredFG);         // Brew stat: measured FG
        p.writeDouble(steepTemp);          // Steep temperature for extract recipes.

        // Fields for auto-calculation ====================================
        // ================================================================
        p.writeInt(calculateBoilVolume ? 1 : 0);
        p.writeInt(calculateStrikeVolume ? 1 : 0);
        p.writeInt(calculateStrikeTemp ? 1 : 0);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {
                @Override
                public Recipe createFromParcel(Parcel p)
                {
                    return new Recipe(p);
                }

                @Override
                public Recipe[] newArray(int size) {
                    return new Recipe[] {};
                }
            };
	
	// Public methods
	public void update()
	{
		setColor(BrewCalculator.Color(this));
		setOG(BrewCalculator.OriginalGravity(this));
		setBitterness(BrewCalculator.Bitterness(this));
		setFG(BrewCalculator.FinalGravity(this));
		setABV(BrewCalculator.AlcoholByVolume(this));
		this.instructionGenerator.generate();
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

    public void removeIngredientWithId(long id)
    {
        for (Ingredient i : getIngredientList())
            if (i.getId() == id)
            {
                Log.d("Recipe", "Removing ingredient " + i.getName());
                removeIngredient(i);
            }
    }
	
	public void removeIngredient(Ingredient i)
    {
        if (i.getType().equals(Ingredient.HOP))
            hops.remove(i);
        else if (i.getType().equals(Ingredient.FERMENTABLE))
            fermentables.remove(i);
        else if (i.getType().equals(Ingredient.MISC))
            miscs.remove(i);
        else if (i.getType().equals(Ingredient.YEAST))
            yeasts.remove(i);
        else if (i.getType().equals(Ingredient.WATER))
            waters.remove(i);

        if (getIngredientList().contains(i))
            Log.d("Recipe", "Failed to remove ingredient");
        else
            Log.d("Recipe", "Successfully removed ingredient");

        update();
    }
	
	public MashProfile getMashProfile()
	{
		return this.mashProfile;
	}
	
	public void setMashProfile(MashProfile profile)
	{
		this.mashProfile = profile;
		this.mashProfile.setRecipe(this);
		update();
	}
	
	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		if (notes.isEmpty())
			this.notes = "No notes provided.";
		else
			this.notes = notes;
	}

	public BeerStyle getStyle() 
	{
		return style;
	}

	public void setStyle(BeerStyle beerStyle) 
	{
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

    public double totalGrainWeight()
    {
        return BrewCalculator.TotalFermentableWeight(this);
    }
	
	public void setIngredientsList(ArrayList<Ingredient> ingredientsList) 
	{
		
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
	
	private void addWater(Ingredient i) 
	{
		Water w = (Water) i;
		waters.add(w);
	}

	private void addYeast(Ingredient i) 
	{
		Yeast y = (Yeast) i;
		yeasts.add(y);
	}

	private void addMisc(Ingredient i) 
	{
		Misc m = (Misc) i;
		miscs.add(m);
	}

	private void addFermentable(Ingredient i) 
	{
		Fermentable f = (Fermentable) i;
		fermentables.add(f);
	}

	private void addHop(Ingredient i) 
	{
		Hop h = (Hop) i;
		hops.add(h);
	}

	public ArrayList<Instruction> getInstructionList()
	{
		return this.instructionGenerator.getInstructions();
	}

	public double getOG() 
	{
		return OG;
	}

	public void setOG(double gravity) 
	{
		gravity = (double) Math.round(gravity * 1000) / 1000;
		this.OG = gravity;
	}

	public double getBitterness() 
	{
		bitterness = (double) Math.round(bitterness * 10) / 10;
		return bitterness;
	}

	public void setBitterness(double bitterness) 
	{
		bitterness = (double) Math.round(bitterness * 10) / 10;
		this.bitterness = bitterness;
	}

	public double getColor() 
	{
		color = (double) Math.round(color * 10) / 10;
		return color;
	}

	public void setColor(double color) 
	{
		color = (double) Math.round(color * 10) / 10;
		this.color = color;
	}

	public double getABV() 
	{
		ABV = (double) Math.round(ABV * 10) / 10;
		return ABV;
	}

	public void setABV(double aBV) 
	{
		ABV = (double) Math.round(ABV * 10) / 10;
		ABV = aBV;
	}

	public int getBatchTime() 
	{
		return batchTime;
	}

	public void setBatchTime(int batchTime) 
	{
		this.batchTime = batchTime;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

    public String getVolumeUnits()
    {
        return Units.getVolumeUnits();
    }
	
	public double getDisplayBatchSize() 
	{
        if (Units.getVolumeUnits().equals(Units.GALLONS))
		    return Units.litersToGallons(this.batchSize);
        else
            return this.batchSize;
	}
	
	public void setDisplayBatchSize(double size) 
	{
        if (Units.getVolumeUnits().equals(Units.GALLONS))
		    this.batchSize = Units.gallonsToLiters(size);
        else
            this.batchSize = size;
	}

	public double getBeerXmlStandardBatchSize() 
	{
		return this.batchSize;
	}
	
	public void setBeerXmlStandardBatchSize(double v)
	{
		this.batchSize = v;
	}

	public int getBoilTime() 
	{
		return boilTime;
	}

	public void setBoilTime(int boilTime) 
	{
		this.boilTime = boilTime;
	}
	
	public double getEfficiency() 
	{
		return efficiency;
	}

	public void setEfficiency(double efficiency) 
	{
		this.efficiency = efficiency;
	}
	
	@Override
	public String toString()
	{
		return this.getRecipeName();
	}
	
	/**
	 * @return the brewer
	 */
	public String getBrewer() 
	{
		return brewer;
	}

	/**
	 * @param brewer the brewer to set
	 */
	public void setBrewer(String brewer) 
	{
		this.brewer = brewer;
	}
	
	public double getDisplayBoilSize()
	{
        if (Units.getVolumeUnits().equals(Units.GALLONS))
        {
            if (this.calculateBoilVolume)
                return Units.litersToGallons(calculateBoilVolume());
            else
		        return Units.litersToGallons(this.boilSize);
        }
        else
        {
            if (this.calculateBoilVolume)
                return calculateBoilVolume();
            else
                return this.boilSize;
        }
	}

    private double calculateBoilVolume()
    {
        double TRUB_LOSS = Units.gallonsToLiters(.3);               // Liters lost
        double SHRINKAGE = .04;                                     // Percent lost
        double EVAP_LOSS = Units.gallonsToLiters(1.5);              // Evaporation loss (L/hr) // TODO: Get this from equipment

        if (this.type.equals(Recipe.ALL_GRAIN))
            return batchSize * (1 + SHRINKAGE ) + TRUB_LOSS + (EVAP_LOSS * Units.minutesToHours(boilTime));
        else
            return (batchSize/3) * (1 + SHRINKAGE ) + TRUB_LOSS + (EVAP_LOSS * Units.minutesToHours(boilTime));
    }
	
	public void setDisplayBoilSize(double size) 
	{
        if (Units.getVolumeUnits().equals(Units.GALLONS))
		    this.boilSize = Units.gallonsToLiters(size);
        else
            this.boilSize = size;
	}

	/**
	 * @return the boilSize
	 */
	public double getBeerXmlStandardBoilSize() 
	{
		return boilSize;
	}

	/**
	 * @param boilSize the boilSize to set
	 */
	public void setBeerXmlStandardBoilSize(double boilSize) 
	{
		this.boilSize = boilSize;
	}

	/**
	 * @return the type
	 */
	public String getType() 
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) 
	{
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
	public int getFermentationStages() 
	{
		return fermentationStages;
	}

	/**
	 * @param fermentationStages the fermentationStages to set
	 */
	public void setFermentationStages(int fermentationStages) 
	{
		this.fermentationStages = fermentationStages;
	}

    public int getFermentationTime()
    {
        return this.primaryAge + this.secondaryAge + this.tertiaryAge;
    }

	/**
	 * @return the version
	 */
	public int getVersion() 
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) 
	{
		this.version = version;
	}
	
	public ArrayList<Misc> getMiscList()
	{
		return miscs;
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
	
	public ArrayList<Water> getWatersList()
	{
		return waters;
	}
	
	public double getMeasuredOG()
	{
		return this.measuredOG;
	}
	
	public double getMeasuredFG()
	{
		return this.measuredFG;
	}
	
	public void setMeasuredOG(double d)
	{
		this.measuredOG = d;
	}
	
	public void setMeasuredFG(double d)
	{
		this.measuredFG = d;
	}
	
	public int getDisplayCoolToFermentationTemp()
	{
        // Metric - imperial conversion is performed in Yeast
		for (Yeast y : this.getYeastsList())
		{
			return y.getDisplayFermentationTemp();
		}
		return 70;
	}
	
	public double getDisplaySteepTemp()
	{
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
		    return Units.celsiusToFahrenheit(steepTemp);
        else
            return steepTemp;
	}
	
	public void setDisplaySteepTemp(double t)
	{
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
		    this.steepTemp = Units.fahrenheitToCelsius(t);
        else
            this.steepTemp = t;
	}
	
	public void setNumberFermentationStages(int stages)
	{
		this.fermentationStages = stages;
	}
	
	public void setFermentationAge(int stage, int age)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
				this.primaryAge = age;
			case STAGE_SECONDARY:
				this.secondaryAge = age;
			case STAGE_TERTIARY:
				this.tertiaryAge = age;
		}
	}
	
	public int getFermentationAge(int stage)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
				return this.primaryAge;
			case STAGE_SECONDARY:
				return this.secondaryAge;
			case STAGE_TERTIARY:
				return this.tertiaryAge;
			default:
				return 7;
		}
	}
	
	public void setBeerXmlStandardFermentationTemp(int stage, double temp)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
				this.primaryTemp = temp;
			case STAGE_SECONDARY:
				this.secondaryTemp = temp;
			case STAGE_TERTIARY:
				this.tertiaryTemp = temp;
		}
	}
	
	public double getBeerXmlStandardFermentationTemp(int stage)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
				return this.primaryTemp;
			case STAGE_SECONDARY:
				return this.secondaryTemp;
			case STAGE_TERTIARY:
				return this.tertiaryTemp;
			default:
				return 21;
		}
	}
	
	public void setDisplayFermentationTemp(int stage, double temp)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    this.primaryTemp = Units.fahrenheitToCelsius(temp);
                else
                    this.primaryTemp = temp;
                break;
			case STAGE_SECONDARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    this.secondaryTemp = Units.fahrenheitToCelsius(temp);
                else
                    this.secondaryTemp = temp;
                break;
			case STAGE_TERTIARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    this.tertiaryTemp = Units.fahrenheitToCelsius(temp);
                else
                    this.secondaryTemp = temp;
                break;
		}
	}
	
	public double getDisplayFermentationTemp(int stage)
	{
		switch (stage)
		{
			case STAGE_PRIMARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    return Units.celsiusToFahrenheit(this.primaryTemp);
                else
                    return this.primaryTemp;
			case STAGE_SECONDARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    return Units.celsiusToFahrenheit(this.secondaryTemp);
                else
                    return this.secondaryTemp;
			case STAGE_TERTIARY:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    return Units.celsiusToFahrenheit(this.tertiaryTemp);
                else
                    return this.tertiaryTemp;
			default:
                if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
				    return Units.celsiusToFahrenheit(21);
                else
                    return 21;
		}
	}

    public String getTasteNotes()
    {
        return this.tasteNotes;
    }

    public void setTasteNotes(String s)
    {
        this.tasteNotes = s;
    }

    public int getTasteRating()
    {
        return this.tasteRating;
    }

    public void setTasteRating(int i)
    {
        this.tasteRating = i;
    }

    public int getBottleAge()
    {
        return this.bottleAge;
    }

    public void setBottleAge(int i)
    {
        this.bottleAge = i;
    }

    public double getBeerXmlStandardBottleTemp()
    {
        return this.bottleTemp;
    }

    public void setBeerXmlStandardBottleTemp(double d)
    {
        this.bottleTemp = d;
    }

    public void setDisplayBottleTemp(double d)
    {
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
            this.bottleTemp = Units.fahrenheitToCelsius(d);
        else
            this.bottleTemp = d;
    }

    public double getDisplayBottleTemp()
    {
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
            return Units.celsiusToFahrenheit(this.bottleTemp);
        else
            return this.bottleTemp;
    }

    public boolean isForceCarbonated()
    {
        return this.isForceCarbonated;
    }

    public void setIsForceCarbonated(boolean b)
    {
        this.isForceCarbonated = b;
    }

    public double getCarbonation()
    {
        return this.carbonation;
    }

    public void setCarbonation (double d)
    {
        this.carbonation = d;
    }

    public String getBrewDate()
    {
        return this.brewDate;
    }

    public void setBrewDate(String s)
    {
        this.brewDate = s;
    }

    public String getPrimingSugarName()
    {
        return this.primingSugarName;
    }

    public void setPrimingSugarName(String s)
    {
        this.primingSugarName = s;
    }

    public double getPrimingSugarEquiv()
    {
        // TODO
        return 0.0;
    }

    public void setPrimingSugarEquiv(double d)
    {
        this.primingSugarEquiv = d;
    }

    public double getBeerXmlStandardCarbonationTemp()
    {
        return this.carbonationTemp;
    }

    public void setBeerXmlStandardCarbonationTemp(double d)
    {
        this.carbonationTemp = d;
    }

    public void setDisplayCarbonationTemp(double d)
    {
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
            this.carbonationTemp = Units.fahrenheitToCelsius(d);
        else
            this.carbonationTemp = d;
    }

    public double getDisplayCarbonationTemp()
    {
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT))
            return Units.celsiusToFahrenheit(this.carbonationTemp);
        else
            return this.carbonationTemp;
    }

    public double getKegPrimingFactor()
    {
        return this.kegPrimingFactor;
    }

    public void setKegPrimingFactor(double d)
    {
        this.kegPrimingFactor = d;
    }

    public int getCalories()
    {
        return this.calories;
    }

    public void setCalories(int i)
    {
        this.calories = i;
    }

    public boolean getCalculateBoilVolume()
    {
        return this.calculateBoilVolume;
    }

    public void setCalculateBoilVolume(boolean b)
    {
        this.calculateBoilVolume = b;
    }

    public boolean getCalculateStrikeVolume()
    {
        return this.calculateStrikeVolume;
    }

    public void setCalculateStrikeVolume(boolean b)
    {
        this.calculateStrikeVolume = b;
    }

    public boolean getCalculateStrikeTemp()
    {
        return this.calculateStrikeTemp;
    }

    public void setCalculateStrikeTemp(boolean b)
    {
        this.calculateStrikeTemp = b;
    }

    public double getMeasuredABV()
    {
        if (this.getMeasuredFG() > 0 && this.getMeasuredOG() > this.getMeasuredFG())
            return (this.getMeasuredOG() - this.getMeasuredFG()) * 131;
        else
            return 0;
    }

    public double getMeasuredEfficiency()
    {
        double gravP, measGravP;
        double eff = 100;

        if (!this.getType().equals(Recipe.EXTRACT))
            eff = getEfficiency();

        if (this.getMeasuredFG() > 0 && this.getMeasuredOG() > this.getMeasuredFG())
        {
            gravP = (BrewCalculator.OriginalGravity(this)-1)/(eff/100);
            measGravP = this.getMeasuredOG() - 1;
            return 100 * measGravP / gravP;
        }
        else
            return 0;
    }
}
