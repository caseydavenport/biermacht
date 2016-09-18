package com.biermacht.brews.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.comparators.BeerStyleComparator;
import com.biermacht.brews.utils.comparators.RecipeIngredientsComparator;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.xml.BeerXmlReader;
import com.biermacht.brews.xml.BsmxXmlReader;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import biermacht.org.apache.commons.lang3.StringEscapeUtils;

public class IngredientHandler {

  private Context mContext;
  private DatabaseAPI databaseApi;
  private ArrayList<Ingredient> fermentablesList;
  private ArrayList<Ingredient> yeastsList;
  private ArrayList<Ingredient> hopsList;
  private ArrayList<Ingredient> miscsList;
  private ArrayList<BeerStyle> styleList;
  private ArrayList<MashProfile> profileList;

  public IngredientHandler(Context c) {
    this.mContext = c;
    this.databaseApi = new DatabaseAPI(c);
  }

  // First time use - put ingredients and mash profiles into SQLite
  public void ImportAssets() {
    try {
      // Import all ingredient assets.
      this.importIngredients();

      // Import mash profile assets.
      this.databaseApi.addMashProfileList(Constants.DATABASE_USER_RESOURCES,
                                          getProfilesFromXml(),
                                          Constants.OWNER_NONE);

      // Import style assets.
      this.databaseApi.addStyleList(Constants.DATABASE_SYSTEM_RESOURCES,
                                    getStylesFromXml(),
                                    Constants.OWNER_NONE);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Imports ingredient assets only (not mash profiles).
  public void importIngredients() throws IOException {
    databaseApi.addIngredientList(Constants.DATABASE_SYSTEM_RESOURCES,
                                  getFermentablesFromXml(),
                                  Constants.OWNER_NONE);
    databaseApi.addIngredientList(Constants.DATABASE_SYSTEM_RESOURCES,
                                  getYeastsFromXml(),
                                  Constants.OWNER_NONE);
    databaseApi.addIngredientList(Constants.DATABASE_SYSTEM_RESOURCES,
                                  getHopsFromXml(),
                                  Constants.OWNER_NONE);
    databaseApi.addIngredientList(Constants.DATABASE_SYSTEM_RESOURCES,
                                  getMiscsFromXml(),
                                  Constants.OWNER_NONE);
  }

  public void importIngredients(String filePath) throws IOException {
    databaseApi.addIngredientList(Constants.DATABASE_SYSTEM_RESOURCES,
                                  getIngredientsFromXml(filePath),
                                  Constants.OWNER_NONE);
  }

  /**
   * Returns a list of valid fermentables for use in recipes
   *
   * @return ArrayList of ingredient objects
   */
  public ArrayList<Ingredient> getFermentablesList() {
    if (this.fermentablesList == null) {
      Log.d("getFermentablesList", "List is null, creating");
      fermentablesList = new ArrayList<Ingredient>();
    }

    fermentablesList.removeAll(fermentablesList);
    fermentablesList.addAll(databaseApi.getIngredients(Constants.DATABASE_USER_RESOURCES,
                                                       Ingredient.FERMENTABLE));
    fermentablesList.addAll(databaseApi.getIngredients(Constants.DATABASE_SYSTEM_RESOURCES, Ingredient.FERMENTABLE));

    Collections.sort(fermentablesList, new ToStringComparator());
    Log.d("getFermentablesList", "Returning " + fermentablesList.size() + " fermentables");
    return this.fermentablesList;
  }

  /**
   * Returns a list of valid yeasts for use in recipes
   *
   * @return ArrayList of ingredient objects
   */
  public ArrayList<Ingredient> getYeastsList() {
    if (this.yeastsList == null) {
      Log.d("getYeastsList", "List is null, creating");
      yeastsList = new ArrayList<Ingredient>();
    }

    yeastsList.removeAll(yeastsList);
    yeastsList.addAll(databaseApi.getIngredients(Constants.DATABASE_USER_RESOURCES,
                                                 Ingredient.YEAST));
    yeastsList.addAll(databaseApi.getIngredients(Constants.DATABASE_SYSTEM_RESOURCES,
                                                 Ingredient.YEAST));

    Collections.sort(yeastsList, new ToStringComparator());
    return this.yeastsList;
  }

  /**
   * Returns a list of valid hops for use in recipes
   *
   * @return ArrayList of ingredient objects
   */
  public ArrayList<Ingredient> getHopsList() {
    if (this.hopsList == null) {
      Log.d("getHopsList", "List is null, creating");
      hopsList = new ArrayList<Ingredient>();
    }

    hopsList.removeAll(hopsList);
    hopsList.addAll(databaseApi.getIngredients(Constants.DATABASE_USER_RESOURCES,
                                               Ingredient.HOP));
    hopsList.addAll(databaseApi.getIngredients(Constants.DATABASE_SYSTEM_RESOURCES,
                                               Ingredient.HOP));

    Collections.sort(hopsList, new ToStringComparator());
    return this.hopsList;
  }

  /**
   * Returns a list of valid miscs for use in recipes
   *
   * @return ArrayList of ingredient objects
   */
  public ArrayList<Ingredient> getMiscsList() {
    if (this.miscsList == null) {
      Log.d("getMiscsList", "List is null, creating");
      miscsList = new ArrayList<Ingredient>();
    }

    miscsList.removeAll(miscsList);
    miscsList.addAll(databaseApi.getIngredients(Constants.DATABASE_USER_RESOURCES,
                                                Ingredient.MISC));
    miscsList.addAll(databaseApi.getIngredients(Constants.DATABASE_SYSTEM_RESOURCES,
                                                Ingredient.MISC));

    Collections.sort(miscsList, new ToStringComparator());
    return this.miscsList;
  }

  /**
   * Returns a list of valid styles for use in recipes
   *
   * @return ArrayList of style objects
   */
  public ArrayList<BeerStyle> getStylesList() {
    if (this.styleList == null) {
      this.styleList = this.databaseApi.getStyles(Constants.DATABASE_SYSTEM_RESOURCES);
      this.styleList.addAll(this.databaseApi.getStyles(Constants.DATABASE_USER_RESOURCES));
    }

    return styleList;
  }

  /**
   * @return ArrayList of MashProfiles
   */
  public ArrayList<MashProfile> getMashProfileList() {
    if (this.profileList == null) {
      Log.d("getMashProfileList", "List is null, creating");
      profileList = new ArrayList<MashProfile>();
    }

    profileList.removeAll(profileList);
    profileList.addAll(databaseApi.getMashProfiles(Constants.DATABASE_USER_RESOURCES));
    profileList.addAll(databaseApi.getMashProfiles(Constants.DATABASE_SYSTEM_RESOURCES));

    Collections.sort(profileList, new ToStringComparator());
    return this.profileList;
  }

  /**
   * Gets fermentables from XMl files in assets/Fermentables Some fermentables from..
   * http://byo.com/resources/grains
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<Ingredient> getFermentablesFromXml() throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();

    for (String s : mContext.getAssets().list("Fermentables")) {
      list.addAll(getIngredientsFromXml("Fermentables/" + s));
    }

    return list;
  }

  /**
   * Gets yeasts from XMl files in assets/Yeasts
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<Ingredient> getYeastsFromXml() throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();

    for (String s : mContext.getAssets().list("Yeasts")) {
      list.addAll(getIngredientsFromXml("Yeasts/" + s));
    }

    return list;
  }

  /**
   * Gets hops from XMl files in assets/Hops
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<Ingredient> getHopsFromXml() throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();

    for (String s : mContext.getAssets().list("Hops")) {
      list.addAll(getIngredientsFromXml("Hops/" + s));
    }

    Log.d("IngredientHandler", "Got " + list.size() + " hops from XML");
    return list;
  }

  /**
   * Gets Styles from XMl files in assets/Styles
   *
   * @return ArrayList of BeerStyle Objects
   * @throws IOException
   */
  public ArrayList<BeerStyle> getStylesFromXml() throws IOException {
    ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();

    for (String s : mContext.getAssets().list("Styles")) {
      list.addAll(getStylesFromXml("Styles/" + s));
    }

    Collections.sort(list, new BeerStyleComparator<BeerStyle>());
    return list;
  }

  public ArrayList<BeerStyle> getStylesFromXml(String filePath) throws IOException {
    ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();

    try {
      SAXParser sp = spf.newSAXParser();
      InputStream is = mContext.getAssets().open(filePath);
      sp.parse(is, myXMLHandler);

      list.addAll(myXMLHandler.getBeerStyles());
    } catch (Exception e) {
      Log.e("getStylesFromXml", e.toString());
    }

    return list;
  }

  /**
   * Gets Miscs from XMl files in assets/Miscs
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<Ingredient> getMiscsFromXml() throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();

    for (String s : mContext.getAssets().list("Miscs")) {
      list.addAll(getIngredientsFromXml("Miscs/" + s));
    }

    Collections.sort(list, new RecipeIngredientsComparator());
    return list;
  }

  private ArrayList<Ingredient> getIngredientsFromXml(String filePath) throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();

    Log.d("IngredientHandler", "Importing ingredients from: " + filePath);

    try {
      SAXParser sp = spf.newSAXParser();
      InputStream is = mContext.getAssets().open(filePath);
      sp.parse(is, myXMLHandler);

      list.addAll(myXMLHandler.getAllIngredients());
    } catch (Exception e) {
      Log.e("IngredientHandler", e.toString());
    }

    Collections.sort(list, new RecipeIngredientsComparator());
    Log.d("IngredientHandler", filePath + " had " + list.size() + " ingredients");
    return list;
  }

  /**
   * Gets Recipes from XMl file passed in path s.
   * <p/>
   * If the file extension if .bsmx, will use the .bsmx parser.  Otherwise, the BeerXML parser is
   * used.
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  public ArrayList<Recipe> getRecipesFromXml(InputStream is, String path) throws Exception {
    ArrayList<Recipe> retlist = new ArrayList<Recipe>();
    ArrayList<Recipe> list;
    BeerXmlReader beerXmlReader = new BeerXmlReader();
    BsmxXmlReader bsmxXmlReader = new BsmxXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser sp = spf.newSAXParser();

    // Read the input source to a String for use below.
    String asString = Utils.convertStreamToString(is);

    Log.d("IngredientHandler", "Parsing file: " + path);
    try {
      // First, try to parse this as a BeerXML file.  If it fails then try BeerSmith format.
      Log.d("IngredientHandler", "Attempting to parse BeerXML file");
      InputSource inSrc = new InputSource(new StringReader(asString));
      inSrc.setEncoding("ISO-8859-2");
      sp.parse(inSrc, beerXmlReader);
      list = beerXmlReader.getRecipes();
    }
    catch (Exception e) {
      try {
        Log.d("IngredientHandler", "Failed to parse as .xml, attempting to parse as a .bsmx file");
        // Failed to parse as a .xml file.  Attempt to parse as .bsmx format instead.
        // If this fails, then we'll raise both errors to the user.

        // .bsmx files generated by BeerSmith include HTML encoded characters without declaring
        // the encoding - pull in the whole file as a string and escape any HTML entities before
        // passing to the XML parser.
        String escaped = StringEscapeUtils.unescapeHtml4(asString);

        // Re-escape things properly as XML.
        escaped = StringEscapeUtils.escapeXml11(escaped);
        escaped = escaped.replaceAll("&lt;", "<");
        escaped = escaped.replaceAll("&gt;", ">");

        // Now, parse the String.
        sp.parse(new InputSource(new StringReader(escaped)), bsmxXmlReader);
        list = bsmxXmlReader.getRecipes();

      } catch (Exception e2) {
        // Failed to parse the file as beerXML, as well as Beersmith format.
        // Raise an error to the user that the file is not valid.
        Log.e("IngredientHandler", "Failed to parse as either .bsmx, or .xml format");
        String eTrace = Log.getStackTraceString(e);
        String e2Trace = Log.getStackTraceString(e2);
        String estr = "The following errors were raised:\n\n" +
                ".xml parse errror: \n%s\n\n--------\n\n" +
                ".bsmx parse error: \n%s\n\n--------\n";
        estr = String.format(estr, eTrace, e2Trace);
        throw new Exception(estr);
      }
    }

    retlist.addAll(list);

    return retlist;
  }

  /**
   * Gets MashProfiles from XMl files in assets/Profiles
   *
   * @return ArrayList of MashProfile Objects
   * @throws IOException
   */
  public ArrayList<MashProfile> getProfilesFromXml() throws IOException {
    ArrayList<MashProfile> list = new ArrayList<MashProfile>();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Profiles")) {
      list.addAll(getProfilesFromXml("Profiles/" + s));
    }
    return list;
  }

  public ArrayList<MashProfile> getProfilesFromXml(String path) throws IOException {
    ArrayList<MashProfile> list = new ArrayList<MashProfile>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    try {
      // Parse files.
      SAXParser sp = spf.newSAXParser();
      InputStream is = am.open(path);
      sp.parse(is, myXMLHandler);

      // Default mash profiles should auto-calculate infustion temp / amount,
      // so set that here.
      list = myXMLHandler.getMashProfiles();
      for (MashProfile p : list) {
        for (MashStep step : p.getMashStepList()) {
          if (step.getType().equals(MashStep.INFUSION)) {
            Log.d("IngredientHandler", "Enabling auto-calculation for: " + p.getName());
            step.setAutoCalcInfuseAmt(true);
            step.setAutoCalcInfuseTemp(true);
          }
        }
      }

    } catch (Exception e) {
      Log.e("getProfilesFromXml", e.toString());
    }
    return list;
  }

}
