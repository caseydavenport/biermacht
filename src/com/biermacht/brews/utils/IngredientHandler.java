package com.biermacht.brews.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.comparators.IngredientComparator;
import com.biermacht.brews.xml.FermentableHandler;
import com.biermacht.brews.xml.YeastHandler;

public class IngredientHandler {
	
	private Context mContext;
	private ArrayList<Ingredient> fermentablesList;
	private ArrayList<Ingredient> yeastsList;
	
	public IngredientHandler(Context c)
	{
		this.mContext = c;
	}
	
	/**
	 * Returns a list of valid fermentables for use in recipes
	 * @return ArrayList of ingredient objects
	 */
	public ArrayList<Ingredient> getFermentablesList()
	{
		if (this.fermentablesList == null)
		{
			try 
			{
				this.fermentablesList = getFermentablesFromXml();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return fermentablesList;
	}
	
	/**
	 * Returns a list of valid yeasts for use in recipes
	 * @return ArrayList of ingredient objects
	 */
	public ArrayList<Ingredient> getYeastsList()
	{
		if (this.yeastsList == null)
		{
			try 
			{
				this.yeastsList = getYeastsFromXml();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return yeastsList;
	}
	
	/**
	 * Gets fermentables from XMl files in assets/Fermentables
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	private ArrayList<Ingredient> getFermentablesFromXml() throws IOException
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        FermentableHandler myXMLHandler = new FermentableHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Fermentables") )
        {
	        try 
	        {
	        	Log.e("IngrdientHandler", "File Path: " + s);
	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Fermentables/" + s);
	            sp.parse(is, myXMLHandler);
	 
	            Fermentable fermentable = myXMLHandler.getFermentable();
	            list.add(fermentable);
	        }
	        catch (Exception e)
	        {
	        	Log.e("IngredientHandler", e.toString());
	        }
        }
        
        return list;
	}
	
	/**
	 * Gets fermentables from XMl files in assets/Fermentables
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	private ArrayList<Ingredient> getYeastsFromXml() throws IOException
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        YeastHandler myXMLHandler = new YeastHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Yeasts") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Yeasts/" + s);
	            sp.parse(is, myXMLHandler);
	 
	            Yeast yeast = myXMLHandler.getYeast();
	            Log.e("IngredientHandler", "Getting ingredient: " + yeast.getName());
	            list.add(yeast);
	        }
	        catch (Exception e)
	        {
	        	Log.e("IngredientHandler", e.toString());
	        }
        }
        
        return list;
	}
	
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	 
	
	
	// Fermentables.. http://byo.com/resources/grains
	public static void getFermentablesList2()
	{
		// List to return
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		
		Fermentable FERMENTABLE_ROASTED_BARELY = new Fermentable("Roasted Barley");
						FERMENTABLE_ROASTED_BARELY.setLovibondColor(300);
						FERMENTABLE_ROASTED_BARELY.setGravity(1.025);
						FERMENTABLE_ROASTED_BARELY.setFermentableType(Fermentable.GRAIN);
						
		Fermentable FERMENTABLE_VICTORY_MALT = new Fermentable("Victory Malt");
						FERMENTABLE_VICTORY_MALT.setLovibondColor(25);
						FERMENTABLE_VICTORY_MALT.setGravity(1.034);
						FERMENTABLE_VICTORY_MALT.setFermentableType(Fermentable.GRAIN); // TODO
						list.add(FERMENTABLE_VICTORY_MALT);
		
		Fermentable FERMENTABLE_VIENNA_MALT = new Fermentable("Vienna Malt");
						FERMENTABLE_VIENNA_MALT.setLovibondColor(3.7);
						FERMENTABLE_VIENNA_MALT.setGravity(1.035);
						FERMENTABLE_VIENNA_MALT.setFermentableType(Fermentable.GRAIN); // TODO
						list.add(FERMENTABLE_VIENNA_MALT);
		
		Fermentable FERMENTABLE_WHEAT_MALT = new Fermentable("Wheat Malt");
						FERMENTABLE_WHEAT_MALT.setLovibondColor(2);
						FERMENTABLE_WHEAT_MALT.setGravity(1.038);
						FERMENTABLE_WHEAT_MALT.setFermentableType(Fermentable.GRAIN); //TODO
						list.add(FERMENTABLE_WHEAT_MALT);
		
		Fermentable FERMENTABLE_WHITE_WHEAT_MALT = new Fermentable("White Wheat Malt");
						FERMENTABLE_WHITE_WHEAT_MALT.setLovibondColor(2);
						FERMENTABLE_WHITE_WHEAT_MALT.setGravity(1.037);
						FERMENTABLE_WHITE_WHEAT_MALT.setFermentableType(Fermentable.GRAIN); // TODO
						list.add(FERMENTABLE_WHITE_WHEAT_MALT);
		 
		Collections.sort(list, new IngredientComparator<Ingredient>());
		
	}
	
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	
	
	public static ArrayList<Ingredient> getHopsList()
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		// Hops.. http://byo.com/resources/hops
		 Ingredient HOP_OTHER = new Hop("Custom Hop", 6, "Custom added hop");
		 Ingredient HOP_AHTANUM = new Hop("Ahtanum", 5, "Floral, citrus, sharp, piney");
		 Ingredient HOP_AMARILLO = new Hop("Amarillo", 8.5, "Citrus, flowery");
		 Ingredient HOP_APOLLO = new Hop("Apollo", 17, "High alpha acid content, disease resistant");
		 Ingredient HOP_BOADICEA = new Hop("Boadicea", 8.5, "Spicy");
		 Ingredient HOP_BRAVO = new Hop("Bravo", 16, "High alpha acid content, disease resistant");
		 Ingredient HOP_CASCADE_US = new Hop("Cascade (US)", 5.5, "Flowery, citrusy.  Can have a grapefruit flavor");
		 Ingredient HOP_CASCADE_NZ = new Hop("Cascade (NZ)", 7, "Flowery, citrusy.  Can have a grapefruit flavor");
		 Ingredient HOP_CENTENNIAL = new Hop("Centennial", 9.75, "Medium floral and citrus tones");
		 Ingredient HOP_CHINOOK = new Hop("Chinook", 12, "Medium strength, spicy, piney");
		 Ingredient HOP_CITRA = new Hop("Citra", 12, "Intense Flavor");
		 Ingredient HOP_CLUSTER = new Hop("Cluster", 7.5, "Quite spicy");
		 Ingredient HOP_COLUMBUS = new Hop("Columbus", 13.5, "Pleasant, strong aroma");
		 Ingredient HOP_EL_DORADO = new Hop("El Dorado (US)", 15, "Candy-like");
		 Ingredient HOP_GALAXY = new Hop("Galaxy (AUS)", 14, "");
		 Ingredient HOP_GALENA = new Hop("Galena", 12, "Medium strength, citrusy");
		 Ingredient HOP_GLACIER = new Hop("Glacier (US)", 7, "Earthy, Citrus");
		 Ingredient HOP_HORIZON = new Hop("Horizon", 12.5, "Pleasantly hoppy");
		 Ingredient HOP_MAGNUM = new Hop("Magnum", 14, "Bitter");
		 Ingredient HOP_NELSON_SAUVIN = new Hop("Nelson Sauvin", 13, "Grape-like flavor");
		 Ingredient HOP_NEWPORT = new Hop("Newport", 15, "Pungent");
		 Ingredient HOP_NORTHERN_BREWER_GER = new Hop("Northern Brewer (GER)", 8.5, "Medium strength");
		 Ingredient HOP_NORTHERN_BREWER_US = new Hop("Northern Brewer (US)", 8, "Medium strength");
		 Ingredient HOP_NW_GOLDING = new Hop("Northwest Golding", 7, "Earthy, Citrus");
		 Ingredient HOP_NUGGET = new Hop("Nugget", 12.75, "Heavy and herbal");
		 Ingredient HOP_OLYMPIC = new Hop("Olympic", 12, "Citrusy, spicy");
		 Ingredient HOP_OPAL_GER = new Hop("Opal (GER)", 6.5, "dual-purpose");
		 Ingredient HOP_PACIFIC_GEM_NZ = new Hop("Pacific Gem (NZ)", 14, "Woody");
		 Ingredient HOP_PACIFIC_JADE_NZ = new Hop("Pacific Jade (NZ)", 13, "Spicy, citrusy");
		 Ingredient HOP_PALISADE = new Hop("Palisade", 6.5, " ");
		 Ingredient HOP_PERLE_GER = new Hop("Perle (GER)", 7.25, "Fruity, mild spice");
		 Ingredient HOP_PERLE_US = new Hop("Perle (US)", 7.75, "Aromatic, bitter");
		 Ingredient HOP_PILGRIM_UK = new Hop("Pilgrim (UK)", 11, " ");
		 Ingredient HOP_PR_OF_RINGWOOD_AUS = new Hop("Pride of Ringwood (AUS)", 8.5, "Woody, earthy, herbal");
		 Ingredient HOP_RIWAKA_NZ = new Hop("Riwaka (NZ)", 5.5, "Citrusy, grapefruit aroma");
		 Ingredient HOP_SANTIAM = new Hop("Santiam (GER)", 6.5, "Noble");
		 Ingredient HOP_SATUS = new Hop("Satus (GER)", 13.25, "Bitter, aromatic");
		 Ingredient HOP_SIMCOE = new Hop("Simcoe (GER)", 13, "Bitter, aromatic");
		 Ingredient HOP_SORACHI_ACE = new Hop("Sorachi Ace (JAP)", 14.5, "Bitter, lemon aroma");
		 Ingredient HOP_SOVEREIGN = new Hop("Sovereign (UK)", 5.5, "Mild");
		 Ingredient HOP_SUMMIT = new Hop("Summit", 17, "Ultra bitter");
		 Ingredient HOP_SUN = new Hop("Sun", 14, "Intense character");
		 Ingredient HOP_SUPER_ALPHA = new Hop("Super Alpha (NZ))", 11, "Earthy, pineyc");
		 Ingredient HOP_SUPER_PRIDE = new Hop("Super Pride (AUS)", 14, "Bitter");
		 Ingredient HOP_TARGET = new Hop("Target (UK)", 11, "Intense");
		 Ingredient HOP_TOMAHAWK = new Hop("Tomahawk", 16, "Bitter");
		 Ingredient HOP_WARRIOR = new Hop("Warrior", 16, "Bitter, aromatic");
		 Ingredient HOP_WILLAMETTE = new Hop("Willamette", 4.5, "Mild, spicy, fruity, floral");
		 Ingredient HOP_YAKIMA_CLUSTER = new Hop("Yakima Cluster", 7.25, "kettle hop for bittering");
		 Ingredient HOP_ZEUS = new Hop("Zeus", 15, "Pleasant, aromatic");
		
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
		list.add(HOP_HORIZON);
		list.add(HOP_MAGNUM);
		list.add(HOP_NELSON_SAUVIN);
		list.add(HOP_NEWPORT);
		list.add(HOP_NORTHERN_BREWER_GER);
		list.add(HOP_NORTHERN_BREWER_US);
		list.add(HOP_NW_GOLDING);
		list.add(HOP_NUGGET);
		list.add(HOP_OLYMPIC);
		list.add(HOP_OPAL_GER);
		list.add(HOP_PACIFIC_GEM_NZ);
		list.add(HOP_PACIFIC_JADE_NZ);
		list.add(HOP_PALISADE);
		list.add(HOP_PERLE_GER);
		list.add(HOP_PERLE_US);
		list.add(HOP_PILGRIM_UK);
		list.add(HOP_PR_OF_RINGWOOD_AUS);
		list.add(HOP_RIWAKA_NZ);
		list.add(HOP_SANTIAM);
		list.add(HOP_SATUS);
		list.add(HOP_SIMCOE);
		list.add(HOP_SORACHI_ACE);
		list.add(HOP_SOVEREIGN);
		list.add(HOP_SUMMIT);
		list.add(HOP_SUN);
		list.add(HOP_SUPER_ALPHA);
		list.add(HOP_SUPER_PRIDE);
		list.add(HOP_TARGET);
		list.add(HOP_TOMAHAWK);
		list.add(HOP_WARRIOR);
		list.add(HOP_WILLAMETTE);
		list.add(HOP_YAKIMA_CLUSTER);
		list.add(HOP_ZEUS); 
		
		Collections.sort(list, new IngredientComparator<Ingredient>());
		list.add(0, HOP_OTHER);
	
		return list;
	}
}
