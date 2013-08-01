package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.ingredient.Ingredient;
import java.util.Comparator;

// Comparator for sorting ingredients list
public class IngredientComparator implements Comparator<Ingredient>
{

    public int compare(Ingredient i1, Ingredient i2)
    {
        // If of same time, alphabetize, otherwise sort based on type
        if (i1.getType().equals(i2.getType()))
            return i1.getName().compareTo(i2.getName());
        return i1.getType().compareTo(i2.getType());
    }
}