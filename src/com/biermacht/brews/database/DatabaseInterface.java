package com.biermacht.brews.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.recipe.Recipe;

public class DatabaseInterface {
	
	// Database Fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] recipeAllColumns = {
			DatabaseHelper.REC_COL_ID,
			DatabaseHelper.REC_COL_NAME,
			DatabaseHelper.REC_COL_DESC,
			DatabaseHelper.REC_COL_TYPE,
			DatabaseHelper.REC_COL_TIME,
			DatabaseHelper.REC_COL_VOL,
			DatabaseHelper.REC_COL_GRAV,
			DatabaseHelper.REC_COL_ABV,
			DatabaseHelper.REC_COL_BITTER,
			DatabaseHelper.REC_COL_COLOR
			};
	
	private String[] ingredientAllColumns = {
			DatabaseHelper.ING_COL_ID,
			DatabaseHelper.ING_COL_OWNER_ID,
			DatabaseHelper.ING_COL_TYPE,
			DatabaseHelper.ING_COL_NAME,
			DatabaseHelper.ING_COL_UNIT,
			DatabaseHelper.ING_COL_AMT,
			DatabaseHelper.ING_COL_TIME,
			DatabaseHelper.ING_GR_COL_WEIGHT,
			DatabaseHelper.ING_GR_COL_COLOR,
			DatabaseHelper.ING_GR_COL_GRAV,
			DatabaseHelper.ING_GR_COL_TYPE,
			DatabaseHelper.ING_GR_COL_EFF
			};
	
	// Constructor
	public DatabaseInterface(Context context)
	{
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public long addRecipeToDatabase(Recipe r)
	{
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.REC_COL_NAME, r.getRecipeName());
		values.put(DatabaseHelper.REC_COL_DESC, r.getDescription());
		values.put(DatabaseHelper.REC_COL_TYPE, r.getBeerType());
		values.put(DatabaseHelper.REC_COL_TIME, r.getBatchTime());
		values.put(DatabaseHelper.REC_COL_VOL, r.getVolume());
		values.put(DatabaseHelper.REC_COL_GRAV, r.getGravity());
		values.put(DatabaseHelper.REC_COL_ABV, r.getABV());
		values.put(DatabaseHelper.REC_COL_BITTER, r.getBitterness());
		values.put(DatabaseHelper.REC_COL_COLOR, r.getColor());
		
		long id = database.insert(DatabaseHelper.TABLE_RECIPES, null, values);
		addIngredientListToDatabase(r.getIngredientList(), id);
		
		return id;
	}
	
	public boolean updateExistingRecipe(Recipe r)
	{
		String whereClause = DatabaseHelper.REC_COL_ID + "=" + r.getId();
		
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.REC_COL_NAME, r.getRecipeName());
		values.put(DatabaseHelper.REC_COL_DESC, r.getDescription());
		values.put(DatabaseHelper.REC_COL_TYPE, r.getBeerType());
		values.put(DatabaseHelper.REC_COL_TIME, r.getBatchTime());
		values.put(DatabaseHelper.REC_COL_VOL, r.getVolume());
		values.put(DatabaseHelper.REC_COL_GRAV, r.getGravity());
		values.put(DatabaseHelper.REC_COL_ABV, r.getABV());
		values.put(DatabaseHelper.REC_COL_BITTER, r.getBitterness());
		values.put(DatabaseHelper.REC_COL_COLOR, r.getColor());
		addIngredientListToDatabase(r.getIngredientList(), r.getId());
		
		return database.update(DatabaseHelper.TABLE_RECIPES, values, whereClause, null) > 0;
	}
	
	private void addIngredientListToDatabase(ArrayList<Ingredient> ingredientList, long id) {
		
		// Load up values to store
		ContentValues values = new ContentValues();
		
		for (Ingredient ing : ingredientList)
		{
			values.put(DatabaseHelper.ING_COL_OWNER_ID, id);
			values.put(DatabaseHelper.ING_COL_TYPE, ing.getType());
			values.put(DatabaseHelper.ING_COL_NAME, ing.getName());
			values.put(DatabaseHelper.ING_COL_UNIT, ing.getUnit());
			values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
			values.put(DatabaseHelper.ING_COL_TIME, ing.getTime());
			
			// Grain specific values
			if (ing.getType().equals(Ingredient.GRAIN))
			{
				Grain gr = (Grain) ing;
				values.put(DatabaseHelper.ING_GR_COL_WEIGHT, gr.getWeight());
				values.put(DatabaseHelper.ING_GR_COL_COLOR, gr.getLovibondColor());
				values.put(DatabaseHelper.ING_GR_COL_GRAV, gr.getGravity());
				values.put(DatabaseHelper.ING_GR_COL_TYPE, gr.getGrainType());
				values.put(DatabaseHelper.ING_GR_COL_EFF, gr.getEfficiency());
			}
			
			database.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);
			values.clear();
		}
	}

	/**
	 * Takes recipe hashCode as input, deletes if it exists
	 * @param hash
	 * @return if it was deleted or not
	 */
	public boolean deleteRecipeIfExists(int hash)
	{
		String whereClause = DatabaseHelper.REC_COL_ID + "=" + hash;
		return database.delete(DatabaseHelper.TABLE_RECIPES, whereClause, null) > 0;
	}
	
	/**
	 * takes recipe ID and returns the recipe with that ID from the database
	 * @param id
	 * @return
	 */
	public Recipe getRecipeWithId(long id)
	{
		Recipe r = new Recipe("Database Problem");
		Cursor cursor = database.query(DatabaseHelper.TABLE_RECIPES, recipeAllColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(r.getId() != id)
		{
			r = cursorToRecipe(cursor);
			cursor.moveToNext();
			
			if(cursor.isAfterLast() || r.getId() == id)
				break;
		}
		return r;
	}
	
	/**
	 * returns arraylist of all recipes in database
	 * @return
	 */
	public ArrayList<Recipe> getRecipeList()
	{
		ArrayList<Recipe> list = new ArrayList<Recipe>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_RECIPES, recipeAllColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Recipe recipe = cursorToRecipe(cursor);
			list.add(recipe);
			cursor.moveToNext();
		}
		
		cursor.close();
		return list;
	}

	/**
	 * This takes a cursor and converts it into a Recipe object
	 * @param cursor
	 * @return
	 */
	private Recipe cursorToRecipe(Cursor cursor) {
		long id = cursor.getLong(0);
		String recipeName = cursor.getString(1);
		String recipeDesc = cursor.getString(2);
		String beerType = cursor.getString(3);
		int batchTime = cursor.getInt(4);
		float volume = cursor.getFloat(5);
		float gravity = cursor.getFloat(6);
		float ABV = cursor.getFloat(7);
		float bitterness = cursor.getFloat(8);
		float color = cursor.getFloat(9);
		ArrayList<Ingredient> ingredientsList = readIngredientsList(id);
		
		Recipe r = new Recipe(recipeName);
		r.setId(id);
		r.setDescription(recipeDesc);
		r.setBeerType(beerType);
		r.setBatchTime(batchTime);
		r.setVolume(volume);
		r.setGravity(gravity);
		r.setABV(ABV);
		r.setBitterness(bitterness);
		r.setColor(color);
		r.setIngredientsList(ingredientsList);
		
		return r;
	}

	private ArrayList<Ingredient> readIngredientsList(long id) {
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast())
		{
			Ingredient ing = cursorToIngredient(cursor);
			
			if (ing.getOwnerId() == id)
				list.add(ing);
			
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	private Ingredient cursorToIngredient(Cursor cursor) {

		// Ingredient type agnostic stuff
		long id = cursor.getLong(0);
		long ownerId = cursor.getLong(1);
		String ingType = cursor.getString(2);
		String ingName = cursor.getString(3);
		String ingUnit = cursor.getString(4);
		float ingAmount = cursor.getFloat(5);
		float ingTime = cursor.getFloat(6);
		
		// Grain specific stuff
		if (ingType.equals(Ingredient.GRAIN))
		{
			float grainWeight = cursor.getFloat(7);
			float grainColor = cursor.getFloat(8);
			float grainGrav = cursor.getFloat(9);
			String grainType = cursor.getString(10);
			float grainEff = cursor.getFloat(11);
			
			Grain grain = new Grain(ingName);
			grain.setId(id);
			grain.setOwnerId(ownerId);
			grain.setUnit(ingUnit);
			grain.setAmount(ingAmount);
			grain.setTime(ingTime);
			grain.setWeight(grainWeight);
			grain.setLovibondColor(grainColor);
			grain.setGravity(grainGrav);
			grain.setGrainType(grainType);
			grain.setEfficiency(grainEff);
			
			return grain;
		}
		
		return new Grain("NO DATA READ");
	}
}
