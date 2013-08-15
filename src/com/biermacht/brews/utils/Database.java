package com.biermacht.brews.utils;

import android.util.Log;

import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.comparators.RecipeComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Database {

    // Get all recipes in database, sorted
	public static ArrayList<Recipe> getRecipeList(DatabaseInterface dbi)
	{
		ArrayList<Recipe> list = dbi.getRecipeList();
		
		for (Recipe r : list)
			r.update();
		
		Collections.sort(list, new RecipeComparator<Recipe>());
		
		return list;
	}
	
    // Create recipe with the given name
	public static Recipe createRecipeWithName(String name)
	{
		Recipe r = new Recipe(name);
		
		long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
		r = MainActivity.databaseInterface.getRecipeWithId(id);
		
		return r;
	}
	
    // Creates recipe from an existing one.
	public static Recipe createRecipeFromExisting(Recipe r)
	{
		long id = MainActivity.databaseInterface.addRecipeToDatabase(r);
		r = MainActivity.databaseInterface.getRecipeWithId(id);
		
		return r;
	}
	
    // Updates existing recipe
	public static boolean updateRecipe(Recipe r)
	{
		r.update();
		return MainActivity.databaseInterface.updateExistingRecipe(r);
	}
	
    // Updates existing ingredient
	public static boolean updateIngredient(Ingredient i)
	{
        return MainActivity.databaseInterface.updateExistingIngredient(i);
    }
	
    // Deletes the given recipe
	public static boolean deleteRecipe(Recipe r)
	{
		return MainActivity.databaseInterface.deleteRecipeIfExists(r.getId());
	}

    // Deletes all recipes, and their ingredients
	public static boolean deleteAllRecipes()
	{
		boolean bool = true;
		
		for (Recipe r : getRecipeList(MainActivity.databaseInterface))
		{
                for (Ingredient i : r.getIngredientList())
                {
                        deleteIngredientWithId(i.getId(), Constants.DATABASE_DEFAULT);
                }
                bool = deleteRecipe(r);
		}
		return bool;
	}

    // Deletes the given ingredient, in the given database
    public static boolean deleteIngredientWithId(long id, long dbid)
    {
        Log.d("deleteIngredient", "Trying to delete ingredient from database: " + dbid);
        boolean b = MainActivity.databaseInterface.deleteIngredientIfExists(id, dbid);
        if (b)
            Log.d("deleteIngredient", "Successfully deleted ingredient");
        else
            Log.d("deleteIngredient", "Failed to delete ingredient");
        return b;
    }
	
    // Gets the recipe with the given ID
	public static Recipe getRecipeWithId(long id) throws RecipeNotFoundException
	{
        // If we receive a special ID, handle that here
        if (id == Constants.INVALID_ID)
            throw new RecipeNotFoundException("Passed id with value Utils.INVALID_ID");

        // Actually perform the lookup
		return MainActivity.databaseInterface.getRecipeWithId(id);
	}

    // Gets the ingredient with the given ID
	public static Ingredient getIngredientWithId(long id)
	{
		return MainActivity.databaseInterface.getIngredientWithId(id);
	}

    // Adds a list of ingredients to the specified virtual ingredient database
    public static void addIngredientListToVirtualDatabase(long dbid, ArrayList<Ingredient> list, long ownerId)
    {
        for (Ingredient i : list)
        {
            MainActivity.databaseInterface.addIngredientToDatabase(i, ownerId, dbid);
        }
    }

    // Adds a single ingredient to the specified virtual ingredient database
    public static void addIngredientToVirtualDatabase(long dbid, Ingredient i, long ownerId)
    {
        MainActivity.databaseInterface.addIngredientToDatabase(i, ownerId, dbid);
    }

    // Returns all ingredients in the given virtual database with the given ingredient type
    public static ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid, String type)
    {
        return MainActivity.databaseInterface.getIngredientsFromVirtualDatabase(dbid, type);
    }

    // Returns all ingredients in the given database with the given ingredient type
    public static ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid)
    {
        return MainActivity.databaseInterface.getIngredientsFromVirtualDatabase(dbid);
    }

    // Adds a list of ingredients to the specified virtual ingredient database
    public static void addMashProfileListToVirtualDatabase(long dbid, ArrayList<MashProfile> list, long ownerId)
    {
        for (MashProfile p : list)
        {
            MainActivity.databaseInterface.addMashProfileToDatabase(p, ownerId, dbid);
        }
    }

    // Returns all mash profiles in the given database
    public static ArrayList<MashProfile> getMashProfilesFromVirtualDatabase(long dbid)
    {
        return MainActivity.databaseInterface.getMashProfilesFromVirtualDatabase(dbid);
    }
}
