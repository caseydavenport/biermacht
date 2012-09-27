package com.biermacht.brews.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Hop;
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeReccomendedValues;

public class Utils {
	
	// Ingredient types
	public static String TYPE_HOPS = "hops";
	public static String TYPE_YEAST = "yeast";
	public static String TYPE_MALT = "malt";
	public static String TYPE_SPICE = "spice";
	public static String TYPE_OTHER = "other";
	
	// Instruction Types
	public static String INSTRUCTION_TYPE_BOIL = "Brew";
	public static String INSTRUCTION_TYPE_FERMENT = "Ferment";
	public static String INSTRUCTION_TYPE_OTHER = "Other";
	
	// Beer types... http://www.bjcp.org/docs/2008_stylebook.pdf
	public static BeerStyle BEERTYPE_OTHER = new BeerStyle("Other");
	
	// Stouts
	public static BeerStyle BEERTYPE_STOUT = new BeerStyle("Stout");
	public static BeerStyle BEERTYPE_DRY_STOUT = new BeerStyle("Dry Stout");
	public static BeerStyle BEERTYPE_OATMEAL_STOUT = new BeerStyle("Oatmeal Stout");
	public static BeerStyle BEERTYPE_IMPERIAL_STOUT = new BeerStyle("Imperial Stout");
	public static BeerStyle BEERTYPE_HEFEWEIZEN = new BeerStyle("Hefeweizen");
	public static BeerStyle BEERTYPE_IPA = new BeerStyle("India Pale Ale");
	
	// Light Lager
	public static BeerStyle BEERTYPE_LIGHT_AMERICAN_LAGER = new BeerStyle("Lager, Light American");
	public static BeerStyle BEERTYPE_STANDARD_AMERICAN_LAGER = new BeerStyle("Lager, Standard American");
	public static BeerStyle BEERTYPE_PREMIUN_AMERICAN_LAGER = new BeerStyle("Lager, Premium American");
	public static BeerStyle BEERTYPE_MUNICH_HELLES = new BeerStyle("Munich Helles");
	
	// European Amber Lager
	public static BeerStyle BEERTYPE_VIENNA_LAGER = new BeerStyle("Lager, Vienna");
	public static BeerStyle BEERTYPE_OKT_MARZEN = new BeerStyle("Oktoberfest / Marzen");

	// Dark Lager
	public static BeerStyle BEERTYPE_DARK_AMERICAN_LAGER = new BeerStyle("Lager, Dark American");
	public static BeerStyle BEERTYPE_MUNICH_DUNKEL = new BeerStyle("Munich Dunkel");
	public static BeerStyle BEERTYPE_SCHWARZBIER = new BeerStyle("Schwarzbier (Black Beer)");
	
	// Pilsners
	public static BeerStyle BEERTYPE_GERMAN_PILSNER = new BeerStyle("Pilsner, German");
	public static BeerStyle BEERTYPE_BOHEMIAN_PILSNER = new BeerStyle("Pilsner, Bohemian");
	public static BeerStyle BEERTYPE_AMERICAN_PILSNER = new BeerStyle("Pilsner, Classic American");
	
	// Bocks
	public static BeerStyle BEERTYPE_MAILBOCK = new BeerStyle("Mailbock / Helles Bock");
	public static BeerStyle BEERTYPE_TRADITIONAL_BOCK = new BeerStyle("Bock, Traditional");
	public static BeerStyle BEERTYPE_DOPPELBOCK = new BeerStyle("Doppelbock");
	public static BeerStyle BEERTYPE_EISBOCK = new BeerStyle("Eisbock");
	
	// Light Hybrid Beers
	public static BeerStyle BEERTYPE_CREAM_ALE = new BeerStyle("Cream Ale");
	public static BeerStyle BEERTYPE_BLONDE_ALE = new BeerStyle("Blonde Ale");
	public static BeerStyle BEERTYPE_KOLSCH = new BeerStyle("Kolsch");
	public static BeerStyle BEERTYPE_AMERICAN_RYE_WHEAT = new BeerStyle("American Wheat or Rye");
	
