package com.biermacht.brews.ingredient;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

/**
 * Subclass of Ingredient which represents a Fermentable.  Fermentables can be a grain, extract,
 * sugar, or adjunct.
 */
public class Fermentable extends Ingredient implements Parcelable {

  // Beer XML 1.0 Required Fields ===================================
  // ================================================================
  // Name - Inherited
  // Version - Inherited
  private String type;                           // Grain, Extract, Adjunct
  private double yield;                          // Dry yeild / raw yield
  private double color;                          // Color in Lovibond (SRM)

  // Beer XML 1.0 Optional Fields ===================================
  // ================================================================
  private boolean addAfterBoil;                  // True if added after boil
  private double maxInBatch;                     // Max reccomended in this batch

  // Custom Fields ==================================================
  // ================================================================
  private String description;                     // Description of fermentable

  // Static values =================================================
  // ===============================================================
  public static final String TYPE_GRAIN = "Grain";
  public static final String TYPE_EXTRACT = "Extract";
  public static final String TYPE_ADJUNCT = "Adjunct";
  public static final String TYPE_SUGAR = "Sugar";

  public Fermentable(String name) {
    super(name);
    this.type = TYPE_GRAIN;
    this.amount = 0;
    this.yield = 1;
    this.color = 0;
    this.addAfterBoil = false;
    this.setMaxInBatch(0);
    this.description = "No description provided.";
  }

  public Fermentable(Parcel p) {
    super(p);
    type = p.readString();
    yield = p.readDouble();
    color = p.readDouble();
    addAfterBoil = p.readInt() > 0;
    maxInBatch = p.readDouble();
    description = p.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    super.writeToParcel(p, flags);
    p.writeString(type);                           // Grain, Extract, Adjunct
    p.writeDouble(yield);                          // Dry yeild / raw yield
    p.writeDouble(color);                          // Color in Lovibond (SRM)
    p.writeInt(addAfterBoil ? 1 : 0);              // True if added after boil
    p.writeDouble(maxInBatch);                     // Max reccomended in this batch
    p.writeString(description);                    // Description of fermentable
  }

  public static final Parcelable.Creator<Fermentable> CREATOR =
          new Parcelable.Creator<Fermentable>() {
            @Override
            public Fermentable createFromParcel(Parcel p) {
              return new Fermentable(p);
            }

            @Override
            public Fermentable[] newArray(int size) {
              return null;
            }
          };

  @Override
  public String getType() {
    return Ingredient.FERMENTABLE;
  }

  public double getLovibondColor() {
    color = (float) Math.round(color * 10) / 10;
    return color;
  }

  public void setLovibondColor(double color) {
    color = (float) Math.round(color * 10) / 10;
    this.color = color;
  }

  public double getGravity() {
    return yieldToGravity(yield);
  }

  public void setGravity(double gravity) {
    this.yield = gravityToYield(gravity);
  }

  public float getPpg() {
    return (float) (yield * 46) / 100;
  }

  public String getFermentableType() {
    return type;
  }

  public void setFermentableType(String type) {
    if (! Constants.FERMENTABLE_TYPES.contains(type)) {
      Log.e("Fermentable", "Setting invalid fermentable type: " + type);
    }
    this.type = type;
  }

  @Override
  public double getDisplayAmount() {
    if (getDisplayUnits().equals(Units.POUNDS)) {
      return Units.kilosToPounds(this.amount);
    }
    else {
      return this.amount;
    }
  }

  @Override
  public double getBeerXmlStandardAmount() {
    return this.amount;
  }

  @Override
  public void setDisplayAmount(double amt) {
    if (getDisplayUnits().equals(Units.POUNDS)) {
      this.amount = Units.poundsToKilos(amt);
    }
    else {
      this.amount = amt;
    }
  }

  @Override
  public void setBeerXmlStandardAmount(double amt) {
    this.amount = amt;
  }

  @Override
  public int hashCode() {
    int hc = this.getName().hashCode();
    hc += this.getLovibondColor();
    hc |= (int) this.getGravity() * 1234;
    hc += (int) this.getBeerXmlStandardAmount();
    hc |= this.getType().hashCode();
    return hc;
  }

