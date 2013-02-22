package com.biermacht.brews.utils;

import java.util.ArrayList;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import android.util.Log;
import android.os.*;

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
	
	public static double calculateOriginalGravityFromRecipe(Recipe r)
	{
		if (r.getType().equals(Recipe.EXTRACT))
			return calculateExtractOG(r);
		else
			return 2;
	}
	
	public static double calculateExtractOG(Recipe r)
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
				double size = r.getBatchSize();
				double gVol = .2; // TODO: Actually calculate grain volume!
				
				grav += (ppg * amt) / (1015) / (size + gVol);
			}
		}
		return 1 + grav;
	}
	
	public static double calculateIbuFromRecipe(Recipe r)
	{
		double ibu = 0;
		double AAU = 0;
		double utilization = 0;
		
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		// http://www.howtobrew.com/section1/chapter5-5.html
		for (Ingredient i : ingredientsList)
		{
			if (i.getType().equals(Ingredient.HOP))
			{
				Hop h = (Hop) i;
				// Only add bitterness if we are boiling the hops!
				if (h.getUse().equals(Hop.USE_BOIL))
				{
					utilization = getHopUtilization(r, h);
					AAU += h.getAmount() * h.getAlphaAcidContent();
					ibu += (AAU * utilization * 75)/r.getBatchSize();
				}
			}
		}
		
		return ibu;
	}
	
	public static double estimateFinalGravityFromRecipe(Recipe r)
	{
		double OG = r.getOG();
		double attn;          // Yeast attentuation
		
		for (Ingredient i : r.getIngredientList())
		{
			if (i.getType().equals(Ingredient.YEAST))
			{
				// First, try to determine if we have an attenuation value
				Yeast y = (Yeast) i;
				attn = y.getAttenuation();
				
				if (attn > 0)
				{
					double FG = OG - (attn)*(OG-1)*(.0106);
					return FG;
				}
					
			}
		}
		
		// If we don't have any yeast, there's no fermentation!
		return OG;
	}
	
	public static double calculateAbvFromRecipe(Recipe r)
	{
		double FG = r.getFG();
		double OG = r.getOG();
		
		return (OG-FG) * 131 * (r.getEfficiency() / 100);
	}
	
	public static float getHopUtilization(Recipe r, Hop i)
	{
		float utilization;
		double bignessFactor;
		double boilTimeFactor;
		double kettleFactor = .435; // Varies based on equipment...
		
		bignessFactor = Math.pow(.000125, r.getOG()-1);
		boilTimeFactor = (1 - Math.pow(Math.E, -.04*i.getTime()));
		
		utilization = (float) (bignessFactor * boilTimeFactor * kettleFactor);
		
		return utilization;
	}

}
