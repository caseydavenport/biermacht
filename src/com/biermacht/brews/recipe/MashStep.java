package com.biermacht.brews.recipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Units;

public class MashStep implements Parcelable {
  // ================================================================
  // Beer XML 1.0 Required Fields ===================================
  // ================================================================
  private String name;              // profile name
  private int version;              // XML Version -- 1
  private String type;              // infusion, temp, decoc
  private double infuseAmount;      // Amount
  private double stepTemp;          // Temp for this step in C
  private double stepTime;          // Time for this step

  // ================================================================
  // Beer XML 1.0 Optional Fields ===================================
  // ================================================================
  private double rampTime;            // Time to ramp temp
  private double endTemp;             // Final temp for long steps
  private String description;         // Description of step
  private double waterToGrainRatio;   // Water to grain ratio (L/kg)
  private double decoctAmount;        // Amount of mash to decoct. (L)

  // ================================================================
  // Custom Fields ==================================================
  // ================================================================
  private long ownerId;             // id for parent mash profile
  private long id;                  // id for use in database
  public int order;                 // Order in step list
  private double infuseTemp;        // Temperature of infuse water
  private boolean calcInfuseTemp;   // Auto calculate the infusion temperature if true.
  private boolean calcInfuseAmt;    // Auto calculate the infusion amount if true.
  private Recipe recipe;            // Reference to the recipe that owns this mash.

  // ================================================================
  // Static values ==================================================
  // ================================================================
  public static String INFUSION = "Infusion";
  public static String TEMPERATURE = "Temperature";
  public static String DECOCTION = "Decoction";

  // Basic Constructor
  public MashStep(Recipe r) {
    this.setName("");
    this.setVersion(1);
    this.setType(MashStep.INFUSION);
    this.setDisplayInfuseAmount(0);
    this.setBeerXmlStandardStepTemp(65.555556);
    this.setStepTime(60);
    this.setRampTime(0);
    this.setBeerXmlStandardEndTemp(0.0);
    this.setDescription("");
    this.setBeerXmlStandardWaterToGrainRatio(2.60793889);
    this.infuseTemp = 0.0;
    this.id = - 1;
    this.ownerId = - 1;
    this.order = 1;
    this.decoctAmount = 0;
    this.calcInfuseAmt = true;
    this.calcInfuseTemp = true;
    this.recipe = r;
  }

  // Only use this when we don't have a mash profile to
  // use!
  public MashStep() {
    this(new Recipe());
  }

  public MashStep(Parcel p) {
    // Beer XML 1.0 Required Fields ===================================
    // ================================================================
    name = p.readString();
    version = p.readInt();
    type = p.readString();
    infuseAmount = p.readDouble();
    stepTemp = p.readDouble();
    stepTime = p.readDouble();

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    rampTime = p.readDouble();
    endTemp = p.readDouble();
    description = p.readString();
    waterToGrainRatio = p.readDouble();
    decoctAmount = p.readDouble();

    // Custom Fields ==================================================
    // ================================================================
    ownerId = p.readLong();
    id = p.readLong();
    order = p.readInt();
    infuseTemp = p.readDouble();
    calcInfuseTemp = p.readInt() == 0 ? false : true;
    calcInfuseAmt = p.readInt() == 0 ? false : true;
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    // Beer XML 1.0 Required Fields ===================================
    // ================================================================
    p.writeString(name);                // profile name
    p.writeInt(version);              // XML Version -- 1
    p.writeString(type);                    // infusion, temp, decoc
    p.writeDouble(infuseAmount);            // Amount
    p.writeDouble(stepTemp);        // Temp for this step in C
    p.writeDouble(stepTime);        // Time for this step

    // Beer XML 1.0 Optional Fields ===================================
    // ================================================================
    p.writeDouble(rampTime);        // Time to ramp temp
    p.writeDouble(endTemp);             // Final temp for long steps
    p.writeString(description);         // Description of step
    p.writeDouble(waterToGrainRatio);   // Water to grain ratio (L/kg)
    p.writeDouble(decoctAmount);        // Amount of water to decoct.

    // Custom Fields ==================================================
    // ================================================================
    p.writeLong(ownerId);        // id for parent mash profile
    p.writeLong(id);                    // id for use in database
    p.writeInt(order);                  // Order in step list
    p.writeDouble(infuseTemp);
    p.writeInt(calcInfuseTemp ? 1 : 0);
    p.writeInt(calcInfuseAmt ? 1 : 0);
  }

