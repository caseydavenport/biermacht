package com.biermacht.brews.utils;

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
	
	// Metric Units
	public static final String KILOGRAMS = "kg";
	public static final String GRAMS = "grams";
	public static final String LITERS = "liters";
	public static final String MILLILITERS = "ml";
	
	// Agnostic Units
	public static final String PACKAGES = "pkg";
	
	// Functions for converting units below
	public static double litersToGallons(double l)
	{
		return l * .264172052;
	}
	
	public static double gallonsToLiters(double g)
	{
		return g * 3.87541178;
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

	public static double ouncesToGrams(double o)
	{
		return o * 28.3495231;
	}
	
	public static double gramsToOunces(double g)
	{
		return g / 28.3495231;
	}
}
