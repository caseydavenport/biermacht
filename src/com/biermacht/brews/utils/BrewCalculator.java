package com.biermacht.brews.utils;

import java.util.ArrayList;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
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
				float ppg = g.getPpg();
				double amt = g.getAmount();
				float size = r.getBatchSize();
				double gVol = .2; // TODO
				
				grav += (ppg * amt) / (1000) / (size - gVol);
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
	
	public static double getFgEstimateFromRecipe(Recipe r)
	{
		float OG = r.getOG();
		double attn;          // Yeast attentuation
		
		for (Ingredient i : r.getIngredientList())
		{
			if (i.getType().equals(Ingredient.YEAST))
			{
				// First, try to determine if we have an attenuation value
				Yeast y = (Yeast) i;
				attn = y.getAttenuation();
				
				if (attn > 0)
					return (100-attn)*OG/1000 + 1;
					
			}
		}
		
		// If no attenuation value given, base off average for this style
		// TODO: Or base it off something else...
		return 0;
	}
	
	public static double getAbvFromRecipe(Recipe r)
	{
		double FG = r.getFG();
		double OG = r.getOG();
		
		return (OG-FG) * 131;
	}
	
	public static float getHopUtilization(Recipe r, Hop i)
	{
		float utilization;
		double bignessFactor;
		double boilTimeFactor;
		
		bignessFactor = 1.65 * Math.pow(.000125, r.getOG()-1);
		boilTimeFactor = (1 - Math.pow(Math.E, -.04*i.getTime()))/4.15;
		
		utilization = (float) (bignessFactor * boilTimeFactor);
		
		return utilization;
	}

}
