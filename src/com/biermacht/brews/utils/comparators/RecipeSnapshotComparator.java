package com.biermacht.brews.utils.comparators;

import android.util.Log;

import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.RecipeSnapshot;

import java.text.ParseException;
import java.util.Comparator;

public class RecipeSnapshotComparator<K> implements Comparator<K> {

  public int compare(K k1, K k2) {

    RecipeSnapshot s1 = (RecipeSnapshot) k1;
    RecipeSnapshot s2 = (RecipeSnapshot) k2;

    try {
      // Compare full snapshot dates.
      return s2.getSnapshotDate().compareTo(s1.getSnapshotDate());
    } catch (ParseException e) {
      // Failed to parse the date for some reason - a malformed snapshot?
      e.printStackTrace();
      return 1;
    }
  }
}
