package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.*;
import java.util.*;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.utils.*;

public class RecipeHandler extends DefaultHandler {

	// Types of things
	private static int RECIPES = 1;
	private static int RECIPE = 2;
	private static int HOPS = 3;
	private static int FERMENTABLES = 4;
	private static int HOP = 5;
	private static int FERMENTABLE = 6;
	private static int YEAST = 7;
	private static int YEASTS = 8;
	private static int MISCS = 9;
	private static int MISC = 10;
	private static int STYLE = 11;
	private static int STYLES = 12;
	private static int PROFILE = 13;
	private static int PROFILES = 14;
	private static int MASH_STEP = 15;
	private static int MASH_STEPS = 16;
	
	// Hold the current elements
    boolean currentElement = false;
    String currentValue = null;

	// Lists to store all the things
	ArrayList<Recipe> list = new ArrayList<Recipe>();
	ArrayList<Ingredient> fermList = new ArrayList<Ingredient>();
	ArrayList<Ingredient> hopList = new ArrayList<Ingredient>();	
	ArrayList<Ingredient> yeastList = new ArrayList<Ingredient>();
	ArrayList<Ingredient> miscList = new ArrayList<Ingredient>();
	ArrayList<BeerStyle> beerStyleList = new ArrayList<BeerStyle>();
	ArrayList<MashProfile> mashProfileList = new ArrayList<MashProfile>();
	ArrayList<MashStep> mashStepList = new ArrayList<MashStep>();

	// Objects for each type of thing
	Recipe r = new Recipe("");
    Fermentable f = new Fermentable("");
	Hop h = new Hop("");
	Yeast y = new Yeast("");
	Misc misc = new Misc("");
	BeerStyle style = new BeerStyle("");
	MashProfile profile = new MashProfile();
	MashStep mashStep = new MashStep();
	
	// How we know what thing we're looking at
	int thingType = 0;

	/**
	* The return methods that will return lists of all of the elements
	* that have been parsed in the current file.
	*/
    public ArrayList<Recipe> getRecipes() {
        return list;
    }
	
	public ArrayList<Ingredient> getFermentables(){
		return fermList;
	}
	
	public ArrayList<Ingredient> getHops() {
		return hopList;
	}
	
	public ArrayList<Ingredient> getYeasts() {
		return yeastList;
	}
	
	public ArrayList<Ingredient> getMiscs() {
		return miscList;
	}

	public ArrayList<BeerStyle> getBeerStyles()
	{
		return beerStyleList;
	}
	
	public ArrayList<MashProfile> getMashProfiles()
	{
		return mashProfileList;
	}
	
	public ArrayList<MashStep> getMashSteps()
	{
		return mashStepList;
	}
	
	/**
	* This gets called whenever we encounter a new start element.  In this function
	* we create the new object to be populated and set the type of what we are looking at
	* so we can properly parse the following tags
	*/
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;

		// We encounter a new recipe
        if (qName.equalsIgnoreCase("RECIPES"))
        {
			thingType = RECIPES;
        }
		
		// We encounter a new recipe
		if (qName.equalsIgnoreCase("RECIPE"))
		{
			thingType = RECIPE;
			r = new Recipe("");
		}
		
		// We encounter a new fermentables list
		if (qName.equalsIgnoreCase("FERMENTABLES"))
		{
			thingType = FERMENTABLES;
		}
		
		// We encounter a new hops list
		if (qName.equalsIgnoreCase("HOPS"))
		{
			thingType = HOPS;
		}
		
		// We encounter a new hop
		if (qName.equalsIgnoreCase("HOP"))
		{
			thingType = HOP;
			h = new Hop("");
		}
		// We encounter a new fermentable
		if (qName.equalsIgnoreCase("FERMENTABLE"))
		{
			thingType = FERMENTABLE;
			f = new Fermentable("");
		}
		// We encounter a new yeast list
		if (qName.equalsIgnoreCase("YEASTS"))
		{
			thingType = YEASTS;
		}

