package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.ingredient.Ingredient;

import java.util.Comparator;

// Comparator for sorting ingredients list
public class IngredientsComparator implements Comparator<Ingredient> {

  public int compare(Ingredient i1, Ingredient i2) {
    // Sort based on type first.
    int typeCompare = i1.getType().compareTo(i2.getType());
    if (typeCompare != 0 ) {
      return typeCompare;
    }

    // Then sort based on name.
    return i1.getName().compareTo(i2.getName());
  }
}