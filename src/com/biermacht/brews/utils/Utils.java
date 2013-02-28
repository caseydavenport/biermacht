package com.biermacht.brews.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeReccomendedValues;
import com.biermacht.brews.utils.comparators.BeerStyleComparator;
import com.biermacht.brews.utils.comparators.RecipeComparator;

public class Utils {
		
	// Instruction Types
	public static String INSTRUCTION_TYPE_BOIL = "Brew";
	public static String INSTRUCTION_TYPE_FERMENT = "Ferment";
	public static String INSTRUCTION_TYPE_OTHER = "Other";
	
	
	
	// Beer styles... http://www.bjcp.org/docs/2008_stylebook.pdf
	public static BeerStyle BEERSTYLE_OTHER = new BeerStyle("Other");
	public static BeerStyle BEERSTYLE_FRUIT_BEER = new BeerStyle("Fruit Beer");
	public static BeerStyle BEERSTYLE_SPICE_HERB_VEG = new BeerStyle("Spice/Herb/Vegetable Beer");
	public static BeerStyle BEERSTYLE_SPECIALTY = new BeerStyle("Specialty Beer");
	public static BeerStyle BEERSTYLE_RAUNCHBIER = new BeerStyle("Raunchbier");
	
	// Stouts
	public static BeerStyle BEERSTYLE_SWEET_STOUT = new BeerStyle("Stout, Sweet");
	public static BeerStyle BEERSTYLE_DRY_STOUT = new BeerStyle("Stout, Dry");
	public static BeerStyle BEERSTYLE_OATMEAL_STOUT = new BeerStyle("Stout, Oatmeal");
	public static BeerStyle BEERSTYLE_IMPERIAL_STOUT = new BeerStyle("Stout, Imperial");
	public static BeerStyle BEERSTYLE_FOREIGN_EXTRA_STOUT = new BeerStyle("Stout, Foreign Extra");
	public static BeerStyle BEERSTYLE_AMERICAN_STOUT = new BeerStyle("Stout, American");
	
	// German Wheat and Rye Beer
	public static BeerStyle BEERSTYLE_WEIZEN = new BeerStyle("Weizen / Weissbier");
	public static BeerStyle BEERSTYLE_DUNKELWEIZEN = new BeerStyle("Dunkelweizen");
	public static BeerStyle BEERSTYLE_WEIZENBOCK = new BeerStyle("Weizenbock");
	public static BeerStyle BEERSTYLE_GERMAN_RYE = new BeerStyle("German Rye Beer");

	
	// India Pale Ale (IPA)
	public static BeerStyle BEERSTYLE_ENGLISH_IPA = new BeerStyle("IPA, English");
	public static BeerStyle BEERSTYLE_AMERICAN_IPA = new BeerStyle("IPA, American");
	public static BeerStyle BEERSTYLE_IMPERIAL_IPA = new BeerStyle("IPA, Imperial");
	
	// Light Lager
	public static BeerStyle BEERSTYLE_LIGHT_AMERICAN_LAGER = new BeerStyle("Lager, Light American");
	public static BeerStyle BEERSTYLE_STANDARD_AMERICAN_LAGER = new BeerStyle("Lager, Standard American");
	public static BeerStyle BEERSTYLE_PREMIUN_AMERICAN_LAGER = new BeerStyle("Lager, Premium American");
	public static BeerStyle BEERSTYLE_MUNICH_HELLES = new BeerStyle("Munich Helles");
	
	// European Amber Lager
	public static BeerStyle BEERSTYLE_VIENNA_LAGER = new BeerStyle("Lager, Vienna");
	public static BeerStyle BEERSTYLE_OKT_MARZEN = new BeerStyle("Oktoberfest / Marzen");

	// Dark Lager
	public static BeerStyle BEERSTYLE_DARK_AMERICAN_LAGER = new BeerStyle("Lager, Dark American");
	public static BeerStyle BEERSTYLE_MUNICH_DUNKEL = new BeerStyle("Munich Dunkel");
	public static BeerStyle BEERSTYLE_SCHWARZBIER = new BeerStyle("Schwarzbier (Black Beer)");
	