	// Amber Hybrid Beers
	public static BeerStyle BEERTYPE_GERMAN_ALTBIER = new BeerStyle("Northern German Altbier");
	public static BeerStyle BEERTYPE_CALIF_COMMON = new BeerStyle("California Common Beer");
	public static BeerStyle BEERTYPE_DUSSELDORF_ALTBIER = new BeerStyle("Dusseldorf Altbier");
	
	// English Pale Ales
	public static BeerStyle BEERTYPE_STANDARD_BITTER = new BeerStyle("English Standard Bitter");
	public static BeerStyle BEERTYPE_PREMIUM_BITTER = new BeerStyle("English Premium Bitter");
	public static BeerStyle BEERTYPE_STRONG_BITTER = new BeerStyle("English Strong Bitter");
	
	// Scottish and Irish Ale
	public static BeerStyle BEERTYPE_SCOTTISH_LIGHT = new BeerStyle("Scottish Light 60");
	public static BeerStyle BEERTYPE_SCOTTISH_HEAVY = new BeerStyle("Scottish Heavy 70");
	public static BeerStyle BEERTYPE_SCOTTISH_EXPORT = new BeerStyle("Scottish Export 80");
	public static BeerStyle BEERTYPE_IRISH_RED = new BeerStyle("Irish Red Ale");
	public static BeerStyle BEERTYPE_STRONG_SCOTCH_ALE = new BeerStyle("Strong Scotch Ale");
	
	// American Ale
	public static BeerStyle BEERTYPE_AMERICAN_PALE_ALE = new BeerStyle("American Pale Ale");
	public static BeerStyle BEERTYPE_AMERICAN_AMBER_ALE = new BeerStyle("American Amber Ale");
	public static BeerStyle BEERTYPE_AMERICAN_BROWN_ALE = new BeerStyle("American Brown Ale");
	
	// English brown ale
	public static BeerStyle BEERTYPE_ENGLISH_MILD_BROWN_ALE = new BeerStyle("English Mild Brown Ale");
	public static BeerStyle BEERTYPE_ENGLISH_SOUTHERN_BROWN = new BeerStyle("English Southern Brown");
	public static BeerStyle BEERTYPE_ENGLISH_NORTHERN_BROWN = new BeerStyle("English Northern Brown");
	
	// Porter
	public static BeerStyle BEERTYPE_BROWN_PORTER = new BeerStyle("Porter, Brown");

	public static BeerStyle BEERTYPE_GERMAN_WHEAT = new BeerStyle("German Wheat");
	public static BeerStyle BEERTYPE_WEIZENBOCK = new BeerStyle("Weizenbock");
	public static BeerStyle BEERTYPE_WITBIER = new BeerStyle("Witbier");
	public static BeerStyle BEERTYPE_BELGIAN_ALE = new BeerStyle("Belgian Ale");
	public static BeerStyle BEERTYPE_SOUR_ALE = new BeerStyle("Sour Ale");
	public static BeerStyle BEERTYPE_BELGIAN_STRONG_ALE = new BeerStyle("Belgian Strong Ale");
	public static BeerStyle BEERTYPE_STRONG_ALE = new BeerStyle("Strong Ale");
	public static BeerStyle BEERTYPE_FRUIT_BEER = new BeerStyle("Fruit Beer");
	public static BeerStyle BEERTYPE_SPICE_HERB_VEG = new BeerStyle("Spice/Herb/Vegetable Beer");
	public static BeerStyle BEERTYPE_SPECIALTY = new BeerStyle("Specialty Beer");
	
