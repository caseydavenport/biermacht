package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.recipe.MashStep;

import java.util.Comparator;

// Comparator for sorting mash step list
public class FromDatabaseMashStepComparator implements Comparator<MashStep> {
  public int compare(MashStep i1, MashStep i2) {
    if (i1.order > i2.order) {
      return 1;
    }
    else {
      return - 1;
    }
  }
}
