package com.biermacht.brews.recipe;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Water;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.DateUtil;
import com.biermacht.brews.utils.InstructionGenerator;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.comparators.RecipeIngredientsComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Recipe implements Parcelable {
  // ===============================================================
  // Static values =================================================
  // ===============================================================
  public static final String EXTRACT = "Extract";
  public static final String ALL_GRAIN = "All Grain";
  public static final String PARTIAL_MASH = "Partial Mash";
  public static final int STAGE_PRIMARY = 1;
  public static final int STAGE_SECONDARY = 2;
  public static final int STAGE_TERTIARY = 3;
  public static final Parcelable.Creator<Recipe> CREATOR =
          new Parcelable.Creator<Recipe>() {
            @Override
            public Recipe createFromParcel(Parcel p) {
              return new Recipe(p);
            }

            @Override
            public Recipe[] newArray(int size) {
              return new Recipe[]{};
            }
          };

  // ================================================================
  // Beer XML 1.0 Required Fields ===================================
  // ================================================================
  private String name;                 // Recipe name
  private int version;                 // XML Version -- 1
  private String type;                 // Extract, Grain, Mash
  private BeerStyle style;             // Stout, Pilsner, etc.
  private String brewer;               // Brewer's name
  private double batchSize;            // Target size (L)
  private double boilSize;             // Pre-boil vol (L)
  private int boilTime;                // In Minutes
  private double efficiency;           // 100 for extract
  private ArrayList<Hop> hops;         // Hops used
  private ArrayList<Fermentable> fermentables;  // Fermentables used
  private ArrayList<Yeast> yeasts;     // Yeasts used
  private ArrayList<Misc> miscs;       // Misc ingredients used
  private ArrayList<Water> waters;     // Waters used
  private MashProfile mashProfile;     // Mash profile for non-extracts

  // ================================================================
  // Beer XML 1.0 Optional Fields ===================================
  // ================================================================
  private double OG;                // Original Gravity
  private double FG;                // Final Gravity
  private int fermentationStages;   // # of Fermentation stages
  private int primaryAge;           // Time in primary in days
  private double primaryTemp;       // Temp in primary in C
  private int secondaryAge;         // Time in Secondary in days
  private double secondaryTemp;     // Temp in secondary in C
  private int tertiaryAge;          // Time in tertiary in days
  private double tertiaryTemp;      // Temp in tertiary in C
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

  // ================================================================
  // Custom Fields ==================================================
  // ================================================================
  private long id;                   // id for use in database
  private String notes;              // User input notes
  private int batchTime;             // Total length in weeks
  private double ABV;                // Alcohol by volume
  private double bitterness;         // Bitterness in IBU
  private double color;              // Color - SRM
  private InstructionGenerator instructionGenerator;
  private double measuredOG;         // Brew day stat: measured OG
  private double measuredFG;         // Brew stat: measured FG
  private double measuredVol;        // Measured final volume (L) of batch.
  private double steepTemp;          // Temperature to steep grains.

  // ================================================================
  // Fields for auto-calculation ====================================
  // ================================================================
  private boolean calculateBoilVolume;            // Calculate the boil volume automatically
  private boolean calculateStrikeVolume;          // Calculate strike vol automatically
  private boolean calculateStrikeTemp;            // Calculate strike temp automatically

  // Public constructors
  public Recipe(String s) {
    // ================================================================
    // Beer XML 1.0 Required Fields ===================================
    // ================================================================
    this.name = s;
    this.setVersion(1);
    this.setType(ALL_GRAIN);
    this.style = Constants.BEERSTYLE_OTHER;
    this.setBrewer("Unknown Brewer");
    this.setDisplayBatchSize(5);
    this.setDisplayBoilSize(2.5);
    this.setBoilTime(60);
    this.setEfficiency(70);
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
    this.secondaryTemp = 21;
    this.tertiaryTemp = 21;
    this.bottleAge = 14;
    this.brewDate = "";

    // Custom Fields ==================================================
    // ================================================================
    this.id = - 1;
    this.notes = "";
    this.tasteNotes = "";
    this.batchTime = 60;
    this.ABV = 0;
    this.bitterness = 0;
    this.color = 0;
    this.instructionGenerator = new InstructionGenerator(this);
    this.measuredOG = 0;
    this.measuredFG = 0;
    this.measuredVol = 0;
    this.steepTemp = Units.fahrenheitToCelsius(155);

    // Fields for auto-calculation ====================================
    // ================================================================
    calculateBoilVolume = true;
    calculateStrikeVolume = false;
    calculateStrikeTemp = false;

    update();
  }

  // Constructor with no arguments!
  public Recipe() {
    this("New Recipe");
  }

  public Recipe(Parcel p) {
    hops = new ArrayList<Hop>();
    fermentables = new ArrayList<Fermentable>();
    yeasts = new ArrayList<Yeast>();
    miscs = new ArrayList<Misc>();
    waters = new ArrayList<Water>();

    name = p.readString();            // Recipe name
    version = p.readInt();          // XML Version -- 1
    type = p.readString();                // Extract, Grain, Mash
    style = p.readParcelable(BeerStyle.class.getClassLoader());    // Stout, Pilsner, etc.
    brewer = p.readString();            // Brewer's name
    batchSize = p.readDouble();
    boilSize = p.readDouble();
    boilTime = p.readInt();            // In Minutes
    efficiency = p.readDouble();          // 100 for extract
    p.readTypedList(hops, Hop.CREATOR);              // Hops used
    p.readTypedList(fermentables, Fermentable.CREATOR);      // Fermentables used
    p.readTypedList(yeasts, Yeast.CREATOR);            // Yeasts used
    p.readTypedList(miscs, Misc.CREATOR);           // Misc ingredients used
    p.readTypedList(waters, Water.CREATOR);           // Waters used
    mashProfile = p.readParcelable(MashProfile.class.getClassLoader()); // Mash profile for non-extracts
    mashProfile.setRecipe(this);

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    OG = p.readDouble();            // Original Gravity
    FG = p.readDouble();            // Final Gravity
    fermentationStages = p.readInt();   // # of Fermentation stages
    primaryAge = p.readInt();        // Time in primary in days
    primaryTemp = p.readDouble();      // Temp in primary in C
    secondaryAge = p.readInt();      // Time in Secondary in days
    secondaryTemp = p.readDouble();    // Temp in secondary in C
    tertiaryAge = p.readInt();      // Time in tertiary in days
    tertiaryTemp = p.readDouble();    // Temp in tertiary in C
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
    measuredVol = p.readDouble();        // Brew stat: measured volume
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
  public void writeToParcel(Parcel p, int flags) {
    p.writeString(name);            // Recipe name
    p.writeInt(version);          // XML Version -- 1
    p.writeString(type);                // Extract, Grain, Mash
    p.writeParcelable(style, flags);    // Stout, Pilsner, etc.
    p.writeString(brewer);            // Brewer's name
    p.writeDouble(batchSize);           // Target size (L)
    p.writeDouble(boilSize);        // Pre-boil vol (L)
    p.writeInt(boilTime);            // In Minutes
    p.writeDouble(efficiency);          // 100 for extract
    p.writeTypedList(hops);             // Hops used
    p.writeTypedList(fermentables);     // Fermentables used
    p.writeTypedList(yeasts);           // Yeasts used
    p.writeTypedList(miscs);            // Misc ingredients used
    p.writeTypedList(waters);           // Waters used
    p.writeParcelable(mashProfile, flags); // Mash profile for non-extracts

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    p.writeDouble(OG);            // Original Gravity
    p.writeDouble(FG);            // Final Gravity
    p.writeInt(fermentationStages);   // # of Fermentation stages
    p.writeInt(primaryAge);        // Time in primary in days
    p.writeDouble(primaryTemp);      // Temp in primary in C
    p.writeInt(secondaryAge);      // Time in Secondary in days
    p.writeDouble(secondaryTemp);    // Temp in secondary in C
    p.writeInt(tertiaryAge);      // Time in tertiary in days
    p.writeDouble(tertiaryTemp);    // Temp in tertiary in C
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
    p.writeLong(id);                  // id for use in database
    p.writeString(notes);              // User input notes
    p.writeInt(batchTime);             // Total length in weeks
    p.writeDouble(ABV);                // Alcohol by volume
    p.writeDouble(bitterness);         // Bitterness in IBU
    p.writeDouble(color);              // Color - SRM
    // Instruction generator not included in parcel
    p.writeDouble(measuredOG);         // Brew day stat: measured OG
    p.writeDouble(measuredFG);         // Brew stat: measured FG
    p.writeDouble(measuredVol);        // Brew stat: measured volume
    p.writeDouble(steepTemp);          // Steep temperature for extract recipes.

    // Fields for auto-calculation ====================================
    // ================================================================
    p.writeInt(calculateBoilVolume ? 1 : 0);
    p.writeInt(calculateStrikeVolume ? 1 : 0);
    p.writeInt(calculateStrikeTemp ? 1 : 0);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Recipe objects are identified by the recipe name and the recipe's ID, as used when stored in
   * the database.  If a recipe has not been stored in the database, it will not necessarily have a
   * unique ID.
   */
  @Override
  public boolean equals(Object o) {
    // If the given object is not a Recipe, it cannot be equal.
    if (! (o instanceof Recipe)) {
      return false;
    }

    // The given object is a recipe - cast it.
    Recipe other = (Recipe) o;

    // Check index fields.
    if (! other.getRecipeName().equals(this.getRecipeName())) {
      // If the given recipe does not have the same name, it is not equal.
      return false;
    }
    else if (other.getId() != this.getId()) {
      // If the given recipe does not have the same ID, it is not equal.
      return false;
    }

    // Otherwise, the two recipes are equal.
    return true;
  }

  @Override
  public String toString() {
    return this.getRecipeName();
  }

  // Public methods
  public void update() {
    setColor(BrewCalculator.Color(this));
    setOG(BrewCalculator.OriginalGravity(this));
    setBitterness(BrewCalculator.Bitterness(this));
    setFG(BrewCalculator.FinalGravity(this));
    setABV(BrewCalculator.AlcoholByVolume(this));
    this.instructionGenerator.generate();
  }

  public String getRecipeName() {
    return this.name;
  }

  public void setRecipeName(String name) {
    this.name = name;
  }

  public void addIngredient(Ingredient i) {
    Log.d(getRecipeName() + "::addIngredient", "Adding ingredient: " + i.getName());
    if (i.getType().equals(Ingredient.HOP)) {
      addHop(i);
    }
    else if (i.getType().equals(Ingredient.FERMENTABLE)) {
      addFermentable(i);
    }
    else if (i.getType().equals(Ingredient.MISC)) {
      addMisc(i);
    }
    else if (i.getType().equals(Ingredient.YEAST)) {
      addYeast(i);
    }
    else if (i.getType().equals(Ingredient.WATER)) {
      addWater(i);
    }

    update();
  }

  public void removeIngredientWithId(long id) {
    for (Ingredient i : getIngredientList()) {
      if (i.getId() == id) {
        Log.d("Recipe", "Removing ingredient " + i.getName());
        removeIngredient(i);
      }
    }
  }

  public void removeIngredient(Ingredient i) {
    if (i.getType().equals(Ingredient.HOP)) {
      hops.remove(i);
    }
    else if (i.getType().equals(Ingredient.FERMENTABLE)) {
      fermentables.remove(i);
    }
    else if (i.getType().equals(Ingredient.MISC)) {
      miscs.remove(i);
    }
    else if (i.getType().equals(Ingredient.YEAST)) {
      yeasts.remove(i);
    }
    else if (i.getType().equals(Ingredient.WATER)) {
      waters.remove(i);
    }

    if (getIngredientList().contains(i)) {
      Log.d("Recipe", "Failed to remove ingredient");
    }
    else {
      Log.d("Recipe", "Successfully removed ingredient");
    }

    update();
  }

  public MashProfile getMashProfile() {
    return this.mashProfile;
  }

  public void setMashProfile(MashProfile profile) {
    this.mashProfile = profile;
    this.mashProfile.setRecipe(this);
    update();
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BeerStyle getStyle() {
    return style;
  }

  public void setStyle(BeerStyle beerStyle) {
    this.style = beerStyle;
  }

  public ArrayList<Ingredient> getIngredientList() {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    list.addAll(hops);
    list.addAll(fermentables);
    list.addAll(yeasts);
    list.addAll(miscs);
    list.addAll(waters);

    Collections.sort(list, new RecipeIngredientsComparator());
    return list;
  }

  public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {

    for (Ingredient i : ingredientsList) {
      if (i.getType().equals(Ingredient.HOP)) {
        addHop(i);
      }
      else if (i.getType().equals(Ingredient.FERMENTABLE)) {
        addFermentable(i);
      }
      else if (i.getType().equals(Ingredient.MISC)) {
        addMisc(i);
      }
      else if (i.getType().equals(Ingredient.YEAST)) {
        addYeast(i);
      }
      else if (i.getType().equals(Ingredient.WATER)) {
        addWater(i);
      }
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
    Log.d(getRecipeName() + "::addFermentable", "Adding fermentable: " + i.getName());
    Fermentable f = (Fermentable) i;
    this.fermentables.add(f);
  }

  private void addHop(Ingredient i) {
    Hop h = (Hop) i;
    this.hops.add(h);
  }

  public ArrayList<Instruction> getInstructionList() {
    return this.instructionGenerator.getInstructions();
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

  public String getVolumeUnits() {
    return Units.getVolumeUnits();
  }

  public double getDisplayBatchSize() {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      return Units.litersToGallons(this.batchSize);
    }
    else {
      return this.batchSize;
    }
  }

  public void setDisplayBatchSize(double size) {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      this.batchSize = Units.gallonsToLiters(size);
    }
    else {
      this.batchSize = size;
    }
  }

  public double getDisplayMeasuredBatchSize() {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      return Units.litersToGallons(this.measuredVol);
    }
    else {
      return this.measuredVol;
    }
  }

  public void setDisplayMeasuredBatchSize(double size) {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      this.measuredVol = Units.gallonsToLiters(size);
    }
    else {
      this.measuredVol = size;
    }
  }

  public double getBeerXmlStandardBatchSize() {
    return this.batchSize;
  }

  public void setBeerXmlStandardBatchSize(double v) {
    this.batchSize = v;
  }

  public double getBeerXmlMeasuredBatchSize() {
    return this.measuredVol;
  }

  public void setBeerXmlMeasuredBatchSize(double d) {
    this.measuredVol = d;
  }

  public int getBoilTime() {
    return boilTime;
  }

  public void setBoilTime(int boilTime) {
    this.boilTime = boilTime;
  }

  public double getEfficiency() {
    if (this.getType().equals(EXTRACT)) {
      return 100;
    }
    return efficiency;
  }

  public void setEfficiency(double efficiency) {
    this.efficiency = efficiency;
  }

  public String getBrewer() {
    return brewer;
  }

  public void setBrewer(String brewer) {
    this.brewer = brewer;
  }

  public double getDisplayBoilSize() {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      if (this.calculateBoilVolume) {
        return Units.litersToGallons(calculateBoilVolume());
      }
      else {
        return Units.litersToGallons(this.boilSize);
      }
    }
    else {
      if (this.calculateBoilVolume) {
        return calculateBoilVolume();
      }
      else {
        return this.boilSize;
      }
    }
  }

  public void setDisplayBoilSize(double size) {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      this.boilSize = Units.gallonsToLiters(size);
    }
    else {
      this.boilSize = size;
    }
  }

  private double calculateBoilVolume() {
    // TODO: Get parameters from equipment profile.
    double TRUB_LOSS = Units.gallonsToLiters(.3);     // Liters lost
    double SHRINKAGE = .04;                           // Percent lost
    double EVAP_LOSS = Units.gallonsToLiters(1.5);    // Evaporation loss (L/hr)

    if (this.type.equals(Recipe.ALL_GRAIN)) {
      return batchSize * (1 + SHRINKAGE) + TRUB_LOSS + (EVAP_LOSS * Units.minutesToHours(boilTime));
    }
    else {
      return (batchSize / 3) * (1 + SHRINKAGE) + TRUB_LOSS + (EVAP_LOSS * Units.minutesToHours
              (boilTime));
    }
  }

  public double getBeerXmlStandardBoilSize() {
    return boilSize;
  }

  public void setBeerXmlStandardBoilSize(double boilSize) {
    this.boilSize = boilSize;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getFG() {
    this.FG = (double) Math.round(FG * 1000) / 1000;
    return this.FG;
  }

  public void setFG(double fG) {
    fG = (double) Math.round(fG * 1000) / 1000;
    this.FG = fG;
  }

  public int getFermentationStages() {
    return fermentationStages;
  }

  public void setFermentationStages(int fermentationStages) {
    this.fermentationStages = fermentationStages;
  }

  public int getTotalFermentationDays() {
    return this.primaryAge + this.secondaryAge + this.tertiaryAge;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public ArrayList<Misc> getMiscList() {
    return miscs;
  }

  public ArrayList<Fermentable> getFermentablesList() {
    return fermentables;
  }

  public ArrayList<Hop> getHopsList() {
    return hops;
  }

  public ArrayList<Yeast> getYeastsList() {
    return yeasts;
  }

  public ArrayList<Water> getWatersList() {
    return waters;
  }

  /**
   * Generates a list of hops in this recipe with the given use.
   *
   * @param use
   *         One of Ingredient.USE_*
   * @return An ArrayList of Ingredients.
   */
  public ArrayList<Ingredient> getHops(String use) {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    for (Hop h : this.getHopsList()) {
      if (h.getUse().equals(use)) {
        list.add(h);
      }
    }
    return list;
  }

  public double getMeasuredOG() {
    return this.measuredOG;
  }

  public void setMeasuredOG(double d) {
    this.measuredOG = d;
  }

  public double getMeasuredFG() {
    return this.measuredFG;
  }

  public void setMeasuredFG(double d) {
    this.measuredFG = d;
  }

  public int getDisplayCoolToFermentationTemp() {
    // Metric - imperial conversion is performed in Yeast
    for (Yeast y : this.getYeastsList()) {
      return y.getDisplayFermentationTemp();
    }
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return 65;
    }
    else {
      return (int) Units.fahrenheitToCelsius(65);
    }
  }

  public double getDisplaySteepTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(steepTemp);
    }
    else {
      return steepTemp;
    }
  }

  public void setDisplaySteepTemp(double t) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.steepTemp = Units.fahrenheitToCelsius(t);
    }
    else {
      this.steepTemp = t;
    }
  }

  public void setNumberFermentationStages(int stages) {
    this.fermentationStages = stages;
  }

  public void setFermentationAge(int stage, int age) {
    switch (stage) {
      case STAGE_PRIMARY:
        this.primaryAge = age;
      case STAGE_SECONDARY:
        this.secondaryAge = age;
      case STAGE_TERTIARY:
        this.tertiaryAge = age;
    }
  }

  public int getFermentationAge(int stage) {
    switch (stage) {
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

  public void setBeerXmlStandardFermentationTemp(int stage, double temp) {
    switch (stage) {
      case STAGE_PRIMARY:
        this.primaryTemp = temp;
      case STAGE_SECONDARY:
        this.secondaryTemp = temp;
      case STAGE_TERTIARY:
        this.tertiaryTemp = temp;
    }
  }

  public double getBeerXmlStandardFermentationTemp(int stage) {
    switch (stage) {
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

  public void setDisplayFermentationTemp(int stage, double temp) {
    switch (stage) {
      case STAGE_PRIMARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          this.primaryTemp = Units.fahrenheitToCelsius(temp);
        }
        else {
          this.primaryTemp = temp;
        }
        break;

      case STAGE_SECONDARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          this.secondaryTemp = Units.fahrenheitToCelsius(temp);
        }
        else {
          this.secondaryTemp = temp;
        }
        break;

      case STAGE_TERTIARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          this.tertiaryTemp = Units.fahrenheitToCelsius(temp);
        }
        else {
          this.secondaryTemp = temp;
        }
        break;
    }
  }

  public double getDisplayFermentationTemp(int stage) {
    switch (stage) {
      case STAGE_PRIMARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          return Units.celsiusToFahrenheit(this.primaryTemp);
        }
        else {
          return this.primaryTemp;
        }

      case STAGE_SECONDARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          return Units.celsiusToFahrenheit(this.secondaryTemp);
        }
        else {
          return this.secondaryTemp;
        }

      case STAGE_TERTIARY:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          return Units.celsiusToFahrenheit(this.tertiaryTemp);
        }
        else {
          return this.tertiaryTemp;
        }

      default:
        if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
          return Units.celsiusToFahrenheit(21);
        }
        else {
          return 21;
        }
    }
  }

  public String getTasteNotes() {
    if (this.tasteNotes == null) {
      this.tasteNotes = "";
    }
    return this.tasteNotes;
  }

  public void setTasteNotes(String s) {
    this.tasteNotes = s;
  }

  public int getTasteRating() {
    return this.tasteRating;
  }

  public void setTasteRating(int i) {
    this.tasteRating = i;
  }

  public int getBottleAge() {
    return this.bottleAge;
  }

  public void setBottleAge(int i) {
    this.bottleAge = i;
  }

  public double getBeerXmlStandardBottleTemp() {
    return this.bottleTemp;
  }

  public void setBeerXmlStandardBottleTemp(double d) {
    this.bottleTemp = d;
  }

  public double getDisplayBottleTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.bottleTemp);
    }
    else {
      return this.bottleTemp;
    }
  }

  public void setDisplayBottleTemp(double d) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.bottleTemp = Units.fahrenheitToCelsius(d);
    }
    else {
      this.bottleTemp = d;
    }
  }

  public boolean isForceCarbonated() {
    return this.isForceCarbonated;
  }

  public void setIsForceCarbonated(boolean b) {
    this.isForceCarbonated = b;
  }

  public double getCarbonation() {
    return this.carbonation;
  }

  public void setCarbonation(double d) {
    this.carbonation = d;
  }

  public String getBrewDate() {
    if (this.brewDate == null) {
      this.brewDate = "";
    }
    return this.brewDate;
  }

  public void setBrewDate(String s) {
    this.brewDate = s;
  }

  /* There is no standardized date format in beerXML.  Thus, we need
   * to try and parse as many formats as possible.  This method takes the given raw
   * date string and returns the best effort Date object.  If not we're unable to parse
   * the date, then this method returns today's date.
   */
  public Date getBrewDateDate() {
    // First, try the common date formats to speed things up.  We'll resort to a full search
    // of known formats if these fail.

    // This format is common for BeerSmith recipes.
    try {
      return new SimpleDateFormat("MM/dd/yyyy").parse(this.brewDate);
    } catch (ParseException e) {
      // Do nothing.
    }

    // This format is used by Biermacht.
    try {
      return new SimpleDateFormat("dd MMM yyyy").parse(this.brewDate);
    } catch (ParseException e) {
      // Do nothing.
    }

    // This takes a long time, so only do it as a last resort.
    // Look through a bunch of known formats to figure out what it is.
    String fmt = DateUtil.determineDateFormat(this.brewDate);
    if (fmt == null) {
      Log.w("Recipe", "Failed to parse date: " + this.brewDate);
      return new Date();
    }
    try {
      return new SimpleDateFormat(fmt).parse(this.brewDate);
    } catch (ParseException e) {
      Log.e("Recipe", "Failed to parse date: " + this.brewDate);
      e.printStackTrace();
      return new Date();
    }
  }

  public String getPrimingSugarName() {
    return this.primingSugarName;
  }

  public void setPrimingSugarName(String s) {
    this.primingSugarName = s;
  }

  public double getPrimingSugarEquiv() {
    // TODO
    return 0.0;
  }

  public void setPrimingSugarEquiv(double d) {
    this.primingSugarEquiv = d;
  }

  public double getBeerXmlStandardCarbonationTemp() {
    return this.carbonationTemp;
  }

  public void setBeerXmlStandardCarbonationTemp(double d) {
    this.carbonationTemp = d;
  }

  public double getDisplayCarbonationTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.carbonationTemp);
    }
    else {
      return this.carbonationTemp;
    }
  }

  public void setDisplayCarbonationTemp(double d) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.carbonationTemp = Units.fahrenheitToCelsius(d);
    }
    else {
      this.carbonationTemp = d;
    }
  }

  public double getKegPrimingFactor() {
    return this.kegPrimingFactor;
  }

  public void setKegPrimingFactor(double d) {
    this.kegPrimingFactor = d;
  }

  public int getCalories() {
    return this.calories;
  }

  public void setCalories(int i) {
    this.calories = i;
  }

  public boolean getCalculateBoilVolume() {
    return this.calculateBoilVolume;
  }

  public void setCalculateBoilVolume(boolean b) {
    this.calculateBoilVolume = b;
  }

  public boolean getCalculateStrikeVolume() {
    return this.calculateStrikeVolume;
  }

  public void setCalculateStrikeVolume(boolean b) {
    this.calculateStrikeVolume = b;
  }

  public boolean getCalculateStrikeTemp() {
    return this.calculateStrikeTemp;
  }

  public void setCalculateStrikeTemp(boolean b) {
    this.calculateStrikeTemp = b;
  }

  public double getMeasuredABV() {
    if (this.getMeasuredFG() > 0 && this.getMeasuredOG() > this.getMeasuredFG()) {
      return (this.getMeasuredOG() - this.getMeasuredFG()) * 131;
    }
    else {
      return 0;
    }
  }

  public double getMeasuredEfficiency() {
    double potGravP, measGravP;
    double eff = 100;
    double measBatchSize = this.getBeerXmlMeasuredBatchSize();

    // If the user hasn't input a measured batch size, assume the recipe went as planned
    // and that the target final batch size was hit.
    if (measBatchSize == 0) {
      Log.d("Recipe", "No measured batch size, try using recipe batch size");
      measBatchSize = this.getBeerXmlStandardBatchSize();
    }

    if (! this.getType().equals(Recipe.EXTRACT)) {
      eff = getEfficiency();
    }

    // Computation only valid if measured gravity is greater than 1, and batch size is non-zero.
    // Theoretically, measured gravity could be less than 1, but we don't support that yet.
    if ((this.getMeasuredOG() > 1) && (batchSize > 0)) {
      // Calculate potential milli-gravity points.  Adjust the value returned by the
      // brew calculator, because it takes the expected efficiency into account (which
      // we don't want here).
      potGravP = (BrewCalculator.OriginalGravity(this) - 1) / (eff / 100);

      // Adjust potential gravity points to account for measured batch size.
      potGravP = potGravP * (getBeerXmlStandardBatchSize() / measBatchSize);

      // Calculate the measured milli-gravity points.
      measGravP = this.getMeasuredOG() - 1;

      // Return the efficiency.
      return 100 * measGravP / potGravP;
    }
    else {
      return 0;
    }
  }

  public void save(Context c) {
    Log.d(getRecipeName() + "::save", "Saving with id: " + this.getId());
    new DatabaseAPI(c).updateRecipe(this);
  }
}
