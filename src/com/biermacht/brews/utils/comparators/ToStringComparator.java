package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.ingredient.Ingredient;

import java.util.Comparator;

// Comparator for sorting ingredients list
public class ToStringComparator implements Comparator<Object>
{

    public int compare(Object o, Object p)
    {
        return o.toString().compareTo(p.toString());
    }
}