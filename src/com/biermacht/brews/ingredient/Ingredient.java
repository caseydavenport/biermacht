package com.biermacht.brews.ingredient;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.utils.Constants;

public abstract class Ingredient implements Parcelable {

  // Beer XML 1.0 Required Fields (To be inherited) =================
  // ================================================================
  private String name;                    // Ingredient name
  private int version;                   // bXML Version being used
  public double amount;                  // Amount in beerXML standard units
  public int time;                       // Time ingredient is used - units vary based on use

  // Custom Fields ==================================================
  // ================================================================
  private long id;                        // Lookup ID for database
  private long ownerId;                   // ID of recipe that contains this
  private double inventory;               // Amount in inventory (standard units)
  private long databaseId;                // Which virtual database to store this in

  // Static values =================================================
  // ===============================================================
  public static final String FERMENTABLE = "Fermentable";
  public static final String HOP = "Hop";
  public static final String YEAST = "Yeast";
  public static final String MISC = "Misc";
  public static final String WATER = "Water";
  public static final String PLACEHOLDER = "Placeholder";

  // Ingredient uses
  public static final String USE_BOIL = "Boil";
  public static final String USE_MASH = "Mash";
  public static final String USE_PRIMARY = "Primary";
  public static final String USE_SECONDARY = "Secondary";
  public static final String USE_BOTTLING = "Bottling";
  public static final String USE_DRY_HOP = "Dry Hop";
  public static final String USE_FIRST_WORT = "First Wort";
  public static final String USE_AROMA = "Aroma";
  public static final String USE_OTHER = "other";

  // Public constructors
  public Ingredient(String name) {
    this.name = name;
    this.id = Constants.INVALID_ID;
    this.ownerId = Constants.INVALID_ID;
    this.databaseId = Constants.DATABASE_DEFAULT;
    this.amount = 0;
    this.time = 0;
    this.inventory = 0;
  }

  public Ingredient(Parcel p) {
    name = p.readString();
    version = p.readInt();
    amount = p.readDouble();
    id = p.readLong();
    ownerId = p.readLong();
    inventory = p.readDouble();
    databaseId = p.readLong();
    time = p.readInt();
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    // Should be called by all sub-classes
    p.writeString(name);
    p.writeInt(version);
    p.writeDouble(amount);
    p.writeLong(id);
    p.writeLong(ownerId);
    p.writeDouble(inventory);
    p.writeLong(databaseId);
    p.writeInt(time);
  }

  public static final Parcelable.Creator<Ingredient> CREATOR =
          new Parcelable.Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel p) {
              Ingredient i = null;
              try {
                i = new Hop(p);
              } catch (Exception e) {
                Log.d("Ingredient", "Not a hop");
              }
              try {
                i = new Misc(p);
              } catch (Exception e) {
                Log.d("Ingredient", "Not a Misc");
              }
              try {
                i = new Fermentable(p);
              } catch (Exception e) {
                Log.d("Ingredient", "Not a Fermentable");
              }
              try {
                i = new Yeast(p);
              } catch (Exception e) {
                Log.d("Ingredient", "Not a Yeast");
              }

              return i;
            }

            @Override
            public Ingredient[] newArray(int size) {
              return new Ingredient[]{};
            }
          };

  // Abstract methods of Ingredient
  public abstract String getType();

  public abstract String getShortDescription();

  public abstract void setShortDescription(String description);

  // Get beerXML 1.0 Units
  public abstract String getBeerXmlStandardUnits();

  // Get display units
  public abstract String getDisplayUnits();

  // Set display units
  public abstract void setDisplayUnits(String s);

  // Returns display amount
  public abstract double getDisplayAmount();

  // Takes display amount
  public abstract void setDisplayAmount(double amt);

  // Returns amount in units specified by beerXML 1.0
  public abstract double getBeerXmlStandardAmount();

  // Takes amount in beerXML 1.0 units
  public abstract void setBeerXmlStandardAmount(double amt);

  public abstract int hashCode();

  public abstract boolean equals(Object o);

  public abstract void setTime(int time);

  public abstract int getTime();

  @Override
  public String toString() {
    return name;
  }

  public String getUse() {
    return Ingredient.USE_OTHER;
  }

  public long getDatabaseId() {
    return this.databaseId;
  }

  public void setDatabaseId(long i) {
    this.databaseId = i;
  }

  public double getBeerXmlStandardInventory() {
    return this.inventory;
  }

  public void setBeerXmlStandardInventory(double d) {
    this.inventory = d;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  /**
   * Returns: 0 if argument is equal to this < 0 if argument is greater than this > 0 if argument is
   * less than this
   */
  public int compareTo(Ingredient other) {
    // If they are not the same type, sort based on type.
    if (! this.getType().equals(other.getType())) {
      return this.getType().compareTo(other.getType());
    }

    // Otherwise, sort based on name.
    return this.name.compareTo(other.getName());
  }
}
