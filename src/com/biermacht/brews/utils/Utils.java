package com.biermacht.brews.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.biermacht.brews.database.RecipeDataSource;
import com.biermacht.brews.recipe.BeerStyle;
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
	public static BeerStyle BEERTYPE_OTHER = new BeerStyle("Other");
	public static BeerStyle BEERTYPE_STOUT = new BeerStyle("Stout");
	public static BeerStyle BEERTYPE_DRY_STOUT = new BeerStyle("Dry Stout");
	public static BeerStyle BEERTYPE_OATMEAL_STOUT = new BeerStyle("Oatmeal Stout");
	public static BeerStyle BEERTYPE_IMPERIAL_STOUT = new BeerStyle("Imperial Stout");
	public static BeerStyle BEERTYPE_HEFEWEIZEN = new BeerStyle("Hefeweizen");
	public static BeerStyle BEERTYPE_IPA = new BeerStyle("India Pale Ale");
	public static BeerStyle BEERTYPE_LIGHT_LAGER = new BeerStyle("Light Lager");
	public static BeerStyle BEERTYPE_PILSNER = new BeerStyle("Pilsner");
	public static BeerStyle BEERTYPE_EUROPEAN_AMBER_LAGER = new BeerStyle("European Amber Lager");
	public static BeerStyle BEERTYPE_DARK_LAGER = new BeerStyle("Dark Lager");
	public static BeerStyle BEERTYPE_BOCK = new BeerStyle("Bock");
	public static BeerStyle BEERTYPE_LIGHT_HYBRID = new BeerStyle("Light Hybrid Beer");
	public static BeerStyle BEERTYPE_AMBER_HYBRID = new BeerStyle("Amber Hybrid Beer");
	public static BeerStyle BEERTYPE_ENGLISH_PALE_ALE = new BeerStyle("English Pale Ale");
	public static BeerStyle BEERTYPE_SCOT_IRISH_ALE = new BeerStyle("Scottish and Irish Ale");
	public static BeerStyle BEERTYPE_AMERICAN_ALE = new BeerStyle("American Ale");
	public static BeerStyle BEERTYPE_ENGLISH_BROWN_ALE = new BeerStyle("English Brown Ale");
	public static BeerStyle BEERTYPE_PORTER = new BeerStyle("Porter");
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

	
// ====================================================================================
// ====================================================================================
	
	
	public static ArrayList<BeerStyle> getBeerStyleList()
	{
		ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
		
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
		
		Collections.sort(list, new BeerStyleComparator<BeerStyle>());
		list.add(0, BEERTYPE_OTHER);
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
	
	public static ArrayList<Recipe> getRecipeList(RecipeDataSource rds)
	{
		return rds.getRecipeList();
	}
}
