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
import com.biermacht.brews.xml.RecipeXmlReader;

import java.io.IOException;
import java.io.InputStream;
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

  // First time use - put ingredients into SQLite
  public void ImportAssets() {
    try {
      Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT,
                                                  getFermentablesFromXml(), Constants.OWNER_NONE);
      Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT, getYeastsFromXml
              (), Constants.OWNER_NONE);
      Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT, getHopsFromXml(),
                                                  Constants.OWNER_NONE);
      Database.addIngredientListToVirtualDatabase(Constants.DATABASE_PERMANENT, getMiscsFromXml()
              , Constants.OWNER_NONE);
      Database.addMashProfileListToVirtualDatabase(Constants.DATABASE_CUSTOM, getProfilesFromXml
              (), Constants.OWNER_NONE);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
   * Gets Recipes from XMl file passed in path s
   *
   * @return ArrayList of Ingredient Objects
   * @throws IOException
   */
  public ArrayList<Recipe> getRecipesFromXml(String s) throws IOException {
    ArrayList<Recipe> retlist = new ArrayList<Recipe>();
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
    SAXParserFactory spf = SAXParserFactory.newInstance();

    try {
      ContentResolver cr = mContext.getContentResolver();
      SAXParser sp = spf.newSAXParser();
      InputStream is = cr.openInputStream((Uri.parse("file://" + s)));
      sp.parse(is, myXMLHandler);

      ArrayList<Recipe> list = myXMLHandler.getRecipes();
      retlist.addAll(list);
    } catch (Exception e) {
      Log.e("getRecipesFromXml", e.toString());
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
    RecipeXmlReader myXMLHandler = new RecipeXmlReader();
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