  public static final Parcelable.Creator<MashStep> CREATOR =
          new Parcelable.Creator<MashStep>() {
            @Override
            public MashStep createFromParcel(Parcel p) {
              return new MashStep(p);
            }

            @Override
            public MashStep[] newArray(int size) {
              return new MashStep[size];
            }
          };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    // Non-MashStep objects cannot equal a MashStep.
    if (!(o instanceof MashStep)) {
      return false;
    }

    // Comparing to a MashStep - cast the given Object.
    MashStep other = (MashStep) o;

    // Both are MashStep objects - compare important fields.
    if (!this.getName().equals(other.getName())) {
      Log.d("MashStep", "MashStep.equals(): " + this.getName() + " != " + other.getName());
      return false;
    }
    else if (this.getStepTime() != other.getStepTime()) {
      Log.d("MashStep", "MashStep.equals(): " + this.getStepTime() + " != " + other.getStepTime());
      return false;
    }
    else if (this.getBeerXmlStandardStepTemp() != other.getBeerXmlStandardStepTemp()) {
      Log.d("MashStep", "MashStep.equals(): " + this.getBeerXmlStandardStepTemp() + " != " + other.getBeerXmlStandardStepTemp());
      return false;
    }
    else if (!this.getType().equals(other.getType())) {
      Log.d("MashStep", "MashStep.equals(): " + this.getType() + " != " + other.getType());
      return false;
    }

    // All index fields match - these objects are equal.
    return true;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    if (this.name == "") {
      this.name = "Mash step (" + this.getOrder() + ")";
    }
    return this.name;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getVersion() {
    return this.version;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public double getDisplayDecoctAmount() {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      return Units.litersToGallons(this.decoctAmount);
    }
    else {
      return this.decoctAmount;
    }
  }

