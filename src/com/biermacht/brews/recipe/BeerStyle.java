package com.biermacht.brews.recipe;

import android.os.Parcel;
import android.os.Parcelable;

public class BeerStyle implements Parcelable {

  // Categories based on beerXMl standard
  private String name;
  private String category;
  private Integer version;
  private String categoryNumber;
  private String styleLetter;
  private String styleGuide;
  private String type;
  private String notes;
  private String profile;
  private String ingredients;
  private String examples;

  // Reccomended values
  private double MinOg;
  private double MaxOg;
  private double MinFg;
  private double MaxFg;
  private double MinIbu;
  private double MaxIbu;
  private double minColor;
  private double maxColor;
  private double minAbv;
  private double maxAbv;

  // More
  private long ownerId;

  // Defines
  public static String TYPE_ALE = "Ale";
  public static String TYPE_LAGER = "Lager";
  public static String TYPE_MEAD = "Mead";
  public static String TYPE_WHEAT = "Wheat";
  public static String TYPE_MIXED = "Mixed";
  public static String TYPE_CIDER = "Cider";

  public BeerStyle(String name) {
    setName(name);
    setType("");
    setCategory("");
    setStyleLetter("");
    setNotes("");
    setExamples("");
    setIngredients("");
    setProfile("");
    setStyleGuide("");
    setCategoryNumber("");
    setVersion(1);
    setOwnerId(- 1);

    this.MinOg = 1;
    this.MaxOg = 2;
    this.MinFg = 1;
    this.MaxFg = 2;
    this.MinIbu = 0;
    this.MaxIbu = 200;
    this.minColor = 0;
    this.maxColor = 100;
    this.minAbv = 0;
    this.maxAbv = 100;
  }

  public BeerStyle(Parcel p) {
    // Categories based on beerXMl standard
    name = p.readString();
    category = p.readString();
    version = p.readInt();
    categoryNumber = p.readString();
    styleLetter = p.readString();
    styleGuide = p.readString();
    type = p.readString();
    notes = p.readString();
    profile = p.readString();
    ingredients = p.readString();
    examples = p.readString();
    MinOg = p.readDouble();
    MaxOg = p.readDouble();
    MinFg = p.readDouble();
    MaxFg = p.readDouble();
    MinIbu = p.readDouble();
    MaxIbu = p.readDouble();
    minColor = p.readDouble();
    maxColor = p.readDouble();
    minAbv = p.readDouble();
    maxAbv = p.readDouble();
    ownerId = p.readLong();
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    // Categories based on beerXMl standard
    p.writeString(name);
    p.writeString(category);
    p.writeInt(version);
    p.writeString(categoryNumber);
    p.writeString(styleLetter);
    p.writeString(styleGuide);
    p.writeString(type);
    p.writeString(notes);
    p.writeString(profile);
    p.writeString(ingredients);
    p.writeString(examples);
    p.writeDouble(MinOg);
    p.writeDouble(MaxOg);
    p.writeDouble(MinFg);
    p.writeDouble(MaxFg);
    p.writeDouble(MinIbu);
    p.writeDouble(MaxIbu);
    p.writeDouble(minColor);
    p.writeDouble(maxColor);
    p.writeDouble(minAbv);
    p.writeDouble(maxAbv);
    p.writeLong(ownerId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<BeerStyle> CREATOR =
          new Parcelable.Creator<BeerStyle>() {
            @Override
            public BeerStyle createFromParcel(Parcel p) {
              return new BeerStyle(p);
            }

            @Override
            public BeerStyle[] newArray(int size) {
              return new BeerStyle[]{};
            }
          };

  @Override
  public boolean equals(Object o) {
    // Fist make sure its a BeerStyle
    if (! (o instanceof BeerStyle)) {
      return false;
    }

    // Based only off the name
    if (this.toString().equals(o.toString())) {
      return true;
    }
    else {
      return false;
    }

  }

  public String toString() {
    return name;
  }

  public void setOwnerId(long i) {
    this.ownerId = i;
  }

  public long getOwnerId() {
    return this.ownerId;
  }

  public void setMinCarb(double d) {
    // TODO
  }

  public void setMaxCarb(double d) {
    // TODO
  }

  public void setName(String s) {
    this.name = s;
  }

  public String getName() {
    return this.name;
  }

  public void setType(String s) {
    this.type = s;
  }

  public void setCategory(String s) {
    this.category = s;
  }

  public void setStyleLetter(String s) {
    this.styleLetter = s;
  }

  public void setNotes(String s) {
    this.notes = s;
  }

  public void setExamples(String s) {
    this.examples = s;
  }

  public void setProfile(String s) {
    this.profile = s;
  }

  public void setCategoryNumber(String s) {
    this.categoryNumber = s;
  }

  public void setStyleGuide(String s) {
    this.styleGuide = s;
  }

  public void setIngredients(String s) {
    this.ingredients = s;
  }

  public void setVersion(int i) {
    this.version = i;
  }

  // Methods for getting individual mins and maxes
  public double getMinOg() {
    return MinOg;
  }

  public void setMinOg(double minGrav) {
    this.MinOg = minGrav;
  }

  public double getMaxOg() {
    return MaxOg;
  }

  public void setMaxOg(double maxGrav) {
    this.MaxOg = maxGrav;
  }

  public double getMinIbu() {
    return MinIbu;
  }

  public void setMinIbu(double MinIbu) {
    this.MinIbu = MinIbu;
  }

  public double getMaxIbu() {
    return MaxIbu;
  }

  public void setMaxIbu(double MaxIbu) {
    this.MaxIbu = MaxIbu;
  }

  public double getMinColor() {
    return minColor;
  }

  public void setMinColor(double minColor) {
    this.minColor = minColor;
  }

  public double getMaxColor() {
    return maxColor;
  }

  public void setMaxColor(double maxColor) {
    this.maxColor = maxColor;
  }

  public double getMinAbv() {
    return minAbv;
  }

  public void setMinAbv(double minAbv) {
    this.minAbv = minAbv;
  }

  public double getMaxAbv() {
    return maxAbv;
  }

  public void setMaxAbv(double maxAbv) {
    this.maxAbv = maxAbv;
  }

  public double getMaxFg() {
    return MaxFg;
  }

  public void setMaxFg(double MaxFg) {
    this.MaxFg = MaxFg;
  }

  public double getMinFg() {
    return MinFg;
  }

  public void setMinFg(double MinFg) {
    this.MinFg = MinFg;
  }

  public String getCategory() {
    return this.category;
  }

  public String getCatNum() {
    return this.categoryNumber;
  }

  public String getStyleLetter() {
    return this.styleLetter;
  }

  public String getStyleGuide() {
    return this.styleGuide;
  }

  public String getType() {
    return this.type;
  }

  public double getMinCarb() {
    return 0; // TODO
  }

  public double getMaxCarb() {
    return 0; // TODO
  }

  public String getNotes() {
    return this.notes;
  }

  public String getProfile() {
    return this.profile;
  }

  public String getIngredients() {
    return this.ingredients;
  }

  public String getExamples() {
    return this.examples;
  }
}
