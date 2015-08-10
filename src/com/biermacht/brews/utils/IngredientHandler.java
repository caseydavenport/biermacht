package com.biermacht.brews.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.comparators.BeerStyleComparator;
import com.biermacht.brews.utils.comparators.IngredientComparator;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.xml.BeerXmlReader;
import com.biermacht.brews.xml.BsmxXmlReader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class IngredientHandler {

  private Context mContext;
  private ArrayList<Ingredient> fermentablesList;
  private ArrayList<Ingredient> yeastsList;
  private ArrayList<Ingredient> hopsList;
  private ArrayList<Ingredient> miscsList;
  private ArrayList<BeerStyle> styleList;
  private ArrayList<MashProfile> profileList;

  public IngredientHandler(Context c) {
    this.mContext = c;
  }

  // First time use - put ingredients and mash profiles into SQLite
  public void ImportAssets() {
    try {
      // Import all ingredient assets.
      this.importIngredients();

      // Import mash profile assets.
      Database.addMashProfileListToVirtualDatabase(Constants.DATABASE_CUSTOM,
                                                   getProfilesFromXml(),
                                                   Constants.OWNER_NONE,
                                                   Constants.SNAPSHOT_NONE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Imports ingredient assets only (not mash profiles).
  public void importIngredients() throws IOException {
    Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT,
                                                getFermentablesFromXml(),
                                                Constants.OWNER_NONE,
                                                Constants.SNAPSHOT_NONE);

    Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT,
                                                getYeastsFromXml(),
                                                Constants.OWNER_NONE,
                                                Constants.SNAPSHOT_NONE);

    Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT,
                                                getHopsFromXml(),
                                                Constants.OWNER_NONE,
                                                Constants.SNAPSHOT_NONE);

    Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT,
                                                getMiscsFromXml(),
                                                Constants.OWNER_NONE,
                                                Constants.SNAPSHOT_NONE);
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
    fermentablesList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_CUSTOM,
                                                                       Ingredient.FERMENTABLE));
    fermentablesList.addAll(Database.getIngredientsFromVirtualDatabase(Constants
                                                                               .DATABASE_PERMANENT, Ingredient.FERMENTABLE));

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
    yeastsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_CUSTOM,
                                                                 Ingredient.YEAST));
    yeastsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_PERMANENT,
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
    hopsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_CUSTOM,
                                                               Ingredient.HOP));
    hopsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_PERMANENT,
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
    miscsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_CUSTOM,
                                                                Ingredient.MISC));
    miscsList.addAll(Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_PERMANENT,
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
      try {
        this.styleList = getStylesFromXml();
        this.styleList.add(0, Constants.BEERSTYLE_OTHER);
      } catch (IOException e) {
        e.printStackTrace();
      }
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
    profileList.addAll(Database.getMashProfilesFromVirtualDatabase(Constants.DATABASE_CUSTOM));
    profileList.addAll(Database.getMashProfilesFromVirtualDatabase(Constants.DATABASE_PERMANENT));

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
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Fermentables")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Fermentables/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getFermentables());
        Collections.sort(list, new IngredientComparator());
      } catch (Exception e) {
        Log.e("getFermentablesFromXml", e.toString());
      }
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
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Yeasts")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Yeasts/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getYeasts());
        Collections.sort(list, new IngredientComparator());
      } catch (Exception e) {
        Log.e("getYeastsFromXml", e.toString());
      }
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
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Hops")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Hops/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getHops());
        Collections.sort(list, new IngredientComparator());
      } catch (Exception e) {
        Log.e("getHopsFromXml", e.toString());
      }
    }

    Log.d("IngredientHandler", "Got " + list.size() + " hops from XML");
    return list;
  }

  /**
   * Gets hops from XMl files in assets/Hops
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<BeerStyle> getStylesFromXml() throws IOException {
    ArrayList<BeerStyle> list = new ArrayList<BeerStyle>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Styles")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Styles/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getBeerStyles());
        Collections.sort(list, new BeerStyleComparator<BeerStyle>());
      } catch (Exception e) {
        Log.e("getStylesFromXml", e.toString());
      }
    }

    return list;
  }

  /**
   * Gets miscs from XMl files in assets/Hops
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  private ArrayList<Ingredient> getMiscsFromXml() throws IOException {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Miscs")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Miscs/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getMiscs());
        Collections.sort(list, new IngredientComparator());
      } catch (Exception e) {
        Log.e("getMiscsFromXml", e.toString());
      }
    }

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
  public ArrayList<Recipe> getRecipesFromXml(Uri uri) throws IOException {
    ArrayList<Recipe> retlist = new ArrayList<Recipe>();
    ArrayList<Recipe> list;
    BeerXmlReader beerXmlReader = new BeerXmlReader();
    BsmxXmlReader bsmxXmlReader = new BsmxXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    String path = uri.getPath().toString();

    try {
      ContentResolver cr = mContext.getContentResolver();
      SAXParser sp = spf.newSAXParser();
      InputStream is = cr.openInputStream(uri);

      // If this is a .bsmx, use the .bsmx parser.  Otherwise, assume it is BeerXML.
      if (path.endsWith(".bsmx")) {
        Log.d("IngredientHandler", "Attempting to parse .bsmx file");
        // .bsmx files generated by BeerSmith include HTML encoded characters without declaring
        // the encoding - pull in the whole file as a string and escape any HTML entities before
        // passing to the XML parser.
        String escaped = StringEscapeUtils.unescapeHtml4(Utils.convertStreamToString(is));

        // Re-escape things properly as XML.
        escaped = StringEscapeUtils.escapeXml11(escaped);
        escaped = escaped.replaceAll("&lt;", "<");
        escaped = escaped.replaceAll("&gt;", ">");

        // Now, parse the String.
        sp.parse(new InputSource(new StringReader(escaped)), bsmxXmlReader);
        list = bsmxXmlReader.getRecipes();
      }
      else {
        Log.d("IngredientHandler", "Attempting to parse BeerXML file");
        InputSource inSrc = new InputSource(new StringReader(Utils.convertStreamToString(is)));
        inSrc.setEncoding("ISO-8859-2");
        sp.parse(inSrc, beerXmlReader);
        list = beerXmlReader.getRecipes();
      }

      retlist.addAll(list);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("getRecipesFromXml", "Failed to load file '" + path + "' : " + e.toString());
    }

    return retlist;
  }

  /**
   * Gets MashProfiles from XMl files in assets/Profiles
   *
   * @return ArrayList of MashProfile Objects
   * @throws IOException
   */
  private ArrayList<MashProfile> getProfilesFromXml() throws IOException {
    ArrayList<MashProfile> list = new ArrayList<MashProfile>();
    BeerXmlReader myXMLHandler = new BeerXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    AssetManager am = mContext.getAssets();

    for (String s : am.list("Profiles")) {
      try {
        SAXParser sp = spf.newSAXParser();
        InputStream is = am.open("Profiles/" + s);
        sp.parse(is, myXMLHandler);

        list.addAll(myXMLHandler.getMashProfiles());
      } catch (Exception e) {
        Log.e("getProfilesFromXml", e.toString());
      }
    }
    return list;
  }
}