	// Fermentables.. http://byo.com/resources/grains
	public static Ingredient FERMENTABLE_BLACK_BARLEY = new Grain("Black Barley", "lbs", 525, 1.025, Grain.GRAIN);
	public static Ingredient FERMENTABLE_MUNICH_MALT = new Grain("Munich Malt", "lbs", 10, 1.034, Grain.GRAIN);
	public static Ingredient FERMENTABLE_CHOCOLATE_MALT = new Grain("Chocolate Malt", "lbs", 350, 1.034, Grain.GRAIN);
	public static Ingredient FERMENTABLE_DEXTRIN_MALT = new Grain("Dextrin Malt", "lbs", 1.5, 1.033, Grain.GRAIN);
	public static Ingredient FERMENTABLE_PALE_MALT_2_ROW = new Grain("Pale Malt, 2-row", "lbs", 1.8, 1.037, Grain.GRAIN);
	public static Ingredient FERMENTABLE_PALE_MALT_6_ROW = new Grain("Pale Malt, 6-row", "lbs", 1.8, 1.035, Grain.GRAIN);
	public static Ingredient FERMENTABLE_ROASTED_BARELY = new Grain("Roasted Barley", "lbs", 300, 1.025, Grain.GRAIN);
	public static Ingredient FERMENTABLE_VICTORY_MALT = new Grain("Victory Malt", "lbs", 25, 1.034, Grain.GRAIN);
	public static Ingredient FERMENTABLE_VIENNA_MALT = new Grain("Vienna Malt", "lbs", 3.7, 1.035, Grain.GRAIN);
	public static Ingredient FERMENTABLE_WHEAT_MALT = new Grain("Wheat Malt", "lbs", 2, 1.038, Grain.GRAIN);
	public static Ingredient FERMENTABLE_WHITE_WHEAT_MALT = new Grain("White Wheat Malt", "lbs", 2, 1.037, Grain.GRAIN);

