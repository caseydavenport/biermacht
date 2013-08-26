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
				MCU += Units.kilosToPounds(g.getBeerXmlStandardAmount()) * g.getLovibondColor() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
		}
		SRM = (float) (1.4922*Math.pow(MCU, .6859));
		return SRM;
	}
	
	public static double calculateGrainPercent(Recipe r, Ingredient i)
	{
		if(i.getType().equals(Ingredient.FERMENTABLE))
		{
			double g_amt = Units.kilosToPounds(i.getBeerXmlStandardAmount());
			double tot_amt = getTotalGrainWeight(r);
			
			return (g_amt / tot_amt) * 100;
		}
		
		return 0;
	}
	
	public static double getTotalGrainWeight(Recipe r)
	{
		double amt = 0;
		for(Ingredient i : r.getIngredientList())
		{
			if (i.getType().equals(Ingredient.FERMENTABLE))
			{
				amt += Units.kilosToPounds(i.getBeerXmlStandardAmount());
			}
		}
		
		return amt;
	}
	
	public static double calculateOriginalGravityFromRecipe(Recipe r)
	{
		float gravity_points = 0;
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		// http://homebrew.stackexchange.com/questions/1434/wiki-how-do-you-calculate-original-gravity
		for (Ingredient i : ingredientsList)
		{
			gravity_points += calculateGravityPoints(r, i);
		}
		return 1 + gravity_points/1000;
	}
	
	public static double calculateBoilGrav(Recipe r)
	{
		if (r.getType().equals(Recipe.EXTRACT))
			return calculateExtractBoilGrav(r);
		else
			return calculateAllGrainBoilGrav(r);
	}
	
	public static double calculateExtractBoilGrav(Recipe r)
	{
		// Because this is used for hop utilization calculation,
		// We want to adjust this based on late extract additions

		double mGPs = calculateOriginalGravityFromRecipe(r) - 1; //milliGPs
		double avgBoilTime = 0;
		int t=0;
		
		// TODO: This is imprecise as it doesn't take into account
		// how much extract is used as a late addition.. Its is only an approximate
		for (Fermentable f : r.getFermentablesList())
		{
			if (!f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
			{
				t++;
				avgBoilTime += f.getTime();
			}
		}
		if (t != 0)
		    avgBoilTime = avgBoilTime/t;
		else
		    avgBoilTime = r.getBoilTime();
			
		return 1 + (mGPs * Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / Units.litersToGallons(r.getBeerXmlStandardBoilSize()))*(avgBoilTime/r.getBoilTime());
	}

	public static double calculateAllGrainBoilGrav(Recipe r)
	{
		// Because this is used for hop utilization calculation

		double mGPs = calculateOriginalGravityFromRecipe(r) - 1; //milliGPs
		return 1 + (mGPs * Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / Units.litersToGallons(r.getBeerXmlStandardBoilSize()));
	}
	
	public static double calculateGravityPoints(Recipe r, Ingredient i)
	{
		if (r.getType().equals(Recipe.EXTRACT))
			return calculateExtractGravityPoints(r, i);
		if (r.getType().equals(Recipe.ALL_GRAIN))
			return calculateAllGrainGravityPoints(r, i);
		if (r.getType().equals(Recipe.PARTIAL_MASH))
			return calculateAllGrainGravityPoints(r, i);
		else
			return 1;
	}
	
	public static double calculateExtractGravityPoints(Recipe r, Ingredient i)
	{
		double pts = 0;
		if (i.getType().equals(Ingredient.FERMENTABLE))
		{
			Fermentable f = (Fermentable) i;
					
			if(f.getFermentableType().equals(Fermentable.TYPE_EXTRACT))
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
			else if (f.getFermentableType().equals(Fermentable.TYPE_SUGAR))
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
			else if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
			{
				pts = 5 * Units.kilosToPounds(f.getBeerXmlStandardAmount()) / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
				
				if(f.getName().equalsIgnoreCase("Roasted Barley"))
					pts = 10 * f.getDisplayAmount() / r.getDisplayBatchSize();
				if(f.getName().equalsIgnoreCase("Black Patent Malt"))
					pts = 10 * f.getDisplayAmount() / r.getDisplayBatchSize();
			}
			else
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
		}
		else if (i.getName().equals("Malto-Dextrine"))
		{
			pts = 10 * Units.kilosToPounds(i.getBeerXmlStandardAmount()) * 40 / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
		}
		return pts;
	}
	
	public static double calculateAllGrainGravityPoints(Recipe r, Ingredient i)
	{
		double pts = 0;
		if (i.getType().equals(Ingredient.FERMENTABLE))
		{
			Fermentable f = (Fermentable) i;

			if(f.getFermentableType().equals(Fermentable.TYPE_EXTRACT))
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
			else if (f.getFermentableType().equals(Fermentable.TYPE_SUGAR))
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
			else if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
			{
				pts = r.getEfficiency() * Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize()) / 100;
			}
			else
			{
				pts = Units.kilosToPounds(f.getBeerXmlStandardAmount()) * f.getPpg() / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
			}
		}
		else if (i.getName().equals("Malto-Dextrin"))
		{
			pts = 10 * Units.kilosToPounds(i.getBeerXmlStandardAmount()) * 40 / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
		}
		return pts;
	}
	
	public static double calculateIbuFromRecipe(Recipe r)
	{
		double ibu = 0;
		
		ArrayList<Ingredient> ingredientsList = r.getIngredientList();
		
		// http://www.howtobrew.com/section1/chapter5-5.html
		for (Ingredient i : ingredientsList)
		{
			ibu += calculateHopIbu(r, i);
		}
		
		return ibu;
	}
	
	public static double calculateHopIbu(Recipe r, Ingredient i)
	{
		double ibu = 0;
		double AAU = 0;
		double utilization = 0;
		
			if (i.getType().equals(Ingredient.HOP))
			{
				Hop h = (Hop) i;
				// Only add bitterness if we are boiling the hops!
				if (h.getUse().equals(Hop.USE_BOIL))
				{
					utilization = getHopUtilization(r, h);
					AAU = Units.kilosToOunces(h.getBeerXmlStandardAmount()) * h.getAlphaAcidContent();
					ibu = (AAU * utilization * 75) / Units.litersToGallons(r.getBeerXmlStandardBatchSize());
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
		
		return (OG-FG) * 131;
	}
	
	public static double getHopUtilization(Recipe r, Hop i)
	{
		float utilization;
		double bignessFactor;
		double boilTimeFactor;
		double boilGrav = calculateBoilGrav(r);
		
		// Default : 1.65/4.25
		bignessFactor = 1.65 * Math.pow(.00025, boilGrav-1);
		boilTimeFactor = (1 - Math.pow(Math.E, -.04*i.getTime()))/4.25;
		
		utilization = (float) (bignessFactor * boilTimeFactor);
		
		// Pellets require a little bit more hops.
		if (i.getForm().equals(Hop.FORM_PELLET))
			return .94 * utilization;
		else
			return utilization;
	}
	
	/**
	 * Adjusts a gravity reading based on temperature
	 * @param mg - Measured gravity
	 * @param temp - Measured temperature (F)
	 * @param ct - Temperature to which to calibrate (F)
	 * @return - Calibrated gravity
	 */
	public static double adjustGravityForTemp(double mg, double temp, double ct)
	{
		double cg; // Corrected grav
		cg = mg * (1.00130346 - 0.000134722124 * temp + 0.00000204052596 * temp*temp - 0.00000000232820948 * temp*temp*temp);
		cg = cg / (1.00130346 - 0.000134722124 * ct + 0.00000204052596 * ct*ct - 0.00000000232820948 * ct*ct*ct);
		
		return cg;
	}

}