	// Pilsners
	public static BeerStyle BEERSTYLE_GERMAN_PILSNER = new BeerStyle("Pilsner, German");
	public static BeerStyle BEERSTYLE_BOHEMIAN_PILSNER = new BeerStyle("Pilsner, Bohemian");
	public static BeerStyle BEERSTYLE_AMERICAN_PILSNER = new BeerStyle("Pilsner, Classic American");
	
	// Bocks
	public static BeerStyle BEERSTYLE_MAILBOCK = new BeerStyle("Mailbock / Helles Bock");
	public static BeerStyle BEERSTYLE_TRADITIONAL_BOCK = new BeerStyle("Bock, Traditional");
	public static BeerStyle BEERSTYLE_DOPPELBOCK = new BeerStyle("Doppelbock");
	public static BeerStyle BEERSTYLE_EISBOCK = new BeerStyle("Eisbock");
	
	// Light Hybrid Beers
	public static BeerStyle BEERSTYLE_CREAM_ALE = new BeerStyle("Cream Ale");
	public static BeerStyle BEERSTYLE_BLONDE_ALE = new BeerStyle("Blonde Ale");
	public static BeerStyle BEERSTYLE_KOLSCH = new BeerStyle("Kolsch");
	public static BeerStyle BEERSTYLE_AMERICAN_RYE_WHEAT = new BeerStyle("American Wheat or Rye");
	
	// Amber Hybrid Beers
	public static BeerStyle BEERSTYLE_GERMAN_ALTBIER = new BeerStyle("Northern German Altbier");
	public static BeerStyle BEERSTYLE_CALIF_COMMON = new BeerStyle("California Common Beer");
	public static BeerStyle BEERSTYLE_DUSSELDORF_ALTBIER = new BeerStyle("Dusseldorf Altbier");
	
	// English Pale Ales
	public static BeerStyle BEERSTYLE_STANDARD_BITTER = new BeerStyle("English Standard Bitter");
	public static BeerStyle BEERSTYLE_PREMIUM_BITTER = new BeerStyle("English Premium Bitter");
	public static BeerStyle BEERSTYLE_STRONG_BITTER = new BeerStyle("English Strong Bitter");
	
	// Scottish and Irish Ale
	public static BeerStyle BEERSTYLE_SCOTTISH_LIGHT = new BeerStyle("Scottish Light 60");
	public static BeerStyle BEERSTYLE_SCOTTISH_HEAVY = new BeerStyle("Scottish Heavy 70");
	public static BeerStyle BEERSTYLE_SCOTTISH_EXPORT = new BeerStyle("Scottish Export 80");
	public static BeerStyle BEERSTYLE_IRISH_RED = new BeerStyle("Irish Red Ale");
	public static BeerStyle BEERSTYLE_STRONG_SCOTCH_ALE = new BeerStyle("Strong Scotch Ale");
	
	// American Ale
	public static BeerStyle BEERSTYLE_AMERICAN_PALE_ALE = new BeerStyle("American Pale Ale");
	public static BeerStyle BEERSTYLE_AMERICAN_AMBER_ALE = new BeerStyle("American Amber Ale");
	public static BeerStyle BEERSTYLE_AMERICAN_BROWN_ALE = new BeerStyle("American Brown Ale");
	
	// English brown ale
	public static BeerStyle BEERSTYLE_ENGLISH_MILD_BROWN_ALE = new BeerStyle("English Mild Brown Ale");
	public static BeerStyle BEERSTYLE_ENGLISH_SOUTHERN_BROWN = new BeerStyle("English Southern Brown");
	public static BeerStyle BEERSTYLE_ENGLISH_NORTHERN_BROWN = new BeerStyle("English Northern Brown");
	
