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

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.xml.RecipeHandler;
import com.biermacht.brews.recipe.*;
import android.content.*;
import android.net.*;
import com.biermacht.brews.utils.comparators.*;

public class IngredientHandler {
	
	private Context mContext;
	private ArrayList<Ingredient> fermentablesList;
	private ArrayList<Ingredient> yeastsList;
	private ArrayList<Ingredient> hopsList;
	private ArrayList<Ingredient> miscsList;
	private ArrayList<BeerStyle> styleList;
	private ArrayList<MashProfile> profileList;
	
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
		
		return this.fermentablesList;
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
		
		return this.yeastsList;
	}
	
	/**
	 * Returns a list of valid hops for use in recipes
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
		
		return this.hopsList;
	}
	
	/**
	 * Returns a list of valid miscs for use in recipes
	 * @return ArrayList of ingredient objects
	 */
	public ArrayList<Ingredient> getMiscsList()
	{
		if (this.miscsList == null)
		{
			try 
			{
				this.miscsList = getMiscsFromXml();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this.miscsList;
	}

	/**
	 * Returns a list of valid styles for use in recipes
	 * @return ArrayList of style objects
	 */
	public ArrayList<BeerStyle> getStylesList()
	{
		if (this.styleList == null)
		{
			try 
			{
				this.styleList = getStylesFromXml();
				this.styleList.add(0, Utils.BEERSTYLE_OTHER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return styleList;
	}
	
	/**
	 * Returns a list of valid hops for use in recipes
	 * @return ArrayList of MashProfiles
	 */
	public ArrayList<MashProfile> getMashProfileList()
	{
		if (this.profileList == null)
		{
			try 
			{
				this.profileList = getProfilesFromXml();
				this.profileList.add(0, new MashProfile());
				for (MashProfile p : profileList)
				{
					Log.d("IngredientHandler", p.getName());
				}
				//this.profileList.add(0, Utils.BEERSTYLE_OTHER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return profileList;
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
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Fermentables") )
        {
	        try 
	        {
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Fermentables/" + s);
	            sp.parse(is, myXMLHandler);
	 
	            list.addAll(myXMLHandler.getFermentables());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getFermentablesFromXml", e.toString());
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
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Yeasts") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Yeasts/" + s);
	            sp.parse(is, myXMLHandler);
				
	            list.addAll(myXMLHandler.getYeasts());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getYeastsFromXml", e.toString());
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
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Hops") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Hops/" + s);
	            sp.parse(is, myXMLHandler);
	 
	            list.addAll(myXMLHandler.getHops());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getHopsFromXml", e.toString());
	        }
        }
        
        return list;
	}
	
	/**
	 * Gets hops from XMl files in assets/Hops
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	private ArrayList<BeerStyle> getStylesFromXml() throws IOException
	{
		ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();
		
        for (String s : am.list("Styles") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Styles/" + s);
	            sp.parse(is, myXMLHandler);
				
		        list.addAll(myXMLHandler.getBeerStyles());
				Collections.sort(list, new BeerStyleComparator<BeerStyle>());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getStylesFromXml", e.toString());
	        }
        }

        return list;
	}
	
	/**
	 * Gets miscs from XMl files in assets/Hops
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	private ArrayList<Ingredient> getMiscsFromXml() throws IOException
	{
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Miscs") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Miscs/" + s);
	            sp.parse(is, myXMLHandler);

	            list.addAll(myXMLHandler.getMiscs());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getMiscsFromXml", e.toString());
	        }
        }

        return list;
	}
	
	/**
	 * Gets Recipes from XMl file passed in path s
	 * @return ArrayList of Ingredient Objects
	 * @throws IOException
	 */
	public ArrayList<Recipe> getRecipesFromXml(String s) throws IOException
	{
		ArrayList<Recipe> retlist = new ArrayList<Recipe>();
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
		
        try 
	    {	
			ContentResolver cr = mContext.getContentResolver();
	        SAXParser sp = spf.newSAXParser();
	        InputStream is = cr.openInputStream((Uri.parse("file://" + s)));
	        sp.parse(is, myXMLHandler);

	        ArrayList<Recipe> list = myXMLHandler.getRecipes();
	        retlist.addAll(list);

	     }
	     catch (Exception e)
	     {
	        Log.e("getRecipesFromXml", e.toString());
	     }

        return retlist;
	}	
	
	/**
	 * Gets MashProfiles from XMl files in assets/Profiles
	 * @return ArrayList of MashProfile Objects
	 * @throws IOException
	 */
	private ArrayList<MashProfile> getProfilesFromXml() throws IOException
	{
		ArrayList<MashProfile> list = new ArrayList<MashProfile>();
        RecipeHandler myXMLHandler = new RecipeHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AssetManager am = mContext.getAssets();

        for (String s : am.list("Profiles") )
        {
	        try 
	        {	        	
	            SAXParser sp = spf.newSAXParser();
	            InputStream is = am.open("Profiles/" + s);
	            sp.parse(is, myXMLHandler);

	            list.addAll(myXMLHandler.getMashProfiles());
	        }
	        catch (Exception e)
	        {
	        	Log.e("getProfilesFromXml", e.toString());
	        }
        }
        return list;
	}
}
