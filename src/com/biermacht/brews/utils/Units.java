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
	
	// Agnostic Units

}