	// Porter
	public static BeerStyle BEERSTYLE_BROWN_PORTER = new BeerStyle("Porter, Brown");
	public static BeerStyle BEERSTYLE_ROBUST_PORTER = new BeerStyle("Porter, Robust");
	public static BeerStyle BEERSTYLE_BALTIC_PORTER = new BeerStyle("Porter, Baltic");
	
	// Belgian and French Ale TODO: Biere de Garde, Belgian Specialty Ale
	public static BeerStyle BEERSTYLE_WITBIER = new BeerStyle("Witbier");
	public static BeerStyle BEERSTYLE_BELGIAN_PALE_ALE = new BeerStyle("Belgian Ale");
	public static BeerStyle BEERSTYLE_SAISON = new BeerStyle("Saison");
	
	// Sour Ale TODO:
	public static BeerStyle BEERSTYLE_BERLINER_WEISSE = new BeerStyle("Berliner Weisse");
	
	// Belgian Ale TODO:
	public static BeerStyle BEERSTYLE_BELGIAN_BLOND_ALE = new BeerStyle("Belgian Blond Ale");
	
	// Strong Ale TODO:
	public static BeerStyle BEERSTYLE_OLD_ALE = new BeerStyle("Old Ale");

	
// ====================================================================================
// ====================================================================================
	
	
	public static ArrayList<BeerStyle> getBeerStyleList()
	{
		ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
		
		list.add(BEERSTYLE_WEIZEN);
		list.add(BEERSTYLE_SWEET_STOUT);
		list.add(BEERSTYLE_ENGLISH_IPA);
		list.add(BEERSTYLE_LIGHT_AMERICAN_LAGER);
		list.add(BEERSTYLE_GERMAN_PILSNER);
		list.add(BEERSTYLE_VIENNA_LAGER);
		list.add(BEERSTYLE_DARK_AMERICAN_LAGER);
		list.add(BEERSTYLE_MAILBOCK);
		list.add(BEERSTYLE_CREAM_ALE);
		list.add(BEERSTYLE_GERMAN_ALTBIER);
		list.add(BEERSTYLE_STANDARD_BITTER);
		list.add(BEERSTYLE_SCOTTISH_LIGHT);
		list.add(BEERSTYLE_AMERICAN_PALE_ALE);
		list.add(BEERSTYLE_ENGLISH_MILD_BROWN_ALE);
		list.add(BEERSTYLE_BROWN_PORTER);
		list.add(BEERSTYLE_GERMAN_RYE);
		list.add(BEERSTYLE_WITBIER);
		list.add(BEERSTYLE_BELGIAN_PALE_ALE);
		list.add(BEERSTYLE_BERLINER_WEISSE);
		list.add(BEERSTYLE_BELGIAN_BLOND_ALE);
		list.add(BEERSTYLE_OLD_ALE);
		list.add(BEERSTYLE_FRUIT_BEER);
		list.add(BEERSTYLE_SPICE_HERB_VEG);
		list.add(BEERSTYLE_SPECIALTY);
		list.add(BEERSTYLE_OATMEAL_STOUT);
		list.add(BEERSTYLE_IMPERIAL_STOUT);
		list.add(BEERSTYLE_DRY_STOUT);
		list.add(BEERSTYLE_STANDARD_AMERICAN_LAGER);
		list.add(BEERSTYLE_PREMIUN_AMERICAN_LAGER);
		list.add(BEERSTYLE_MUNICH_HELLES);
		list.add(BEERSTYLE_BOHEMIAN_PILSNER);
		list.add(BEERSTYLE_AMERICAN_PILSNER);
		list.add(BEERSTYLE_OKT_MARZEN);
		list.add(BEERSTYLE_MUNICH_DUNKEL);
		list.add(BEERSTYLE_SCHWARZBIER);
		list.add(BEERSTYLE_TRADITIONAL_BOCK);
		list.add(BEERSTYLE_DOPPELBOCK);
		list.add(BEERSTYLE_EISBOCK);
		list.add(BEERSTYLE_BLONDE_ALE);
		list.add(BEERSTYLE_KOLSCH);
		list.add(BEERSTYLE_AMERICAN_RYE_WHEAT);
		list.add(BEERSTYLE_CALIF_COMMON);
		list.add(BEERSTYLE_DUSSELDORF_ALTBIER);
		list.add(BEERSTYLE_PREMIUM_BITTER);
		list.add(BEERSTYLE_STRONG_BITTER);
		list.add(BEERSTYLE_SCOTTISH_HEAVY);
		list.add(BEERSTYLE_SCOTTISH_EXPORT);
		list.add(BEERSTYLE_IRISH_RED);
		list.add(BEERSTYLE_STRONG_SCOTCH_ALE);
		list.add(BEERSTYLE_AMERICAN_AMBER_ALE);
		list.add(BEERSTYLE_AMERICAN_BROWN_ALE);
		list.add(BEERSTYLE_ENGLISH_SOUTHERN_BROWN);
		list.add(BEERSTYLE_ENGLISH_NORTHERN_BROWN);
		list.add(BEERSTYLE_ROBUST_PORTER);
		list.add(BEERSTYLE_BALTIC_PORTER);
		list.add(BEERSTYLE_FOREIGN_EXTRA_STOUT);
		list.add(BEERSTYLE_AMERICAN_STOUT);
		list.add(BEERSTYLE_AMERICAN_IPA);
		list.add(BEERSTYLE_IMPERIAL_IPA);
		list.add(BEERSTYLE_DUNKELWEIZEN);
		list.add(BEERSTYLE_WEIZENBOCK);
		list.add(BEERSTYLE_SAISON);
		list.add(BEERSTYLE_RAUNCHBIER);
		
		Collections.sort(list, new BeerStyleComparator<BeerStyle>());
		list.add(0, BEERSTYLE_OTHER);
		return list;
	}
	
