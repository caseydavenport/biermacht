package com.biermacht.brews.utils;

import android.util.Log;

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.recipe.Recipe;

public class Utils {
	/**
	 * Determines if the given value is within the given range
	 * @param a
	 * @param low
	 * @param high
	 * @return
	 */
	public static boolean isWithinRange(double a, double low, double high)
	{
		if (a <= high && a >= low)
			return true;
		else
			return false;
	}

    public static int getHours(double time, String units)
    {
        int num_hours = 0;
        if (units.equals(Units.MINUTES))
            for (int i = 60; i <= time; i += 60)
                num_hours++;
        if (units.equals(Units.HOURS))
            num_hours = (int) time;
        return num_hours;
    }

    public static int getMinutes(double time, String units)
    {
        int num_minutes = 0;
        int num_hours = 0;

        if (units.equals(Units.HOURS))
        {
            time = 60 * time;
            units = Units.MINUTES;
        }

        if (units.equals(Units.MINUTES))
        {
            num_hours = getHours(time, units);
            num_minutes = (int) (time - (60 * num_hours));
        }

        return num_minutes;
    }

	/**
	 * @return Beer XML standard version in use
	 */
	public static int getXmlVersion()
	{
		return 1;
	}
		
	public static Recipe scaleRecipe(Recipe r, double newVolume)
	{
		double oldVolume = r.getDisplayBatchSize();
		double ratio = newVolume / oldVolume;
		
		r.setDisplayBatchSize(newVolume);
		r.setDisplayBoilSize(r.getDisplayBoilSize() * ratio);
		
		for (Ingredient i : r.getIngredientList())
		{
			if (i instanceof Misc)
			{
				((Misc) i).setDisplayAmount(i.getDisplayAmount() * ratio, i.getDisplayUnits());
			}
			else
			{
				i.setDisplayAmount(i.getDisplayAmount() * ratio);
			}
			Database.updateIngredient(i, Constants.DATABASE_DEFAULT);
		}

        Database.updateRecipe(r);
		return r;
	}
}
