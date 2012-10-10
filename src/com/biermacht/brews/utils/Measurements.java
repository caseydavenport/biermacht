package com.biermacht.brews.utils;

public class Measurements {
	
	// Static values =================================================
	// ===============================================================
	public static final String METRIC = "Metric";
	public static final String IMPERIAL = "Imperial";
	public static final String KILOGRAMS = "kg";
	public static final String POUNDS = "lbs";
	public static final String OUNCES = "oz";
	
	// Fields ========================================================
	// ===============================================================
	private static String measurementSystem = IMPERIAL;
	
	
	/**
	 * Set the type of units. Can be one of Settings.METRIC or Settings.IMPERIAL
	 * @param s
	 */
	public static void setMeasurementSystem(String s)
	{
		Measurements.measurementSystem = s;
	}
	
	/**
	 * Determine the type of units chosen by the user
	 * @return true if metric units, false if imperial
	 */
	public static boolean useMetric()
	{
		if (measurementSystem.equals(METRIC))
			return true;
		else
			return false;
	}

}
