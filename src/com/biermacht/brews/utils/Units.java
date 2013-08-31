package com.biermacht.brews.utils;
import android.util.Log;

import com.biermacht.brews.frontend.MainActivity;

import java.util.*;

public class Units {
	
	// Unit systems
	public static final String IMPERIAL = "Imperial";
	public static final String METRIC = "Metric";
	
	// Imperial Units
	public static final String OUNCES = "oz";
	public static final String GALLONS = "gal";
	public static final String POUNDS = "lbs";
	public static final String TEASPOONS = "tsp";
	public static final String FAHRENHEIT = "\u2109";
	public static final String CUP = "Cup";
	public static final String CUPS = "Cups";
    public static final String QUARTS_PER_POUND = "qt/lb";
	
	// Metric Units
	public static final String KILOGRAMS = "kg";
	public static final String GRAMS = "grams";
	public static final String LITERS = "liters";
	public static final String MILLILITERS = "ml";
	public static final String CELSIUS = "\u2103";
    public static final String LITERS_PER_KG = "L/kg";
	
	// Agnostic Units
	public static final String PACKAGES = "pkg";
	public static final String ITEMS = "items";
	public static final String DAYS = "days";
	public static final String MINUTES = "mins";
	public static final String HOURS = "hours";
    public static final String PLATO = "plato";
    public static final String GRAVITY = "sg";
	
	// Helper funcs
	public static String getUnitsFromDisplayAmount(String s)
	{
        Log.d("Units", "Getting units from display amount: " + s);
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
        if (unit.equalsIgnoreCase(PLATO))
            unit = Units.PLATO;
        if (unit.equalsIgnoreCase(GRAVITY))
            unit = Units.GRAVITY;

        Log.d("Units", "Got units: " + unit);
		return unit;
	}
	
	public static double getAmountFromDisplayAmount(String s)
	{
        Log.d("Units", "Getting amount from display amount: " + s);
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(s.split(" ")));
		if (getUnitsFromDisplayAmount(s).equals(""))
			return 0;
		else
		    if (temp.size() <= 2 && temp.size() > 0)
            {
                try
                {
                    Log.d("Units", "Returning amount: " + Double.parseDouble(temp.get(0)));
			        return Double.parseDouble(temp.get(0));
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("Units", "Error parsing display amount: " + s);
                }
            }
            else
            {
                Log.d("Units", "Received bad display amount: " + s);
            }
        Log.d("Units", "Failed to get amount. Returning 0");
		return 0;
	}

    public static int toSeconds(double time, String units)
    {
        if (units.equals(MINUTES))
            return (int) (time * 60);
        if (units.equals(HOURS))
            return (int) (time * 3600);

        return 0;
    }
	
	// Functions for converting units below
    public static double platoToGravity(double p)
    {
        return p/(258.6-((p/258.2)*227.1))+1;
    }

	public static double fahrenheitToCelsius(double f)
	{
		return (f - 32)/1.8;
	}
	
	public static double celsiusToFahrenheit(double c)
	{
		return c*1.8 + 32;
	}

    public static double LPKGtoQPLB(double d)
    {
        return d * .479305709;
    }

    public static double QPLBtoLPKG(double d)
    {
        return d / .479305709;
    }

	public static double litersToGallons(double l)
	{
		return l * .264172052;
	}
	
	public static double cupsToLiters(double c)
	{
		return c * .236;
	}
	
	public static double litersToCups(double l)
	{
		return l / .236;
	}

    public static double litersToMillis(double l)
    {
        return l * 1000;
    }

    public static double millisToLiters(double m)
    {
        return m / 1000;
    }
	
	public static double minutesToDays(double m)
	{
		return m / 1440;
	}

    public static double minutesToHours(double m)
    {
        return m /60;
    }
	
	public static double daysToMinutes(double d)
	{
		return d * 1440;
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

    // Methods to return the units for each measurement system
    public static String getUnitSystem()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return IMPERIAL;
        else
            return METRIC;
    }
    public static String getHopUnits()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return OUNCES;
        else
            return GRAMS;
    }

    public static String getFermentableUnits()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return POUNDS;
        else
            return KILOGRAMS;
    }

    public static String getTemperatureUnits()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return FAHRENHEIT;
        else
            return CELSIUS;
    }

    public static String getVolumeUnits()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return GALLONS;
        else
            return LITERS;
    }

    public static String getWeightUnits()
    {
        if (MainActivity.preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL).equals(IMPERIAL))
            return POUNDS;
        else
            return KILOGRAMS;
    }

    public static String getMetricEquivalent(String imp, boolean isWeight)
    {
        if (imp.equals(Units.GALLONS))
            return LITERS;
        else if (imp.equals(Units.TEASPOONS))
            return MILLILITERS;
        else if (imp.equals(Units.OUNCES))
            if (isWeight)
                return GRAMS;
            else
                return MILLILITERS;
        else if (imp.equals(Units.POUNDS))
            return KILOGRAMS;
        else if (imp.equals(Units.CUP) || imp.equals(Units.CUPS))
            return LITERS;
        else
            return imp;
    }
}
