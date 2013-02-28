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
import com.biermacht.brews.xml.HopsHandler;
import com.biermacht.brews.xml.YeastHandler;

public class IngredientHandler {
	
	private Context mContext;
	private ArrayList<Ingredient> fermentablesList;
	private ArrayList<Ingredient> yeastsList;
	private ArrayList<Ingredient> hopsList;
	
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
	 * Returns a list of valid yeasts for use in recipes
	 * @return ArrayList of ingredient objects
	 */
	public ArrayList<Ingredient> getHopsList()
	{
		if (this.hopsList == null)
		{
			try 
			{
				this.hopsList = getHopsFromXml();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return hopsList;
	}
	
	/**
	 * Gets fermentables from XMl files in assets/Fermentables
	 * Some fermentables from.. http://byo.com/resources/grains
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
	 * Gets yeasts from XMl files in assets/Yeasts
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
	
	/**
	 * Gets hops from XMl files in assets/Hops
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	private ArrayList<Ingredient> getHopsFromXml() throws IOException
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        HopsHandler myXMLHandler = new HopsHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Hops") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Hops/" + s);
	            sp.parse(is, myXMLHandler);
	 
	            Hop hop = myXMLHandler.getHop();
	            Log.e("IngredientHandler", "Getting ingredient: " + hop.getName());
	            list.add(hop);
	        }
	        catch (Exception e)
	        {
	        	Log.e("IngredientHandler", e.toString());
	        }
        }
        
        return list;
	}
	
}