  @Override
  public String getDisplayUnits() {
    return Units.getFermentableUnits();
  }

  @Override
  public void setDisplayUnits(String s) {
  }

  @Override
  public String getBeerXmlStandardUnits() {
    return Units.KILOGRAMS;
  }

  /**
   * Evaluates equality of this Fermentable and the given Object.
   *
   * @param o
   * @return true if this Fermentable equals Object o, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Fermentable) {
      // Compare to other.
      return this.compareTo((Fermentable) o) == 0;
    }

    // Not a fermentable, and so is not equal.
    return false;
  }

  @Override
  public int getTime() {
    return time;
  }

  @Override
  public void setTime(int time) {
    this.time = time;
  }

  /**
   * @return the yield
   */
  public double getYield() {
    return yield;
  }

  /**
   * @param yield
   *         the yield to set
   */
  public void setYield(double yield) {
    this.yield = yield;
  }

  /**
   * @return the addAfterBoil
   */
  public boolean isAddAfterBoil() {
    return addAfterBoil;
  }

  /**
   * @param addAfterBoil
   *         the addAfterBoil to set
   */
  public void setAddAfterBoil(boolean addAfterBoil) {
    this.addAfterBoil = addAfterBoil;
  }

  @Override
  public void setShortDescription(String description) {
    this.description = description;
  }

  @Override
  public String getShortDescription() {
    return this.description;
  }

  /**
   * @return the maxInBatch
   */
  public double getMaxInBatch() {
    return maxInBatch;
  }

  @Override
  public String getUse() {
    if (this.getFermentableType().equals(TYPE_EXTRACT) ||
            this.getFermentableType().equals(TYPE_SUGAR)) {
      // Sugars and Extracts should be boiled.
      return Ingredient.USE_BOIL;
    }
    else {
      // Everything else should be mashed. Grains, Adjuncts.
      return Ingredient.USE_MASH;
    }
  }

  /**
   * @param maxInBatch
   *         the maxInBatch to set
   */
  public void setMaxInBatch(double maxInBatch) {
    this.maxInBatch = maxInBatch;
  }

  /**
   * Turns given gravity into a yield
   *
   * @param gravity
   * @return
   */
  public double gravityToYield(double gravity) {
    double yield = 0;

    yield = gravity - 1;
    yield = yield * 1000;
    yield = yield * 100 / 46;

    return yield;
  }

  /**
   * turns given yield into a gravity
   *
   * @param yield
   * @return
   */
  public double yieldToGravity(double yield) {
    double gravity = 0;
    gravity = 1 + (((yield * 46) / 100) / 1000);

    return gravity;
  }

  /**
   * Compares the given Ingredient to this Fermentable and returns and indicator of (in)equality.
   *
   * @param other
   *         The Ingredient with which to compare this Fermentable
   * @return 0 if argument is equal to this, < 0 if argument is greater than this, > 0 if argument
   * is less than this
   */
  @Override
  public int compareTo(Ingredient other) {
    // If not the same type of Ingredient, sort based on Ingredient type.
    int typeCompare = this.getType().compareTo(other.getType());
    if (typeCompare != 0) {
      return typeCompare;
    }
    Fermentable f = (Fermentable) other;

    // If they are both Fermentables, sort based on amount.
    int amountCompare = Double.compare(f.getBeerXmlStandardAmount(), this.getBeerXmlStandardAmount());
    if (amountCompare != 0) {
      return amountCompare;
    }

    // Sort based on name.
    int nameCompare = this.getName().compareTo(f.getName());
    if (nameCompare != 0) {
      return nameCompare;
    }

    // Sort based on color.
    int colorCompare = Double.compare(this.getLovibondColor(), f.getLovibondColor());
    if (colorCompare != 0) {
      return colorCompare;
    }

    // Sort based on gravity.
    int gravCompare = Double.compare(this.getGravity(), f.getGravity());
    if (gravCompare != 0) {
      return gravCompare;
    }

    // If all the above are equal, they are the same.
    return 0;
  }
}

