package com.biermacht.brews.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class DatabaseInterface {
	
	// Database Fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	private String[] recipeAllColumns = {
			DatabaseHelper.REC_COL_ID,
			DatabaseHelper.REC_COL_NAME,
			DatabaseHelper.REC_COL_VER,
			DatabaseHelper.REC_COL_TYPE,
			DatabaseHelper.REC_COL_STYLE,
			DatabaseHelper.REC_COL_BREWER,
			DatabaseHelper.REC_COL_BATCH_SIZE,
			DatabaseHelper.REC_COL_BOIL_SIZE,
			DatabaseHelper.REC_COL_BOIL_TIME,
			DatabaseHelper.REC_COL_BOIL_EFF,
			DatabaseHelper.REC_COL_OG,
			DatabaseHelper.REC_COL_FG,
			DatabaseHelper.REC_COL_STAGES,
			DatabaseHelper.REC_COL_DESC,
			DatabaseHelper.REC_COL_BATCH_TIME,
			DatabaseHelper.REC_COL_ABV,
			DatabaseHelper.REC_COL_BITTER,
			DatabaseHelper.REC_COL_COLOR,
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
		values.put(DatabaseHelper.REC_COL_VER, 1);
		values.put(DatabaseHelper.REC_COL_TYPE, r.getType());
		values.put(DatabaseHelper.REC_COL_STYLE, r.getStyle());
		values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
		values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBatchSize());
		values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBoilSize());
		values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
		values.put(DatabaseHelper.REC_COL_BOIL_EFF, r.getEfficiency());
		values.put(DatabaseHelper.REC_COL_OG, r.getOG());
		values.put(DatabaseHelper.REC_COL_FG, r.getFG());
		values.put(DatabaseHelper.REC_COL_STAGES, r.getFermentationStages());
		values.put(DatabaseHelper.REC_COL_DESC, r.getDescription());
		values.put(DatabaseHelper.REC_COL_BATCH_TIME, r.getBatchTime());
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
		values.put(DatabaseHelper.REC_COL_VER, 1);
		values.put(DatabaseHelper.REC_COL_TYPE, r.getType());
		values.put(DatabaseHelper.REC_COL_STYLE, r.getStyle());
		values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
		values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBatchSize());
		values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBoilSize());
		values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
		values.put(DatabaseHelper.REC_COL_BOIL_EFF, r.getEfficiency());
		values.put(DatabaseHelper.REC_COL_OG, r.getOG());
		values.put(DatabaseHelper.REC_COL_FG, r.getFG());
		values.put(DatabaseHelper.REC_COL_STAGES, r.getFermentationStages());
		values.put(DatabaseHelper.REC_COL_DESC, r.getDescription());
		values.put(DatabaseHelper.REC_COL_BATCH_TIME, r.getBatchTime());
		values.put(DatabaseHelper.REC_COL_ABV, r.getABV());
		values.put(DatabaseHelper.REC_COL_BITTER, r.getBitterness());
		values.put(DatabaseHelper.REC_COL_COLOR, r.getColor());
		
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
		values.put(DatabaseHelper.ING_COL_UNIT, ing.getUnits());
		values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
		values.put(DatabaseHelper.ING_COL_BOIL_START_TIME, ing.getBoilStartTime());
		values.put(DatabaseHelper.ING_COL_BOIL_END_TIME, ing.getBoilEndTime());
		
		// Grain specific values
		if (ing.getType().equals(Ingredient.FERMENTABLE))
		{
			Fermentable gr = (Fermentable) ing;
			values.put(DatabaseHelper.ING_GR_COL_WEIGHT, gr.getAmount());
			values.put(DatabaseHelper.ING_GR_COL_COLOR, gr.getLovibondColor());
			values.put(DatabaseHelper.ING_GR_COL_GRAV, gr.getGravity());
			values.put(DatabaseHelper.ING_GR_COL_TYPE, gr.getFermentableType());
			values.put(DatabaseHelper.ING_GR_COL_EFF, gr.getEfficiency());
		}
		
		// Hop specific values
		if (ing.getType().equals(Ingredient.HOP))
		{
			Hop hop = (Hop) ing;
			values.put(DatabaseHelper.ING_HP_COL_DESC, hop.getDescription());
			values.put(DatabaseHelper.ING_HP_COL_ACID, hop.getAlphaAcidContent());
			values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getForm());
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
			values.put(DatabaseHelper.ING_COL_UNIT, ing.getUnits());
			values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
			values.put(DatabaseHelper.ING_COL_BOIL_START_TIME, ing.getBoilStartTime());
			values.put(DatabaseHelper.ING_COL_BOIL_END_TIME, ing.getBoilEndTime());
			
			// Grain specific values
			if (ing.getType().equals(Ingredient.FERMENTABLE))
			{
				Fermentable gr = (Fermentable) ing;
				values.put(DatabaseHelper.ING_GR_COL_WEIGHT, gr.getAmount());
				values.put(DatabaseHelper.ING_GR_COL_COLOR, gr.getLovibondColor());
				values.put(DatabaseHelper.ING_GR_COL_GRAV, gr.getGravity());
				values.put(DatabaseHelper.ING_GR_COL_TYPE, gr.getFermentableType());
				values.put(DatabaseHelper.ING_GR_COL_EFF, gr.getEfficiency());
			}
			
			// Hop specific values
			if (ing.getType().equals(Ingredient.HOP))
			{
				Hop hop = (Hop) ing;
				values.put(DatabaseHelper.ING_HP_COL_DESC, hop.getDescription());
				values.put(DatabaseHelper.ING_HP_COL_ACID, hop.getAlphaAcidContent());
				values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getForm());
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
		int cid = 0;
		
		long id = cursor.getLong(cid);                cid++;
		String recipeName = cursor.getString(cid);    cid++;
		int version = cursor.getInt(cid);             cid++;
		String type = cursor.getString(cid);          cid++;
		String style = cursor.getString(cid);         cid++;
		String brewer = cursor.getString(cid);        cid++;
		float batchSize = cursor.getFloat(cid);       cid++;
		float boilSize = cursor.getFloat(cid);        cid++;
		int boilTime = cursor.getInt(cid);            cid++;
		float boilEff = cursor.getFloat(cid);         cid++;
		float OG = cursor.getFloat(cid);              cid++;
		float FG = cursor.getFloat(cid);              cid++;
		int fermentationStages = cursor.getInt(cid);  cid++;
		String description = cursor.getString(cid);   cid++;
		int batchTime = cursor.getInt(cid);           cid++;
		float ABV = cursor.getFloat(cid);             cid++;
		float bitterness = cursor.getFloat(cid);      cid++;
		float color = cursor.getFloat(cid);           cid++;
		
		ArrayList<Ingredient> ingredientsList = readIngredientsList(id);
		
		Recipe r = new Recipe(recipeName);
		r.setId(id);
		r.setType(type);
		r.setStyle(style);
		r.setBrewer(brewer);
		r.setBatchSize(batchSize);
		r.setBoilSize(boilSize);
		r.setBoilTime(boilTime);
		r.setEfficiency(boilEff);
		r.setOG(OG);
		r.setFG(FG);
		r.setFermentationStages(fermentationStages);
		r.setDescription(description);
		r.setBatchTime(batchTime);
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
		float ingStartTime = cursor.getFloat(6);
		int ingEndTime = cursor.getInt(7);
		
		// Grain specific stuff
		if (ingType.equals(Ingredient.FERMENTABLE))
		{
			float grainWeight = cursor.getFloat(8);
			float grainColor = cursor.getFloat(9);
			float grainGrav = cursor.getFloat(10);
			String grainType = cursor.getString(11);
			float grainEff = cursor.getFloat(12);
			
			Fermentable grain = new Fermentable(ingName);
			grain.setId(id);
			grain.setOwnerId(ownerId);
			grain.setAmount(ingAmount);
			grain.setBoilStartTime(ingStartTime);
			grain.setBoilEndTime(ingEndTime);
			grain.setAmount(grainWeight);
			grain.setLovibondColor(grainColor);
			grain.setGravity(grainGrav);
			grain.setFermentableType(grainType);
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
			hop.setAmount(ingAmount);
			hop.setBoilStartTime(ingStartTime);
			hop.setBoilEndTime(ingEndTime);
			hop.setDescription(hopDesc);
			hop.setAlphaAcidContent(hopAcid);
			hop.setForm(hopType);
			
			return hop;
		}
		
		return new Fermentable("NO DATA READ");
	}

}
