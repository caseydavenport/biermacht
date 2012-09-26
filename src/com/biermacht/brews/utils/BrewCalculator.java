package com.biermacht.brews.utils;

import java.util.ArrayList;

import android.util.Log;

import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Hop;
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.recipe.Recipe;

public class BrewCalculator {
	
	/**
	 *  Beer details calculations
	 *  http://www.howtobrew.com/section1/chapter5-5.html
	 *  
	 *  MCU = (weight of grain in lbs)*(color of grain in lovibond) / (volume in gal)
	 *  SRM = 1.4922 * MCU**.6859
	 *  
	 *  OG = (Weight of grain in lbs)*(Extract potential of grain)*(Extraction Efficiency)
	 *  
	 *  IBU = 7498*(Weight of hops in OZ)*(% of Alpha Acids)*(Utilization factor) / (Volume in gal)
	 */
	
	public static float calculateColorFromRecipe(Recipe r)
	{
		float SRM = 0;
		float MCU = 0;
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.GRAIN))
			{
				Grain g = (Grain) i;
				MCU += g.getWeight() * g.getLovibondColor() / r.getVolume();
			}
		}
		SRM = (float) (1.4922*Math.pow(MCU, .6859));
		return SRM;
	}
	
	public static float calculateGravityFromRecipe(Recipe r)
	{
		float grav = 0;
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		// http://homebrew.stackexchange.com/questions/1434/wiki-how-do-you-calculate-original-gravity
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.GRAIN))
			{
				Grain g = (Grain) i;
				
				grav += (g.getWeight() * g.getPpg() / (r.getVolume()-(g.getWeight()/2.7)))/100;
			}
		}
		return (1 + grav);
	}
	
	public static float calculateIbuFromRecipe(Recipe r)
	{
		float ibu;
		float AAU = 0;
		float utilization = getHopUtilization(r);
		
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		http://www.howtobrew.com/section1/chapter5-5.html
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.HOP))
			{
				Hop h = (Hop) i;
				AAU += h.getWeight() * h.getAlphaAcidContent();
			}
		}
		
		ibu = (AAU * utilization * 75)/r.getVolume();
		
		Log.e("BrewCalc", "Calculated IBU: " + ibu);
		
		return ibu;
	}
	
	public static float getHopUtilization(Recipe r)
	{
		float utilization;
		double bignessFactor;
		double boilTimeFactor;
		
		bignessFactor = 1.65 * Math.pow(.000125, r.getGravity()-1);
		boilTimeFactor = (1 - Math.pow(Math.E, -.04*r.getBoilTime()))/4.15;
		
		utilization = (float) (bignessFactor * boilTimeFactor);
		
		Log.e("BrewCalc", "Calculated Utilization: " + utilization);
		return utilization;
	}

}
