package com.biermacht.brews.recipe;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.comparators.FromDatabaseMashStepComparator;

import java.util.ArrayList;
import java.util.Collections;

public class MashProfile implements Parcelable {
  // Beer XML 1.0 Required Fields ===================================
  // ================================================================
  private String name;                     // profile name
  private Integer version;                 // XML Version -- 1
  private double grainTemp;                // Grain temp in C
  private ArrayList<MashStep> mashSteps;   // List of steps

  // Beer XML 1.0 Optional Fields ===================================
  // ================================================================
  private double tunTemp;         // TUN Temperature in C
  private double spargeTemp;      // Sparge Temp in C
  private double pH;              // pH of water
  private double tunWeight;       // Weight of TUN in kG
  private double tunSpecificHeat; // Specific heat of TUN
  private String notes;           // Notes
  private Boolean equipAdj;       // Adjust for heating of equip?

  // Custom Fields ==================================================
  // ================================================================
  private long id;                  // id for use in database
  private long ownerId;             // id for parent recipe
  private String mashType;          // one of infusion, decoction, temperature
  private String spargeType;        // one of batch, fly
  private Recipe recipe;            // Recipe which owns this mash.

  // Static values =================================================
  // ===============================================================
  public static String MASH_TYPE_INFUSION = "Infusion";
  public static String MASH_TYPE_DECOCTION = "Decoction";
  public static String MASH_TYPE_TEMPERATURE = "Temperature";
  public static String MASH_TYPE_BIAB = "BIAB";
  public static String SPARGE_TYPE_BATCH = "Batch";
  public static String SPARGE_TYPE_FLY = "Fly";
  public static String SPARGE_TYPE_BIAB = "BIAB";

  // Basic Constructor
  public MashProfile(Recipe r) {
    this.setName("New Mash Profile");
    this.setVersion(1);
    this.setBeerXmlStandardGrainTemp(20);
    this.mashSteps = new ArrayList<MashStep>();
    this.setBeerXmlStandardTunTemp(20);
    this.setBeerXmlStandardSpargeTemp(75.5555);
    this.setpH(7);
    this.setBeerXmlStandardTunWeight(0);
    this.setBeerXmlStandardTunSpecHeat(0);
    this.setEquipmentAdjust(false);
    this.setNotes("");
    this.id = - 1;
    this.ownerId = - 1;
    this.mashType = MASH_TYPE_INFUSION;
    this.spargeType = SPARGE_TYPE_BATCH;
    this.recipe = r;
  }

  public MashProfile() {
    this(new Recipe());
  }

