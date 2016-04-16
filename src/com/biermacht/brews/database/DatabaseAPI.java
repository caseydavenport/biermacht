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

  public ArrayList<RecipeSnapshot> getSnapshots(Recipe r) {
    ArrayList<RecipeSnapshot> list = new ArrayList<RecipeSnapshot>();

    // Get all the snapshots for the given recipe from the database.
    list.addAll(this.databaseInterface.getRecipeSnapshots(r.getId()));

    return list;
  }

  public RecipeSnapshot getSnapshot(long snapshotId) {
    return this.databaseInterface.getSnapshot(snapshotId);
  }

  /**
   * Saves the given RecipeSnapshot to the database.
   * @param snap
   */
  public long saveSnapshot(RecipeSnapshot snap) {
    return this.databaseInterface.addSnapshotToDatabase(snap, snap.getRecipeId());
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

  // Updates an existing RecipeSnapshot
  public boolean updateSnapshot(RecipeSnapshot snap) {
    snap.update();
    return this.databaseInterface.updateExistingSnapshot(snap);
  }

  // Updates existing ingredient
  public boolean updateIngredient(Ingredient i, long dbid) {
    return this.databaseInterface.updateExistingIngredientInDatabase(i, dbid);
  }

  // Deletes the given recipe if it exists in the database.
  public boolean deleteRecipe(Recipe r) {
    // Delete all of this Recipe's ingredients.
    for (Ingredient i : r.getIngredientList()) {
      deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
    }

    // Delete this Recipe's MashProfile
    r.getMashProfile().delete(this.context, Constants.DATABASE_DEFAULT);

    // Delete this Recipe's Snapshots
    for (RecipeSnapshot snap : this.getSnapshots(r)) {
      deleteSnapshot(snap);
    }

    // Delete this Recipe's style
    this.databaseInterface.deleteStyleRecipe(r.getId());

    // And delete the recipe.
    return this.databaseInterface.deleteRecipe(r);
  }

  // Deletes the given snapshot if it exists in the database.
  public boolean deleteSnapshot(RecipeSnapshot snapshot) {
    // Delete ingredients.
    for (Ingredient i : snapshot.getIngredientList()) {
      deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
    }

    // Delete MashProfile
    snapshot.getMashProfile().delete(this.context, Constants.DATABASE_DEFAULT);

    // Delete style
    this.databaseInterface.deleteStyleSnapshot(snapshot.getId());

    // And delete the Snapshot
    return this.databaseInterface.deleteSnapshot(snapshot);
  }

  /**
   * Deletes all recipes in the Database, as well as any Ingredients associated
   * with them.
   * @return
   */
  public void deleteAllRecipes() {
    for (Recipe r : getRecipeList()) {
      deleteRecipe(r);
    }
  }

  // Deletes the given ingredient, in the given database
  public boolean deleteIngredientWithId(long id, long dbid) {
    Log.d("Database", "Trying to delete ingredient from database: " + dbid);
    boolean b = this.databaseInterface.deleteIngredientIfExists(id, dbid);
    if (b) {
      Log.d("Database", "  Successfully deleted ingredient");
    }
    else {
      Log.d("Database", "  Failed to delete ingredient");
    }
    return b;
  }

  // Gets the recipe with the given ID
  public Recipe getRecipeWithId(long id) throws ItemNotFoundException {
    // If we receive a special ID, handle that here
    if (id == Constants.INVALID_ID) {
      throw new ItemNotFoundException();
    }

    // Actually perform the lookup
    return this.databaseInterface.getRecipeWithId(id);
  }

  // Gets the ingredient with the given ID
  public Ingredient getIngredientWithId(long id) {
    return this.databaseInterface.getIngredientWithId(id);
  }

  // Adds a list of ingredients to the specified virtual ingredient database
  public void addIngredientList(long dbid, ArrayList<Ingredient> list, long recipeId, long snapshotId) {
    for (Ingredient i : list) {
      this.databaseInterface.addIngredientToDatabase(i, recipeId, dbid, snapshotId);
    }
  }

  // Adds a single ingredient to the specified virtual ingredient database
  public void addIngredient(long dbid, Ingredient i, long recipeId, long snapshotId) {
    this.databaseInterface.addIngredientToDatabase(i, recipeId, dbid, snapshotId);
  }

  // Returns all ingredients in the given virtual database with the given ingredient type
  public ArrayList<Ingredient> getIngredients(long dbid, String type) {
    return this.databaseInterface.getIngredients(dbid, type);
  }

  // Returns all ingredients in the given database with the given ingredient type
  public ArrayList<Ingredient> getIngredients(long dbid) {
    return this.databaseInterface.getIngredients(dbid);
  }

  public void addMashProfileList(long dbid, ArrayList<MashProfile> list, long recipeId, long snapshotId) {
    for (MashProfile p : list) {
      this.databaseInterface.addMashProfileToDatabase(p, recipeId, snapshotId, dbid);
    }
  }

  public long addMashProfile(long dbid, MashProfile p, long recipeId, long snapshotId) {
    return this.databaseInterface.addMashProfileToDatabase(p, recipeId, snapshotId, dbid);
  }

  public void updateMashProfile(MashProfile p, long recipeId, long snapshotId, long dbid) {
    this.databaseInterface.updateMashProfile(p, recipeId, dbid, snapshotId);
  }

  // Returns all mash profiles in the given database
  public ArrayList<MashProfile> getMashProfiles(long dbid) {
    return this.databaseInterface.getMashProfiles(dbid);
  }

  // Deletes the given mash profile
  public boolean deleteMashProfile(MashProfile p, long dbid) {
    return this.databaseInterface.deleteMashProfile(p.getId(), dbid);
  }
}