	// Hops.. http://byo.com/resources/hops
	public static Ingredient HOP_AHTANUM = new Hop("Ahtanum", 5, "Floral, citrus, sharp, piney");
	public static Ingredient HOP_AMARILLO = new Hop("Amarillo", 8.5, "Citrus, flowery");
	public static Ingredient HOP_APOLLO = new Hop("Apollo", 17, "High alpha acid content, disease resistant");
	public static Ingredient HOP_BOADICEA = new Hop("Boadicea", 8.5, "Spicy");
	public static Ingredient HOP_BRAVO = new Hop("Bravo", 16, "High alpha acid content, disease resistant");
	public static Ingredient HOP_CASCADE_US = new Hop("Cascade (US)", 5.5, "Flowery, citrusy.  Can have a grapefruit flavor");
	public static Ingredient HOP_CASCADE_NZ = new Hop("Cascade (NZ)", 7, "Flowery, citrusy.  Can have a grapefruit flavor");
	public static Ingredient HOP_CENTENNIAL = new Hop("Centennial", 9.75, "Medium floral and citrus tones");
	public static Ingredient HOP_CHINOOK = new Hop("Chinook", 12, "Medium strength, spicy, piney");
	public static Ingredient HOP_CITRA = new Hop("Citra", 12, "Intense Flavor");
	public static Ingredient HOP_CLUSTER = new Hop("Cluster", 7.5, "Quite spicy");
	public static Ingredient HOP_COLUMBUS = new Hop("Columbus", 13.5, "Pleasant, strong aroma");
	public static Ingredient HOP_EL_DORADO = new Hop("El Dorado (US)", 15, "Candy-like");
	public static Ingredient HOP_GALAXY = new Hop("Galaxy (AUS)", 14, "");
	public static Ingredient HOP_GALENA = new Hop("Galena", 12, "Medium strength, citrusy");
	public static Ingredient HOP_GLACIER = new Hop("Glacier (US)", 7, "Earthy, Citrus");

	
// ====================================================================================
// ====================================================================================
	
	
	public static ArrayList<BeerStyle> getBeerStyleList()
	{
		ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
		
		list.add(BEERTYPE_HEFEWEIZEN);
		list.add(BEERTYPE_STOUT);
		list.add(BEERTYPE_IPA);
		list.add(BEERTYPE_LIGHT_AMERICAN_LAGER);
		list.add(BEERTYPE_GERMAN_PILSNER);
		list.add(BEERTYPE_VIENNA_LAGER);
		list.add(BEERTYPE_DARK_AMERICAN_LAGER);
		list.add(BEERTYPE_MAILBOCK);
		list.add(BEERTYPE_CREAM_ALE);
		list.add(BEERTYPE_GERMAN_ALTBIER);
		list.add(BEERTYPE_STANDARD_BITTER);
		list.add(BEERTYPE_SCOTTISH_LIGHT);
		list.add(BEERTYPE_AMERICAN_PALE_ALE);
		list.add(BEERTYPE_ENGLISH_MILD_BROWN_ALE);
		list.add(BEERTYPE_BROWN_PORTER);
		list.add(BEERTYPE_GERMAN_WHEAT);
		list.add(BEERTYPE_WEIZENBOCK);
		list.add(BEERTYPE_WITBIER);
		list.add(BEERTYPE_BELGIAN_ALE);
		list.add(BEERTYPE_SOUR_ALE);
		list.add(BEERTYPE_BELGIAN_STRONG_ALE);
		list.add(BEERTYPE_STRONG_ALE);
		list.add(BEERTYPE_FRUIT_BEER);
		list.add(BEERTYPE_SPICE_HERB_VEG);
		list.add(BEERTYPE_SPECIALTY);
		list.add(BEERTYPE_OATMEAL_STOUT);
		list.add(BEERTYPE_IMPERIAL_STOUT);
		list.add(BEERTYPE_DRY_STOUT);
		list.add(BEERTYPE_STANDARD_AMERICAN_LAGER);
		list.add(BEERTYPE_PREMIUN_AMERICAN_LAGER);
		list.add(BEERTYPE_MUNICH_HELLES);
		list.add(BEERTYPE_BOHEMIAN_PILSNER);
		list.add(BEERTYPE_AMERICAN_PILSNER);
		list.add(BEERTYPE_OKT_MARZEN);
		list.add(BEERTYPE_MUNICH_DUNKEL);
		list.add(BEERTYPE_SCHWARZBIER);
		list.add(BEERTYPE_TRADITIONAL_BOCK);
		list.add(BEERTYPE_DOPPELBOCK);
		list.add(BEERTYPE_EISBOCK);
		list.add(BEERTYPE_BLONDE_ALE);
		list.add(BEERTYPE_KOLSCH);
		list.add(BEERTYPE_KOLSCH);
		list.add(BEERTYPE_AMERICAN_RYE_WHEAT);
		list.add(BEERTYPE_CALIF_COMMON);
		list.add(BEERTYPE_DUSSELDORF_ALTBIER);
		list.add(BEERTYPE_PREMIUM_BITTER);
		list.add(BEERTYPE_STRONG_BITTER);
		list.add(BEERTYPE_SCOTTISH_HEAVY);
		list.add(BEERTYPE_SCOTTISH_EXPORT);
		list.add(BEERTYPE_IRISH_RED);
		list.add(BEERTYPE_STRONG_SCOTCH_ALE);
		list.add(BEERTYPE_AMERICAN_AMBER_ALE);
		list.add(BEERTYPE_AMERICAN_BROWN_ALE);
		list.add(BEERTYPE_ENGLISH_SOUTHERN_BROWN);
		list.add(BEERTYPE_ENGLISH_NORTHERN_BROWN);
		
		Collections.sort(list, new BeerStyleComparator<BeerStyle>());
		list.add(0, BEERTYPE_OTHER);
		return list;
	}
	
	public static ArrayList<Ingredient> getFermentablesList()
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		list.add(FERMENTABLE_BLACK_BARLEY);
		list.add(FERMENTABLE_MUNICH_MALT);
		list.add(FERMENTABLE_CHOCOLATE_MALT);
		list.add(FERMENTABLE_DEXTRIN_MALT);
		list.add(FERMENTABLE_PALE_MALT_2_ROW);
		list.add(FERMENTABLE_PALE_MALT_6_ROW);
		list.add(FERMENTABLE_ROASTED_BARELY);
		list.add(FERMENTABLE_VICTORY_MALT);
		list.add(FERMENTABLE_VIENNA_MALT);
		list.add(FERMENTABLE_WHEAT_MALT);
		list.add(FERMENTABLE_WHITE_WHEAT_MALT);