	public static RecipeReccomendedValues getRecipeReccomendedValues(Recipe r)
	{
		String style = r.getStyle().toString();
		
		if (style.equals(BEERSTYLE_OTHER))
			return new RecipeReccomendedValues();
		
		else if (style.equals(BEERSTYLE_SWEET_STOUT.toString()))
			return new RecipeReccomendedValues(1.044, 1.060, 1.012, 1.024, 20, 40, 30, 40, 4, 6); 
		
		else if (style.equals(BEERSTYLE_DRY_STOUT.toString()))
			return new RecipeReccomendedValues(1.036, 1.050, 1.007, 1.011, 30, 45, 25, 40, 4, 5);
		
		else if (style.equals(BEERSTYLE_OATMEAL_STOUT.toString()))
			return new RecipeReccomendedValues(1.048, 1.065, 1.010, 1.018, 25, 40, 22, 40, 4.2, 5.9);
		
		else if (style.equals(BEERSTYLE_FOREIGN_EXTRA_STOUT.toString()))
			return new RecipeReccomendedValues(1.056, 1.075, 1.010, 1.018, 30, 70, 30, 40, 5.5, 8);
		
		else if (style.equals(BEERSTYLE_AMERICAN_STOUT.toString()))
			return new RecipeReccomendedValues(1.050, 1.075, 1.010, 1.022, 35, 75, 30, 40, 5, 7);
		
		else if (style.equals(BEERSTYLE_IMPERIAL_STOUT.toString()))
			return new RecipeReccomendedValues(1.075, 1.115, 1.018, 1.030, 50, 90, 30, 40, 8, 12);
		
		else if (style.equals(BEERSTYLE_AMERICAN_IPA.toString()))
			return new RecipeReccomendedValues(1.056, 1.075, 1.010, 1.018, 40, 70, 6, 15, 5.5, 7.5);
		
		else if (style.equals(BEERSTYLE_AMERICAN_IPA.toString()))
			return new RecipeReccomendedValues(1.070, 1.090, 1.010, 1.020, 60, 120, 8, 15, 7.5, 10);
		
		else if (style.equals(BEERSTYLE_WEIZEN.toString()))
			return new RecipeReccomendedValues(1.044, 1.052, 1.010, 1.014, 8, 15, 2, 8, 4.2, 5.6);
		
		else if (style.equals(BEERSTYLE_DUNKELWEIZEN.toString()))
			return new RecipeReccomendedValues(1.044, 1.056, 1.010, 1.014, 10, 18, 14, 23, 4.3, 5.6);
		
		else if (style.equals(BEERSTYLE_WEIZENBOCK.toString()))
			return new RecipeReccomendedValues(1.064, 1.090, 1.015, 1.022, 15, 30, 12,25, 6.5, 8.0);
		
		else if (style.equals(BEERSTYLE_ENGLISH_IPA.toString()))
			return new RecipeReccomendedValues(1.050, 1.075, 1.010, 1.018, 40, 60, 8, 14, 5, 7.5);
		
		else if (style.equals(BEERSTYLE_LIGHT_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.028, 1.040, .998, 1.008, 8.0, 12.0, 2.0, 3.0, 2.8, 4.2);
		
		else if (style.equals(BEERSTYLE_STANDARD_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.040, 1.050, 1.004, 1.010, 8, 15, 2, 4, 4.2, 5.3);
		
		else if (style.equals(BEERSTYLE_PREMIUN_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.046, 1.056, 1.008, 1.012, 15, 25, 2, 6, 4.6, 6);
		
		else if (style.equals(BEERSTYLE_MUNICH_HELLES.toString()))
			return new RecipeReccomendedValues(1.045, 1.051, 1.008, 1.012, 16, 22, 3, 5, 4.7, 5.4);
		
		else if (style.equals(BEERSTYLE_GERMAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.050, 1.008, 1.013, 25, 45, 2, 5, 4.4, 5.2);
		
		else if (style.equals(BEERSTYLE_BOHEMIAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.056, 1.013, 1.017, 35, 45, 3.5, 6, 4.2, 5.4);
		
		else if (style.equals(BEERSTYLE_AMERICAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.060, 1.010, 1.015, 25, 40, 3, 6, 4.5, 6);
		
		else if (style.equals(BEERSTYLE_VIENNA_LAGER.toString()))
			return new RecipeReccomendedValues(1.046, 1.052, 1.010, 1.014, 18, 30, 10, 16, 4.5, 5.5);
		
		else if (style.equals(BEERSTYLE_OKT_MARZEN.toString()))
			return new RecipeReccomendedValues(1.050, 1.057, 1.012, 1.016, 20, 28, 7, 14, 4.8, 5.7);
		
		else if (style.equals(BEERSTYLE_DARK_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.044, 1.056, 1.008, 1.012, 8, 20, 14, 22, 4.2, 6);
		
		else if (style.equals(BEERSTYLE_MUNICH_DUNKEL.toString()))
			return new RecipeReccomendedValues(1.048, 1.056, 1.010, 1.016, 18, 28, 14, 28, 4.5, 5.6);
		
		else if (style.equals(BEERSTYLE_SCHWARZBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.052, 1.010, 1.016, 22, 32, 17, 30, 4.4, 5.4);
		
		else if (style.equals(BEERSTYLE_MAILBOCK.toString()))
			return new RecipeReccomendedValues(1.064, 1.072, 1.011, 1.018, 23, 35, 6, 11, 6.3, 7.4);
		
		else if (style.equals(BEERSTYLE_TRADITIONAL_BOCK.toString()))
			return new RecipeReccomendedValues(1.064, 1.072, 1.013, 1.019, 20, 27, 14, 22, 6.3, 7.2);
		
		else if (style.equals(BEERSTYLE_DOPPELBOCK.toString()))
			return new RecipeReccomendedValues(1.072, 1.112, 1.016, 1.024, 16, 26, 6, 25, 7, 10);
		
		else if (style.equals(BEERSTYLE_EISBOCK.toString()))
			return new RecipeReccomendedValues(1.078, 1.120, 1.020, 1.035, 25, 35, 18, 30, 9, 14);
		
		else if (style.equals(BEERSTYLE_CREAM_ALE.toString()))
			return new RecipeReccomendedValues(1.042, 1.055, 1.006, 1.012, 15, 20, 2.5, 5, 4.2, 5.6);
		
		else if (style.equals(BEERSTYLE_BLONDE_ALE.toString()))
			return new RecipeReccomendedValues(1.038, 1.054, 1.008, 1.013, 15, 28, 3, 6, 3.8, 5.5);
		
		else if (style.equals(BEERSTYLE_KOLSCH.toString()))
			return new RecipeReccomendedValues(1.044, 1.050, 1.007, 1.011, 20, 30, 3.5, 5, 4.4, 5.2);
		
		else if (style.equals(BEERSTYLE_AMERICAN_RYE_WHEAT.toString()))
			return new RecipeReccomendedValues(1.040, 1.055, 1.008, 1.013, 15, 30, 3, 6, 4, 5.5);
		
		else if (style.equals(BEERSTYLE_GERMAN_ALTBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.054, 1.010, 1.015, 25, 40, 13, 19, 4.5, 5.2);
		
		else if (style.equals(BEERSTYLE_CALIF_COMMON.toString()))
			return new RecipeReccomendedValues(1.048, 1.054, 1.011, 1.014, 30, 45, 10, 14, 4.5, 5.5);
		
		else if (style.equals(BEERSTYLE_DUSSELDORF_ALTBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.054, 1.010, 1.015, 35, 50, 11, 17, 4.5, 5.2);
		
		else if (style.equals(BEERSTYLE_STANDARD_BITTER.toString()))
			return new RecipeReccomendedValues(1.032, 1.040, 1.007, 1.011, 25, 35, 4, 14, 3.2, 3.8);
		
		else if (style.equals(BEERSTYLE_PREMIUM_BITTER.toString()))
			return new RecipeReccomendedValues(1.040, 1.048, 1.008, 1.012, 25, 40, 5, 16, 3.8, 4.6);
		
		else if (style.equals(BEERSTYLE_STRONG_BITTER.toString()))
			return new RecipeReccomendedValues(1.048, 1.060, 1.010, 1.016, 30, 50, 6, 18, 4.6, 6.2);
		
		else if (style.equals(BEERSTYLE_SCOTTISH_LIGHT.toString()))
			return new RecipeReccomendedValues(1.030, 1.035, 1.010, 1.013, 10, 20, 9, 17, 2.5, 3.2);
		
		else if (style.equals(BEERSTYLE_SCOTTISH_HEAVY.toString()))
			return new RecipeReccomendedValues(1.035, 1.040, 1.010, 1.015, 10, 25, 9, 17, 3.2, 3.9);
		
		else if (style.equals(BEERSTYLE_SCOTTISH_EXPORT.toString()))
			return new RecipeReccomendedValues(1.040, 1.054, 1.010, 1.016, 15, 30, 9, 17, 3.9, 5.0);
		
		else if (style.equals(BEERSTYLE_IRISH_RED.toString()))
			return new RecipeReccomendedValues(1.044, 1.060, 1.010, 1.014, 17, 28, 9, 18, 4.0, 6.0);
		
		else if (style.equals(BEERSTYLE_STRONG_SCOTCH_ALE.toString()))
			return new RecipeReccomendedValues(1.070, 1.130, 1.018, 1.056, 17, 35, 14, 25, 6.5, 10);
		
		else if (style.equals(BEERSTYLE_AMERICAN_PALE_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.015, 30, 45, 5, 14, 4.5, 6.2);
		
		else if (style.equals(BEERSTYLE_AMERICAN_AMBER_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.015, 25, 40, 10, 17, 4.5, 6.2);
		
		else if (style.equals(BEERSTYLE_AMERICAN_BROWN_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.016, 20, 40, 18, 35, 4.2, 6.2);
		
		else if (style.equals(BEERSTYLE_ENGLISH_MILD_BROWN_ALE.toString()))
			return new RecipeReccomendedValues(1.030, 1.038, 1.008, 1.013, 10, 25, 12, 25, 2.8, 4.5);
		
		else if (style.equals(BEERSTYLE_ENGLISH_SOUTHERN_BROWN.toString()))
			return new RecipeReccomendedValues(1.033, 1.042, 1.011, 1.014, 12, 20, 19, 35, 2.8, 4.1);
		
		else if (style.equals(BEERSTYLE_ENGLISH_NORTHERN_BROWN.toString()))
			return new RecipeReccomendedValues(1.040, 1.052, 1.008, 1.013, 20, 30, 12, 22, 4.2, 5.4);
		
		else if (style.equals(BEERSTYLE_BROWN_PORTER.toString()))
			return new RecipeReccomendedValues(1.040, 1.052, 1.008, 1.014, 18, 35, 20, 30, 4, 5.4);
		
		else if (style.equals(BEERSTYLE_ROBUST_PORTER.toString()))
			return new RecipeReccomendedValues(1.048, 1.065, 1.012, 1.016, 25, 50, 22, 35, 4.8, 6.5);
		
		else if (style.equals(BEERSTYLE_BALTIC_PORTER.toString()))
			return new RecipeReccomendedValues(1.060, 1.090, 1.016, 1.024, 20, 40, 17, 30, 5.5, 9.5);
		
		else if (style.equals(BEERSTYLE_GERMAN_RYE.toString()))
			return new RecipeReccomendedValues(1.046, 1.056, 1.010, 1.014, 10, 20, 14, 19, 4.5, 6.0);
		
		else if (style.equals(BEERSTYLE_WITBIER.toString()))
			return new RecipeReccomendedValues(1.044, 1.052, 1.008, 1.012, 10, 20, 2.0, 4.0, 4.5, 5.5);
		
		else if (style.equals(BEERSTYLE_BELGIAN_PALE_ALE.toString()))
			return new RecipeReccomendedValues(1.048, 1.054, 1.010, 1.014, 20, 30, 8, 14, 4.8, 5.5);
		
		else if (style.equals(BEERSTYLE_SAISON.toString()))
			return new RecipeReccomendedValues(1.048, 1.065, 1.002, 1.012, 20, 35, 5, 14, 5, 7);
		
		else if (style.equals(BEERSTYLE_BERLINER_WEISSE.toString()))
			return new RecipeReccomendedValues(1.028, 1.032, 1.003, 1.006, 3, 8, 2, 3, 2.8, 3.8);
		
		else if (style.equals(BEERSTYLE_BELGIAN_BLOND_ALE.toString()))
			return new RecipeReccomendedValues(1.062, 1.075, 1.008, 1.018, 15, 30, 4, 7, 6, 7.5);
		
		else if (style.equals(BEERSTYLE_OLD_ALE.toString()))
			return new RecipeReccomendedValues(1.060, 1.090, 1.015, 1.022, 30, 60, 10, 22, 6, 9);
		
		else if (style.equals(BEERSTYLE_RAUNCHBIER.toString()))
			return new RecipeReccomendedValues(1.050, 1.057, 1.012, 1.016, 20, 30, 12, 22, 4.8, 6);
		
		else if (style.equals(BEERSTYLE_FRUIT_BEER.toString()))
			return new RecipeReccomendedValues();
		
		else if (style.equals(BEERSTYLE_SPICE_HERB_VEG.toString()))
			return new RecipeReccomendedValues();
		
		else if (style.equals(BEERSTYLE_SPECIALTY.toString()))
			return new RecipeReccomendedValues();
		
		// If all else fails
		
		else
			return new RecipeReccomendedValues();
	}
	
	/**
	 * Returns a list of strings corresponding to beer styles
	 * @return
	 */
	public static ArrayList<String> getBeerStyleStringList()
	{
		ArrayList<BeerStyle> listA = getBeerStyleList();
		ArrayList<String> listToReturn = new ArrayList<String>();
		
		for (BeerStyle b : listA)
		{
			listToReturn.add(b.toString());
		}	
		return listToReturn;
	}
	
	/**
	 * Returns a list of strings corresponding to ingredient objects
	 * @return
	 */
	public static ArrayList<String> getIngredientStringList(ArrayList<Ingredient> list)
	{
		ArrayList<Ingredient> listA = list;
		ArrayList<String> listToReturn = new ArrayList<String>();
		
		for (Ingredient b : listA)
		{
			listToReturn.add(b.toString());
		}	
		return listToReturn;
	}
	
	/**
	 * Determins if the given value is within the given range
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
	
	// Methods for dealing with the Database
	/**
	 * Get all recipes in the database
	 * @param dbi
	 * @return
	 */
	public static ArrayList<Recipe> getRecipeList(DatabaseInterface dbi)
	{
		ArrayList<Recipe> list = dbi.getRecipeList();
		
		for (Recipe r : list)
			r.update();
		
		Collections.sort(list, new RecipeComparator<Recipe>());
		
		return list;
	}
	
	/**
	 * Create a recipe with the given name
	 * @param name
	 * @return
	 */
	public static Recipe createRecipeWithName(String name)
	{
		Recipe r = new Recipe(name);
		
		long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
		r = MainActivity.databaseInterface.getRecipeWithId(id);
		
		return r;
	}
	
	/**
	 * Create a recipe with the given name
	 * @param name
	 * @return
	 */
	public static Recipe createRecipeFromExisting(Recipe r)
	{
		long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
		r = MainActivity.databaseInterface.getRecipeWithId(id);
		
		return r;
	}
	
	/**
	 * Updates existing recipe to match the given recipe
	 * @param r
	 * @return
	 */
	public static boolean updateRecipe(Recipe r)
	{
		r.update();
		return MainActivity.databaseInterface.updateExistingRecipe(r);
	}
	
	/**
	 * Updates the existing ingredient in the database
	 * @param i
	 * @return
	 */
	public static boolean updateIngredient(Ingredient i)
	{
		return MainActivity.databaseInterface.updateExistingIngredient(i);
	}
	
	/**
	 * Deletes the given recipe from the database
	 * @param r
	 * @return
	 */
	public static boolean deleteRecipe(Recipe r)
	{
		return MainActivity.databaseInterface.deleteRecipeIfExists(r.getId());
	}
	
	/**
	 * Deletes the given ingredient from database
	 * @param i
	 * @return
	 */
	public static boolean deleteIngredient(Ingredient i)
	{
		return MainActivity.databaseInterface.deleteIngredientIfExists(i.getId());
	}
	
	/**
	 * Gets recipe with given ID from database
	 * @param id
	 * @return
	 */
	public static Recipe getRecipeWithId(long id)
	{
		return MainActivity.databaseInterface.getRecipeWithId(id);
	}
	
	/**
	 * Gets ingredient with given ID from database
	 * @param id
	 * @return
	 */
	public static Ingredient getIngredientWithId(long id)
	{
		return MainActivity.databaseInterface.getIngredientWithId(id);
	}
	
	/**
	 * 
	 * @return Beer XML standard version in use
	 */
	public static int getXmlVersion()
	{
		return 1;
	}
	
	/**
	 * Gets beerStyle object for given string
	 * @param s
	 */
	public static BeerStyle getBeerStyleFromString(String s)
	{
		for (BeerStyle b : getBeerStyleList())
		{
			if(b.toString().equals(s))
				return b;
		}
		
		return new BeerStyle(null); // TODO: THIS IS SHITTY
	}
	
	public static Recipe scaleRecipe(Recipe r, double newVolume)
	{
		double oldVolume = r.getBatchSize();
		double ratio = newVolume / oldVolume;
		
		r.setBatchSize(newVolume);
		r.setBoilSize(r.getBoilSize() * ratio);
		
		Log.e("UTILS", "New Volume: " + newVolume + " Scale: " + ratio);
		
		for (Ingredient i : r.getIngredientList())
		{
			i.setAmount(i.getAmount() * ratio);
			updateIngredient(i);
		}
		
		r.update();
		updateRecipe(r);
		return r;
	}
}
