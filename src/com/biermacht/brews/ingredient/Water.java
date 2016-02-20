package com.biermacht.brews.ingredient;

import android.os.Parcel;
import android.os.Parcelable;

import com.biermacht.brews.utils.Units;

public class Water extends Ingredient {
  public Water(String name) {
    super(name);
  }

  public Water(Parcel p) {
    // TODO Implement this?
    this("New Water");
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {

  }

  public static final Parcelable.Creator<Water> CREATOR =
          new Parcelable.Creator<Water>() {
            @Override
            public Water createFromParcel(Parcel p) {
              return new Water(p);
            }

            @Override
            public Water[] newArray(int size) {
              return new Water[]{};
            }
          };

  @Override
  public String getType() {
    return Ingredient.WATER;
  }

  @Override
  public String getShortDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  public double getDisplayAmount() {
    // TODO: Implement this method
    return 0;
  }

  public double getBeerXmlStandardAmount() {
    // TODO: Implement this method
    return 0;
  }

  public void setDisplayAmount(double amt) {
    // TODO: Implement this method
  }

  public void setBeerXmlStandardAmount(double amt) {
    // TODO: Implement this method
  }

  @Override
  public String getDisplayUnits() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDisplayUnits(String units) {
    // TODO Auto-generated method stub

  }

  public String getBeerXmlStandardUnits() {
    // TODO: Implement this method
    return Units.LITERS;
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int compareTo(Ingredient other) {
    // If not the same type of Ingredient, sort based on Ingredient type.
    int typeCompare = this.getType().compareTo(other.getType());
    if (typeCompare != 0) {
      return typeCompare;
    }

    // Equal.
    return 0;
  }

  @Override
  public int getTime() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setTime(int i) {
    this.time = i;
  }

  @Override
  public void setShortDescription(String description) {
    // TODO Auto-generated method stub

  }
}
