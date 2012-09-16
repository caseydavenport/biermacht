package com.biermacht.brews.utils;

import java.util.ArrayList;

import com.biermacht.brews.recipe.Grain;
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
	
	public static double calculateColorFromRecipe(Recipe r)
	{
		double SRM = 0.0;
		double MCU = 0.0;
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals("Grain"))
			{
				MCU += ((Grain) i).getWeight() * ((Grain) i).getLovibondColor() / r.getVolume();
			}
		}
		SRM = 1.4922*Math.pow(MCU, .6859);
		return SRM;
	}
	
	public static double calculateGravityFromRecipe(Recipe r)
	{
		double grav = 0.0;
		
		return grav;
	}
	
	public static double calculateIbuFromRecipe(Recipe r)
	{
		double ibu = 0.0;
		
		return ibu;
	}

}
