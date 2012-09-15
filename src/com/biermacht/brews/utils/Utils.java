package com.biermacht.brews.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.biermacht.brews.database.RecipeDataSource;
import com.biermacht.brews.recipe.Recipe;

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
	
	// Beer types
	public static String BEERTYPE_OTHER = "Other";
	public static String BEERTYPE_STOUT = "Stout";
	public static String BEERTYPE_DRY_STOUT = "Dry Stout";
	public static String BEERTYPE_OATMEAL_STOUT = "Oatmeal Stout";
	public static String BEERTYPE_IMPERIAL_STOUT = "Imperial Stout";
	public static String BEERTYPE_HEFEWEIZEN = "Hefeweizen";
	public static String BEERTYPE_IPA = "India Pale Ale";
	public static String BEERTYPE_LIGHT_LAGER = "Light Lager";
	public static String BEERTYPE_PILSNER = "Pilsner";
	public static String BEERTYPE_EUROPEAN_AMBER_LAGER = "European Amber Lager";
	public static String BEERTYPE_DARK_LAGER = "Dark Lager";
	public static String BEERTYPE_BOCK = "Bock";
	public static String BEERTYPE_LIGHT_HYBRID = "Light Hybrid Beer";
	public static String BEERTYPE_AMBER_HYBRID = "Amber Hybrid Beer";
	public static String BEERTYPE_ENGLISH_PALE_ALE = "English Pale Ale";
	public static String BEERTYPE_SCOT_IRISH_ALE = "Scottish and Irish Ale";
	public static String BEERTYPE_AMERICAN_ALE = "American Ale";
	public static String BEERTYPE_ENGLISH_BROWN_ALE = "English Brown Ale";
	public static String BEERTYPE_PORTER = "Porter";
	public static String BEERTYPE_GERMAN_WHEAT = "German Wheat";
	public static String BEERTYPE_WEIZENBOCK = "Weizenbock";
	public static String BEERTYPE_WITBIER = "Witbier";
	public static String BEERTYPE_BELGIAN_ALE = "Belgian Ale";
	public static String BEERTYPE_SOUR_ALE = "Sour Ale";
	public static String BEERTYPE_BELGIAN_STRONG_ALE = "Belgian Strong Ale";
	public static String BEERTYPE_STRONG_ALE = "Strong Ale";
	public static String BEERTYPE_FRUIT_BEER = "Fruit Beer";
	public static String BEERTYPE_SPICE_HERB_VEG = "Spice/Herb/Vegetable Beer";
	public static String BEERTYPE_SPECIALTY = "Specialty Beer";

	
// ====================================================================================
// ====================================================================================
	
	
	public static ArrayList<String> getBeerTypeList()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(BEERTYPE_HEFEWEIZEN);
		list.add(BEERTYPE_STOUT);
		list.add(BEERTYPE_IPA);
		list.add(BEERTYPE_LIGHT_LAGER);
		list.add(BEERTYPE_PILSNER);
		list.add(BEERTYPE_EUROPEAN_AMBER_LAGER);
		list.add(BEERTYPE_DARK_LAGER);
		list.add(BEERTYPE_BOCK);
		list.add(BEERTYPE_LIGHT_HYBRID);
		list.add(BEERTYPE_AMBER_HYBRID);
		list.add(BEERTYPE_ENGLISH_PALE_ALE);
		list.add(BEERTYPE_SCOT_IRISH_ALE);
		list.add(BEERTYPE_AMERICAN_ALE);
		list.add(BEERTYPE_ENGLISH_BROWN_ALE);
		list.add(BEERTYPE_PORTER);
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
		
		Collections.sort(list);
		list.add(0, BEERTYPE_OTHER);
		return list;
	}
	
	public static ArrayList<Recipe> getRecipeList(RecipeDataSource rds)
	{
		return rds.getRecipeList();
	}
}