  public MashProfile(Parcel p) {
    name = p.readString();
    version = p.readInt();
    grainTemp = p.readDouble();

    mashSteps = new ArrayList<MashStep>();
    p.readTypedList(mashSteps, MashStep.CREATOR);

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    tunTemp = p.readDouble();
    spargeTemp = p.readDouble();
    pH = p.readDouble();
    tunWeight = p.readDouble();
    tunSpecificHeat = p.readDouble();
    notes = p.readString();
    equipAdj = (p.readInt() > 0 ? true : false);

    // Custom Fields ==================================================
    // ================================================================
    id = p.readLong();
    ownerId = p.readLong();
    mashType = p.readString();
    spargeType = p.readString();
    // Don't read recipe because it recurses.
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    p.writeString(name);                // profile name
    p.writeInt(version);              // XML Version -- 1
    p.writeDouble(grainTemp);               // Grain temp in C
    p.writeTypedList(mashSteps);            // List of steps

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    p.writeDouble(tunTemp);        // TUN Temperature in C
    p.writeDouble(spargeTemp);      // Sparge Temp in C
    p.writeDouble(pH);              // pH of water
    p.writeDouble(tunWeight);       // Weight of TUN in kG
    p.writeDouble(tunSpecificHeat); // Specific heat of TUN
    p.writeString(notes);      // Notes
    p.writeInt(equipAdj ? 1 : 0);   // Adjust for heating of equip?

    // Custom Fields ==================================================
    // ================================================================
    p.writeLong(id);                  // id for use in database
    p.writeLong(ownerId);        // id for parent recipe
    p.writeString(mashType);
    p.writeString(spargeType);
    // Don't write recipe because it recurses.
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<MashProfile> CREATOR =
          new Parcelable.Creator<MashProfile>() {
            @Override
            public MashProfile createFromParcel(Parcel p) {
              return new MashProfile(p);
            }

            @Override
            public MashProfile[] newArray(int size) {
              return new MashProfile[size];
            }
          };

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof MashProfile)) {
      return false;
    }

    MashProfile other = (MashProfile) o;
    if (this.hashCode() != other.hashCode()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hc = this.name.hashCode();
    for (MashStep m : this.mashSteps) {
      hc ^= m.hashCode();
    }
    return hc;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setVersion(Integer v) {
    this.version = v;
  }

  public Integer getVersion() {
    return this.version;
  }

  public void setBeerXmlStandardGrainTemp(double temp) {
    this.grainTemp = temp;
  }

  public void setDisplayGrainTemp(double temp) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.grainTemp = Units.fahrenheitToCelsius(temp);
    }
    else {
      this.grainTemp = temp;
    }
  }

  public double getBeerXmlStandardGrainTemp() {
    return this.grainTemp;
  }

  public double getDisplayGrainTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.grainTemp);
    }
    else {
      return this.grainTemp;
    }
  }

  public String getMashType() {
    return this.mashType;
  }

  public String getSpargeType() {
    return this.spargeType;
  }

  public void setMashType(String s) {
    this.mashType = s;
  }

  public void setSpargeType(String s) {
    Log.d("MashProfile", "Sparge type set: " + s);
    this.spargeType = s;
  }

  public void setBeerXmlStandardTunTemp(double temp) {
    this.tunTemp = temp;
  }

  public void setDisplayTunTemp(double temp) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.tunTemp = Units.fahrenheitToCelsius(temp);
    }
    else {
      this.tunTemp = temp;
    }
  }

  public double getBeerXmlStandardTunTemp() {
    return this.tunTemp;
  }

  public double getDisplayTunTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.tunTemp);
    }
    else {
      return this.tunTemp;
    }
  }

  public void setBeerXmlStandardSpargeTemp(double temp) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.spargeTemp = temp;
    }
  }

  public void setDisplaySpargeTemp(double temp) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.spargeTemp = Units.fahrenheitToCelsius(temp);
    }
    else {
      this.spargeTemp = temp;
    }
  }

  public double getDisplaySpargeTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.spargeTemp);
    }
    else {
      return this.spargeTemp;
    }
  }

  public double getBeerXmlStandardSpargeTemp() {
    return this.spargeTemp;
  }

  public void setpH(double pH) {
    this.pH = pH;
  }

  public double getpH() {
    return this.pH;
  }

  public void setBeerXmlStandardTunWeight(double weight) {
    this.tunWeight = weight;
  }

  public void setDisplayTunWeight(double weight) {
    if (Units.getWeightUnits().equals(Units.POUNDS)) {
      this.tunWeight = Units.poundsToKilos(weight);
    }
    else {
      this.tunWeight = weight;
    }
  }

  public double getBeerXmlStandardTunWeight() {
    return this.tunWeight;
  }

  public double getDisplayTunWeight() {
    if (Units.getWeightUnits().equals(Units.POUNDS)) {
      return Units.kilosToPounds(this.tunWeight);
    }
    else {
      return this.tunWeight;
    }
  }

  public void setBeerXmlStandardTunSpecHeat(double heat) {
    this.tunSpecificHeat = heat;
  }

  public double getBeerXmlStandardTunSpecHeat() {
    // Cal / (g * C)
    return this.tunSpecificHeat;
  }

  public void setEquipmentAdjust(boolean adj) {
    this.equipAdj = adj;
  }

  public Boolean getEquipmentAdjust() {
    return this.equipAdj;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return this.id;
  }

  public void setOwnerId(long id) {
    this.ownerId = id;
  }

  public long getOwnerId() {
    return this.ownerId;
  }

  public void setNotes(String s) {
    this.notes = s;
  }

  public String getNotes() {
    return this.notes;
  }

  public int getNumberOfSteps() {
    return this.mashSteps.size();
  }

  public void setRecipe(Recipe r) {
    this.recipe = r;
    for (MashStep m : this.mashSteps) {
      m.setRecipe(this.recipe);
    }
  }

  public ArrayList<MashStep> getMashStepList() {
    return this.mashSteps;
  }

  public void clearMashSteps() {
    this.mashSteps = new ArrayList<MashStep>();
  }

  /**
   * Sets mash step list to given list.  Assumes list is in the desired order and overrides orders
   * if reorder set to true
   *
   * @param list
   */
  public void setMashStepList(ArrayList<MashStep> list) {
    this.mashSteps = list;
    for (MashStep s : this.mashSteps) {
      s.setRecipe(this.recipe);
    }
    Collections.sort(this.mashSteps, new FromDatabaseMashStepComparator());
  }

  /**
   * Removes the given step, returns true if success
   *
   * @param step
   * @return
   */
  public boolean removeMashStep(MashStep step) {
    return this.mashSteps.remove(step);
  }

  public MashStep removeMashStep(int order) {
    return this.mashSteps.remove(order);
  }

  public void addMashStep(MashStep step) {
    step.setRecipe(this.recipe);
    this.mashSteps.add(step);
  }

  public void addMashStep(int order, MashStep step) {
    step.setRecipe(this.recipe);
    this.mashSteps.add(order, step);
  }

  public void save(Context c, long database) {
    Log.d("MashProfile", "Saving " + name + " to database " + database);
    if (this.id < 0) {
      // We haven't yet saved this.  Add it to the database.
      new DatabaseAPI(c).addMashProfile(database, this, this.getOwnerId());
    }
    else {
      // Already exists.  Update it.
      new DatabaseAPI(c).updateMashProfile(this, this.getOwnerId(), database);
    }
  }

  public void delete(Context c, long database) {
    new DatabaseAPI(c).deleteMashProfileFromDatabase(this, database);
  }
}
