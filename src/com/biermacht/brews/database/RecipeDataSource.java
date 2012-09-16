package com.biermacht.brews.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biermacht.brews.recipe.Recipe;

public class RecipeDataSource {
	
	// Database Fields
	private SQLiteDatabase database;
	private RecipeDbHelper dbHelper;
	private String[] allColumns = {
			RecipeDbHelper.COL_ID,
			RecipeDbHelper.COL_NAME,
			RecipeDbHelper.COL_DESC,
			RecipeDbHelper.COL_TYPE,
			RecipeDbHelper.COL_TIME,
			RecipeDbHelper.COL_VOL,
			RecipeDbHelper.COL_GRAV,
			RecipeDbHelper.COL_ABV,
			RecipeDbHelper.COL_BITTER,
			RecipeDbHelper.COL_COLOR,
			RecipeDbHelper.COL_INGRED,
			RecipeDbHelper.COL_INSTRUC 
			};
	
	// Constructor
	public RecipeDataSource(Context context)
	{
		dbHelper = new RecipeDbHelper(context);
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
		values.put(RecipeDbHelper.COL_NAME, r.getRecipeName());
		values.put(RecipeDbHelper.COL_DESC, r.getDescription());
		values.put(RecipeDbHelper.COL_TYPE, r.getBeerType());
		values.put(RecipeDbHelper.COL_TIME, r.getBatchTime());
		values.put(RecipeDbHelper.COL_VOL, r.getVolume());
		values.put(RecipeDbHelper.COL_GRAV, r.getGravity());
		values.put(RecipeDbHelper.COL_ABV, r.getABV());
		values.put(RecipeDbHelper.COL_BITTER, r.getBitterness());
		values.put(RecipeDbHelper.COL_COLOR, r.getColor());
		values.put(RecipeDbHelper.COL_INGRED, 1);
		values.put(RecipeDbHelper.COL_INSTRUC, 1);
		
		// Insert a new row
		return database.insert(RecipeDbHelper.TABLE_NAME, null, values);
	}
	
	public long deleteRecipeIfExists(long id)
	{
		String whereClause = RecipeDbHelper.COL_ID + "='" + id +"'";
		database.delete(RecipeDbHelper.TABLE_NAME, null, null);
		return database.delete(RecipeDbHelper.TABLE_NAME, whereClause, null);
	}
	
	public Recipe getRecipeWithId(long id)
	{
		Recipe r = new Recipe("Database Problem");
		Cursor cursor = database.query(RecipeDbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
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
	
	public ArrayList<Recipe> getRecipeList()
	{
		ArrayList<Recipe> list = new ArrayList<Recipe>();
		// TODO: SORT ALPHABETICALLY
		Cursor cursor = database.query(RecipeDbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		
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
		int ingredientListId = cursor.getInt(10);
		int instructionListId = cursor.getInt(11);
		
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
		
		Log.e("COLOR", "COLOR FROM DB: " + color);
		
		// TODO: GET INGREDIENT AND INSTRUCTION INFORMATION
		
		return r;
	}

}
