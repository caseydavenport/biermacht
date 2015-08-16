package com.biermacht.brews.database;

import android.content.Context;
import android.util.Log;

import com.biermacht.brews.exceptions.ItemNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.comparators.RecipeComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Wrapper around the DatabaseInterface class that exposes higher-level database APIs.
 */
public class DatabaseAPI {

  public static DatabaseInterface newDatabaseInterface(Context c) {
    return new DatabaseInterface(c);
  }

  /**
   * Returns all Recipes in the database sorted alphabetically.
   * @return Sorted list of Recipe objects.
   */
  public static ArrayList<Recipe> getRecipeList() {
    ArrayList<Recipe> list = MainActivity.databaseInterface.getRecipeList();

    for (Recipe r : list) {
      r.update();
    }
    Collections.sort(list, new RecipeComparator<Recipe>());

    return list;
  }

  public static ArrayList<RecipeSnapshot> getSnapshots(Recipe r) {
    ArrayList<RecipeSnapshot> list = new ArrayList<RecipeSnapshot>();

    // Get all the snapshots for the given recipe from the database.
    list.addAll(MainActivity.databaseInterface.getRecipeSnapshots(r.getId()));

    return list;
  }

  public static RecipeSnapshot getSnapshot(long snapshotId) {
    return MainActivity.databaseInterface.getSnapshot(snapshotId);
  }

  /**
   * Saves the given RecipeSnapshot to the database.
   * @param snap
   */
  public static long saveSnapshot(RecipeSnapshot snap) {
    return MainActivity.databaseInterface.addSnapshotToDatabase(snap, snap.getRecipeId());
  }

  // Create recipe with the given name
  public static Recipe createRecipeWithName(String name) {
    Recipe r = new Recipe(name);

    long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
    r = MainActivity.databaseInterface.getRecipeWithId(id);

    return r;
  }

  // Creates recipe from an existing one.
  public static Recipe createRecipeFromExisting(Recipe r) {
    long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
    r = MainActivity.databaseInterface.getRecipeWithId(id);

    return r;
  }

  // Updates existing recipe
  public static boolean updateRecipe(Recipe r) {
    r.update();
    return MainActivity.databaseInterface.updateExistingRecipe(r);
  }

  // Updates existing recipe
  public static boolean updateSnapshot(RecipeSnapshot snap) {
    snap.update();
    return MainActivity.databaseInterface.updateExistingSnapshot(snap);
  }

  // Updates existing ingredient
  public static boolean updateIngredient(Ingredient i, long dbid) {
    return MainActivity.databaseInterface.updateExistingIngredientInDatabase(i, dbid);
  }

  // Deletes the given recipe if it exists in the database.
  public static boolean deleteRecipe(Recipe r) {
    // Delete all of this Recipe's ingredients.
    for (Ingredient i : r.getIngredientList()) {
      deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
    }

    // Delete this Recipe's MashProfile
    r.getMashProfile().delete(Constants.DATABASE_DEFAULT);

    // Delete this Recipe's Snapshots
    for (RecipeSnapshot snap : DatabaseAPI.getSnapshots(r)) {
      deleteSnapshot(snap);
    }

    // Delete this Recipe's style
    MainActivity.databaseInterface.deleteStyleRecipe(r.getId());

    // And delete the recipe.
    return MainActivity.databaseInterface.deleteRecipe(r);
  }

  // Deletes the given snapshot if it exists in the database.
  public static boolean deleteSnapshot(RecipeSnapshot snapshot) {
    // Delete ingredients.
    for (Ingredient i : snapshot.getIngredientList()) {
      deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
    }

    // Delete MashProfile
    snapshot.getMashProfile().delete(Constants.DATABASE_DEFAULT);

    // Delete style
    MainActivity.databaseInterface.deleteStyleSnapshot(snapshot.getId());

    // And delete the Snapshot
    return MainActivity.databaseInterface.deleteSnapshot(snapshot);
  }

  /**
   * Deletes all recipes in the Database, as well as any Ingredients associated
   * with them.
   * @return
   */
  public static void deleteAllRecipes() {
    for (Recipe r : getRecipeList()) {
      deleteRecipe(r);
    }
  }

  // Deletes the given ingredient, in the given database
  public static boolean deleteIngredientWithId(long id, long dbid) {
    Log.d("Database", "Trying to delete ingredient from database: " + dbid);
    boolean b = MainActivity.databaseInterface.deleteIngredientIfExists(id, dbid);
    if (b) {
      Log.d("Database", "  Successfully deleted ingredient");
    }
    else {
      Log.d("Database", "  Failed to delete ingredient");
    }
    return b;
  }

  // Gets the recipe with the given ID
  public static Recipe getRecipeWithId(long id) throws ItemNotFoundException {
    // If we receive a special ID, handle that here
    if (id == Constants.INVALID_ID) {
      throw new ItemNotFoundException();
    }

    // Actually perform the lookup
    return MainActivity.databaseInterface.getRecipeWithId(id);
  }

  // Gets the ingredient with the given ID
  public static Ingredient getIngredientWithId(long id) {
    return MainActivity.databaseInterface.getIngredientWithId(id);
  }

  // Adds a list of ingredients to the specified virtual ingredient database
  public static void addIngredientListToVirtualDatabase(long dbid, ArrayList<Ingredient> list, long recipeId, long snapshotId) {
    for (Ingredient i : list) {
      MainActivity.databaseInterface.addIngredientToDatabase(i, recipeId, dbid, snapshotId);
    }
  }

  // Adds a single ingredient to the specified virtual ingredient database
  public static void addIngredientToVirtualDatabase(long dbid, Ingredient i, long recipeId, long snapshotId) {
    MainActivity.databaseInterface.addIngredientToDatabase(i, recipeId, dbid, snapshotId);
  }

  // Returns all ingredients in the given virtual database with the given ingredient type
  public static ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid, String type) {
    return MainActivity.databaseInterface.getIngredientsFromVirtualDatabase(dbid, type);
  }

  // Returns all ingredients in the given database with the given ingredient type
  public static ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid) {
    return MainActivity.databaseInterface.getIngredientsFromVirtualDatabase(dbid);
  }

  public static void addMashProfileListToVirtualDatabase(long dbid, ArrayList<MashProfile> list, long recipeId, long snapshotId) {
    for (MashProfile p : list) {
      MainActivity.databaseInterface.addMashProfileToDatabase(p, recipeId, snapshotId, dbid);
    }
  }

  public static long addMashProfileToVirtualDatabase(long dbid, MashProfile p, long recipeId, long snapshotId) {
    return MainActivity.databaseInterface.addMashProfileToDatabase(p, recipeId, snapshotId, dbid);
  }

  public static void updateMashProfile(MashProfile p, long recipeId, long snapshotId, long dbid) {
    MainActivity.databaseInterface.updateMashProfile(p, recipeId, dbid, snapshotId);
  }

  // Returns all mash profiles in the given database
  public static ArrayList<MashProfile> getMashProfilesFromVirtualDatabase(long dbid) {
    return MainActivity.databaseInterface.getMashProfilesFromVirtualDatabase(dbid);
  }

  // Deletes the given mash profile
  public static boolean deleteMashProfile(MashProfile p, long dbid) {
    return MainActivity.databaseInterface.deleteMashProfile(p.getId(), dbid);
  }
}
