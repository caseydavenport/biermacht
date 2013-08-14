package com.biermacht.brews.utils.comparators;

import com.biermacht.brews.recipe.MashStep;

import java.util.Comparator;

// Comparator for sorting ingredients list
public class MashStepComparator implements Comparator<MashStep>
{
    public int compare(MashStep i1, MashStep i2)
    {
        if (i1.getOrder() > i2.getOrder())
            return 1;
        else
            return -1;
    }
}
