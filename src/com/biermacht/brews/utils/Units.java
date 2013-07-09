package com.biermacht.brews.utils;
import java.util.*;

public class Units {
	
	// ========================== UNITS POLICY!! =========================
	//
	//  For the sake of not having to redo a whole bunch of code, we will
	// 	just convert at the interfaces.  The following units will be used for
	// 	storage within the database and various java objects in use.
	//
	//  Fermentables - POUNDS
	//  Yeasts - LITERS
	//  Hops - OUNCES
	//  Recipe Vol - GALLONS
	//
	//	Units will be converted before assignment to java objects
	// ===================================================================
	
	// Unit systems
	public static final String IMPERIAL = "Imperial";
	public static final String METRIC = "Metric";
	
	// Imperial Units
	public static final String OUNCES = "oz";
	public static final String GALLONS = "gal";
	public static final String POUNDS = "lbs";
	public static final String TEASPOONS = "tsp";
	
	// Metric Units
	public static final String KILOGRAMS = "kg";
	public static final String GRAMS = "grams";
	public static final String LITERS = "liters";
	public static final String MILLILITERS = "ml";
	
	// Agnostic Units
	public static final String PACKAGES = "pkg";
	public static final String ITEMS = "items";
	
	// Helper funcs
	public static String getUnitsFromDisplayAmount(String s)
	{
		String unit = "";
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(s.split(" ")));
		if (temp.size() == 2)
			unit = temp.get(temp.size() - 1);
			
		// Assign a Units class standard unit here
		if (unit.equals("tsp") || unit.equals("teaspoons"))
			unit = Units.TEASPOONS;
		if (unit.equals("g") || unit.equals("grams"))
			unit = Units.GRAMS;
		if (unit.equals("oz") || unit.equals("ounces"))
			unit = Units.OUNCES;
		if (unit.contains("gal"))
			unit = Units.GALLONS;
		if (unit.equals("item") || unit.equals("items"))
			unit = Units.ITEMS;
		if (unit.contains("package") || unit.equals("pkg"))
			unit = Units.PACKAGES;
			
		return unit;
	}
	
	public static double getAmountFromDisplayAmount(String s)
	{
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(s.split(" ")));
		if (getUnitsFromDisplayAmount(s).equals(""))
			return 0;
		else
		    if (temp.size() == 2)
			    return Double.parseDouble(temp.get(0));
		return 0;
	}
	
	// Functions for converting units below
	public static double litersToGallons(double l)
	{
		return l * .264172052;
	}
	
	public static double gallonsToLiters(double g)
	{
		return g / .264172052;
	}
	
	public static double litersToOunces(double l)
	{
		return l / .0295735;
	}
	
	public static double ouncesToLiters(double o)
	{
		return o * .0295735;
	}
	
	public static double litersToTeaspoons(double l)
	{
		return l * 202.884;
	}
	
	public static double teaspoonsToLiters(double t)
	{
		return t / 202.884;
	}
	
	public static double kilosToPounds(double k)
	{
		return k * 2.204;
	}
	
	public static double kilosToOunces(double k)
	{
		return k * 35.2739619;
	}
	
	public static double ouncesToKilos(double o)
	{
		return o / 35.2739619;
	}
	
	public static double poundsToKilos(double p)
	{
		return p / 2.204;
	}
	
	public static double kilosToGrams(double k)
	{
		return k * 1000;
	}
	
	public static double gramsToKilos(double g)
	{
		return g / 1000;
	}

	public static double ouncesToGrams(double o)
	{
		return o * 28.3495231;
	}
	
	public static double gramsToOunces(double g)
	{
		return g / 28.3495231;
	}
}
