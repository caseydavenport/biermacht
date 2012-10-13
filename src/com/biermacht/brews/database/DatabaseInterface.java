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
			DatabaseHelper.ING_COL_DESC,
			DatabaseHelper.ING_COL_UNITS,
			DatabaseHelper.ING_COL_AMT,
			DatabaseHelper.ING_COL_START_TIME,
			DatabaseHelper.ING_COL_END_TIME,
			
			DatabaseHelper.ING_FR_COL_TYPE,
			DatabaseHelper.ING_FR_COL_YIELD,
			DatabaseHelper.ING_FR_COL_COLOR,
			DatabaseHelper.ING_FR_COL_ADD_AFTER_BOIL,
			DatabaseHelper.ING_FR_COL_MAX_IN_BATCH,
			DatabaseHelper.ING_FR_COL_GRAV,
			
			DatabaseHelper.ING_HP_COL_TYPE,
			DatabaseHelper.ING_HP_COL_ALPHA,
			DatabaseHelper.ING_HP_COL_USE,
			DatabaseHelper.ING_HP_COL_TIME,
			DatabaseHelper.ING_HP_COL_FORM,
			DatabaseHelper.ING_HP_COL_ORIGIN
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
		values.put(DatabaseHelper.REC_COL_VER, r.getVersion());
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
		values.put(DatabaseHelper.REC_COL_VER, r.getVersion());
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
		values.put(DatabaseHelper.ING_COL_DESC, ing.getShortDescription());
		values.put(DatabaseHelper.ING_COL_UNITS, ing.getUnits());
		values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
		values.put(DatabaseHelper.ING_COL_START_TIME, ing.getStartTime());
		values.put(DatabaseHelper.ING_COL_END_TIME, ing.getEndTime());
		
		// Grain specific values
		if (ing.getType().equals(Ingredient.FERMENTABLE))
		{
			Fermentable fer = (Fermentable) ing;
			values.put(DatabaseHelper.ING_FR_COL_TYPE, fer.getFermentableType());
			values.put(DatabaseHelper.ING_FR_COL_YIELD, fer.getYield());
			values.put(DatabaseHelper.ING_FR_COL_COLOR, fer.getLovibondColor());
			values.put(DatabaseHelper.ING_FR_COL_ADD_AFTER_BOIL, fer.isAddAfterBoil());
			values.put(DatabaseHelper.ING_FR_COL_MAX_IN_BATCH, fer.getMaxInBatch());
			values.put(DatabaseHelper.ING_FR_COL_GRAV, fer.getGravity());
		}
		
		// Hop specific values
		if (ing.getType().equals(Ingredient.HOP))
		{
			Hop hop = (Hop) ing;
			values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getHopType());
			values.put(DatabaseHelper.ING_HP_COL_ALPHA, hop.getAlphaAcidContent());
			values.put(DatabaseHelper.ING_HP_COL_USE, hop.getUse());
			values.put(DatabaseHelper.ING_HP_COL_TIME, hop.getTime());
			values.put(DatabaseHelper.ING_HP_COL_FORM, hop.getForm());
			values.put(DatabaseHelper.ING_HP_COL_ORIGIN, hop.getOrigin());
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
			// Load up values to store
			values.put(DatabaseHelper.ING_COL_OWNER_ID, id);
			values.put(DatabaseHelper.ING_COL_TYPE, ing.getType());
			values.put(DatabaseHelper.ING_COL_NAME, ing.getName());
			values.put(DatabaseHelper.ING_COL_DESC, ing.getShortDescription());
			values.put(DatabaseHelper.ING_COL_UNITS, ing.getUnits());
			values.put(DatabaseHelper.ING_COL_AMT, ing.getAmount());
			values.put(DatabaseHelper.ING_COL_START_TIME, ing.getStartTime());
			values.put(DatabaseHelper.ING_COL_END_TIME, ing.getEndTime());
			
			// Grain specific values
			if (ing.getType().equals(Ingredient.FERMENTABLE))
			{
				Fermentable fer = (Fermentable) ing;
				values.put(DatabaseHelper.ING_FR_COL_TYPE, fer.getFermentableType());
				values.put(DatabaseHelper.ING_FR_COL_YIELD, fer.getYield());
				values.put(DatabaseHelper.ING_FR_COL_COLOR, fer.getLovibondColor());
				values.put(DatabaseHelper.ING_FR_COL_ADD_AFTER_BOIL, fer.isAddAfterBoil());
				values.put(DatabaseHelper.ING_FR_COL_MAX_IN_BATCH, fer.getMaxInBatch());
				values.put(DatabaseHelper.ING_FR_COL_GRAV, fer.getGravity());
			}
			
			// Hop specific values
			if (ing.getType().equals(Ingredient.HOP))
			{
				Hop hop = (Hop) ing;
				values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getHopType());
				values.put(DatabaseHelper.ING_HP_COL_ALPHA, hop.getAlphaAcidContent());
				values.put(DatabaseHelper.ING_HP_COL_USE, hop.getUse());
				values.put(DatabaseHelper.ING_HP_COL_TIME, hop.getTime());
				values.put(DatabaseHelper.ING_HP_COL_FORM, hop.getForm());
				values.put(DatabaseHelper.ING_HP_COL_ORIGIN, hop.getOrigin());
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
		r.setVersion(version);
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
		int cid = 0;
		
		// Ingredient type agnostic stuff
		long id = cursor.getLong(cid);                          cid++;
		long ownerId = cursor.getLong(cid);						cid++;
		String ingType = cursor.getString(cid);					cid++;
		String name = cursor.getString(cid);					cid++;
		String description = cursor.getString(cid);				cid++;
		String units = cursor.getString(cid);					cid++;
		float amount = cursor.getFloat(cid);					cid++;
		int startTime = cursor.getInt(cid);						cid++;
		int endTime = cursor.getInt(cid);						cid++;	
		
		Log.e("DBI", "Creating ingredient from cursor... " + name + " oid: " + cursor.getString(1));
		
		// Fermentable specific stuff
		if (ingType.equals(Ingredient.FERMENTABLE))
		{
			String type = cursor.getString(cid);				cid++;
			float yield = cursor.getFloat(cid);					cid++;
			float color = cursor.getFloat(cid);					cid++;
			int afterBoil = cursor.getInt(cid);					cid++;
			float maxInBatch = cursor.getFloat(cid);			cid++;
			float gravity = cursor.getFloat(cid);				cid++;			
			
			boolean addAfterBoil = (afterBoil == 0) ? false : true;
			
			Fermentable fer = new Fermentable(name);
			fer.setId(id);
			fer.setOwnerId(ownerId);
			fer.setShortDescription(description);
			fer.setUnits(units);
			fer.setAmount(amount);
			fer.setStartTime(startTime);
			fer.setEndTime(endTime);
			fer.setFermentableType(type);
			fer.setYield(yield);
			fer.setLovibondColor(color);
			fer.setAddAfterBoil(addAfterBoil);
			fer.setMaxInBatch(maxInBatch);
			fer.setGravity(gravity);
			
			return fer;
		}
		
		// Hop specific stuff
		else if (ingType.equals(Ingredient.HOP))
		{
			cid += 6;
			
			String type = cursor.getString(cid);				cid++;
			float alpha = cursor.getFloat(cid);					cid++;
			String use = cursor.getString(cid);					cid++;
			int time = cursor.getInt(cid);						cid++;
			String form = cursor.getString(cid);				cid++;
			String origin = cursor.getString(cid);				cid++;
			
			Hop hop = new Hop(name);
			hop.setId(id);
			hop.setOwnerId(ownerId);
			hop.setShortDescription(description);
			hop.setUnits(units);
			hop.setAmount(amount);
			hop.setStartTime(startTime);
			hop.setEndTime(endTime);
			hop.setHopType(type);
			hop.setAlphaAcidContent(alpha);
			hop.setUse(use);
			hop.setTime(time);
			hop.setForm(form);
			hop.setOrigin(origin);
			
			return hop;
		}
		
		return new Fermentable("NO DATA READ");
	}

}