  public void setDisplayDecoctAmount(double amt) {
    Log.d("MashStep", "Setting display decoction amount: " + amt);
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      this.decoctAmount = Units.gallonsToLiters(amt);
    }
    else {
      this.decoctAmount = amt;
    }
  }

  public void setBeerXmlDecoctAmount(double amt) {
    this.decoctAmount = amt;
  }

  public double getBeerXmlDecoctAmount() {
    return this.decoctAmount;
  }

  public void setDisplayInfuseAmount(double amt) {
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      this.infuseAmount = Units.gallonsToLiters(amt);
    }
    else {
      this.infuseAmount = amt;
    }
  }

  public double getDisplayAmount() {
    if (this.getType().equals(DECOCTION)) {
      Log.d("MashStep", "Returning display decoction amount: " + this.getDisplayDecoctAmount());
      return this.getDisplayDecoctAmount();
    }
    else if (this.getType().equals(INFUSION)) {
      Log.d("MashStep", "Returning display infusion amount: " + this.getDisplayInfuseAmount());
      return this.getDisplayInfuseAmount();
    }
    else if (this.getType().equals(TEMPERATURE)) {
      Log.d("MashStep", "Temperature mash, returning 0 for display amount");
      return 0;
    }
    Log.d("MashStep", "Invalid type: " + this.getType() + ".  Returning -1 for display amount");
    return - 1;
  }

  public double getDisplayInfuseAmount() {
    // No infuse amount for decoction steps. Ever.
    if (this.getType().equals(MashStep.DECOCTION)) {
      return 0;
    }

    // If we are autocalculating.
    if (this.calcInfuseAmt) {
      return this.calculateInfuseAmount();
    }

    // If we're not auto-calculating.
    if (Units.getVolumeUnits().equals(Units.GALLONS)) {
      return Units.litersToGallons(this.infuseAmount);
    }
    else {
      return this.infuseAmount;
    }
  }

  // Calculates the infusion amount based on
  // water to grain ratio, water temp, water to add,
  // and the step temperature.
  // Also sets this.infuseAmount to the correct value.
  public double calculateInfuseAmount() {
    // We perform different calculations if this is the initial infusion.
    double amt = - 1;
    if (this.firstInList()) {
      // Initial infusion. Water is constant * amount of grain.
      amt = this.getBeerXmlStandardWaterToGrainRatio() * this.getBeerXmlStandardMashWeight();
    }
    else {
      // The actual temperature of the water being infused.
      double actualInfuseTemp = calcInfuseTemp ? calculateBXSInfuseTemp() : getBeerXmlStandardInfuseTemp();

      // Not initial infusion. Calculate water to add to reach appropriate temp.
      try {
        amt = (this.getBeerXmlStandardStepTemp() - getPreviousStep().getBeerXmlStandardStepTemp());
      } catch (Exception e) {
        e.printStackTrace();
      }
      amt = amt * (.41 * this.getBeerXmlStandardMashWeight() + this.getBXSTotalWaterInMash());
      amt = amt / (actualInfuseTemp - this.getBeerXmlStandardStepTemp());
    }

    // Set BXL amount so that database is consistent.
    this.infuseAmount = amt;

    // Use appropriate units.
    if (Units.getVolumeUnits().equals(Units.LITERS)) {
      return amt;
    }
    else {
      return Units.litersToGallons(amt);
    }
  }

  // Calculates the infusion temperature for both
  // initial infusion, and water adds.
  // http://www.howtobrew.com/section3/chapter16-3.html
  public double calculateInfuseTemp() {
    // We perform different calculations if this is the initial infusion.
    double temp = 0;
    if (this.firstInList()) {
      // Initial infusion.
      // TODO: For now, we don't have equipment so we combine tun / grain temp for calculation.
      double tunTemp = .7 * this.recipe.getMashProfile().getBeerXmlStandardGrainTemp() + .3 * this.recipe.getMashProfile().getBeerXmlStandardTunTemp();
      temp = (.41) / (this.getBeerXmlStandardWaterToGrainRatio());
      temp = temp * (this.getBeerXmlStandardStepTemp() - tunTemp) + this.getBeerXmlStandardStepTemp();
    }
    else {
      // Not initial infusion.  Assume boiling water to make
      // calculation easier.  If the next step has a LOWER temperature,
      // use room temperature water (72F).
      try {
        if (getPreviousStep().getBeerXmlStandardStepTemp() < this.getBeerXmlStandardStepTemp()) {
          temp = 100;
        }
        else {
          temp = 22.2222;
        }
      } catch (Exception e) {
        temp = - 1;
      }
    }

    // Set the infuse temperature.
    this.infuseTemp = temp;

    // Use appropriate units.
    if (Units.getTemperatureUnits().equals(Units.CELSIUS)) {
      return temp;
    }
    else {
      return Units.celsiusToFahrenheit(temp);
    }
  }

  private double calculateBXSInfuseTemp() {
    if (Units.getTemperatureUnits().equals(Units.CELSIUS)) {
      return this.calculateInfuseTemp();
    }
    else {
      return Units.fahrenheitToCelsius(this.calculateInfuseTemp());
    }
  }

  public boolean firstInList() {
    return this.getOrder() == 0;
  }

  public void setBeerXmlStandardInfuseAmount(double amt) {
    this.infuseAmount = amt;
  }

  public double getBeerXmlStandardInfuseAmount() {
    if (this.calcInfuseAmt) {
      calculateInfuseAmount();
    }
    return this.infuseAmount;
  }

  public double getDisplayInfuseTemp() {
    if (this.calcInfuseTemp) {
      return Math.round(this.calculateInfuseTemp());
    }

    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Math.round(Units.celsiusToFahrenheit(this.infuseTemp));
    }
    else {
      return Math.round(this.infuseTemp);
    }
  }

  public void setDisplayInfuseTemp(double d) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.infuseTemp = Units.fahrenheitToCelsius(d);
    }
    else {
      this.infuseTemp = d;
    }
  }

  public double getBeerXmlStandardInfuseTemp() {
    return this.infuseTemp;
  }

  public void setBeerXmlStandardInfuseTemp(double d) {
    this.infuseTemp = d;
  }

  public void setAutoCalcInfuseTemp(boolean b) {
    this.calcInfuseTemp = b;
  }

  public void setAutoCalcInfuseAmt(boolean b) {
    this.calcInfuseAmt = b;
  }

  public boolean getAutoCalcInfuseTemp() {
    return this.calcInfuseTemp;
  }

  public boolean getAutoCalcInfuseAmt() {
    return this.calcInfuseAmt;
  }

  public void setDisplayStepTemp(double temp) {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      this.stepTemp = Units.fahrenheitToCelsius(temp);
    }
    else {
      this.stepTemp = temp;
    }
  }

  public double getDisplayStepTemp() {
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      return Units.celsiusToFahrenheit(this.stepTemp);
    }
    else {
      return this.stepTemp;
    }
  }

  public void setBeerXmlStandardStepTemp(double temp) {
    this.stepTemp = temp;
  }

  public double getBeerXmlStandardStepTemp() {
    return this.stepTemp;
  }

  public double getBeerXmlStandardWaterToGrainRatio() {
    // If this is the first in the list, use the configured value.
    // Otherwise, we need to calculate it based on the water added.
    if (this.firstInList()) {
      return this.waterToGrainRatio;
    }
    return (this.getBeerXmlStandardInfuseAmount() + this.getBXSTotalWaterInMash()) / this.getBeerXmlStandardMashWeight();
  }

  public double getDisplayWaterToGrainRatio() {
    if (Units.getUnitSystem().equals(Units.IMPERIAL)) {
      return Units.LPKGtoQPLB(getBeerXmlStandardWaterToGrainRatio());
    }
    else {
      return getBeerXmlStandardWaterToGrainRatio();
    }
  }

  public void setBeerXmlStandardWaterToGrainRatio(double d) {
    // Don't update if less than 0. Impossible value.
    if (d <= 0) {
      return;
    }
    this.waterToGrainRatio = d;
  }

  public void setDisplayWaterToGrainRatio(double d) {
    if (d <= 0) {
      return;
    }
    if (Units.getUnitSystem().equals(Units.IMPERIAL)) {
      this.waterToGrainRatio = Units.QPLBtoLPKG(d);
    }
    else {
      this.waterToGrainRatio = d;
    }
  }

  public void setOrder(int i) {
    // Order is privately used for ordering mash steps
    // when they are received from the database.  Once they
    // are out of the db, we use the order in the list as the order.
    // When saved, the orders will be updated in the database.
    this.order = i;
  }

  public int getOrder() {
    return this.recipe.getMashProfile().getMashStepList().indexOf(this);
  }

  public void setDescription(String s) {
    this.description = s;
  }

  public String getDescription() {
    return this.description;
  }

  public void setStepTime(double time) {
    this.stepTime = time;
  }

  public double getStepTime() {
    return this.stepTime;
  }

  public void setRampTime(double time) {
    this.rampTime = time;
  }

  public double getRampTime() {
    return this.rampTime;
  }

  public void setBeerXmlStandardEndTemp(double temp) {
    this.endTemp = temp;
  }

  public double getBeerXmlStandardEndTemp() {
    return this.endTemp;
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

  public void setRecipe(Recipe r) {
    this.recipe = r;
  }

  public Recipe getRecipe() {
    return this.recipe;
  }

  public double getBeerXmlStandardMashWeight() {
    // If there are no ingredients for this recipe yet, we need to use something!
    // Fake it, assume 12 pounds of grain by default.
    double weight = BrewCalculator.TotalBeerXmlMashWeight(this.recipe);
    return weight == 0 ? Units.poundsToKilos(12) : weight;
  }

  public MashStep getPreviousStep() throws Exception {
    if (this.firstInList()) {
      throw new Exception(); // TODO: This should throw a specific exception.
    }

    int idx = this.recipe.getMashProfile().getMashStepList().indexOf(this);
    return this.recipe.getMashProfile().getMashStepList().get(idx - 1);
  }

  public double getBXSTotalWaterInMash() {
    double amt = 0;

    for (MashStep step : this.recipe.getMashProfile().getMashStepList()) {
      if (step.equals(this)) {
        break;
      }
      amt += step.getBeerXmlStandardInfuseAmount();
    }
    Log.d("MashStep", "Step " + this.getName() + " has " + Units.litersToGallons(amt) + " gal in mash.");
    return amt;
  }
}
