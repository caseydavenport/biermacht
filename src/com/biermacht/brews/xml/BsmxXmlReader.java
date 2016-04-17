package com.biermacht.brews.xml;

import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Stack;
import com.biermacht.brews.utils.Units;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class BsmxXmlReader extends DefaultHandler {

  // Use a stringBuilder to store the characters read by the parser.  When the end of the
  // element is reached, they are converted to a String and stored in currentValue.
  private String currentValue;
  private StringBuilder stringBuilder;

  // Lists to store all the objects created from the parsed XML.
  private ArrayList<Recipe> recipeList;
  private ArrayList<Ingredient> fermList;
  private ArrayList<Ingredient> hopList;
  private ArrayList<Ingredient> yeastList;
  private ArrayList<Ingredient> miscList;
  private ArrayList<BeerStyle> beerStyleList;
  private ArrayList<MashProfile> mashProfileList;
  private ArrayList<MashStep> mashStepList;

  // Objects for each type of thing
  private Recipe r;
  private Fermentable f;
  private Hop h;
  private Yeast y;
  private Misc misc;
  private BeerStyle style;
  private MashProfile profile;
  private MashStep mashStep;

  // These variables need to be kept and set once both are defined. (Misc)
  private String miscDisplayUnits;
  private double miscAmount;

  // How we know what thing we're looking at.
  private Stack thingTypeStack;

  public BsmxXmlReader() {

    // Lists to store all the objects created from the parsed XML.
    this.recipeList = new ArrayList<Recipe>();
    this.fermList = new ArrayList<Ingredient>();
    this.hopList = new ArrayList<Ingredient>();
    this.yeastList = new ArrayList<Ingredient>();
    this.miscList = new ArrayList<Ingredient>();
    this.beerStyleList = new ArrayList<BeerStyle>();
    this.mashProfileList = new ArrayList<MashProfile>();
    this.mashStepList = new ArrayList<MashStep>();

    // Objects for each type of thing
    this.r = new Recipe();
    this.f = new Fermentable("");
    this.h = new Hop("");
    this.y = new Yeast("");
    this.misc = new Misc("");
    this.style = new BeerStyle("");
    this.profile = new MashProfile(r);

    // Mash steps should not perform auto-calculation, and should instead use any
    // values read from the XML file.
    this.mashStep = new MashStep(r);
    this.mashStep.setAutoCalcInfuseAmt(false);
    this.mashStep.setAutoCalcInfuseTemp(false);
    this.mashStep.setAutoCalcDecoctAmt(false);

    // How we know what thing we're looking at
    this.thingTypeStack = new Stack();
  }

  /**
   * The return methods that will return lists of all of the elements that have been parsed in the
   * current file.
   */
  public ArrayList<Recipe> getRecipes() {
    return recipeList;
  }

  public ArrayList<Ingredient> getFermentables() {
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

  public ArrayList<BeerStyle> getBeerStyles() {
    return beerStyleList;
  }

  public ArrayList<MashProfile> getMashProfiles() {
    return mashProfileList;
  }

  public ArrayList<MashStep> getMashSteps() {
    return mashStepList;
  }

  /**
   * This gets called whenever we encounter a new start element.  In this function we create the new
   * object to be populated and set the type of what we are looking at so we can properly parse the
   * following tags
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
          throws SAXException {

    // Create a new StringBuilder to house the characters read for this element
    stringBuilder = new StringBuilder();

    // Opening tag for a .bsmx file. If this does not exist in the file, the parsing will fail.
    if (qName.equalsIgnoreCase("Selections")) {
      thingTypeStack.push(qName);
      Log.d("BsmxXmlReader", "Started reading .bsmx file");
    }

    // We encounter a new recipe
    if (qName.equalsIgnoreCase("Recipe")) {
      thingTypeStack.push(qName);
      r = new Recipe("");
      r.setCalculateStrikeTemp(false);
      r.setCalculateStrikeVolume(false);
    }

    // We encounter a new hop
    if (qName.equalsIgnoreCase("HOPS")) {
      thingTypeStack.push(qName);
      h = new Hop("");
    }
    // We encounter a new fermentable
    if (qName.equalsIgnoreCase("GRAIN")) {
      thingTypeStack.push(qName);
      f = new Fermentable("");
    }

    // We encounter a new yeast
    if (qName.equalsIgnoreCase("YEAST")) {
      thingTypeStack.push(qName);
      y = new Yeast("");
    }

    // Encounter new misc
    if (qName.equalsIgnoreCase("MISC")) {
      thingTypeStack.push(qName);
      misc = new Misc("");

      // Initialize the holders for misc.
      this.miscDisplayUnits = null;
      this.miscAmount = - 1;
    }

    if (qName.equalsIgnoreCase("WATER")) {
      thingTypeStack.push(qName);
    }

    // Encounter new style
    if (qName.equalsIgnoreCase("F_R_STYLE")) {
      thingTypeStack.push(qName);
      style = new BeerStyle("");
    }

    // Encounter new mash profile
    if (qName.equalsIgnoreCase("F_R_MASH")) {
      thingTypeStack.push(qName);

      if (! thingTypeStack.contains("Recipe")) {
        // We're not operating in the context of a recipe.  As such, we should create a new
        // recipe for this MashProfile before creating the Mashprofile itself, since a MashProfile
        // doesn't make sense outside the context of a recipe.  This occurs when importing
        // mash profiles during initial configuration.
        r = new Recipe();
      }
      profile = new MashProfile(r);
    }

    // Encounter new mash step
    if (qName.equalsIgnoreCase("MashStep")) {
      this.thingTypeStack.push(qName);
      this.mashStep = new MashStep(r);
      this.mashStep.setAutoCalcInfuseAmt(false);
      this.mashStep.setAutoCalcInfuseTemp(false);
      this.mashStep.setAutoCalcDecoctAmt(false);
    }

    // Encounter new mash step list
    if (qName.equalsIgnoreCase("MASH_STEPS")) {
      thingTypeStack.push(qName);
    }

    if (qName.equalsIgnoreCase("EQUIPMENT")) {
      thingTypeStack.push(qName);
    }

  }

  /**
   * This gets called when we run into an end element.  The value of the element is stored in the
   * variable 'currentValue' and is assigned appropriately based on thingType.
   */
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {

    // Convert the contents of the StringBuilder to a String.
    currentValue = stringBuilder.toString().trim();

    Log.d("BsmxXmlReader", "Reading XML - " + qName + ": " + currentValue);

    // Closing tag for a .bsmx file
    if (qName.equalsIgnoreCase("Selections")) {
      thingTypeStack.pop();
      Log.d("BsmxXmlReader", "Finished reading .bsmx file");
      return;
    }

    // We've finished a new recipe
    if (qName.equalsIgnoreCase("Recipe")) {
      recipeList.add(r);
      thingTypeStack.pop();
      return;
    }

    if (qName.equalsIgnoreCase("WATER")) {
      // TODO
      thingTypeStack.pop();
      return;
    }

    // Finished a fermentable.  Add it to recipe and fermentables list.
    else if (qName.equalsIgnoreCase("GRAIN")) {
      thingTypeStack.pop();
      fermList.add(f);

      // BeerXML doesn't specify a time field for fermentables...
      // We need to hack it up in here...
      if (r.getType().equals(Recipe.EXTRACT)) {
        if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
          f.setTime(20);
        }
        else if (f.getFermentableType().equals(Fermentable.TYPE_EXTRACT)) {
          f.setTime(r.getBoilTime());
        }
      }

      r.addIngredient(f);
      return;
    }
    else if (qName.equalsIgnoreCase("HOPS"))
    // Finished a hop.  Add to recipe and list
    {
      thingTypeStack.pop();
      r.addIngredient(h);
      hopList.add(h);
      return;
    }
    else if (qName.equalsIgnoreCase("YEAST"))
    // Finished a yeast. Add to recipe and list
    {
      thingTypeStack.pop();
      r.addIngredient(y);
      yeastList.add(y);
      return;
    }
    else if (qName.equalsIgnoreCase("MISC"))
    // Finished a misc.  Add to recipe and list
    {
      thingTypeStack.pop();
      r.addIngredient(misc);
      miscList.add(misc);
      return;
    }
    else if (qName.equalsIgnoreCase("F_R_STYLE"))
    // Finished a style.  Add to recipe and list
    {
      thingTypeStack.pop();
      beerStyleList.add(style);
      r.setStyle(style);
      return;
    }
    else if (qName.equalsIgnoreCase("MashStep"))
    // Finished a mash step, add to list and profile
    {
      thingTypeStack.pop();
      mashStepList.add(mashStep);
      profile.addMashStep(mashStep);
      return;
    }
    else if (qName.equalsIgnoreCase("F_R_MASH"))
    // Finished a mash profile. Add to recipe and list
    {
      thingTypeStack.pop();
      r.setMashProfile(profile);
      mashProfileList.add(profile);
      return;
    }

    else if (qName.equalsIgnoreCase("EQUIPMENT")) {
      thingTypeStack.pop();
      return;
    }

    /************************************************************
     * Handle individual types of ingredients / things below.  We check
     * the "thingType" and base our actions accordingly.
     ************************************************************/
    if (thingTypeStack.read().equalsIgnoreCase("Recipe") ||
            thingTypeStack.read().equalsIgnoreCase("F_R_EQUIPMENT") ||
            thingTypeStack.read().equalsIgnoreCase("F_R_AGE")) {
      // TODO: Some fields that are part of "Recipe" in Biermacht are part of "Equipment" and "Age" in BSMX, so we group them together.
      if (qName.equalsIgnoreCase("F_R_NAME")) {
        r.setRecipeName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_R_VERSION")) {
        // TODO: Support this field.
      }

      else if (qName.equalsIgnoreCase("F_R_TYPE")) {
        String type;

        if (Integer.parseInt(currentValue) == 0) {
          type = Recipe.EXTRACT;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          type = Recipe.PARTIAL_MASH;
        }
        else {
          type = Recipe.ALL_GRAIN;
        }

        r.setType(type);
      }

      else if (qName.equalsIgnoreCase("F_R_NOTES")) {
        r.setNotes(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_R_DATE")) {
        r.setBrewDate(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_R_BREWER")) {
        r.setBrewer(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_R_ASST_BREWER")) {
        // TODO
      }

      else if (qName.equalsIgnoreCase("F_E_BATCH_VOL")) {
        // .bsmx uses ounces for volume, so convert to liters first.
        double s = Units.ouncesToLiters(Float.parseFloat(currentValue));
        r.setBeerXmlStandardBatchSize(s);
      }

      else if (qName.equalsIgnoreCase("F_E_BOIL_VOL")) {
        // .bsmx uses ounces for volume, so convert to liters first.
        double s = Units.ouncesToLiters(Float.parseFloat(currentValue));
        r.setCalculateBoilVolume(false);
        r.setBeerXmlStandardBoilSize(s);
      }

      else if (qName.equalsIgnoreCase("F_E_BOIL_TIME")) {
        r.setBoilTime((int) Float.parseFloat(currentValue));
      }

      else if (qName.equalsIgnoreCase("F_E_EFFICIENCY")) {
        r.setEfficiency(Float.parseFloat(currentValue));
      }

      else if (qName.equalsIgnoreCase("F_A_TYPE")) {
        // The type appears to be the number of stages - 1.
        r.setFermentationStages(Integer.parseInt(currentValue) + 1);
      }

      else if (qName.equalsIgnoreCase("F_A_PRIM_DAYS")) {
        r.setFermentationAge(Recipe.STAGE_PRIMARY, (int) Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_A_PRIM_TEMP")) {
        // .bsmx uses Fahrenheit, so convert to C before setting.
        Double temp = Units.fahrenheitToCelsius(Double.parseDouble(currentValue));
        r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY, temp);
      }

      else if (qName.equalsIgnoreCase("F_A_SEC_DAYS")) {
        r.setFermentationAge(Recipe.STAGE_SECONDARY, (int) Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_A_SEC_TEMP")) {
        // .bsmx uses Fahrenheit, so convert to C before setting.
        Double temp = Units.fahrenheitToCelsius(Double.parseDouble(currentValue));
        r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY, temp);
      }

      else if (qName.equalsIgnoreCase("F_A_TERT_DAYS")) {
        r.setFermentationAge(Recipe.STAGE_TERTIARY, (int) Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_A_TERT_TEMP")) {
        // .bsmx uses Fahrenheit, so convert to C before setting.
        Double temp = Units.fahrenheitToCelsius(Double.parseDouble(currentValue));
        r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY, temp);
      }

      else if (qName.equalsIgnoreCase("F_R_OG_MEASURED")) {
        r.setMeasuredOG(Double.parseDouble(currentValue));
      }

      else if (qName.equalsIgnoreCase("F_R_FG_MEASURED")) {
        r.setMeasuredFG(Double.parseDouble(currentValue));
      }

      else if (qName.equalsIgnoreCase("F_R_CARB_VOLS")) {
        r.setCarbonation(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("FORCED_CARBONATION")) {
        // TODO: Does this exist in .bsmx?
        r.setIsForceCarbonated(currentValue.equalsIgnoreCase("TRUE"));
      }

      else if (qName.equalsIgnoreCase("CARBONATION_TEMP")) {
        // TODO: Does this exist in .bsmx?
        r.setBeerXmlStandardCarbonationTemp(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("CALORIES")) {
        // TODO
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a FERMENTABLE.
    //**********************************************************************************************
    if (thingTypeStack.read().equalsIgnoreCase("GRAIN")) {
      if (qName.equalsIgnoreCase("F_G_NAME")) {
        f.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_G_TYPE")) {
        String type = "NULL";

        // .bsmx uses integers to determine types.
        if (Integer.parseInt(currentValue) == 0) {
          type = Fermentable.TYPE_GRAIN;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          // Liquid Extract
          type = Fermentable.TYPE_EXTRACT;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          type = Fermentable.TYPE_SUGAR;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          type = Fermentable.TYPE_ADJUNCT;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          // Dry Extract
          type = Fermentable.TYPE_EXTRACT;
        }
        f.setFermentableType(type);
      }

      else if (qName.equalsIgnoreCase("F_G_AMOUNT")) {
        double amt = Double.parseDouble(currentValue.replace(",", "."));
        f.setBeerXmlStandardAmount(Units.ouncesToKilos(amt));
      }

      else if (qName.equalsIgnoreCase("F_G_YIELD")) {
        double yield = Double.parseDouble(currentValue.replace(",", "."));
        f.setYield(yield);
      }

      else if (qName.equalsIgnoreCase("F_G_COLOR")) {
        double color = Double.parseDouble(currentValue.replace(",", "."));
        f.setLovibondColor(color);
      }

      else if (qName.equalsIgnoreCase("ADD_AFTER_BOIL")) {
        boolean aab = (currentValue.equalsIgnoreCase("FALSE")) ? false : true;
        f.setAddAfterBoil(aab);
      }

      else if (qName.equalsIgnoreCase("F_G_ORIGIN")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_SUPPLIER")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_NOTES")) {
        f.setShortDescription(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_G_BOIL_TIME")) {
        f.setTime((int) Double.parseDouble(currentValue));
      }

      else if (qName.equalsIgnoreCase("F_G_COARSE_FINE_DIFF")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_MOISTURE")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_DIASTATIC_POWER")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_PROTEIN")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_MAX_IN_BATCH")) {
        double maxInBatch = Double.parseDouble(currentValue.replace(",", "."));
        f.setMaxInBatch(maxInBatch);
      }

      else if (qName.equalsIgnoreCase("F_G_RECOMMEND_MASH")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_G_CONVERT_GRAIN")) {
        // TODO: Add support for this field
      }
    }
    //**********************************************************************************************
    // The current XML object being examined is a HOP.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("HOPS")) {
      if (qName.equalsIgnoreCase("F_H_NAME")) {
        h.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_H_ORIGIN")) {
        h.setOrigin(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_H_ALPHA")) {
        h.setAlphaAcidContent(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_H_AMOUNT")) {
        double amt = Double.parseDouble(currentValue.replace(",", "."));
        h.setBeerXmlStandardAmount(Units.ouncesToKilos(amt));
      }

      else if (qName.equalsIgnoreCase("F_H_TYPE")) {
        String type = "";

        if (Integer.parseInt(currentValue) == 0) {
          type = Hop.TYPE_BITTERING;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          type = Hop.TYPE_AROMA;
        }
        else {
          type = Hop.TYPE_BOTH;
        }

        h.setHopType(type);
      }

      else if (qName.equalsIgnoreCase("F_H_USE")) {
        String use = "";

        if (Integer.parseInt(currentValue) == 0) {
          use = Hop.USE_BOIL;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          use = Hop.USE_DRY_HOP;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          use = Hop.USE_MASH;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          use = Hop.USE_FIRST_WORT;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          use = Hop.USE_AROMA;
        }
      }

      else if (qName.equalsIgnoreCase("F_H_BOIL_TIME")) {
        double time = Double.parseDouble(currentValue);
        if (time > 0) {
          // Only set the time if non-zero.  This will be zero for dry-hops.
          h.setBeerXmlStandardTime((int) Double.parseDouble(currentValue.replace(",", ".")));
        }
      }

      else if (qName.equalsIgnoreCase("F_H_DRY_HOP_TIME")) {
        double time = Double.parseDouble(currentValue);
        if (time > 0) {
          // Only set the time if non-zero.  This will be zero for boiled hops.
          h.setBeerXmlStandardTime((int) Double.parseDouble(currentValue.replace(",", ".")));
        }
      }

      else if (qName.equalsIgnoreCase("F_H_NOTES")) {
        h.setDescription(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_H_FORM")) {
        String form = "";

        if (Integer.parseInt(currentValue) == 0) {
          form = Hop.FORM_PELLET;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          form = Hop.FORM_PLUG;
        }
        else {
          form = Hop.FORM_WHOLE;
        }

        h.setForm(form);
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a YEAST.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("YEAST")) {
      if (qName.equalsIgnoreCase("F_Y_NAME")) {
        y.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_Y_TYPE")) {
        String type = "Invalid Type";

        if (Integer.parseInt(currentValue) == 0) {
          type = Yeast.TYPE_ALE;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          type = Yeast.TYPE_LAGER;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          type = Yeast.TYPE_WINE;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          type = Yeast.TYPE_CHAMPAGNE;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          type = Yeast.TYPE_WHEAT;
        }

        y.setType(type);
      }

      else if (qName.equalsIgnoreCase("F_Y_FORM")) {
        String form;

        if (Integer.parseInt(currentValue) == 0) {
          form = Yeast.FORM_LIQUID;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          form = Yeast.FORM_DRY;
        }
        else {
          form = Yeast.FORM_CULTURE;
        }

        y.setForm(form);
      }

      else if (qName.equalsIgnoreCase("F_Y_AMOUNT")) {
        double amt = Double.parseDouble(currentValue.replace(",", "."));
        y.setBeerXmlStandardAmount(amt);
      }

      else if (qName.equalsIgnoreCase("F_Y_LAB")) {
        y.setLaboratory(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_Y_PRODUCT_ID")) {
        y.setProductId(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_Y_MIN_TEMP")) {
        double minTemp = Double.parseDouble(currentValue.replace(",", "."));
        y.setMinTemp(Units.fahrenheitToCelsius(minTemp));
      }

      else if (qName.equalsIgnoreCase("F_Y_MAX_TEMP")) {
        double maxTemp = Double.parseDouble(currentValue.replace(",", "."));
        y.setMaxTemp(Units.fahrenheitToCelsius(maxTemp));
      }

      else if (qName.equalsIgnoreCase("F_Y_FLOCCULATION")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_Y_MIN_ATTENUATION")) {
        // TODO: We don't currently support the min/max attenuation model.
        double attenuation = Double.parseDouble(currentValue.replace(",", "."));
        y.setAttenuation(attenuation + 2.0);
      }

      else if (qName.equalsIgnoreCase("F_Y_NOTES")) {
        y.setNotes(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_Y_BEST_FOR")) {
        y.setBestFor(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_Y_MAX_REUSE")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_Y_TIMES_CULTURED")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_Y_ADD_TO_SECONDARY")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_Y_INVENTORY")) {
        // TODO: Add support for this field
      }

      else if (qName.equalsIgnoreCase("F_Y_CULTURE_DATE")) {
        // TODO: Add support for this field
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a MISC.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("MISC")) {
      if (qName.equalsIgnoreCase("F_M_NAME")) {
        misc.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_M_TYPE")) {
        String type = Misc.TYPE_OTHER;

        if (Integer.parseInt(currentValue) == 0) {
          type = Misc.TYPE_SPICE;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          type = Misc.TYPE_FINING;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          type = Misc.TYPE_HERB;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          type = Misc.TYPE_FLAVOR;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          type = Misc.TYPE_OTHER;
        }
        else if (Integer.parseInt(currentValue) == 5) {
          type = Misc.TYPE_WATER_AGENT;
        }

        misc.setMiscType(type);
      }

      else if (qName.equalsIgnoreCase("F_M_AMOUNT")) {
        this.miscAmount = Double.parseDouble(currentValue.replace(",", "."));

        // Only set the units if both the units and amount are known.  Since we don't know the
        // order in which we will parse these values, we need to wait until we've parsed both.
        if (this.miscDisplayUnits != null) {
          misc.setDisplayAmount(this.miscAmount, this.miscDisplayUnits);
          misc.setDisplayUnits(this.miscDisplayUnits);
        }
      }

      else if (qName.equalsIgnoreCase("F_M_UNITS")) {

        if (Integer.parseInt(currentValue) == 0) {
          this.miscDisplayUnits = Units.MILLIGRAMS;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          this.miscDisplayUnits = Units.GRAMS;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          this.miscDisplayUnits = Units.OUNCES;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          this.miscDisplayUnits = Units.POUNDS;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          this.miscDisplayUnits = Units.KILOGRAMS;
        }
        else if (Integer.parseInt(currentValue) == 5) {
          this.miscDisplayUnits = Units.MILLILITERS;
        }
        else if (Integer.parseInt(currentValue) == 6) {
          this.miscDisplayUnits = Units.TEASPOONS;
        }
        else if (Integer.parseInt(currentValue) == 7) {
          this.miscDisplayUnits = Units.TABLESPOONS;
        }
        else if (Integer.parseInt(currentValue) == 8) {
          this.miscDisplayUnits = Units.CUPS;
        }
        else if (Integer.parseInt(currentValue) == 9) {
          this.miscDisplayUnits = Units.PINTS;
        }
        else if (Integer.parseInt(currentValue) == 10) {
          this.miscDisplayUnits = Units.QUARTS;
        }
        else if (Integer.parseInt(currentValue) == 11) {
          this.miscDisplayUnits = Units.LITERS;
        }
        else if (Integer.parseInt(currentValue) == 12) {
          this.miscDisplayUnits = Units.GALLONS;
        }
        else if (Integer.parseInt(currentValue) == 13) {
          this.miscDisplayUnits = Units.ITEMS;
        }
        else {
          this.miscDisplayUnits = "Unknown";
        }

        // Only set the units if both the units and amount are known.  Since we don't know the
        // order in which we will parse these values, we need to wait until we've parsed both.
        if (this.miscAmount >= 0) {
          misc.setDisplayAmount(this.miscAmount, this.miscDisplayUnits);
          misc.setDisplayUnits(this.miscDisplayUnits);
        }
      }

      else if (qName.equalsIgnoreCase("F_M_TIME")) {
        double time = Double.parseDouble(currentValue);
        misc.setTime((int) time);
      }

      else if (qName.equalsIgnoreCase("F_M_USE")) {
        String use = Misc.USE_OTHER;

        if (Integer.parseInt(currentValue) == 0) {
          use = Misc.USE_BOIL;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          use = Misc.USE_MASH;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          use = Misc.USE_PRIMARY;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          use = Misc.USE_SECONDARY;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          use = Misc.USE_BOTTLING;
        }

        misc.setUse(use);
      }

      else if (qName.equalsIgnoreCase("F_M_NOTES")) {
        misc.setShortDescription(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_M_IMPORT_AS_WEIGHT")) {
        if (Integer.parseInt(currentValue) == 1) {
          misc.setAmountIsWeight(true);
        }
        else {
          misc.setAmountIsWeight(false);
        }
      }

      else if (qName.equalsIgnoreCase("F_M_USE_FOR")) {
        misc.setUseFor(currentValue);
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a STYLE.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("F_R_STYLE")) {
      if (qName.equalsIgnoreCase("F_S_NAME")) {
        style.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_CATEGORY")) {
        style.setCategory(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_NUMBER")) {
        style.setCategoryNumber(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_LETTER")) {
        style.setStyleLetter(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_GUIDE")) {
        style.setStyleGuide(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_TYPE")) {
        String type = "Unknown";

        if (Integer.parseInt(currentValue) == 0) {
          type = BeerStyle.TYPE_ALE;
        }
        else if (Integer.parseInt(currentValue) == 1) {
          type = BeerStyle.TYPE_LAGER;
        }
        else if (Integer.parseInt(currentValue) == 2) {
          type = BeerStyle.TYPE_WHEAT;
        }
        else if (Integer.parseInt(currentValue) == 3) {
          type = BeerStyle.TYPE_MEAD;
        }
        else if (Integer.parseInt(currentValue) == 4) {
          type = BeerStyle.TYPE_CIDER;
        }
        else if (Integer.parseInt(currentValue) == 5) {
          type = BeerStyle.TYPE_MIXED;
        }

        style.setType(type);
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_OG")) {
        style.setMinOg(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_OG")) {
        style.setMaxOg(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_FG")) {
        style.setMinFg(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_FG")) {
        style.setMaxFg(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_IBU")) {
        style.setMinIbu(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_IBU")) {
        style.setMaxIbu(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_CARB")) {
        style.setMinCarb(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_CARB")) {
        style.setMaxCarb(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_COLOR")) {
        style.setMinColor(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_COLOR")) {
        style.setMaxColor(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MIN_ABV")) {
        style.setMinAbv(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_MAX_ABV")) {
        style.setMaxAbv(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_S_DESCRIPTION")) {
        style.setNotes(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_PROFILE")) {
        style.setProfile(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_INGREDIENTS")) {
        style.setIngredients(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_S_EXAMPLES")) {
        style.setExamples(currentValue);
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a MASH.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("F_R_MASH")) {
      if (qName.equalsIgnoreCase("F_MH_NAME")) {
        profile.setName(currentValue);

        if (currentValue.contains("BIAB")) {
          // This is a brew-in-a-bag type mash profile.  Set the types.
          profile.setMashType(MashProfile.MASH_TYPE_BIAB);
          profile.setSpargeType(MashProfile.MASH_TYPE_BIAB);
        }
      }

      else if (qName.equalsIgnoreCase("F_MH_GRAIN_TEMP")) {
        double temp = Double.parseDouble(currentValue.replace(",", "."));
        profile.setBeerXmlStandardGrainTemp(Units.fahrenheitToCelsius(temp));
      }

      else if (qName.equalsIgnoreCase("F_MH_NOTES")) {
        profile.setNotes(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_MH_TUN_TEMP")) {
        double temp = Double.parseDouble(currentValue.replace(",", "."));
        profile.setBeerXmlStandardTunTemp(Units.fahrenheitToCelsius(temp));
      }

      else if (qName.equalsIgnoreCase("F_MH_SPARGE_TEMP")) {
        double temp = Double.parseDouble(currentValue.replace(",", "."));
        profile.setBeerXmlStandardSpargeTemp(Units.fahrenheitToCelsius(temp));
      }

      else if (qName.equalsIgnoreCase("F_MH_PH")) {
        profile.setpH(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_MH_TUN_MASS")) {
        // TODO: Support this field.
      }

      else if (qName.equalsIgnoreCase("F_MH_TUN_VOL")) {
        // TODO: Support this field.
      }

      else if (qName.equalsIgnoreCase("F_MH_TUN_HC")) {
        profile.setBeerXmlStandardTunSpecHeat(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_MH_EQUIP_ADJUST")) {
        profile.setEquipmentAdjust(Integer.parseInt(currentValue) == 1);
      }
    }

    //**********************************************************************************************
    // The current XML object being examined is a MASH_STEP.
    //**********************************************************************************************
    else if (thingTypeStack.read().equalsIgnoreCase("MashStep")) {
      if (qName.equalsIgnoreCase("F_MS_NAME")) {
        mashStep.setName(currentValue);
      }

      else if (qName.equalsIgnoreCase("F_MS_TYPE")) {
        if (Integer.parseInt(currentValue) == 0) {
          mashStep.setType(MashStep.INFUSION);
        }
        else if (Integer.parseInt(currentValue) == 1) {
          mashStep.setType(MashStep.DECOCTION);
        }
        else if (Integer.parseInt(currentValue) == 2) {
          mashStep.setType(MashStep.TEMPERATURE);
        }
      }

      else if (qName.equalsIgnoreCase("F_MS_INFUSION")) {
        double amt = Double.parseDouble(currentValue.replace(",", "."));
        mashStep.setBeerXmlStandardInfuseAmount(Units.ouncesToLiters(amt));
      }

      else if (qName.equalsIgnoreCase("F_MS_INFUSION_TEMP")) {
        double temp = Double.parseDouble(currentValue.replace(",", "."));
        mashStep.setBeerXmlStandardInfuseTemp(Units.fahrenheitToCelsius(temp));
      }

      else if (qName.equalsIgnoreCase("F_MS_DECOCTION_AMT")) {
        double amt = Double.parseDouble(currentValue);
        mashStep.setBeerXmlDecoctAmount(Units.ouncesToLiters(amt));
      }

      else if (qName.equalsIgnoreCase("F_MS_STEP_TIME")) {
        mashStep.setStepTime(Double.parseDouble(currentValue.replace(",", ".")));
      }

      else if (qName.equalsIgnoreCase("F_MS_STEP_TEMP")) {
        double temp = Double.parseDouble(currentValue.replace(",", "."));
        mashStep.setBeerXmlStandardStepTemp(Units.fahrenheitToCelsius(temp));
      }

      else if (qName.equalsIgnoreCase("F_MS_RISE_TIME")) {
        mashStep.setRampTime(Double.parseDouble(currentValue.replace(",", ".")));
      }

      // TODO: Missing support for a number of .bsmx MashStep fields.
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {

    // Populate the stringBuilder with characters read from the parser.  When the parser
    // indicates that it has reached the end of this element via a call to endElement(), we will
    // convert the contents of this builder to a string.
    if (stringBuilder != null) {
      for (int i = start; i < start + length; i++) {
        stringBuilder.append(ch[i]);
      }
    }

  }
}
