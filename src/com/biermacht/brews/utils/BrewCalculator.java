package com.biermacht.brews.utils;

import java.util.ArrayList;

import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
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
			if (i.getType().equals(Ingredient.FERMENTABLE))
			{
				Fermentable g = (Fermentable) i;
				MCU += g.getAmount() * g.getLovibondColor() / r.getBatchSize();
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
			if (i.getType().equals(Ingredient.FERMENTABLE))
			{
				Fermentable g = (Fermentable) i;
				
				grav += (g.getAmount() * g.getPpg() / (r.getBatchSize()-(g.getAmount()/2.7)))/100;
			}
		}
		return (1 + grav);
	}
	
	public static float calculateIbuFromRecipe(Recipe r)
	{
		float ibu;
		float AAU = 0;
		float utilization = 0;
		
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		// http://www.howtobrew.com/section1/chapter5-5.html
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.HOP))
			{
				Hop h = (Hop) i;
				utilization = getHopUtilization(r, h);
				AAU += h.getAmount() * h.getAlphaAcidContent();
			}
		}
		
		ibu = (AAU * utilization * 75)/r.getBatchSize();
		
		return ibu;
	}
	
	public static float getHopUtilization(Recipe r, Hop i)
	{
		float utilization;
		double bignessFactor;
		double boilTimeFactor;
		
		bignessFactor = 1.65 * Math.pow(.000125, r.getOG()-1);
		boilTimeFactor = (1 - Math.pow(Math.E, -.04*i.getBoilTime()))/4.15;
		
		utilization = (float) (bignessFactor * boilTimeFactor);
		
		return utilization;
	}

}
