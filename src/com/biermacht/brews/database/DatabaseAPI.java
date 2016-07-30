package com.biermacht.brews.database;

import android.content.Context;
import android.util.Log;

import com.biermacht.brews.exceptions.ItemNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.comparators.RecipeComparator;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseAPI {
  private Context context;
  private DatabaseInterface databaseInterface;

  public DatabaseAPI(Context c) {
    this.context = c;
    this.databaseInterface = new DatabaseInterface(this.context);
    this.databaseInterface.open();
  }

  // Get all recipes in database, sorted
  public ArrayList<Recipe> getRecipeList() {
    ArrayList<Recipe> list = this.databaseInterface.getRecipeList();

    for (Recipe r : list) {
      r.update();
    }

    Collections.sort(list, new RecipeComparator<Recipe>());

    return list;
  }

  // Create recipe with the given name
  public Recipe createRecipeWithName(String name) {
    Recipe r = new Recipe(name);

    long id = this.databaseInterface.addRecipeToDatabase(r);
    r = this.databaseInterface.getRecipeWithId(id);

    return r;
  }

  // Creates recipe from an existing one.
  public Recipe createRecipeFromExisting(Recipe r) {
    long id = this.databaseInterface.addRecipeToDatabase(r);
    r = this.databaseInterface.getRecipeWithId(id);

    return r;
  }

  // Updates existing recipe
  public boolean updateRecipe(Recipe r) {
    r.update();
    return this.databaseInterface.updateExistingRecipe(r);
  }

  // Updates existing ingredient
  public boolean updateIngredient(Ingredient i, long dbid) {
    return this.databaseInterface.updateExistingIngredientInDatabase(i, dbid);
  }

  // Deletes the given recipe if it exists in the database.
  public boolean deleteRecipe(Recipe r) {
    // Delete all ingredients for this recipe.
    for (Ingredient i : r.getIngredientList()) {
      this.deleteIngredientWithId(i.getId(), i.getDatabaseId());
    }

    // Delete the MashProfile.
    this.deleteMashProfileFromDatabase(r.getMashProfile(), Constants.DATABASE_DEFAULT);

    return this.databaseInterface.deleteRecipeIfExists(r.getId());
  }

  // Deletes all recipes, and their ingredients
  public boolean deleteAllRecipes() {
    boolean bool = true;

    for (Recipe r : getRecipeList()) {
      for (Ingredient i : r.getIngredientList()) {
        deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
      }
      bool = deleteRecipe(r);
    }
    return bool;
  }

  // Deletes the given ingredient, in the given database
  public boolean deleteIngredientWithId(long id, long dbid) {
    Log.d("Database", "Trying to delete ingredient from database: " + dbid);
    boolean b = this.databaseInterface.deleteIngredientIfExists(id, dbid);
    if (b) {
      Log.d("Database", "Successfully deleted ingredient");
    }
    else {
      Log.d("Database", "Failed to delete ingredient");
    }
    return b;
  }

  // Gets the recipe with the given ID
  public Recipe getRecipeWithId(long id) throws ItemNotFoundException {
    // If we receive a special ID, handle that here
    if (id == Constants.INVALID_ID) {
      throw new ItemNotFoundException("Passed ID with value Utils.INVALID_ID");
    }

    // Actually perform the lookup
    return this.databaseInterface.getRecipeWithId(id);
  }

  // Gets the ingredient with the given ID
  public Ingredient getIngredientWithId(long id) {
    return this.databaseInterface.getIngredientWithId(id);
  }

  // Adds a list of ingredients to the specified virtual ingredient database
  public void addIngredientList(long dbid, ArrayList<Ingredient> list, long ownerId) {
    for (Ingredient i : list) {
      this.databaseInterface.addIngredientToDatabase(i, ownerId, dbid);
    }
  }

  // Adds a single ingredient to the specified virtual ingredient database
  public void addIngredient(long dbid, Ingredient i, long ownerId) {
    this.databaseInterface.addIngredientToDatabase(i, ownerId, dbid);
  }

  // Returns all ingredients in the given virtual database with the given ingredient type
  public ArrayList<Ingredient> getIngredients(long dbid, String type) {
    return this.databaseInterface.getIngredients(dbid, type);
  }

  // Returns all ingredients in the given database with the given ingredient type
  public ArrayList<Ingredient> getIngredients(long dbid) {
    return this.databaseInterface.getIngredients(dbid);
  }

  public ArrayList<BeerStyle> getStyles(long dbid) {
    return this.databaseInterface.getBeerStyles(dbid);
  }

  public void addStyleList(long databaseId, ArrayList<BeerStyle> list, long ownerID) {
    for (BeerStyle s : list) {
      this.databaseInterface.addStyleToDatabase(s, databaseId, ownerID);
    }

  }

  public void addMashProfileList(long dbid, ArrayList<MashProfile> list, long ownerId) {
    for (MashProfile p : list) {
      this.databaseInterface.addMashProfileToDatabase(p, ownerId, dbid);
    }
  }

  public long addMashProfile(long dbid, MashProfile p, long ownerId) {
    return this.databaseInterface.addMashProfileToDatabase(p, ownerId, dbid);
  }

  public void updateMashProfile(MashProfile p, long ownerId, long dbid) {
    this.databaseInterface.updateMashProfile(p, ownerId, dbid);
  }

  // Returns all mash profiles in the given database
  public ArrayList<MashProfile> getMashProfiles(long dbid) {
    return this.databaseInterface.getMashProfiles(dbid);
  }

  // Deletes the given mash profile
  public boolean deleteMashProfileFromDatabase(MashProfile p, long dbid) {
    for (MashStep s : p.getMashStepList()) {
      this.databaseInterface.deleteMashStep(s.getId());
    }
    return this.databaseInterface.deleteMashProfile(p.getId(), dbid);
  }

  // Deletes the given style.
  public boolean deleteStyle(BeerStyle s) {
    return this.databaseInterface.deleteStyle(s);
  }
}
