package com.biermacht.brews.utils.comparators;

import java.util.Comparator;

public class RecipeComparator<Recipe> implements Comparator<Recipe> {

  public int compare(Recipe s1, Recipe s2) {
    return s1.toString().compareToIgnoreCase(s2.toString());
  }

}
