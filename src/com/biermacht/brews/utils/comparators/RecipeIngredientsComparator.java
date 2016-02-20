package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.ingredient.Ingredient;

import java.util.Comparator;

// Comparator for sorting ingredients list
public class RecipeIngredientsComparator implements Comparator<Ingredient> {

  public int compare(Ingredient i1, Ingredient i2) {
    return i1.compareTo(i2);
  }
}