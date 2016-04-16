package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.recipe.BrewNote;
import com.biermacht.brews.recipe.RecipeSnapshot;

import java.text.ParseException;
import java.util.Comparator;

public class BrewNoteComparator<K> implements Comparator<K> {

  public int compare(K k1, K k2) {

    BrewNote s1 = (BrewNote) k1;
    BrewNote s2 = (BrewNote) k2;

    try {
      // Compare full snapshot dates.
      return s2.getDateAndTime().compareTo(s1.getDateAndTime());
    } catch (ParseException e) {
      // Failed to parse the date for some reason - a malformed snapshot?
      e.printStackTrace();
      return 1;
    }
  }
}
