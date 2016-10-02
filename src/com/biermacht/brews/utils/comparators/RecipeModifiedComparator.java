package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.recipe.Recipe;

import java.util.Comparator;

public class RecipeModifiedComparator<T> implements Comparator<T> {

  public int compare(T t1, T t2) {
    Recipe r1 = ((Recipe) t1);
    Recipe r2 = ((Recipe) t2);

    return r2.getLastModified().compareTo(r1.getLastModified());
  }

}