		return list;
	}
	
	public static ArrayList<Ingredient> getHopsList()
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		list.add(HOP_AHTANUM);
		list.add(HOP_AMARILLO);
		list.add(HOP_APOLLO);
		list.add(HOP_BOADICEA);
		list.add(HOP_BRAVO);
		list.add(HOP_CASCADE_US);
		list.add(HOP_CASCADE_NZ);
		list.add(HOP_CENTENNIAL);
		list.add(HOP_CHINOOK);
		list.add(HOP_CITRA);
		list.add(HOP_CLUSTER);
		list.add(HOP_COLUMBUS);
		list.add(HOP_EL_DORADO);
		list.add(HOP_GALAXY);
		list.add(HOP_GALENA);
		list.add(HOP_GLACIER);
	
		return list;
	}
	
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
	
	public static ArrayList<String> getFermentablesStringList()
	{
		ArrayList<Ingredient> listA = getFermentablesList();
		ArrayList<String> listToReturn = new ArrayList<String>();
		
		for (Ingredient b : listA)
		{
			listToReturn.add(b.toString());
		}	
		return listToReturn;
	}
	
	public static ArrayList<String> getHopsStringList()
	{
		ArrayList<Ingredient> listA = getHopsList();
		ArrayList<String> listToReturn = new ArrayList<String>();
		
		for (Ingredient b : listA)
		{
			listToReturn.add(b.toString());
		}	
		return listToReturn;
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
	 * Updates existing recipe to match the given recipe
	 * @param r
	 * @return
	 */
	public static boolean updateRecipe(Recipe r)
	{
		r.update();
		return MainActivity.databaseInterface.updateExistingRecipe(r);
	}
	
	public static boolean updateIngredient(Ingredient i)
	{
		return MainActivity.databaseInterface.updateExistingIngredient(i);
	}
	
	public static boolean deleteRecipe(Recipe r)
	{
		return MainActivity.databaseInterface.deleteRecipeIfExists(r.getId());
	}
	
	public static boolean deleteIngredient(Ingredient i)
	{
		return MainActivity.databaseInterface.deleteIngredientIfExists(i.getId());
	}
	
	public static Recipe getRecipeWithId(long id)
	{
		return MainActivity.databaseInterface.getRecipeWithId(id);
	}
	
	public static Ingredient getIngredientWithId(long id)
	{
		return MainActivity.databaseInterface.getIngredientWithId(id);
	}
	
	public static RecipeReccomendedValues getRecipeReccomendedValues(Recipe r)
	{
		String type = r.getBeerType();
		
		if (type.equals(BEERTYPE_OTHER))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_STOUT.toString()))
			return new RecipeReccomendedValues(); 
		
		else if (type.equals(BEERTYPE_DRY_STOUT.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_OATMEAL_STOUT.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_IMPERIAL_STOUT.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_HEFEWEIZEN.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_IPA.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_LIGHT_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.028, 1.040, .998, 1.008, 8.0, 12.0, 2.0, 3.0, 2.8, 4.2);
		
		else if (type.equals(BEERTYPE_STANDARD_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.040, 1.050, 1.004, 1.010, 8, 15, 2, 4, 4.2, 5.3);
		
		else if (type.equals(BEERTYPE_PREMIUN_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.046, 1.056, 1.008, 1.012, 15, 25, 2, 6, 4.6, 6);
		
		else if (type.equals(BEERTYPE_MUNICH_HELLES.toString()))
			return new RecipeReccomendedValues(1.045, 1.051, 1.008, 1.012, 16, 22, 3, 5, 4.7, 5.4);
		
		else if (type.equals(BEERTYPE_GERMAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.050, 1.008, 1.013, 25, 45, 2, 5, 4.4, 5.2);
		
		else if (type.equals(BEERTYPE_BOHEMIAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.056, 1.013, 1.017, 35, 45, 3.5, 6, 4.2, 5.4);
		
		else if (type.equals(BEERTYPE_AMERICAN_PILSNER.toString()))
			return new RecipeReccomendedValues(1.044, 1.060, 1.010, 1.015, 25, 40, 3, 6, 4.5, 6);
		
		else if (type.equals(BEERTYPE_VIENNA_LAGER.toString()))
			return new RecipeReccomendedValues(1.046, 1.052, 1.010, 1.014, 18, 30, 10, 16, 4.5, 5.5);
		
		else if (type.equals(BEERTYPE_OKT_MARZEN.toString()))
			return new RecipeReccomendedValues(1.050, 1.057, 1.012, 1.016, 20, 28, 7, 14, 4.8, 5.7);
		
		else if (type.equals(BEERTYPE_DARK_AMERICAN_LAGER.toString()))
			return new RecipeReccomendedValues(1.044, 1.056, 1.008, 1.012, 8, 20, 14, 22, 4.2, 6);
		
		else if (type.equals(BEERTYPE_MUNICH_DUNKEL.toString()))
			return new RecipeReccomendedValues(1.048, 1.056, 1.010, 1.016, 18, 28, 14, 28, 4.5, 5.6);
		
		else if (type.equals(BEERTYPE_SCHWARZBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.052, 1.010, 1.016, 22, 32, 17, 30, 4.4, 5.4);
		
		else if (type.equals(BEERTYPE_MAILBOCK.toString()))
			return new RecipeReccomendedValues(1.064, 1.072, 1.011, 1.018, 23, 35, 6, 11, 6.3, 7.4);
		
		else if (type.equals(BEERTYPE_TRADITIONAL_BOCK.toString()))
			return new RecipeReccomendedValues(1.064, 1.072, 1.013, 1.019, 20, 27, 14, 22, 6.3, 7.2);
		
		else if (type.equals(BEERTYPE_DOPPELBOCK.toString()))
			return new RecipeReccomendedValues(1.072, 1.112, 1.016, 1.024, 16, 26, 6, 25, 7, 10);
		
		else if (type.equals(BEERTYPE_EISBOCK.toString()))
			return new RecipeReccomendedValues(1.078, 1.120, 1.020, 1.035, 25, 35, 18, 30, 9, 14);
		
		else if (type.equals(BEERTYPE_CREAM_ALE.toString()))
			return new RecipeReccomendedValues(1.042, 1.055, 1.006, 1.012, 15, 20, 2.5, 5, 4.2, 5.6);
		
		else if (type.equals(BEERTYPE_BLONDE_ALE.toString()))
			return new RecipeReccomendedValues(1.038, 1.054, 1.008, 1.013, 15, 28, 3, 6, 3.8, 5.5);
		
		else if (type.equals(BEERTYPE_KOLSCH.toString()))
			return new RecipeReccomendedValues(1.044, 1.050, 1.007, 1.011, 20, 30, 3.5, 5, 4.4, 5.2);
		
		else if (type.equals(BEERTYPE_AMERICAN_RYE_WHEAT.toString()))
			return new RecipeReccomendedValues(1.040, 1.055, 1.008, 1.013, 15, 30, 3, 6, 4, 5.5);
		
		else if (type.equals(BEERTYPE_GERMAN_ALTBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.054, 1.010, 1.015, 25, 40, 13, 19, 4.5, 5.2);
		
		else if (type.equals(BEERTYPE_CALIF_COMMON.toString()))
			return new RecipeReccomendedValues(1.048, 1.054, 1.011, 1.014, 30, 45, 10, 14, 4.5, 5.5);
		
		else if (type.equals(BEERTYPE_DUSSELDORF_ALTBIER.toString()))
			return new RecipeReccomendedValues(1.046, 1.054, 1.010, 1.015, 35, 50, 11, 17, 4.5, 5.2);
		
		else if (type.equals(BEERTYPE_STANDARD_BITTER.toString()))
			return new RecipeReccomendedValues(1.032, 1.040, 1.007, 1.011, 25, 35, 4, 14, 3.2, 3.8);
		
		else if (type.equals(BEERTYPE_PREMIUM_BITTER.toString()))
			return new RecipeReccomendedValues(1.040, 1.048, 1.008, 1.012, 25, 40, 5, 16, 3.8, 4.6);
		
		else if (type.equals(BEERTYPE_STRONG_BITTER.toString()))
			return new RecipeReccomendedValues(1.048, 1.060, 1.010, 1.016, 30, 50, 6, 18, 4.6, 6.2);
		
		else if (type.equals(BEERTYPE_SCOTTISH_LIGHT.toString()))
			return new RecipeReccomendedValues(1.030, 1.035, 1.010, 1.013, 10, 20, 9, 17, 2.5, 3.2);
		
		else if (type.equals(BEERTYPE_SCOTTISH_HEAVY.toString()))
			return new RecipeReccomendedValues(1.035, 1.040, 1.010, 1.015, 10, 25, 9, 17, 3.2, 3.9);
		
		else if (type.equals(BEERTYPE_SCOTTISH_EXPORT.toString()))
			return new RecipeReccomendedValues(1.040, 1.054, 1.010, 1.016, 15, 30, 9, 17, 3.9, 5.0);
		
		else if (type.equals(BEERTYPE_IRISH_RED.toString()))
			return new RecipeReccomendedValues(1.044, 1.060, 1.010, 1.014, 17, 28, 9, 18, 4.0, 6.0);
		
		else if (type.equals(BEERTYPE_STRONG_SCOTCH_ALE.toString()))
			return new RecipeReccomendedValues(1.070, 1.130, 1.018, 1.056, 17, 35, 14, 25, 6.5, 10);
		
		else if (type.equals(BEERTYPE_AMERICAN_PALE_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.015, 30, 45, 5, 14, 4.5, 6.2);
		
		else if (type.equals(BEERTYPE_AMERICAN_AMBER_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.015, 25, 40, 10, 17, 4.5, 6.2);
		
		else if (type.equals(BEERTYPE_AMERICAN_BROWN_ALE.toString()))
			return new RecipeReccomendedValues(1.045, 1.060, 1.010, 1.016, 20, 40, 18, 35, 4.2, 6.2);
		
		else if (type.equals(BEERTYPE_ENGLISH_MILD_BROWN_ALE.toString()))
			return new RecipeReccomendedValues(1.030, 1.038, 1.008, 1.013, 10, 25, 12, 25, 2.8, 4.5);
		
		else if (type.equals(BEERTYPE_ENGLISH_SOUTHERN_BROWN.toString()))
			return new RecipeReccomendedValues(1.033, 1.042, 1.011, 1.014, 12, 20, 19, 35, 2.8, 4.1);
		
		else if (type.equals(BEERTYPE_ENGLISH_NORTHERN_BROWN.toString()))
			return new RecipeReccomendedValues(1.040, 1.052, 1.008, 1.013, 20, 30, 12, 22, 4.2, 5.4);
		
		else if (type.equals(BEERTYPE_BROWN_PORTER.toString()))
			return new RecipeReccomendedValues(1.040, 1.052, 1.008, 1.014, 18, 35, 20, 30, 4, 5.4);
		
		else if (type.equals(BEERTYPE_GERMAN_WHEAT.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_WEIZENBOCK.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_WITBIER.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_BELGIAN_ALE.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_SOUR_ALE.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_BELGIAN_STRONG_ALE.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_STRONG_ALE.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_FRUIT_BEER.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_SPICE_HERB_VEG.toString()))
			return new RecipeReccomendedValues();
		
		else if (type.equals(BEERTYPE_SPECIALTY.toString()))
			return new RecipeReccomendedValues();
		
		
		// If all else fails
		
		else
			return new RecipeReccomendedValues();
		
	}
}