		// We encounter a new yeast
		if (qName.equalsIgnoreCase("YEAST"))
		{
			thingType = YEAST;
			y = new Yeast("");
		}
		
		// Encounter new misc
		if (qName.equalsIgnoreCase("MISC"))
		{
			thingType = MISC;
			misc = new Misc("");
		}
		
		// Encounter new miscs list
		if (qName.equalsIgnoreCase("MISCS"))
		{
			thingType = MISCS;
		}
		
		// Encounter new style
		if (qName.equalsIgnoreCase("STYLE"))
		{
			thingType = STYLE;
			style = new BeerStyle("");
		}
		
		// Encounter new style list
		if (qName.equalsIgnoreCase("STYLES"))
		{
			thingType = STYLES;
		}
		
		// Encounter new mash profile list
		if (qName.equalsIgnoreCase("MASHS"))
		{
			thingType = PROFILES;
		}
		
		// Encounter new mash profile
		if (qName.equalsIgnoreCase("MASH"))
		{
			thingType = PROFILE;
		}
		
		// Encounter new mash step
		if (qName.equalsIgnoreCase("MASH_STEP"))
		{
			thingType = MASH_STEP;
		}
		
		// Encounter new mash step list
		if (qName.equalsIgnoreCase("MASH_STEPS"))
		{
			thingType = MASH_STEPS;
		}
		
    }

	/**
	* This gets called when we run into an end element.  The value of the element is stored in
	* the variable 'currentValue' and is assigned appropriately based on thingType.
	*/
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;

		if (qName.equalsIgnoreCase("RECIPE"))
		// We've finished a new recipe
		{
			list.add(r);
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("HOPS"))
		// We have finished a list of hops.
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("FERMENTABLES"))
		// We have finished a list of fermentables
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("YEASTS"))
		// We have finished a list of yeasts
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("MISCS"))
		// We have finished a list of miscs
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("STYLES"))
		// We have finished a list of styles
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("MASH_STEPS"))
		// Finished a list of mash steps
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("MASHS"))
		// Finished a list of MASHES
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("FERMENTABLE"))
		// Finished a fermentable.  Add it to recipe and fermentables list.
		{
			thingType = 0;
			fermList.add(f);
			
			// BeerXML doesn't specify a time field for fermentables...
			// We need to hack it up in here...
			if(r.getType().equals(Recipe.EXTRACT))
			{
				if(f.getFermentableType().equals(Fermentable.GRAIN))
				{
					f.setStartTime(0);
					f.setEndTime(20);
				} 
				else if (f.getFermentableType().equals(Fermentable.EXTRACT))
				{
					f.setStartTime(0);
					f.setEndTime(r.getBoilTime());
				}
			}
			
			r.addIngredient(f);
			return;
		}
		else if (qName.equalsIgnoreCase("HOP"))
		// Finished a hop.  Add to recipe and list
		{
			thingType = 0;
			r.addIngredient(h);
			hopList.add(h);
			return;
		}
		else if (qName.equalsIgnoreCase("YEAST"))
		// Finished a yeast. Add to recipe and list
		{
			thingType = 0;
			r.addIngredient(y);
			yeastList.add(y);
			return;
		}
		else if (qName.equalsIgnoreCase("MISC"))
		// Finished a misc.  Add to recipe and list
		{
			thingType = 0;
			r.addIngredient(misc);
			miscList.add(misc);
			return;
		}
		else if (qName.equalsIgnoreCase("STYLE"))
		// Finished a style.  Add to recipe and list
		{
			thingType = 0;
			beerStyleList.add(style);
			r.setStyle(style);
		}
		else if (qName.equalsIgnoreCase("MASH_STEP"))
		// Finisehd a mash step, add to list and profile
		{
			thingType = 0;
			mashStepList.add(mashStep);
			profile.addMashStep(mashStep);
		}
		else if (qName.equalsIgnoreCase("MASH"))
		// Finished a mash profile. Add to recipe and list
		{
			thingType = 0;
			r.setMashProfile(profile);
			mashProfileList.add(profile);
		}
		
		/************************************************************
		* Handle individual types of ingredients / things below.  We check
		* the "thingType" and base our actions accordingly
		************************************************************/
		if (thingType == RECIPE)
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				r.setRecipeName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				r.setVersion(Integer.parseInt(currentValue));
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "NULL";

				if (currentValue.equalsIgnoreCase(Recipe.EXTRACT))
					type = Recipe.EXTRACT;
				if (currentValue.contains("Extract"))
					type = Recipe.EXTRACT;
				if (currentValue.equalsIgnoreCase(Recipe.ALL_GRAIN))
					type = Recipe.ALL_GRAIN;
				if (currentValue.equalsIgnoreCase(Recipe.PARTIAL_MASH))
					type = Recipe.PARTIAL_MASH;
					
				r.setType(type);
			}
			
			else if (qName.equalsIgnoreCase("BREWER"))
			{
				r.setBrewer(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("ASST_BREWER"))
			{
				// TODO
			}
			
			else if (qName.equalsIgnoreCase("BATCH_SIZE"))
			{
				double s = Float.parseFloat(currentValue);
				r.setBeerXmlStandardBatchSize(s);
			}
			
			else if (qName.equalsIgnoreCase("BOIL_SIZE"))
			{
				double s = Float.parseFloat(currentValue);
				r.setBeerXmlStandardBoilSize(s);
			}
			
			else if (qName.equalsIgnoreCase("BOIL_TIME"))
			{
				r.setBoilTime((int)Float.parseFloat(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("EFFICIENCY"))
			{
				r.setEfficiency(Float.parseFloat(currentValue));
			}
		}
		
		if (thingType == FERMENTABLE)
		// We are looking at a fermentable
		// Do all of the fermentable things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				f.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				// TODO: Set version!
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "NULL";

				if (currentValue.equalsIgnoreCase(Fermentable.ADJUNCT))
					type = Fermentable.ADJUNCT;
				if (currentValue.equalsIgnoreCase(Fermentable.EXTRACT))
					type = Fermentable.EXTRACT;
				if (currentValue.contains("Extract"))
					type = Fermentable.EXTRACT;
				if (currentValue.equalsIgnoreCase(Fermentable.GRAIN))
					type = Fermentable.GRAIN;
				if (currentValue.equalsIgnoreCase(Fermentable.SUGAR))
					type = Fermentable.SUGAR;

				f.setFermentableType(type);
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				f.setBeerXmlStandardAmount(amt);
			}

			else if (qName.equalsIgnoreCase("YIELD"))
			{
				double yield = Double.parseDouble(currentValue);
				f.setYield(yield);
			}

			else if (qName.equalsIgnoreCase("COLOR"))
			{
				double color = Double.parseDouble(currentValue);
				f.setLovibondColor(color);
			}

			else if (qName.equalsIgnoreCase("ADD_AFTER_BOIL"))
			{
				boolean aab = (currentValue.equalsIgnoreCase("FALSE")) ? false : true;
				f.setAddAfterBoil(aab);
			}

			else if (qName.equalsIgnoreCase("ORIGIN"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("SUPPLIER"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				f.setShortDescription(currentValue);
			}

			else if (qName.equalsIgnoreCase("COARSE_FINE_DIFF"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MOISTURE"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DIASTATIC_POWER"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("PROTEIN"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MAX_IN_BATCH"))
			{
				if (currentElement != false)
				{
					double maxInBatch = Double.parseDouble(currentValue);
					f.setMaxInBatch(maxInBatch);
				}
			}

			else if (qName.equalsIgnoreCase("RECOMMEND_MASH"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("IBU_GAL_PER_LB"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("INVENTORY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("POTENTIAL"))
			{
				// TODO: FUCK THIS SHIT
			}

			else if (qName.equalsIgnoreCase("DISPLAY_COLOR"))
			{
				// TODO: Add support for this field
			}
		}
		
		else if (thingType == HOP)
		// We are looking at a hop
		// Set all the hop things here
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				h.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				// TODO: Set version!
			}

			else if (qName.equalsIgnoreCase("ORIGIN"))
			{
				h.setOrigin(currentValue);
			}

			else if (qName.equalsIgnoreCase("ALPHA"))
			{
				h.setAlphaAcidContent(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				h.setBeerXmlStandardAmount(amt);
			}

			else if (qName.equalsIgnoreCase("USE"))
			{
				String use = "";

				if (currentValue.equalsIgnoreCase(Hop.USE_AROMA))
					use = Hop.USE_AROMA;
				if (currentValue.equalsIgnoreCase(Hop.USE_BOIL));
        		use = Hop.USE_BOIL;
				if (currentValue.equalsIgnoreCase(Hop.USE_DRY_HOP))
					use = Hop.USE_DRY_HOP;
				if (currentValue.equalsIgnoreCase(Hop.USE_MASH))
					use = Hop.USE_MASH;
				if (currentValue.equalsIgnoreCase(Hop.USE_FIRST_WORT))
					use = Hop.USE_FIRST_WORT;

				h.setUse(use);
			}

			else if (qName.equalsIgnoreCase("TIME"))
			{
				h.setTime((int) Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				h.setDescription(currentValue);
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "";

				if (currentValue.equalsIgnoreCase(Hop.TYPE_AROMA))
					type = Hop.TYPE_AROMA;
				if (currentValue.equalsIgnoreCase(Hop.TYPE_BITTERING))
					type = Hop.TYPE_BITTERING;
				if (currentValue.equalsIgnoreCase(Hop.TYPE_BOTH))
					type = Hop.TYPE_BOTH;

				h.setHopType(type);
			}

			else if (qName.equalsIgnoreCase("FORM"))
			{
				String form = "";

				if (currentValue.equalsIgnoreCase(Hop.FORM_PELLET))
					form = Hop.FORM_PELLET;
				if (currentValue.equalsIgnoreCase(Hop.FORM_WHOLE))
					form = Hop.FORM_WHOLE;
				if (currentValue.equalsIgnoreCase(Hop.FORM_PLUG))
					form = Hop.FORM_PLUG;

				h.setForm(form);
			}
		}
		
		else if (thingType == YEAST)
		// We are looking at a yeast
		// Set the yeast fiels here
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				y.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				int version = Integer.parseInt(currentValue);
				y.setVersion(version);
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "Invalid Type";

				if (currentValue.equalsIgnoreCase(Yeast.ALE))
					type = Yeast.ALE;
				if (currentValue.equalsIgnoreCase(Yeast.LAGER))
					type = Yeast.LAGER;
				if (currentValue.equalsIgnoreCase(Yeast.WHEAT))
					type = Yeast.WHEAT;
				if (currentValue.equalsIgnoreCase(Yeast.WINE))
					type = Yeast.WINE;
				if (currentValue.equalsIgnoreCase(Yeast.CHAMPAGNE))
					type = Yeast.CHAMPAGNE;

				y.setType(type);
			}

			else if (qName.equalsIgnoreCase("FORM"))
			{
				String form = "Invalid Form";

				if (currentValue.equalsIgnoreCase(Yeast.CULTURE))
					form = Yeast.CULTURE;
				if (currentValue.equalsIgnoreCase(Yeast.DRY))
					form = Yeast.DRY;
				if (currentValue.equalsIgnoreCase(Yeast.LIQUID))
					form = Yeast.LIQUID;

				y.setForm(form);
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				y.setBeerXmlStandardAmount(amt);
			}

			else if (qName.equalsIgnoreCase("AMOUNT_IS_WEIGHT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("LABORATORY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("PRODUCT_ID"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MIN_TEMPERATURE"))
			{
				double minTemp = Double.parseDouble(currentValue);
				y.setMinTemp(minTemp);
			}

			else if (qName.equalsIgnoreCase("MAX_TEMPERATURE"))
			{
				double maxTemp = Double.parseDouble(currentValue);
				y.setMaxTemp(maxTemp);
			}

			else if (qName.equalsIgnoreCase("FLOCCULATION"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("ATTENUATION"))
			{
				double attenuation = Double.parseDouble(currentValue);
				y.setAttenuation(attenuation);
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				y.setNotes(currentValue);
			}

			else if (qName.equalsIgnoreCase("BEST_FOR"))
			{
				y.setBestFor(currentValue);
			}

			else if (qName.equalsIgnoreCase("MAX_REUSE"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("TIMES_CULTURED"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("ADD_TO_SECONDARY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISP_MIN_TEMP"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISP_MAX_TEMP"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("INVENTORY"))
			{
				// TODO: FUCK THIS SHIT
			}

			else if (qName.equalsIgnoreCase("CULTURE_DATE"))
			{
				// TODO: Add support for this field
			}
		}
		
		else if (thingType == MISC)
		// We are looking at a misc
		// Do all of the misc things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				misc.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				misc.setVersion(Integer.parseInt(currentValue));
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = Misc.TYPE_OTHER;

				if (currentValue.equalsIgnoreCase(Misc.TYPE_SPICE))
					type = Misc.TYPE_SPICE;
				if (currentValue.equalsIgnoreCase(Misc.TYPE_FINING))
					type = Misc.TYPE_FINING;
				if (currentValue.equalsIgnoreCase(Misc.TYPE_FLAVOR))
					type = Misc.TYPE_FLAVOR;
				if (currentValue.equalsIgnoreCase(Misc.TYPE_HERB))
					type = Misc.TYPE_HERB;
				if (currentValue.equalsIgnoreCase(Misc.TYPE_WATER_AGENT))
					type = Misc.TYPE_WATER_AGENT;
				if (currentValue.equalsIgnoreCase(Misc.TYPE_OTHER))
					type = Misc.TYPE_OTHER;

				misc.setMiscType(type);
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				misc.setBeerXmlStandardAmount(amt);
			}
			
			else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
			{
				String dunit = Units.getUnitsFromDisplayAmount(currentValue);
				double amt = Units.getAmountFromDisplayAmount(currentValue);
				misc.setDisplayUnits(dunit);
				misc.setDisplayAmount(amt);
			}
			
			else if (qName.equalsIgnoreCase("TIME"))
			{
				misc.setStartTime(0);
				misc.setEndTime((int)Float.parseFloat(currentValue));
			}

			else if (qName.equalsIgnoreCase("USE"))
			{
				String type = "NO_TYPE";
				
				if (currentValue.equalsIgnoreCase(Misc.USE_BOIL))
					type = Misc.USE_BOIL;
				if (currentValue.equalsIgnoreCase(Misc.USE_BOTTLING))
					type = Misc.USE_BOTTLING;
				if (currentValue.equalsIgnoreCase(Misc.USE_MASH))
					type = Misc.USE_MASH;
				if (currentValue.equalsIgnoreCase(Misc.USE_PRIMARY))
					type = Misc.USE_PRIMARY;
				if (currentValue.equalsIgnoreCase(Misc.USE_SECONDARY))
					type = Misc.USE_SECONDARY;
					
				misc.setUse(type);
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				misc.setShortDescription(currentValue);
			}

			else if (qName.equalsIgnoreCase("AMOUNT_IS_WEIGHT"))
			{
				if (currentValue.equalsIgnoreCase("TRUE"))
					misc.setAmountIsWeight(true);
				else
					misc.setAmountIsWeight(false);
			}

			else if (qName.equalsIgnoreCase("USE_FOR"))
			{
				misc.setUseFor(currentValue);
			}
		}
			
		else if (thingType == STYLE)
		// We are looking at a style
		// Do all of the style things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				style.setName(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("CATEGORY"))
			{
				style.setCategory(currentValue);
			}
				
			else if (qName.equalsIgnoreCase("VERSION"))
			{
				style.setVersion(Integer.parseInt(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("CATEGORY_NUMBER"))
			{
				style.setCategoryNumber(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("STYLE_LETTER"))
			{
				style.setStyleLetter(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("STYLE_GUIDE"))
			{
				style.setStyleGuide(currentValue);
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "NULL";

				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_ALE))
					type = BeerStyle.TYPE_ALE;
				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_CIDER))
					type = BeerStyle.TYPE_CIDER;
				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_LAGER))
					type = BeerStyle.TYPE_LAGER;
				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_MEAD))
					type = BeerStyle.TYPE_MEAD;
				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_MIXED))
					type = BeerStyle.TYPE_MIXED;
				if (currentValue.equalsIgnoreCase(BeerStyle.TYPE_WHEAT))
					type = BeerStyle.TYPE_WHEAT;

				style.setType(type);
			}

			else if (qName.equalsIgnoreCase("OG_MIN"))
			{
				style.setMinOg(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("OG_MAX"))
			{
				style.setMaxOg(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("FG_MIN"))
			{
				style.setMinFg(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("FG_MAX"))
			{
				style.setMaxFg(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("IBU_MIN"))
			{
				style.setMinIbu(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("IBU_MAX"))
			{
				style.setMaxIbu(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("COLOR_MIN"))
			{
				style.setMinColor(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("COLOR_MAX"))
			{
				style.setMaxColor(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("ABV_MIN"))
			{
				style.setMinAbv(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("ABV_MAX"))
			{
				style.setMaxAbv(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("NOTES"))
			{
				style.setNotes(currentValue);
			}

			else if (qName.equalsIgnoreCase("PROFILE"))
			{
				style.setProfile(currentValue);
			}

			else if (qName.equalsIgnoreCase("INGREDIENTS"))
			{
				style.setIngredients(currentValue);
			}

			else if (qName.equalsIgnoreCase("EXAMPLES"))
			{
				style.setExamples(currentValue);
			}
		}
		
		else if (thingType == PROFILE)
		// We are looking at a mash profile
		// Do all of the profile things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				profile.setName(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("VERSION"))
			{
				profile.setVersion(Integer.parseInt(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("GRAIN_TEMP"))
			{
				profile.setBeerXmlStandardGrainTemp(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("NOTES"))
			{
				profile.setNotes(currentValue);
			}

			else if (qName.equalsIgnoreCase("TUN_TEMP"))
			{
				profile.setBeerXmlStandardTunTemp(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("SPARGE_TEMP"))
			{
				profile.setBeerXmlStandardSpargeTemp(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("PH"))
			{
				profile.setpH(Double.parseDouble(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("TUN_WEIGHT"))
			{
				profile.setBeerXmlStandardTunWeight(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("TUN_SPECIFIC_HEAT"))
			{
				profile.setBeerXmlStandardTunSpecHeat(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("EQUIP_ADJUST"))
			{
				profile.setEquipmentAdjust(currentValue.equalsIgnoreCase("TRUE") ? true : false);
			}
		}

		else if (thingType == MASH_STEP)
		// We are looking at a mash step
		// Do all of the mash step things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				mashStep.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				mashStep.setVersion(Integer.parseInt(currentValue));
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				if (currentValue.equalsIgnoreCase(MashStep.DECOCTION))
					mashStep.setType(MashStep.DECOCTION);
				else if (currentValue.equalsIgnoreCase(MashStep.INFUSION))
					mashStep.setType(MashStep.INFUSION);
				else if (currentValue.equalsIgnoreCase(MashStep.TEMPERATURE))
					mashStep.setType(MashStep.TEMPERATURE);
			}

			else if (qName.equalsIgnoreCase("INFUSE_AMOUNT"))
			{
				mashStep.setBeerXmlStandardInfuseAmount(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("STEP_TIME"))
			{
				mashStep.setStepTime(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("STEP_TEMP"))
			{
				mashStep.setBeerXmlStandardStepTemp(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("RAMP_TIME"))
			{
				mashStep.setRampTime(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("END_TEMP"))
			{
				mashStep.setBeerXmlStandardEndTemp(Double.parseDouble(currentValue));
			}
		}
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {

        if (currentElement) {
            currentValue = new String(ch, start, length);
            currentElement = false;
        }

    }

}
