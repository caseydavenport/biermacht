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
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.recipe.*;

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
			DatabaseHelper.ING_HP_COL_ORIGIN,
			
			DatabaseHelper.ING_YS_COL_TYPE,
			DatabaseHelper.ING_YS_COL_FORM,
			DatabaseHelper.ING_YS_COL_MIN_TEMP,
			DatabaseHelper.ING_YS_COL_MAX_TEMP,
			DatabaseHelper.ING_YS_COL_ATTENUATION,
			DatabaseHelper.ING_YS_COL_NOTES,
			DatabaseHelper.ING_YS_COL_BEST_FOR,
			
			DatabaseHelper.ING_MC_COL_TYPE,
			DatabaseHelper.ING_MC_COL_VERSION,
			DatabaseHelper.ING_MC_COL_AMT_IS_WEIGHT,
			DatabaseHelper.ING_MC_COL_USE_FOR
			
			};
		
	private String[] stylesAllColumns = {
		DatabaseHelper.STY_COL_ID,
		DatabaseHelper.STY_COL_OWNER_ID,
		DatabaseHelper.STY_COL_NAME,
		DatabaseHelper.STY_COL_CATEGORY,
		DatabaseHelper.STY_COL_CAT_NUM,
		DatabaseHelper.STY_COL_STY_LETTER,
		DatabaseHelper.STY_COL_STY_GUIDE,
		DatabaseHelper.STY_COL_TYPE,
		DatabaseHelper.STY_COL_OG_MIN,
		DatabaseHelper.STY_COL_OG_MAX,
		DatabaseHelper.STY_COL_FG_MIN,
		DatabaseHelper.STY_COL_FG_MAX,
		DatabaseHelper.STY_COL_IBU_MIN,
		DatabaseHelper.STY_COL_IBU_MAX,
		DatabaseHelper.STY_COL_SRM_MIN,
		DatabaseHelper.STY_COL_SRM_MAX,
		DatabaseHelper.STY_COL_CARB_MIN,
		DatabaseHelper.STY_COL_CARB_MAX,
		DatabaseHelper.STY_COL_ABV_MIN,
		DatabaseHelper.STY_COL_ABV_MAX,
		DatabaseHelper.STY_COL_NOTES,
		DatabaseHelper.STY_COL_PROFILE,
		DatabaseHelper.STY_COL_INGREDIENTS,
		DatabaseHelper.STY_COL_EXAMPLES
	};
	
	private String[] profileAllColumns = {
		DatabaseHelper.PRO_COL_ID,
		DatabaseHelper.PRO_COL_OWNER_ID,
	    DatabaseHelper.PRO_COL_NAME,
		DatabaseHelper.PRO_COL_VERSION,
		DatabaseHelper.PRO_COL_GRAIN_TEMP,
		DatabaseHelper.PRO_COL_NOTES,
		DatabaseHelper.PRO_COL_TUN_TEMP,
		DatabaseHelper.PRO_COL_SPARGE_TEMP,
		DatabaseHelper.PRO_COL_PH,
		DatabaseHelper.PRO_COL_TUN_WEIGHT,
		DatabaseHelper.PRO_COL_TUN_SPEC_HEAT,
		DatabaseHelper.PRO_COL_TUN_EQUIP_ADJ
	};
	
	private String[] stepAllColumns = {
		DatabaseHelper.STE_COL_ID,
		DatabaseHelper.STE_COL_OWNER_ID,
		DatabaseHelper.STE_COL_NAME,
		DatabaseHelper.STE_COL_VERSION,
		DatabaseHelper.STE_COL_TYPE,
		DatabaseHelper.STE_COL_INFUSE_AMT,
		DatabaseHelper.STE_COL_STEP_TEMP,
		DatabaseHelper.STE_COL_STEP_TIME,
		DatabaseHelper.STE_COL_RAMP_TIME,
		DatabaseHelper.STE_COL_END_TEMP
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
		values.put(DatabaseHelper.REC_COL_STYLE, r.getStyle().getName());
		values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
		values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBeerXmlStandardBatchSize());
		values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBeerXmlStandardBoilSize());
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
		addStyleToDatabase(r.getStyle(), id);
		addMashProfileToDatabase(r.getMashProfile(), id);
		
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
		values.put(DatabaseHelper.REC_COL_STYLE, r.getStyle().getName());
		values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
		values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBeerXmlStandardBatchSize());
		values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBeerXmlStandardBoilSize());
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
		deleteStyle(r.getId());
		addStyleToDatabase(r.getStyle(), r.getId());
		deleteMashProfile(r.getId());
		addMashProfileToDatabase(r.getMashProfile(), r.getId());
		
		return database.update(DatabaseHelper.TABLE_RECIPES, values, whereClause, null) > 0;
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
			values.put(DatabaseHelper.ING_COL_UNITS, ing.getDisplayUnits());
			values.put(DatabaseHelper.ING_COL_AMT, ing.getBeerXmlStandardAmount());
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
			
			// Yeast specific values
			if (ing.getType().equals(Ingredient.YEAST))
			{
				Yeast yeast = (Yeast) ing;
				values.put(DatabaseHelper.ING_YS_COL_TYPE, yeast.getYeastType());
				values.put(DatabaseHelper.ING_YS_COL_FORM, yeast.getForm());
				values.put(DatabaseHelper.ING_YS_COL_MIN_TEMP, yeast.getMinTemp() );
				values.put(DatabaseHelper.ING_YS_COL_MAX_TEMP, yeast.getMaxTemp());
				values.put(DatabaseHelper.ING_YS_COL_ATTENUATION, yeast.getAttenuation());
				values.put(DatabaseHelper.ING_YS_COL_NOTES, yeast.getNotes());
				values.put(DatabaseHelper.ING_YS_COL_BEST_FOR, yeast.getBestFor());
			}
			
			// Misc values
			if (ing.getType().equals(Ingredient.MISC))
			{
				Misc misc = (Misc) ing;
				values.put(DatabaseHelper.ING_MC_COL_TYPE, misc.getMiscType());
				values.put(DatabaseHelper.ING_MC_COL_VERSION, misc.getVersion());
				values.put(DatabaseHelper.ING_MC_COL_AMT_IS_WEIGHT, misc.amountIsWeight() ? 1 : 0);
				values.put(DatabaseHelper.ING_MC_COL_USE_FOR, misc.getUseFor());
				
			}
			
			database.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);
			values.clear();
		}
	}
	
	public boolean updateExistingIngredient(Ingredient ing)
	{
		String whereClause = DatabaseHelper.ING_COL_ID + "=" + ing.getId();
		
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ING_COL_OWNER_ID, ing.getOwnerId());
		values.put(DatabaseHelper.ING_COL_TYPE, ing.getType());
		values.put(DatabaseHelper.ING_COL_NAME, ing.getName());
		values.put(DatabaseHelper.ING_COL_DESC, ing.getShortDescription());
		values.put(DatabaseHelper.ING_COL_UNITS, ing.getDisplayUnits());
		values.put(DatabaseHelper.ING_COL_AMT, ing.getBeerXmlStandardAmount());
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
		
		// Yeast specific values
		if (ing.getType().equals(Ingredient.YEAST))
		{
			Yeast yeast = (Yeast) ing;
			values.put(DatabaseHelper.ING_YS_COL_TYPE, yeast.getYeastType());
			values.put(DatabaseHelper.ING_YS_COL_FORM, yeast.getForm());
			values.put(DatabaseHelper.ING_YS_COL_MIN_TEMP, yeast.getMinTemp() );
			values.put(DatabaseHelper.ING_YS_COL_MAX_TEMP, yeast.getMaxTemp());
			values.put(DatabaseHelper.ING_YS_COL_ATTENUATION, yeast.getAttenuation());
			values.put(DatabaseHelper.ING_YS_COL_NOTES, yeast.getNotes());
			values.put(DatabaseHelper.ING_YS_COL_BEST_FOR, yeast.getBestFor());
		}
		
		// Misc values
		if (ing.getType().equals(Ingredient.MISC))
		{
			Misc misc = (Misc) ing;
			values.put(DatabaseHelper.ING_MC_COL_TYPE, misc.getMiscType());
			values.put(DatabaseHelper.ING_MC_COL_VERSION, misc.getVersion());
			values.put(DatabaseHelper.ING_MC_COL_AMT_IS_WEIGHT, misc.amountIsWeight() ? 1 : 0);
			values.put(DatabaseHelper.ING_MC_COL_USE_FOR, misc.getUseFor());
		}
		
		return database.update(DatabaseHelper.TABLE_INGREDIENTS, values, whereClause, null) > 0;
	}
	
	public long addStyleToDatabase(BeerStyle s, long ownerId)
	{
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.STY_COL_OWNER_ID, ownerId);
		values.put(DatabaseHelper.STY_COL_NAME, s.getName());
		values.put(DatabaseHelper.STY_COL_CATEGORY, s.getCategory());
		values.put(DatabaseHelper.STY_COL_CAT_NUM, s.getCatNum());
		values.put(DatabaseHelper.STY_COL_STY_LETTER, s.getStyleLetter());
		values.put(DatabaseHelper.STY_COL_STY_GUIDE, s.getStyleGuide());
		values.put(DatabaseHelper.STY_COL_TYPE, s.getType());
		values.put(DatabaseHelper.STY_COL_OG_MIN, s.getMinOg());
		values.put(DatabaseHelper.STY_COL_OG_MAX, s.getMaxOg());
		values.put(DatabaseHelper.STY_COL_FG_MIN, s.getMinFg());
		values.put(DatabaseHelper.STY_COL_FG_MAX, s.getMaxFg());
		values.put(DatabaseHelper.STY_COL_IBU_MIN, s.getMinIbu());
		values.put(DatabaseHelper.STY_COL_IBU_MAX, s.getMaxIbu());
		values.put(DatabaseHelper.STY_COL_SRM_MIN, s.getMinColor());
		values.put(DatabaseHelper.STY_COL_SRM_MAX, s.getMaxColor());
		values.put(DatabaseHelper.STY_COL_CARB_MIN, s.getMinCarb());
		values.put(DatabaseHelper.STY_COL_CARB_MAX, s.getMaxCarb());
		values.put(DatabaseHelper.STY_COL_ABV_MIN, s.getMinAbv());
		values.put(DatabaseHelper.STY_COL_ABV_MAX, s.getMaxAbv());
		values.put(DatabaseHelper.STY_COL_NOTES, s.getNotes());
		values.put(DatabaseHelper.STY_COL_PROFILE, s.getProfile());
		values.put(DatabaseHelper.STY_COL_INGREDIENTS, s.getIngredients());
		values.put(DatabaseHelper.STY_COL_EXAMPLES, s.getExamples());

		long id = database.insert(DatabaseHelper.TABLE_STYLES, null, values);
		
		return id;
	}
	
	public long addMashProfileToDatabase(MashProfile p, long ownerId)
	{
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.PRO_COL_OWNER_ID, ownerId);
        values.put(DatabaseHelper.PRO_COL_NAME, p.getName());
	    values.put(DatabaseHelper.PRO_COL_VERSION, p.getVersion());
		values.put(DatabaseHelper.PRO_COL_GRAIN_TEMP, p.getBeerXmlStandardGrainTemp());
		values.put(DatabaseHelper.PRO_COL_NOTES, p.getNotes());
		values.put(DatabaseHelper.PRO_COL_TUN_TEMP, p.getBeerXmlStandardTunTemp());
		values.put(DatabaseHelper.PRO_COL_SPARGE_TEMP, p.getBeerXmlStandardSpargeTemp());
		values.put(DatabaseHelper.PRO_COL_PH, p.getpH());
		values.put(DatabaseHelper.PRO_COL_TUN_WEIGHT, p.getBeerXmlStandardTunWeight());
		values.put(DatabaseHelper.PRO_COL_TUN_SPEC_HEAT, p.getBeerXmlStandardTunSpecHeat());
		values.put(DatabaseHelper.PRO_COL_TUN_EQUIP_ADJ, (p.getEquipmentAdjust()) ? 1 : 0);
		
		long id = database.insert(DatabaseHelper.TABLE_PROFILES, null, values);
		addMashStepListToDatabase(p.getMashStepList(), id);
		return id;
	}
	
	public void addMashStepListToDatabase(ArrayList<MashStep> l, long ownerId)
	{
		for (MashStep step : l)
		{
			addMashStepToDatabase(step, ownerId);
		}
	}
	
	public long addMashStepToDatabase(MashStep s, long ownerId)
	{
		// Load up values to store
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.STE_COL_OWNER_ID, ownerId);
		values.put(DatabaseHelper.STE_COL_NAME, s.getName());
		values.put(DatabaseHelper.STE_COL_VERSION, s.getVersion());
		values.put(DatabaseHelper.STE_COL_TYPE, s.getType());
		values.put(DatabaseHelper.STE_COL_INFUSE_AMT, s.getBeerXmlStandardInfuseAmount());
		values.put(DatabaseHelper.STE_COL_STEP_TEMP, s.getBeerXmlStandardStepTemp());
		values.put(DatabaseHelper.STE_COL_STEP_TIME, s.getStepTime());
		values.put(DatabaseHelper.STE_COL_RAMP_TIME, s.getRampTime());
		values.put(DatabaseHelper.STE_COL_END_TEMP,	s.getBeerXmlStandardEndTemp());
		
		long id = database.insert(DatabaseHelper.TABLE_STEPS, null, values);
		return id;
	}
	
	/**
	* Deletes all ingredients with the given owner id
	*/
	private boolean deleteIngredientList(long id) {
		String whereClause = DatabaseHelper.ING_COL_OWNER_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
	}
	
	/**
	 * Deletes all styles with given owner id
	 */
	private boolean deleteStyle(long id) {
		String whereClause = DatabaseHelper.STY_COL_OWNER_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_STYLES, whereClause, null) > 0;
	}
	
	/**
	 * Deletes all mash profiles with given owner id
	 */
	private boolean deleteMashProfile(long id) {
		String whereClause = DatabaseHelper.PRO_COL_OWNER_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_PROFILES, whereClause, null) > 0;
	}

	/**
	 * Takes id as input, deletes if it exists
	 * @param id
	 * @return 1 if it was deleted or 0 if not
	 */
	public boolean deleteRecipeIfExists(long id)
	{
		String whereClause = DatabaseHelper.REC_COL_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_RECIPES, whereClause, null) > 0;
	}
	
	/**
	 * Takes id as input, deletes if it exists
	 * @param id
	 * @return 1 if it was deleted or 0 if not
	 */
	
	public boolean deleteIngredientIfExists(long id) {
		String whereClause = DatabaseHelper.ING_COL_ID + "=" + id;
		return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
	}
	
	/**
	 * takes recipe ID and returns the recipe with that ID from the database
	 * undefined for nonexistent IDs
	 * @param id
	 * @return
	 */
	public Recipe getRecipeWithId(long id)
	{
		Recipe r = new Recipe("Database Problem");
		String whereString = DatabaseHelper.REC_COL_ID + "=" + id;

		Cursor cursor = database.query(DatabaseHelper.TABLE_RECIPES, recipeAllColumns, whereString, null, null, null, null);
		cursor.moveToFirst();
		r = cursorToRecipe(cursor);
		
		return r;
	}

	/**
	* Takes ingredient ID and returns the ingredient with that ID from the database
	* Undefined for nonexistent IDs
	*/	
	public Ingredient getIngredientWithId(long id)
	{
		String whereString = DatabaseHelper.ING_COL_ID + "=" + id;
		Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns, whereString, null, null, null, null);
		cursor.moveToFirst();
		Ingredient i = cursorToIngredient(cursor);
		
		i = cursorToIngredient(cursor);
			
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
		String styleString = cursor.getString(cid);   cid++;
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
		BeerStyle style = readStyle(id);
		MashProfile profile = readMashProfile(id);
		
		Recipe r = new Recipe(recipeName);
		r.setId(id);
		r.setVersion(version);
		r.setType(type);
		r.setBrewer(brewer);
		r.setBeerXmlStandardBatchSize(batchSize);
		r.setBeerXmlStandardBoilSize(boilSize);
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
		
		r.setStyle(style);
		r.setMashProfile(profile);
		r.setIngredientsList(ingredientsList);
		
		return r;
	}

	// gets the ingredients list for recipe with given ID=id
	private ArrayList<Ingredient> readIngredientsList(long id) {
		ArrayList<Ingredient> list = new ArrayList<Ingredient>();
		
		String whereString = DatabaseHelper.ING_COL_OWNER_ID + "=" + id;
		Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns, whereString, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast())
		{
			Ingredient ing = cursorToIngredient(cursor);
			list.add(ing);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	// gets the style for recipe with given ID=id
	private BeerStyle readStyle(long id) {
		
		String whereString = DatabaseHelper.STY_COL_OWNER_ID + "=" + id;
		Cursor cursor = database.query(DatabaseHelper.TABLE_STYLES, stylesAllColumns, whereString, null, null, null, null);
		
		cursor.moveToFirst();
		BeerStyle style = cursorToStyle(cursor);
		cursor.close();
		
		return style;
	}
	
	private BeerStyle cursorToStyle(Cursor cursor) {
		int cid = 0;
		
		// Get all the values from the cursor
		long id = cursor.getLong(cid);                          cid++;
		long ownerId = cursor.getLong(cid);						cid++;
		String name = cursor.getString(cid);					cid++;
		String category = cursor.getString(cid);				cid++;
		String catNumber = cursor.getString(cid);				cid++;
		String styleLetter = cursor.getString(cid);				cid++;
		String styleGuide = cursor.getString(cid);				cid++;
		String type = cursor.getString(cid);					cid++;
		float minOg = cursor.getFloat(cid);						cid++;
		float maxOg = cursor.getFloat(cid);						cid++;	
		float minFg = cursor.getFloat(cid);						cid++;
		float maxFg = cursor.getFloat(cid);						cid++;	
		float minIbu = cursor.getFloat(cid);					cid++;
		float maxIbu = cursor.getFloat(cid);					cid++;	
		float minSrm = cursor.getFloat(cid);					cid++;
		float maxSrm = cursor.getFloat(cid);					cid++;	
		float minCarb = cursor.getFloat(cid);					cid++;
		float maxCarb = cursor.getFloat(cid);					cid++;	
		float minAbv = cursor.getFloat(cid);					cid++;
		float maxAbv = cursor.getFloat(cid);					cid++;
		String notes = cursor.getString(cid);					cid++;
		String profile = cursor.getString(cid);					cid++;
		String ingredients = cursor.getString(cid);				cid++;
		String examples = cursor.getString(cid);				cid++;
		
		// Stick them all in a new object
		BeerStyle style = new BeerStyle(name);
		style.setOwnerId(ownerId);
		style.setCategory(category);
		style.setCategoryNumber(catNumber);
		style.setStyleLetter(styleLetter);
		style.setStyleGuide(styleGuide);
		style.setType(type);
		style.setMinOg(minOg);
		style.setMaxOg(maxOg);
		style.setMinFg(minFg);
		style.setMaxFg(maxFg);
		style.setMinIbu(minIbu);
		style.setMaxIbu(maxIbu);
		style.setMinColor(minSrm);
		style.setMaxColor(maxSrm);
		style.setMinCarb(minCarb);
		style.setMaxCarb(maxCarb);
		style.setMinAbv(minAbv);
		style.setMaxAbv(maxAbv);
		style.setProfile(profile);
		style.setIngredients(ingredients);
		style.setExamples(examples);
		style.setNotes(notes);
		
		return style;
	}
	

	private MashProfile readMashProfile(long id) {

		String whereString = DatabaseHelper.PRO_COL_OWNER_ID + "=" + id;
		Cursor cursor = database.query(DatabaseHelper.TABLE_PROFILES, profileAllColumns, whereString, null, null, null, null);

		cursor.moveToFirst();
		MashProfile profile = cursorToMashProfile(cursor);
		cursor.close();

		return profile;
	}
	
	private MashProfile cursorToMashProfile(Cursor cursor) {
		int cid = 0;

		long id = cursor.getLong(cid);                          cid++;
		long ownerId = cursor.getLong(cid);						cid++;
		String name = cursor.getString(cid);					cid++;
		Integer version = cursor.getInt(cid);				    cid++;
		double grainTemp = cursor.getDouble(cid);				cid++;
		String notes = cursor.getString(cid);                   cid++;
		double tunTemp = cursor.getDouble(cid);                 cid++;
		double spargeTemp = cursor.getDouble(cid);              cid++;
		double pH = cursor.getDouble(cid);                      cid++;
		double tunWeight = cursor.getDouble(cid);               cid++;
		double tunSpecHeat = cursor.getDouble(cid);             cid++;
		int equipAdjInt = cursor.getInt(cid);                   cid++;
		
		ArrayList<MashStep> stepsList = readMashStepsList(id);
		
		MashProfile p = new MashProfile();
		p.setId(id);
		p.setOwnerId(ownerId);
		p.setName(name);
		p.setVersion(version);
		p.setBeerXmlStandardGrainTemp(grainTemp);
		p.setNotes(notes);
		p.setBeerXmlStandardTunTemp(tunTemp);
		p.setBeerXmlStandardSpargeTemp(spargeTemp);
		p.setpH(pH);
		p.setBeerXmlStandardTunWeight(tunWeight);
		p.setBeerXmlStandardTunSpecHeat(tunSpecHeat);
		p.setEquipmentAdjust(equipAdjInt > 0 ? true: false);
		p.setMashStepList(stepsList);
		return p;
	}
	
	private ArrayList<MashStep> readMashStepsList(long id) {
		ArrayList<MashStep> list = new ArrayList<MashStep>();
		String whereString = DatabaseHelper.STE_COL_OWNER_ID + "=" + id;
		Cursor cursor = database.query(DatabaseHelper.TABLE_STEPS, stepAllColumns, whereString, null, null, null, null);

		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			MashStep step = cursorToMashStep(cursor);
			list.add(step);
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
	
	private MashStep cursorToMashStep(Cursor cursor) {
		int cid = 0;

		long id = cursor.getLong(cid);                          cid++;
		long ownerId = cursor.getLong(cid);						cid++;
		String name = cursor.getString(cid);					cid++;
		Integer version = cursor.getInt(cid);				    cid++;
		String type = cursor.getString(cid);				    cid++;
		double infuseAmt = cursor.getDouble(cid);               cid++;
		double stepTemp = cursor.getDouble(cid);                cid++;
		Double stepTime = cursor.getDouble(cid);                cid++;
		Double rampTime = cursor.getDouble(cid);                cid++;
		double endTemp = cursor.getDouble(cid);                 cid++;
		
		MashStep s = new MashStep();
		s.setId(id);
		s.setOwnerId(ownerId);
		s.setName(name);
		s.setVersion(version);
		s.setType(type);
		s.setBeerXmlStandardInfuseAmount(infuseAmt);
		s.setStepTime(stepTime);
		s.setBeerXmlStandardStepTemp(stepTemp);
		s.setRampTime(rampTime);
		s.setBeerXmlStandardEndTemp(endTemp);
		return s;
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
			fer.setDisplayUnits(units);
			fer.setBeerXmlStandardAmount(amount);
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
			hop.setDisplayUnits(units);
			hop.setBeerXmlStandardAmount(amount);
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
		
		// Yeast specific stuff
		else if (ingType.equals(Ingredient.YEAST))
		{
			cid += 12;
			
			String type = cursor.getString(cid);				cid++;
			String form = cursor.getString(cid);				cid++;
			Float minTemp = cursor.getFloat(cid);				cid++;
			Float maxTemp = cursor.getFloat(cid);				cid++;
			Float attn = cursor.getFloat(cid);					cid++;
			String notes = cursor.getString(cid);				cid++;
			String bestFor = cursor.getString(cid);				cid++;
			
			Yeast yeast = new Yeast(name);
			yeast.setId(id);
			yeast.setOwnerId(ownerId);
			yeast.setShortDescription(description);
			yeast.setDisplayUnits(units);
			yeast.setBeerXmlStandardAmount(amount);
			yeast.setStartTime(startTime);
			yeast.setEndTime(endTime);
			yeast.setType(type);
			yeast.setForm(form);
			yeast.setMinTemp(minTemp);
			yeast.setMaxTemp(maxTemp);
			yeast.setAttenuation(attn);
			yeast.setNotes(notes);
			yeast.setBestFor(bestFor);
			
			return yeast;
		}
		
		if (ingType.equals(Ingredient.MISC))
		{
			cid += 19;
			
			Misc misc = new Misc(name);
			String miscType = cursor.getString(cid);       cid++;
			int version = cursor.getInt(cid);              cid++;
			int amtIsWeight = cursor.getInt(cid);          cid++;
			String useFor = cursor.getString(cid);         cid++;
			
			misc.setId(id);
			misc.setOwnerId(ownerId);
			misc.setShortDescription(description);
			misc.setDisplayUnits(units);
			misc.setBeerXmlStandardAmount(amount);
			misc.setStartTime(startTime);
			misc.setEndTime(endTime);
			misc.setMiscType(miscType);
			misc.setVersion(version);
			misc.setAmountIsWeight(amtIsWeight > 0 ? true : false);
			misc.setUseFor(useFor);

			return misc;
		}
		
		return new Misc("No Ingredient Found");
	}
}
