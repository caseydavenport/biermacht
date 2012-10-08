package com.biermacht.brews.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Hop;
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
			DatabaseHelper.REC_COL_COLOR,
			DatabaseHelper.REC_COL_BOIL_TIME
			};
	
	private String[] ingredientAllColumns = {
			DatabaseHelper.ING_COL_ID,
			DatabaseHelper.ING_COL_OWNER_ID,
			DatabaseHelper.ING_COL_TYPE,
			DatabaseHelper.ING_COL_NAME,
			DatabaseHelper.ING_COL_UNIT,
			DatabaseHelper.ING_COL_AMT,
			DatabaseHelper.ING_COL_BOIL_START_TIME,
			DatabaseHelper.ING_COL_BOIL_END_TIME,
			
			DatabaseHelper.ING_GR_COL_WEIGHT,
			DatabaseHelper.ING_GR_COL_COLOR,
			DatabaseHelper.ING_GR_COL_GRAV,
			DatabaseHelper.ING_GR_COL_TYPE,
			DatabaseHelper.ING_GR_COL_EFF,
			
			DatabaseHelper.ING_HP_COL_DESC,
			DatabaseHelper.ING_HP_COL_ACID,
			DatabaseHelper.ING_HP_COL_TYPE
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
		values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
		
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
		values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
		
		deleteIngredientList(r.getId());
		addIngredientListToDatabase(r.getIngredientList(), r.getId());
		
		return database.update(DatabaseHelper.TABLE_RECIPES, values, whereClause, null) > 0;
	}
	
	public boolean updateExistingIngredient(Ingredient ing)
	{
		Log.e("DBI", "Updating ingredient with ID: " + ing.getId());
		String whereClause = DatabaseHelper.ING_COL_ID + "=" + ing.getId();
		
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ING_COL_OWNER_ID, ing.getOwnerId());
		values.put(DatabaseHelper.ING_COL_TYPE, ing.getType());
		values.put(DatabaseHelper.ING_COL_NAME, ing.getName());
		values.put(DatabaseHelper.ING_COL_UNIT, ing.getUnit());
		values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
		values.put(DatabaseHelper.ING_COL_BOIL_START_TIME, ing.getBoilStartTime());
		values.put(DatabaseHelper.ING_COL_BOIL_END_TIME, ing.getBoilEndTime());
		
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
		
		// Hop specific values
		if (ing.getType().equals(Ingredient.HOP))
		{
			Hop hop = (Hop) ing;
			values.put(DatabaseHelper.ING_HP_COL_DESC, hop.getDescription());
			values.put(DatabaseHelper.ING_HP_COL_ACID, hop.getAlphaAcidContent());
			values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getHopType());
		}
		
		return database.update(DatabaseHelper.TABLE_INGREDIENTS, values, whereClause, null) > 0;
	}
	
	private boolean deleteIngredientList(long id) {
		String whereClause = DatabaseHelper.ING_COL_OWNER_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
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
			values.put(DatabaseHelper.ING_COL_BOIL_START_TIME, ing.getBoilStartTime());
			values.put(DatabaseHelper.ING_COL_BOIL_END_TIME, ing.getBoilEndTime());
			
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
			
			// Hop specific values
			if (ing.getType().equals(Ingredient.HOP))
			{
				Hop hop = (Hop) ing;
				values.put(DatabaseHelper.ING_HP_COL_DESC, hop.getDescription());
				values.put(DatabaseHelper.ING_HP_COL_ACID, hop.getAlphaAcidContent());
				values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getHopType());
			}
			
			database.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);
			values.clear();
		}
	}

	/**
	 * Takes recipe hashCode as input, deletes if it exists
	 * @param id
	 * @return if it was deleted or not
	 */
	public boolean deleteRecipeIfExists(long id)
	{
		String whereClause = DatabaseHelper.REC_COL_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_RECIPES, whereClause, null) > 0;
	}
	

	public boolean deleteIngredientIfExists(long id) {
		String whereClause = DatabaseHelper.ING_COL_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
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
	
	public Ingredient getIngredientWithId(long id)
	{
		Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns, null, null, null, null, null);
		cursor.moveToFirst();
		Ingredient i = cursorToIngredient(cursor);
		
		while(i.getId() != id)
		{
			i = cursorToIngredient(cursor);
			cursor.moveToNext();
			
			if(cursor.isAfterLast() || i.getId() == id)
				break;
		}
		return i;
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
		int boilTime = cursor.getInt(10);
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
		r.setBoilTime(boilTime);
		
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
		float ingStartTime = cursor.getFloat(6);
		int ingEndTime = cursor.getInt(7);
		
		// Grain specific stuff
		if (ingType.equals(Ingredient.GRAIN))
		{
			float grainWeight = cursor.getFloat(8);
			float grainColor = cursor.getFloat(9);
			float grainGrav = cursor.getFloat(10);
			String grainType = cursor.getString(11);
			float grainEff = cursor.getFloat(12);
			
			Grain grain = new Grain(ingName);
			grain.setId(id);
			grain.setOwnerId(ownerId);
			grain.setUnit(ingUnit);
			grain.setAmount(ingAmount);
			grain.setBoilStartTime(ingStartTime);
			grain.setBoilEndTime(ingEndTime);
			grain.setWeight(grainWeight);
			grain.setLovibondColor(grainColor);
			grain.setGravity(grainGrav);
			grain.setGrainType(grainType);
			grain.setEfficiency(grainEff);
			
			return grain;
		}
		
		// Hop specific stuff
		if (ingType.equals(Ingredient.HOP))
		{
			String hopDesc = cursor.getString(13);
			float hopAcid = cursor.getFloat(14);
			String hopType = cursor.getString(15);
			
			Hop hop = new Hop(ingName);
			hop.setId(id);
			hop.setOwnerId(ownerId);
			hop.setUnit(ingUnit);
			hop.setAmount(ingAmount);
			hop.setBoilStartTime(ingStartTime);
			hop.setBoilEndTime(ingEndTime);
			hop.setDescription(hopDesc);
			hop.setAlphaAcidContent(hopAcid);
			hop.setHopType(hopType);
			
			return hop;
		}
		
		return new Grain("NO DATA READ");
	}

}
