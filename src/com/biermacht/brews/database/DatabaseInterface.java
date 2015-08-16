package com.biermacht.brews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biermacht.brews.exceptions.InvalidCursorException;
import com.biermacht.brews.frontend.adapters.DetailArrayAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseInterface {

  // Database Fields
  private SQLiteDatabase database;
  private DatabaseHelper dbHelper;

  private String[] recipeAllColumns = {
          DatabaseHelper.REC_COL_ID,
          DatabaseHelper.REC_COL_DB_ID,
          DatabaseHelper.REC_COL_NAME,
          DatabaseHelper.REC_COL_VER,
          DatabaseHelper.REC_COL_TYPE,
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
          DatabaseHelper.REC_COL_MEAS_OG,
          DatabaseHelper.REC_COL_MEAS_FG,
          DatabaseHelper.REC_COL_PRIMARY_TEMP,
          DatabaseHelper.REC_COL_PRIMARY_AGE,
          DatabaseHelper.REC_COL_SECONDARY_TEMP,
          DatabaseHelper.REC_COL_SECONDARY_AGE,
          DatabaseHelper.REC_COL_TERTIARY_TEMP,
          DatabaseHelper.REC_COL_TERTIARY_AGE,
          DatabaseHelper.REC_COL_TASTE_NOTES,
          DatabaseHelper.REC_COL_TASTE_RATING,
          DatabaseHelper.REC_COL_BOTTLE_AGE,
          DatabaseHelper.REC_COL_BOTTLE_TEMP,
          DatabaseHelper.REC_COL_BREW_DATE,
          DatabaseHelper.REC_COL_CARBONATION,
          DatabaseHelper.REC_COL_FORCED_CARB,
          DatabaseHelper.REC_COL_PRIMING_SUGAR_NAME,
          DatabaseHelper.REC_COL_CARB_TEMP,
          DatabaseHelper.REC_COL_PRIMING_SUGAR_EQUIV,
          DatabaseHelper.REC_COL_KEG_PRIMING_FACTOR,
          DatabaseHelper.REC_COL_CALORIES,
          DatabaseHelper.REC_COL_CALC_BOIL_VOL,
          DatabaseHelper.REC_COL_CALC_STRIKE_TEMP,
          DatabaseHelper.REC_COL_CALC_STRIKE_VOL,
          DatabaseHelper.REC_COL_MEAS_BATCH_SIZE,
  };

  private String[] snapshotAllColumns = {
          DatabaseHelper.REC_COL_ID,
          DatabaseHelper.SNAP_COL_OWNER_ID,
          DatabaseHelper.REC_COL_DB_ID,
          DatabaseHelper.REC_COL_NAME,
          DatabaseHelper.REC_COL_VER,
          DatabaseHelper.REC_COL_TYPE,
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
          DatabaseHelper.REC_COL_MEAS_OG,
          DatabaseHelper.REC_COL_MEAS_FG,
          DatabaseHelper.REC_COL_PRIMARY_TEMP,
          DatabaseHelper.REC_COL_PRIMARY_AGE,
          DatabaseHelper.REC_COL_SECONDARY_TEMP,
          DatabaseHelper.REC_COL_SECONDARY_AGE,
          DatabaseHelper.REC_COL_TERTIARY_TEMP,
          DatabaseHelper.REC_COL_TERTIARY_AGE,
          DatabaseHelper.REC_COL_TASTE_NOTES,
          DatabaseHelper.REC_COL_TASTE_RATING,
          DatabaseHelper.REC_COL_BOTTLE_AGE,
          DatabaseHelper.REC_COL_BOTTLE_TEMP,
          DatabaseHelper.REC_COL_BREW_DATE,
          DatabaseHelper.REC_COL_CARBONATION,
          DatabaseHelper.REC_COL_FORCED_CARB,
          DatabaseHelper.REC_COL_PRIMING_SUGAR_NAME,
          DatabaseHelper.REC_COL_CARB_TEMP,
          DatabaseHelper.REC_COL_PRIMING_SUGAR_EQUIV,
          DatabaseHelper.REC_COL_KEG_PRIMING_FACTOR,
          DatabaseHelper.REC_COL_CALORIES,
          DatabaseHelper.REC_COL_CALC_BOIL_VOL,
          DatabaseHelper.REC_COL_CALC_STRIKE_TEMP,
          DatabaseHelper.REC_COL_CALC_STRIKE_VOL,
          DatabaseHelper.REC_COL_MEAS_BATCH_SIZE,
          DatabaseHelper.SNAP_COL_DESCRIPTION,
          DatabaseHelper.SNAP_COL_SNAPSHOT_TIME,
  };

  private String[] ingredientAllColumns = {
          DatabaseHelper.ING_COL_ID,
          DatabaseHelper.ING_COL_OWNER_ID,
          DatabaseHelper.ING_COL_DB_ID,
          DatabaseHelper.ING_COL_TYPE,
          DatabaseHelper.ING_COL_NAME,
          DatabaseHelper.ING_COL_DESC,
          DatabaseHelper.ING_COL_UNITS,
          DatabaseHelper.ING_COL_AMT,
          DatabaseHelper.ING_COL_TIME,
          DatabaseHelper.ING_COL_INVENTORY,

          DatabaseHelper.ING_FR_COL_TYPE,
          DatabaseHelper.ING_FR_COL_YIELD,
          DatabaseHelper.ING_FR_COL_COLOR,
          DatabaseHelper.ING_FR_COL_ADD_AFTER_BOIL,
          DatabaseHelper.ING_FR_COL_MAX_IN_BATCH,
          DatabaseHelper.ING_FR_COL_GRAV,

          DatabaseHelper.ING_HP_COL_TYPE,
          DatabaseHelper.ING_HP_COL_ALPHA,
          DatabaseHelper.ING_HP_COL_USE,
          DatabaseHelper.ING_HP_COL_FORM,
          DatabaseHelper.ING_HP_COL_ORIGIN,

          DatabaseHelper.ING_YS_COL_TYPE,
          DatabaseHelper.ING_YS_COL_FORM,
          DatabaseHelper.ING_YS_COL_MIN_TEMP,
          DatabaseHelper.ING_YS_COL_MAX_TEMP,
          DatabaseHelper.ING_YS_COL_ATTENUATION,
          DatabaseHelper.ING_YS_COL_NOTES,
          DatabaseHelper.ING_YS_COL_BEST_FOR,
          DatabaseHelper.ING_YS_COL_LAB,
          DatabaseHelper.ING_YS_COL_PROD_ID,

          DatabaseHelper.ING_MC_COL_TYPE,
          DatabaseHelper.ING_MC_COL_VERSION,
          DatabaseHelper.ING_MC_COL_AMT_IS_WEIGHT,
          DatabaseHelper.ING_MC_COL_USE_FOR,
          DatabaseHelper.ING_MC_COL_USE,

          DatabaseHelper.COL_SNAPSHOT_ID
  };
  private ArrayList<String> ingredientColumns = new ArrayList<>(Arrays.asList(ingredientAllColumns));

  private String[] stylesAllColumns = {
          DatabaseHelper.STY_COL_ID,
          DatabaseHelper.STY_COL_OWNER_ID,
          DatabaseHelper.STY_COL_DB_ID,
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
          DatabaseHelper.STY_COL_EXAMPLES,
          DatabaseHelper.COL_SNAPSHOT_ID
  };

  private String[] profileAllColumns = {
          DatabaseHelper.PRO_COL_ID,
          DatabaseHelper.PRO_COL_OWNER_ID,
          DatabaseHelper.PRO_COL_DB_ID,
          DatabaseHelper.PRO_COL_NAME,
          DatabaseHelper.PRO_COL_VERSION,
          DatabaseHelper.PRO_COL_GRAIN_TEMP,
          DatabaseHelper.PRO_COL_NOTES,
          DatabaseHelper.PRO_COL_TUN_TEMP,
          DatabaseHelper.PRO_COL_SPARGE_TEMP,
          DatabaseHelper.PRO_COL_PH,
          DatabaseHelper.PRO_COL_TUN_WEIGHT,
          DatabaseHelper.PRO_COL_TUN_SPEC_HEAT,
          DatabaseHelper.PRO_COL_TUN_EQUIP_ADJ,
          DatabaseHelper.PRO_COL_MASH_TYPE,
          DatabaseHelper.PRO_COL_SPARGE_TYPE,
          DatabaseHelper.COL_SNAPSHOT_ID
  };

  private String[] stepAllColumns = {
          DatabaseHelper.STE_COL_ID,
          DatabaseHelper.STE_COL_OWNER_ID,
          DatabaseHelper.STE_COL_DB_ID,
          DatabaseHelper.STE_COL_NAME,
          DatabaseHelper.STE_COL_VERSION,
          DatabaseHelper.STE_COL_TYPE,
          DatabaseHelper.STE_COL_INFUSE_AMT,
          DatabaseHelper.STE_COL_STEP_TEMP,
          DatabaseHelper.STE_COL_STEP_TIME,
          DatabaseHelper.STE_COL_RAMP_TIME,
          DatabaseHelper.STE_COL_END_TEMP,
          DatabaseHelper.STE_COL_WATER_GRAIN_RATIO,
          DatabaseHelper.STE_COL_DESCRIPTION,
          DatabaseHelper.STE_COL_ORDER,
          DatabaseHelper.STE_COL_INFUSE_TEMP,
          DatabaseHelper.STE_COL_DECOCT_AMT,
          DatabaseHelper.STE_COL_CALC_INFUSE_TEMP,
          DatabaseHelper.STE_COL_CALC_INFUSE_AMT,
  };

  // Constructor
  protected DatabaseInterface(Context context) {
    dbHelper = new DatabaseHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  protected long addRecipeToDatabase(Recipe r) {
    // Load up values to store
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.REC_COL_DB_ID, Constants.DATABASE_DEFAULT);
    values.put(DatabaseHelper.REC_COL_NAME, r.getRecipeName());
    values.put(DatabaseHelper.REC_COL_VER, r.getVersion());
    values.put(DatabaseHelper.REC_COL_TYPE, r.getType());
    values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
    values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBeerXmlStandardBatchSize());
    values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBeerXmlStandardBoilSize());
    values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
    values.put(DatabaseHelper.REC_COL_BOIL_EFF, r.getEfficiency());
    values.put(DatabaseHelper.REC_COL_OG, r.getOG());
    values.put(DatabaseHelper.REC_COL_FG, r.getFG());
    values.put(DatabaseHelper.REC_COL_STAGES, r.getFermentationStages());
    values.put(DatabaseHelper.REC_COL_DESC, r.getNotes());
    values.put(DatabaseHelper.REC_COL_BATCH_TIME, r.getBatchTime());
    values.put(DatabaseHelper.REC_COL_ABV, r.getABV());
    values.put(DatabaseHelper.REC_COL_BITTER, r.getBitterness());
    values.put(DatabaseHelper.REC_COL_COLOR, r.getColor());
    values.put(DatabaseHelper.REC_COL_MEAS_OG, r.getMeasuredOG());
    values.put(DatabaseHelper.REC_COL_MEAS_FG, r.getMeasuredFG());
    values.put(DatabaseHelper.REC_COL_PRIMARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_PRIMARY_AGE, r.getFermentationAge(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_AGE, r.getFermentationAge(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_AGE, r.getFermentationAge(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TASTE_NOTES, r.getTasteNotes());
    values.put(DatabaseHelper.REC_COL_TASTE_RATING, r.getTasteRating());
    values.put(DatabaseHelper.REC_COL_BOTTLE_AGE, r.getBottleAge());
    values.put(DatabaseHelper.REC_COL_BOTTLE_TEMP, r.getBeerXmlStandardBottleTemp());
    values.put(DatabaseHelper.REC_COL_BREW_DATE, r.getBrewDate());
    values.put(DatabaseHelper.REC_COL_CARBONATION, r.getCarbonation());
    values.put(DatabaseHelper.REC_COL_FORCED_CARB, r.isForceCarbonated());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_NAME, r.getPrimingSugarName());
    values.put(DatabaseHelper.REC_COL_CARB_TEMP, r.getBeerXmlStandardCarbonationTemp());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_EQUIV, r.getPrimingSugarEquiv());
    values.put(DatabaseHelper.REC_COL_KEG_PRIMING_FACTOR, r.getKegPrimingFactor());
    values.put(DatabaseHelper.REC_COL_CALORIES, r.getCalories());
    values.put(DatabaseHelper.REC_COL_CALC_BOIL_VOL, r.getCalculateBoilVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_TEMP, r.getCalculateStrikeTemp() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_VOL, r.getCalculateStrikeVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_MEAS_BATCH_SIZE, r.getBeerXmlMeasuredBatchSize());

    long recipeId = database.insert(DatabaseHelper.TABLE_RECIPES, null, values);
    addIngredientListToDatabase(r.getIngredientList(), recipeId, Constants.DATABASE_DEFAULT, Constants.SNAPSHOT_NONE);
    addStyleToDatabase(r.getStyle(), recipeId, Constants.SNAPSHOT_NONE);
    addMashProfileToDatabase(r.getMashProfile(), recipeId, Constants.SNAPSHOT_NONE, Constants.DATABASE_DEFAULT);

    return recipeId;
  }

  protected boolean updateExistingRecipe(Recipe r) {
    String whereClause = DatabaseHelper.REC_COL_ID + "=" + r.getId();

    // Load up values to store
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.REC_COL_NAME, r.getRecipeName());
    values.put(DatabaseHelper.REC_COL_VER, r.getVersion());
    values.put(DatabaseHelper.REC_COL_TYPE, r.getType());
    values.put(DatabaseHelper.REC_COL_BREWER, r.getBrewer());
    values.put(DatabaseHelper.REC_COL_BATCH_SIZE, r.getBeerXmlStandardBatchSize());
    values.put(DatabaseHelper.REC_COL_BOIL_SIZE, r.getBeerXmlStandardBoilSize());
    values.put(DatabaseHelper.REC_COL_BOIL_TIME, r.getBoilTime());
    values.put(DatabaseHelper.REC_COL_BOIL_EFF, r.getEfficiency());
    values.put(DatabaseHelper.REC_COL_OG, r.getOG());
    values.put(DatabaseHelper.REC_COL_FG, r.getFG());
    values.put(DatabaseHelper.REC_COL_STAGES, r.getFermentationStages());
    values.put(DatabaseHelper.REC_COL_DESC, r.getNotes());
    values.put(DatabaseHelper.REC_COL_BATCH_TIME, r.getBatchTime());
    values.put(DatabaseHelper.REC_COL_ABV, r.getABV());
    values.put(DatabaseHelper.REC_COL_BITTER, r.getBitterness());
    values.put(DatabaseHelper.REC_COL_COLOR, r.getColor());
    values.put(DatabaseHelper.REC_COL_MEAS_OG, r.getMeasuredOG());
    values.put(DatabaseHelper.REC_COL_MEAS_FG, r.getMeasuredFG());
    values.put(DatabaseHelper.REC_COL_PRIMARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_PRIMARY_AGE, r.getFermentationAge(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_AGE, r.getFermentationAge(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_TEMP, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_AGE, r.getFermentationAge(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TASTE_NOTES, r.getTasteNotes());
    values.put(DatabaseHelper.REC_COL_TASTE_RATING, r.getTasteRating());
    values.put(DatabaseHelper.REC_COL_BOTTLE_AGE, r.getBottleAge());
    values.put(DatabaseHelper.REC_COL_BOTTLE_TEMP, r.getBeerXmlStandardBottleTemp());
    values.put(DatabaseHelper.REC_COL_BREW_DATE, r.getBrewDate());
    values.put(DatabaseHelper.REC_COL_CARBONATION, r.getCarbonation());
    values.put(DatabaseHelper.REC_COL_FORCED_CARB, r.isForceCarbonated());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_NAME, r.getPrimingSugarName());
    values.put(DatabaseHelper.REC_COL_CARB_TEMP, r.getBeerXmlStandardCarbonationTemp());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_EQUIV, r.getPrimingSugarEquiv());
    values.put(DatabaseHelper.REC_COL_KEG_PRIMING_FACTOR, r.getKegPrimingFactor());
    values.put(DatabaseHelper.REC_COL_CALORIES, r.getCalories());
    values.put(DatabaseHelper.REC_COL_CALC_BOIL_VOL, r.getCalculateBoilVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_TEMP, r.getCalculateStrikeTemp() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_VOL, r.getCalculateStrikeVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_MEAS_BATCH_SIZE, r.getBeerXmlMeasuredBatchSize());

    for (Ingredient i : r.getIngredientList()) {
      Boolean exists = updateExistingIngredientInDatabase(i, Constants.DATABASE_DEFAULT);
      if (! exists) {
        addIngredientToDatabase(i, r.getId(), Constants.DATABASE_DEFAULT, Constants.SNAPSHOT_NONE);
      }
    }

    // Update mash profile
    Boolean exists = updateMashProfile(r.getMashProfile(), r.getId(), Constants.DATABASE_DEFAULT, Constants.SNAPSHOT_NONE);
    if (! exists) {
      // Delete any mash profiles owned by this recipe so we don't build up
      // a bunch over time.
      MashProfile oldProfile = readMashProfileRecipe(r.getId());
      deleteMashProfile(oldProfile.getId(), Constants.DATABASE_DEFAULT);

      // Add the new one to the database.
      addMashProfileToDatabase(r.getMashProfile(), r.getId(), Constants.SNAPSHOT_NONE, Constants.DATABASE_DEFAULT);
    }

    // TODO: Implement style update methods.
    deleteStyleRecipe(r.getId());
    addStyleToDatabase(r.getStyle(), r.getId(), Constants.SNAPSHOT_NONE);

    return database.update(DatabaseHelper.TABLE_RECIPES, values, whereClause, null) > 0;
  }

  /**
   * Deletes to give Recipe if it exists.
   * @param r
   * @return True if a recipe is deleted, False otherwise.
   */
  protected boolean deleteRecipe(Recipe r) {
    String whereClause = DatabaseHelper.REC_COL_ID + "=" + r.getId();
    return database.delete(DatabaseHelper.TABLE_RECIPES, whereClause, null) > 0;
  }

  protected void addIngredientListToDatabase(ArrayList<Ingredient> ingredientList, long recipeId, long dbid, long snapshotId) {
    for (Ingredient ing : ingredientList) {
      addIngredientToDatabase(ing, recipeId, dbid, snapshotId);
    }
  }

  protected long addIngredientToDatabase(Ingredient ing, long recipeId, long dbid, long snapshotId) {
    ContentValues values = getIngredientValues(ing, dbid, recipeId, snapshotId);
    long ingId = database.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);
    values.clear();
    return ingId;
  }

  protected boolean updateExistingIngredientInDatabase(Ingredient ing, long dbid) {
    String whereClause = DatabaseHelper.ING_COL_ID + "=" + ing.getId() + " AND " +
            DatabaseHelper.ING_COL_DB_ID + "=" + dbid;
    ContentValues values = getIngredientValues(ing, dbid, ing.getRecipeId(), ing.getSnapshotId());
    return database.update(DatabaseHelper.TABLE_INGREDIENTS, values, whereClause, null) > 0;
  }

  protected long addSnapshotToDatabase(RecipeSnapshot snap, long recipeId) {
    ContentValues values = getSnapshotValues(snap, recipeId);
    long snapshotId = database.insert(DatabaseHelper.TABLE_SNAPSHOTS, null, values);
    addIngredientListToDatabase(snap.getIngredientList(), Constants.INVALID_ID, Constants.DATABASE_DEFAULT, snapshotId);
    addStyleToDatabase(snap.getStyle(), Constants.INVALID_ID, snapshotId);
    addMashProfileToDatabase(snap.getMashProfile(), Constants.INVALID_ID, snapshotId, Constants.DATABASE_DEFAULT);
    return snapshotId;
  }

  protected boolean updateExistingSnapshot(RecipeSnapshot snap) {
    String whereClause = DatabaseHelper.REC_COL_ID + "=" + snap.getId();
    ContentValues values = getSnapshotValues(snap, snap.getRecipeId());

    // Add / update all ingredients.
    for (Ingredient i : snap.getIngredientList()) {
      Boolean exists = updateExistingIngredientInDatabase(i, Constants.DATABASE_DEFAULT);
      if (! exists) {
        addIngredientToDatabase(i, Constants.INVALID_ID, Constants.DATABASE_DEFAULT, snap.getId());
      }
    }

    // Update mash profile
    Boolean exists = updateMashProfile(snap.getMashProfile(), Constants.INVALID_ID, Constants.DATABASE_DEFAULT, snap.getId());
    if (! exists) {
      // Delete any mash profiles owned by this recipe so we don't build up
      // a bunch over time.
      MashProfile oldProfile = readMashProfileSnapshot(snap.getId());
      deleteMashProfile(oldProfile.getId(), Constants.DATABASE_DEFAULT);

      // Add the new one to the database.
      addMashProfileToDatabase(snap.getMashProfile(), Constants.INVALID_ID, snap.getId(), Constants.DATABASE_DEFAULT);
    }

    // Update the style profile
    deleteStyleSnapshot(snap.getId());
    addStyleToDatabase(snap.getStyle(), Constants.INVALID_ID, snap.getId());

    // Finally, update fields on the Snapshot row.
    return database.update(DatabaseHelper.TABLE_SNAPSHOTS, values, whereClause, null) > 0;
  }

  protected boolean deleteSnapshot(RecipeSnapshot snap) {
    String whereClause = DatabaseHelper.REC_COL_ID + "=" + snap.getId();
    return database.delete(DatabaseHelper.TABLE_SNAPSHOTS, whereClause, null) > 0;
  }

  protected ContentValues getSnapshotValues (RecipeSnapshot rs, long ownerId) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.REC_COL_DB_ID, Constants.DATABASE_DEFAULT);
    values.put(DatabaseHelper.SNAP_COL_OWNER_ID, ownerId);
    values.put(DatabaseHelper.REC_COL_NAME, rs.getRecipeName());
    values.put(DatabaseHelper.REC_COL_VER, rs.getVersion());
    values.put(DatabaseHelper.REC_COL_TYPE, rs.getType());
    values.put(DatabaseHelper.REC_COL_BREWER, rs.getBrewer());
    values.put(DatabaseHelper.REC_COL_BATCH_SIZE, rs.getBeerXmlStandardBatchSize());
    values.put(DatabaseHelper.REC_COL_BOIL_SIZE, rs.getBeerXmlStandardBoilSize());
    values.put(DatabaseHelper.REC_COL_BOIL_TIME, rs.getBoilTime());
    values.put(DatabaseHelper.REC_COL_BOIL_EFF, rs.getEfficiency());
    values.put(DatabaseHelper.REC_COL_OG, rs.getOG());
    values.put(DatabaseHelper.REC_COL_FG, rs.getFG());
    values.put(DatabaseHelper.REC_COL_STAGES, rs.getFermentationStages());
    values.put(DatabaseHelper.REC_COL_DESC, rs.getNotes());
    values.put(DatabaseHelper.REC_COL_BATCH_TIME, rs.getBatchTime());
    values.put(DatabaseHelper.REC_COL_ABV, rs.getABV());
    values.put(DatabaseHelper.REC_COL_BITTER, rs.getBitterness());
    values.put(DatabaseHelper.REC_COL_COLOR, rs.getColor());
    values.put(DatabaseHelper.REC_COL_MEAS_OG, rs.getMeasuredOG());
    values.put(DatabaseHelper.REC_COL_MEAS_FG, rs.getMeasuredFG());
    values.put(DatabaseHelper.REC_COL_PRIMARY_TEMP, rs.getBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_PRIMARY_AGE, rs.getFermentationAge(Recipe.STAGE_PRIMARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_TEMP, rs.getBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_SECONDARY_AGE, rs.getFermentationAge(Recipe.STAGE_SECONDARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_TEMP, rs.getBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TERTIARY_AGE, rs.getFermentationAge(Recipe.STAGE_TERTIARY));
    values.put(DatabaseHelper.REC_COL_TASTE_NOTES, rs.getTasteNotes());
    values.put(DatabaseHelper.REC_COL_TASTE_RATING, rs.getTasteRating());
    values.put(DatabaseHelper.REC_COL_BOTTLE_AGE, rs.getBottleAge());
    values.put(DatabaseHelper.REC_COL_BOTTLE_TEMP, rs.getBeerXmlStandardBottleTemp());
    values.put(DatabaseHelper.REC_COL_BREW_DATE, rs.getBrewDate());
    values.put(DatabaseHelper.REC_COL_CARBONATION, rs.getCarbonation());
    values.put(DatabaseHelper.REC_COL_FORCED_CARB, rs.isForceCarbonated());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_NAME, rs.getPrimingSugarName());
    values.put(DatabaseHelper.REC_COL_CARB_TEMP, rs.getBeerXmlStandardCarbonationTemp());
    values.put(DatabaseHelper.REC_COL_PRIMING_SUGAR_EQUIV, rs.getPrimingSugarEquiv());
    values.put(DatabaseHelper.REC_COL_KEG_PRIMING_FACTOR, rs.getKegPrimingFactor());
    values.put(DatabaseHelper.REC_COL_CALORIES, rs.getCalories());
    values.put(DatabaseHelper.REC_COL_CALC_BOIL_VOL, rs.getCalculateBoilVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_TEMP, rs.getCalculateStrikeTemp() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_CALC_STRIKE_VOL, rs.getCalculateStrikeVolume() ? 1 : 0);
    values.put(DatabaseHelper.REC_COL_MEAS_BATCH_SIZE, rs.getBeerXmlMeasuredBatchSize());
    values.put(DatabaseHelper.SNAP_COL_DESCRIPTION, rs.getDescription());
    values.put(DatabaseHelper.SNAP_COL_SNAPSHOT_TIME, rs.getSnapshotTime());

    return values;
  }

  protected ContentValues getIngredientValues(Ingredient ing, long dbid, long recipeId, long snapshotId) {
    // values stored here
    ContentValues values = new ContentValues();

    // Load up values to store
    values.put(DatabaseHelper.ING_COL_OWNER_ID, recipeId);
    values.put(DatabaseHelper.ING_COL_DB_ID, dbid);
    values.put(DatabaseHelper.ING_COL_TYPE, ing.getType());
    values.put(DatabaseHelper.ING_COL_NAME, ing.getName());
    values.put(DatabaseHelper.ING_COL_DESC, ing.getShortDescription());
    values.put(DatabaseHelper.ING_COL_UNITS, ing.getDisplayUnits());
    values.put(DatabaseHelper.ING_COL_AMT, ing.getBeerXmlStandardAmount());
    values.put(DatabaseHelper.ING_COL_TIME, ing.getTime());
    values.put(DatabaseHelper.ING_COL_INVENTORY, ing.getBeerXmlStandardInventory());
    values.put(DatabaseHelper.COL_SNAPSHOT_ID, snapshotId);

    // Grain specific values
    if (ing.getType().equals(Ingredient.FERMENTABLE)) {
      Fermentable fer = (Fermentable) ing;
      values.put(DatabaseHelper.ING_FR_COL_TYPE, fer.getFermentableType());
      values.put(DatabaseHelper.ING_FR_COL_YIELD, fer.getYield());
      values.put(DatabaseHelper.ING_FR_COL_COLOR, fer.getLovibondColor());
      values.put(DatabaseHelper.ING_FR_COL_ADD_AFTER_BOIL, fer.isAddAfterBoil());
      values.put(DatabaseHelper.ING_FR_COL_MAX_IN_BATCH, fer.getMaxInBatch());
      values.put(DatabaseHelper.ING_FR_COL_GRAV, fer.getGravity());
    }

    // Hop specific values
    if (ing.getType().equals(Ingredient.HOP)) {
      Hop hop = (Hop) ing;
      values.put(DatabaseHelper.ING_HP_COL_TYPE, hop.getHopType());
      values.put(DatabaseHelper.ING_HP_COL_ALPHA, hop.getAlphaAcidContent());
      values.put(DatabaseHelper.ING_HP_COL_USE, hop.getUse());
      values.put(DatabaseHelper.ING_HP_COL_FORM, hop.getForm());
      values.put(DatabaseHelper.ING_HP_COL_ORIGIN, hop.getOrigin());
    }

    // Yeast specific values
    if (ing.getType().equals(Ingredient.YEAST)) {
      Yeast yeast = (Yeast) ing;
      values.put(DatabaseHelper.ING_YS_COL_TYPE, yeast.getYeastType());
      values.put(DatabaseHelper.ING_YS_COL_FORM, yeast.getForm());
      values.put(DatabaseHelper.ING_YS_COL_MIN_TEMP, yeast.getMinTemp());
      values.put(DatabaseHelper.ING_YS_COL_MAX_TEMP, yeast.getMaxTemp());
      values.put(DatabaseHelper.ING_YS_COL_ATTENUATION, yeast.getAttenuation());
      values.put(DatabaseHelper.ING_YS_COL_LAB, yeast.getLaboratory());
      values.put(DatabaseHelper.ING_YS_COL_PROD_ID, yeast.getProductId());
      values.put(DatabaseHelper.ING_YS_COL_NOTES, yeast.getNotes());
      values.put(DatabaseHelper.ING_YS_COL_BEST_FOR, yeast.getBestFor());
    }

    // Misc values
    if (ing.getType().equals(Ingredient.MISC)) {
      Misc misc = (Misc) ing;
      values.put(DatabaseHelper.ING_MC_COL_TYPE, misc.getMiscType());
      values.put(DatabaseHelper.ING_MC_COL_VERSION, misc.getVersion());
      values.put(DatabaseHelper.ING_MC_COL_AMT_IS_WEIGHT, misc.amountIsWeight() ? 1 : 0);
      values.put(DatabaseHelper.ING_MC_COL_USE_FOR, misc.getUseFor());
      values.put(DatabaseHelper.ING_MC_COL_USE, misc.getUse());
      values.put(DatabaseHelper.ING_COL_UNITS, ing.getDisplayUnits());
    }
    return values;
  }

  protected long addStyleToDatabase(BeerStyle s, long recipeId, long snapshotId) {
    // Load up values to store
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.STY_COL_OWNER_ID, recipeId);
    values.put(DatabaseHelper.STY_COL_DB_ID, Constants.DATABASE_DEFAULT);
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
    values.put(DatabaseHelper.COL_SNAPSHOT_ID, snapshotId);

    long id = database.insert(DatabaseHelper.TABLE_STYLES, null, values);

    return id;
  }

  protected long addMashProfileToDatabase(MashProfile p, long recipeId, long snapshotId, long dbid) {
    ContentValues values = getMashProfileValues(p, recipeId, dbid, snapshotId);
    long id = database.insert(DatabaseHelper.TABLE_PROFILES, null, values);
    addMashStepListToDatabase(p.getMashStepList(), id);
    return id;
  }

  protected boolean updateMashProfile(MashProfile p, long recipeId, long dbid, long snapshotId) {
    String whereClause = DatabaseHelper.PRO_COL_ID + "=" + p.getId() + " AND " +
            DatabaseHelper.PRO_COL_DB_ID + "=" + dbid;
    ContentValues values = getMashProfileValues(p, recipeId, dbid, snapshotId);
    updateMashStepList(p.getMashStepList(), p.getId());
    return database.update(DatabaseHelper.TABLE_PROFILES, values, whereClause, null) > 0;
  }

  protected ContentValues getMashProfileValues(MashProfile p, long recipeId, long dbid, long snapshotId) {
    // Load up values to store
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.PRO_COL_OWNER_ID, recipeId);
    values.put(DatabaseHelper.PRO_COL_DB_ID, dbid);
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
    values.put(DatabaseHelper.PRO_COL_MASH_TYPE, p.getMashType());
    values.put(DatabaseHelper.PRO_COL_SPARGE_TYPE, p.getSpargeType());
    values.put(DatabaseHelper.COL_SNAPSHOT_ID, snapshotId);
    return values;
  }

  protected void addMashStepListToDatabase(ArrayList<MashStep> l, long mashProfileId) {
    for (MashStep step : l) {
      addMashStepToDatabase(step, mashProfileId);
    }
  }

  protected void updateMashStepList(ArrayList<MashStep> l, long mashProfileId) {
    ArrayList<MashStep> existingSteps = readMashStepsList(mashProfileId);
    for (MashStep step : l) {
      Log.d("DatabaseInterface", "Updating MashStep " + step.getName());
      boolean exists = updateMashStep(step, mashProfileId);
      if (! exists) {
        this.addMashStepToDatabase(step, mashProfileId);
      }
    }

    // We need to delete any steps that used to exist, but
    // no longer do.  Check those here.
    for (MashStep step : existingSteps) {
      boolean keep = false;
      for (MashStep s : l) {
        if (step.getId() == s.getId()) {
          keep = true;
        }
      }
      if (! keep) {
        deleteMashStep(step.getId());
      }
    }
  }

  protected long addMashStepToDatabase(MashStep s, long mashStepId) {
    ContentValues values = getMashStepValues(s, mashStepId);
    long id = database.insert(DatabaseHelper.TABLE_STEPS, null, values);
    return id;
  }

  protected boolean updateMashStep(MashStep s, long recipeId) {
    String whereClause = DatabaseHelper.STE_COL_ID + "=" + s.getId();
    ContentValues values = getMashStepValues(s, recipeId);
    return database.update(DatabaseHelper.TABLE_STEPS, values, whereClause, null) > 0;
  }

  protected ContentValues getMashStepValues(MashStep s, long mashProfileId) {
    // Load up values to store
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.STE_COL_OWNER_ID, mashProfileId);
    values.put(DatabaseHelper.STE_COL_DB_ID, Constants.DATABASE_DEFAULT);
    values.put(DatabaseHelper.STE_COL_NAME, s.getName());
    values.put(DatabaseHelper.STE_COL_VERSION, s.getVersion());
    values.put(DatabaseHelper.STE_COL_TYPE, s.getType());
    values.put(DatabaseHelper.STE_COL_INFUSE_AMT, s.getBeerXmlStandardInfuseAmount());
    values.put(DatabaseHelper.STE_COL_STEP_TEMP, s.getBeerXmlStandardStepTemp());
    values.put(DatabaseHelper.STE_COL_STEP_TIME, s.getStepTime());
    values.put(DatabaseHelper.STE_COL_RAMP_TIME, s.getRampTime());
    values.put(DatabaseHelper.STE_COL_END_TEMP, s.getBeerXmlStandardEndTemp());
    values.put(DatabaseHelper.STE_COL_WATER_GRAIN_RATIO, s.getBeerXmlStandardWaterToGrainRatio());
    values.put(DatabaseHelper.STE_COL_DESCRIPTION, s.getDescription());
    values.put(DatabaseHelper.STE_COL_ORDER, s.getOrder());
    values.put(DatabaseHelper.STE_COL_INFUSE_TEMP, s.getBeerXmlStandardInfuseTemp());
    values.put(DatabaseHelper.STE_COL_DECOCT_AMT, s.getBeerXmlDecoctAmount());
    values.put(DatabaseHelper.STE_COL_CALC_INFUSE_TEMP, s.getAutoCalcInfuseTemp() ? 1 : 0);
    values.put(DatabaseHelper.STE_COL_CALC_INFUSE_AMT, s.getAutoCalcInfuseAmt() ? 1 : 0);
    return values;
  }

  /**
   * Deletes all ingredients with the given recipeId
   */
  protected boolean deleteIngredientList(long recipeId) {
    String whereClause = DatabaseHelper.ING_COL_OWNER_ID + "=" + recipeId;
    return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
  }

  protected boolean deleteStyleRecipe(long recipeId) {
    String whereClause = DatabaseHelper.STY_COL_OWNER_ID + "=" + recipeId;
    return deleteStyleWhere(whereClause);
  }

  protected boolean deleteStyleSnapshot(long snapshotId) {
    String whereClause = DatabaseHelper.COL_SNAPSHOT_ID + "=" + snapshotId;
    return deleteStyleWhere(whereClause);
  }

  /**
   * Deletes all styles that match the given SQL string.
   * @param whereClause
   * @return
   */
  private boolean deleteStyleWhere(String whereClause) {
    return database.delete(DatabaseHelper.TABLE_STYLES, whereClause, null) > 0;
  }

  /**
   * Takes id as input, deletes if it exists
   *
   * @param id
   * @return 1 if it was deleted or 0 if not
   */

  protected boolean deleteIngredientIfExists(long id, long dbid) {
    String whereClause = DatabaseHelper.ING_COL_ID + "=" + id + " AND " +
            DatabaseHelper.ING_COL_DB_ID + "=" + dbid;
    return database.delete(DatabaseHelper.TABLE_INGREDIENTS, whereClause, null) > 0;
  }

  protected boolean deleteMashProfile(long id, long dbid) {
    String whereClause = DatabaseHelper.PRO_COL_ID + "=" + id + " AND " +
            DatabaseHelper.PRO_COL_DB_ID + "=" + dbid;
    return database.delete(DatabaseHelper.TABLE_PROFILES, whereClause, null) > 0;
  }

  protected boolean deleteMashStep(long id) {
    String whereClause = DatabaseHelper.STE_COL_ID + "=" + id;
    return database.delete(DatabaseHelper.TABLE_STEPS, whereClause, null) > 0;
  }

  protected Recipe getRecipeWithId(long id) {
    String whereString = DatabaseHelper.REC_COL_ID + "=" + id;

    Cursor cursor = database.query(DatabaseHelper.TABLE_RECIPES, recipeAllColumns, whereString,
                                   null, null, null, null);
    cursor.moveToFirst();
    return cursorToRecipe(cursor);
  }

  /**
   * Takes ingredient ID and returns the ingredient with that ID from the database Undefined for
   * nonexistent IDs
   */
  protected Ingredient getIngredientWithId(long id) {
    String whereString = DatabaseHelper.ING_COL_ID + "=" + id;
    Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns,
                                   whereString, null, null, null, null);
    cursor.moveToFirst();

    try {
      return cursorToIngredient(cursor);
    } catch (Exception e) {
      return null;
    }

  }

  /**
   * Returns ingredients from the given database with the given ingredient type
   */
  protected ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid, String type) {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    String whereString = DatabaseHelper.ING_COL_DB_ID + "=" + dbid + " AND " +
            DatabaseHelper.ING_COL_TYPE + "=?";
    String[] args = {type};
    Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns,
                                   whereString, args, null, null, null);

    cursor.moveToFirst();
    while (! cursor.isAfterLast()) {
      try {
        list.add(cursorToIngredient(cursor));
      } catch (Exception e) {
        Log.e("DatabaseInterface", "Exception reading cursor!");
        e.printStackTrace();
        return list;
      }
      cursor.moveToNext();
    }
    cursor.close();

    return list;
  }

  /**
   * Returns ingredients from the given database with the given ingredient type
   */
  protected ArrayList<MashProfile> getMashProfilesFromVirtualDatabase(long dbid) {
    ArrayList<MashProfile> list = new ArrayList<MashProfile>();
    String whereString = DatabaseHelper.PRO_COL_DB_ID + "=" + dbid;
    Cursor cursor = database.query(DatabaseHelper.TABLE_PROFILES, profileAllColumns, whereString,
                                   null, null, null, null);

    cursor.moveToFirst();
    while (! cursor.isAfterLast()) {
      try {
        list.add(cursorToMashProfile(cursor));
      } catch (Exception e) {
        Log.e("DatabaseInterface", "Exception reading cursor!");
        e.printStackTrace();
        return list;
      }
      cursor.moveToNext();
    }
    cursor.close();

    return list;
  }

  /**
   * Returns ingredients from the given database with the given ingredient type
   */
  protected ArrayList<Ingredient> getIngredientsFromVirtualDatabase(long dbid) {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    String whereString = DatabaseHelper.ING_COL_DB_ID + "=" + dbid;
    Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns,
                                   whereString, null, null, null, null);

    cursor.moveToFirst();
    while (! cursor.isAfterLast()) {
      try {
        list.add(cursorToIngredient(cursor));
      } catch (Exception e) {
        Log.e("DatabaseInterface", "Exception reading cursor!");
        e.printStackTrace();
        return list;
      }
      cursor.moveToNext();
    }
    cursor.close();

    return list;
  }

  protected RecipeSnapshot getSnapshot(long snapshotId) {
    String whereString = DatabaseHelper.REC_COL_ID + "=" + snapshotId;

    Cursor cursor = database.query(DatabaseHelper.TABLE_SNAPSHOTS, snapshotAllColumns, whereString,
                                   null, null, null, null);
    cursor.moveToFirst();
    return cursorToSnapshot(cursor);
  }

  /**
   * Returns a list of snapshots stored for the recipe with the given ID.
   * @param recipeId Index of the stored Recipe whose snapshots will be returned.
   * @return List of RecipeSnapshots.
   */
  protected ArrayList<RecipeSnapshot> getRecipeSnapshots(long recipeId) {
    ArrayList<RecipeSnapshot> list = new ArrayList<RecipeSnapshot>();
    String whereString = DatabaseHelper.SNAP_COL_OWNER_ID + "=" + recipeId;

    Cursor cursor = database.query(DatabaseHelper.TABLE_SNAPSHOTS, snapshotAllColumns, whereString,
                                   null, null, null, null);
    cursor.moveToFirst();

    while (! cursor.isAfterLast()) {
      list.add(cursorToSnapshot(cursor));
      cursor.moveToNext();
    }

    return list;
  }

  /**
   * returns arraylist of all recipes in database
   *
   * @return
   */
  protected ArrayList<Recipe> getRecipeList() {
    ArrayList<Recipe> list = new ArrayList<Recipe>();

    Cursor cursor = database.query(DatabaseHelper.TABLE_RECIPES, recipeAllColumns, null, null,
                                   null, null, null);
    cursor.moveToFirst();

    // Move one forward, so we skip the master recipe
    // TODO What? Get rid of Master Recipe.  I don't remember why it is a thing.
    cursor.moveToNext();

    while (! cursor.isAfterLast()) {
      list.add(cursorToRecipe(cursor));
      cursor.moveToNext();
    }

    cursor.close();
    return list;
  }

  /**
   * This takes a cursor and converts it into a Recipe object
   *
   * @param cursor
   * @return
   */
  private Recipe cursorToRecipe(Cursor cursor) {
    int cid = 0;

    long recipeId = cursor.getLong(cid);
    cid++;
    long dbid = cursor.getLong(cid);
    cid++;
    String recipeName = cursor.getString(cid);
    cid++;
    int version = cursor.getInt(cid);
    cid++;
    String type = cursor.getString(cid);
    cid++;
    String brewer = cursor.getString(cid);
    cid++;
    double batchSize = cursor.getDouble(cid);
    cid++;
    double boilSize = cursor.getDouble(cid);
    cid++;
    int boilTime = cursor.getInt(cid);
    cid++;
    double boilEff = cursor.getDouble(cid);
    cid++;
    double OG = cursor.getDouble(cid);
    cid++;
    double FG = cursor.getDouble(cid);
    cid++;
    int fermentationStages = cursor.getInt(cid);
    cid++;
    String description = cursor.getString(cid);
    cid++;
    int batchTime = cursor.getInt(cid);
    cid++;
    double ABV = cursor.getDouble(cid);
    cid++;
    double bitterness = cursor.getDouble(cid);
    cid++;
    double color = cursor.getDouble(cid);
    cid++;
    double measOG = cursor.getDouble(cid);
    cid++;
    double measFG = cursor.getDouble(cid);
    cid++;
    double primaryTemp = cursor.getDouble(cid);
    cid++;
    int primaryAge = cursor.getInt(cid);
    cid++;
    double secondaryTemp = cursor.getDouble(cid);
    cid++;
    int secondaryAge = cursor.getInt(cid);
    cid++;
    double tertiaryTemp = cursor.getDouble(cid);
    cid++;
    int tertiaryAge = cursor.getInt(cid);
    cid++;
    String tasteNotes = cursor.getString(cid);
    cid++;
    int tasteRating = cursor.getInt(cid);
    cid++;
    int bottleAge = cursor.getInt(cid);
    cid++;
    double bottleTemp = cursor.getDouble(cid);
    cid++;
    String brewDate = cursor.getString(cid);
    cid++;
    double carbonation = cursor.getDouble(cid);
    cid++;
    int isForceCarbed = cursor.getInt(cid);
    cid++;
    String primingSugar = cursor.getString(cid);
    cid++;
    double carbTemp = cursor.getDouble(cid);
    cid++;
    double sugarEquivalent = cursor.getDouble(cid);
    cid++;
    double kegPrimingFactor = cursor.getDouble(cid);
    cid++;
    int calories = cursor.getInt(cid);
    cid++;
    int calcBoilVol = cursor.getInt(cid);
    cid++;
    int calcStrikeTemp = cursor.getInt(cid);
    cid++;
    int calcStrikeVol = cursor.getInt(cid);
    cid++;
    double measBatchsize = cursor.getFloat(cid);
    cid++;

    ArrayList<Ingredient> ingredientsList = readIngredientListRecipe(recipeId);
    BeerStyle style = readStyleRecipe(recipeId);
    MashProfile profile = readMashProfileRecipe(recipeId);

    Log.d("DatabaseInterface", "Creating recipe '" + recipeName + "' from cursor");

    Recipe r = new Recipe(recipeName);
    r.setId(recipeId);
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
    r.setNotes(description);
    r.setBatchTime(batchTime);
    r.setABV(ABV);
    r.setBitterness(bitterness);
    r.setColor(color);
    r.setMeasuredFG(measFG);
    r.setMeasuredOG(measOG);
    r.setFermentationAge(Recipe.STAGE_PRIMARY, primaryAge);
    r.setFermentationAge(Recipe.STAGE_SECONDARY, secondaryAge);
    r.setFermentationAge(Recipe.STAGE_TERTIARY, tertiaryAge);
    r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY, primaryTemp);
    r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY, secondaryTemp);
    r.setBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY, tertiaryTemp);
    r.setTasteNotes(tasteNotes);
    r.setTasteRating(tasteRating);
    r.setBottleAge(bottleAge);
    r.setBeerXmlStandardBottleTemp(bottleTemp);
    r.setBrewDate(brewDate);
    r.setCarbonation(carbonation);
    r.setIsForceCarbonated(isForceCarbed > 0);
    r.setPrimingSugarName(primingSugar);
    r.setBeerXmlStandardCarbonationTemp(carbTemp);
    r.setPrimingSugarEquiv(sugarEquivalent);
    r.setKegPrimingFactor(kegPrimingFactor);
    r.setCalories(calories);
    r.setCalculateBoilVolume(calcBoilVol > 0);
    r.setCalculateStrikeTemp(calcStrikeTemp > 0);
    r.setCalculateStrikeVolume(calcStrikeVol > 0);
    r.setBeerXmlMeasuredBatchSize(measBatchsize);

    r.setStyle(style);
    r.setMashProfile(profile);
    r.setIngredientsList(ingredientsList);

    return r;
  }

  /**
   * This takes a cursor and converts it into a Recipe object
   *
   * @param cursor
   * @return
   */
  private RecipeSnapshot cursorToSnapshot(Cursor cursor) {
    int cid = 0;

    long snapshotId = cursor.getLong(cid);
    cid++;
    long recipeId = cursor.getLong(cid);
    cid++;
    long dbid = cursor.getLong(cid);
    cid++;
    String recipeName = cursor.getString(cid);
    cid++;
    int version = cursor.getInt(cid);
    cid++;
    String type = cursor.getString(cid);
    cid++;
    String brewer = cursor.getString(cid);
    cid++;
    double batchSize = cursor.getDouble(cid);
    cid++;
    double boilSize = cursor.getDouble(cid);
    cid++;
    int boilTime = cursor.getInt(cid);
    cid++;
    double boilEff = cursor.getDouble(cid);
    cid++;
    double OG = cursor.getDouble(cid);
    cid++;
    double FG = cursor.getDouble(cid);
    cid++;
    int fermentationStages = cursor.getInt(cid);
    cid++;
    String description = cursor.getString(cid);
    cid++;
    int batchTime = cursor.getInt(cid);
    cid++;
    double ABV = cursor.getDouble(cid);
    cid++;
    double bitterness = cursor.getDouble(cid);
    cid++;
    double color = cursor.getDouble(cid);
    cid++;
    double measOG = cursor.getDouble(cid);
    cid++;
    double measFG = cursor.getDouble(cid);
    cid++;
    double primaryTemp = cursor.getDouble(cid);
    cid++;
    int primaryAge = cursor.getInt(cid);
    cid++;
    double secondaryTemp = cursor.getDouble(cid);
    cid++;
    int secondaryAge = cursor.getInt(cid);
    cid++;
    double tertiaryTemp = cursor.getDouble(cid);
    cid++;
    int tertiaryAge = cursor.getInt(cid);
    cid++;
    String tasteNotes = cursor.getString(cid);
    cid++;
    int tasteRating = cursor.getInt(cid);
    cid++;
    int bottleAge = cursor.getInt(cid);
    cid++;
    double bottleTemp = cursor.getDouble(cid);
    cid++;
    String brewDate = cursor.getString(cid);
    cid++;
    double carbonation = cursor.getDouble(cid);
    cid++;
    int isForceCarbed = cursor.getInt(cid);
    cid++;
    String primingSugar = cursor.getString(cid);
    cid++;
    double carbTemp = cursor.getDouble(cid);
    cid++;
    double sugarEquivalent = cursor.getDouble(cid);
    cid++;
    double kegPrimingFactor = cursor.getDouble(cid);
    cid++;
    int calories = cursor.getInt(cid);
    cid++;
    int calcBoilVol = cursor.getInt(cid);
    cid++;
    int calcStrikeTemp = cursor.getInt(cid);
    cid++;
    int calcStrikeVol = cursor.getInt(cid);
    cid++;
    double measBatchsize = cursor.getFloat(cid);
    cid++;
    String snapDescription = cursor.getString(cid);
    cid++;
    String snapshotTime = cursor.getString(cid);
    cid++;

    ArrayList<Ingredient> ingredientsList = readIngredientsListSnapshot(snapshotId);
    BeerStyle style = readStyleSnapshot(snapshotId);
    MashProfile profile = readMashProfileSnapshot(snapshotId);

    Log.d("DatabaseInterface", "Creating recipe '" + recipeName + "' from cursor");

    RecipeSnapshot snapshot = new RecipeSnapshot(recipeName);
    snapshot.setId(snapshotId);
    snapshot.setRecipeId(recipeId);
    snapshot.setVersion(version);
    snapshot.setType(type);
    snapshot.setBrewer(brewer);
    snapshot.setBeerXmlStandardBatchSize(batchSize);
    snapshot.setBeerXmlStandardBoilSize(boilSize);
    snapshot.setBoilTime(boilTime);
    snapshot.setEfficiency(boilEff);
    snapshot.setOG(OG);
    snapshot.setFG(FG);
    snapshot.setFermentationStages(fermentationStages);
    snapshot.setNotes(description);
    snapshot.setBatchTime(batchTime);
    snapshot.setABV(ABV);
    snapshot.setBitterness(bitterness);
    snapshot.setColor(color);
    snapshot.setMeasuredFG(measFG);
    snapshot.setMeasuredOG(measOG);
    snapshot.setFermentationAge(Recipe.STAGE_PRIMARY, primaryAge);
    snapshot.setFermentationAge(Recipe.STAGE_SECONDARY, secondaryAge);
    snapshot.setFermentationAge(Recipe.STAGE_TERTIARY, tertiaryAge);
    snapshot.setBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY, primaryTemp);
    snapshot.setBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY, secondaryTemp);
    snapshot.setBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY, tertiaryTemp);
    snapshot.setTasteNotes(tasteNotes);
    snapshot.setTasteRating(tasteRating);
    snapshot.setBottleAge(bottleAge);
    snapshot.setBeerXmlStandardBottleTemp(bottleTemp);
    snapshot.setBrewDate(brewDate);
    snapshot.setCarbonation(carbonation);
    snapshot.setIsForceCarbonated(isForceCarbed > 0);
    snapshot.setPrimingSugarName(primingSugar);
    snapshot.setBeerXmlStandardCarbonationTemp(carbTemp);
    snapshot.setPrimingSugarEquiv(sugarEquivalent);
    snapshot.setKegPrimingFactor(kegPrimingFactor);
    snapshot.setCalories(calories);
    snapshot.setCalculateBoilVolume(calcBoilVol > 0);
    snapshot.setCalculateStrikeTemp(calcStrikeTemp > 0);
    snapshot.setCalculateStrikeVolume(calcStrikeVol > 0);
    snapshot.setBeerXmlMeasuredBatchSize(measBatchsize);
    snapshot.setStyle(style);
    snapshot.setMashProfile(profile);
    snapshot.setIngredientsList(ingredientsList);
    snapshot.setDescription(snapDescription);
    snapshot.setSnapshotTime(snapshotTime);

    return snapshot;
  }

  private ArrayList<Ingredient> readIngredientsListSnapshot(long snapshotId) {
    String whereString = DatabaseHelper.COL_SNAPSHOT_ID + "=" + snapshotId;
    return getIngredientsWhere(whereString);
  }

  private ArrayList<Ingredient> readIngredientListRecipe(long recipeId) {
    String whereString = DatabaseHelper.ING_COL_OWNER_ID + "=" + recipeId;
    return getIngredientsWhere(whereString);
  }

  /**
   * Generic helper for retrieving a list of ingredients from the database that match the given
   * SQL query string.
   * @param whereString
   * @return
   */
  private ArrayList<Ingredient> getIngredientsWhere(String whereString) {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();

    Cursor cursor = database.query(DatabaseHelper.TABLE_INGREDIENTS, ingredientAllColumns,
                                   whereString, null, null, null, null);
    cursor.moveToFirst();

    while (! cursor.isAfterLast()) {
      Ingredient ing;
      try {
        ing = cursorToIngredient(cursor);
      } catch (Exception e) {
        Log.e("DatabaseInterface", "Exception reading cursor!");
        e.printStackTrace();
        return list;
      }
      list.add(ing);
      cursor.moveToNext();
    }
    cursor.close();
    return list;
  }

  private BeerStyle readStyleRecipe(long recipeId) {
    String whereString = DatabaseHelper.STY_COL_OWNER_ID + "=" + recipeId;
    return readStyleWhere(whereString);
  }

  private BeerStyle readStyleSnapshot(long snapshotId) {
    String whereString = DatabaseHelper.COL_SNAPSHOT_ID + "=" + snapshotId;
    return readStyleWhere(whereString);
  }

  /**
   * Returns the first Style in the database to match the given SQL string.
   * @param whereString
   * @return
   */
  private BeerStyle readStyleWhere(String whereString) {
    Cursor cursor = database.query(DatabaseHelper.TABLE_STYLES, stylesAllColumns, whereString,
                                   null, null, null, null);

    cursor.moveToFirst();
    BeerStyle style = cursorToStyle(cursor);
    cursor.close();

    return style;
  }

  private BeerStyle cursorToStyle(Cursor cursor) {
    int cid = 0;

    // Get all the values from the cursor
    long id = cursor.getLong(cid);
    cid++;
    long ownerId = cursor.getLong(cid);
    cid++;
    long dbid = cursor.getLong(cid);
    cid++;
    String name = cursor.getString(cid);
    cid++;
    String category = cursor.getString(cid);
    cid++;
    String catNumber = cursor.getString(cid);
    cid++;
    String styleLetter = cursor.getString(cid);
    cid++;
    String styleGuide = cursor.getString(cid);
    cid++;
    String type = cursor.getString(cid);
    cid++;
    double minOg = cursor.getDouble(cid);
    cid++;
    double maxOg = cursor.getDouble(cid);
    cid++;
    double minFg = cursor.getDouble(cid);
    cid++;
    double maxFg = cursor.getDouble(cid);
    cid++;
    double minIbu = cursor.getDouble(cid);
    cid++;
    double maxIbu = cursor.getDouble(cid);
    cid++;
    double minSrm = cursor.getDouble(cid);
    cid++;
    double maxSrm = cursor.getDouble(cid);
    cid++;
    double minCarb = cursor.getDouble(cid);
    cid++;
    double maxCarb = cursor.getDouble(cid);
    cid++;
    double minAbv = cursor.getDouble(cid);
    cid++;
    double maxAbv = cursor.getDouble(cid);
    cid++;
    String notes = cursor.getString(cid);
    cid++;
    String profile = cursor.getString(cid);
    cid++;
    String ingredients = cursor.getString(cid);
    cid++;
    String examples = cursor.getString(cid);
    cid++;
    long snapshotId = cursor.getLong(cid);
    cid++;

    // Stick them all in a new object
    BeerStyle style = new BeerStyle(name);
    style.setId(id);
    style.setRecipeId(ownerId);
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
    style.setSnapshotId(snapshotId);

    return style;
  }

  private MashProfile readMashProfileRecipe(long recipeId) {
    String whereString = DatabaseHelper.PRO_COL_OWNER_ID + "=" + recipeId;
    return readMashProfileWhere(whereString);
  }

  private MashProfile readMashProfileSnapshot(long snapshotId) {
    String whereString = DatabaseHelper.COL_SNAPSHOT_ID + "=" + snapshotId;
    return readMashProfileWhere(whereString);
  }

  /**
   * Returns the first MashProfile in the database that matches the given SQL string.
   * @param whereString
   * @return
   */
  private MashProfile readMashProfileWhere(String whereString) {
    Cursor cursor = database.query(DatabaseHelper.TABLE_PROFILES, profileAllColumns, whereString,
                                   null, null, null, null);

    cursor.moveToFirst();
    try {
      MashProfile profile = cursorToMashProfile(cursor);
      cursor.close();
      return profile;
    } catch (Exception e) {
      Log.e("DatabaseInterface", "Exception reading cursor!");
      e.printStackTrace();
      return new MashProfile();
    }
  }

  private MashProfile cursorToMashProfile(Cursor cursor) throws Exception {
    int cid = 0;

    long id = cursor.getLong(cid);
    cid++;
    long recipeId = cursor.getLong(cid);
    cid++;
    long dbid = cursor.getLong(cid);
    cid++;
    String name = cursor.getString(cid);
    cid++;
    Integer version = cursor.getInt(cid);
    cid++;
    double grainTemp = cursor.getDouble(cid);
    cid++;
    String notes = cursor.getString(cid);
    cid++;
    double tunTemp = cursor.getDouble(cid);
    cid++;
    double spargeTemp = cursor.getDouble(cid);
    cid++;
    double pH = cursor.getDouble(cid);
    cid++;
    double tunWeight = cursor.getDouble(cid);
    cid++;
    double tunSpecHeat = cursor.getDouble(cid);
    cid++;
    int equipAdjInt = cursor.getInt(cid);
    cid++;
    String mashType = cursor.getString(cid);
    cid++;
    String spargeType = cursor.getString(cid);
    cid++;
    long snapshotId = cursor.getLong(cid);

    ArrayList<MashStep> stepsList = readMashStepsList(id);

    MashProfile p = new MashProfile();
    p.setId(id);
    p.setRecipeId(recipeId);
    p.setName(name);
    p.setVersion(version);
    p.setBeerXmlStandardGrainTemp(grainTemp);
    p.setNotes(notes);
    p.setBeerXmlStandardTunTemp(tunTemp);
    p.setBeerXmlStandardSpargeTemp(spargeTemp);
    p.setpH(pH);
    p.setBeerXmlStandardTunWeight(tunWeight);
    p.setBeerXmlStandardTunSpecHeat(tunSpecHeat);
    p.setEquipmentAdjust(equipAdjInt > 0 ? true : false);
    p.setMashStepList(stepsList);
    p.setMashType(mashType);
    p.setSpargeType(spargeType);
    p.setSnapshotId(snapshotId);
    return p;
  }

  private ArrayList<MashStep> readMashStepsList(long id) {
    ArrayList<MashStep> list = new ArrayList<MashStep>();
    String whereString = DatabaseHelper.STE_COL_OWNER_ID + "=" + id;
    Cursor cursor = database.query(DatabaseHelper.TABLE_STEPS, stepAllColumns, whereString, null,
                                   null, null, null);

    cursor.moveToFirst();
    while (! cursor.isAfterLast()) {
      MashStep step = cursorToMashStep(cursor);
      list.add(step);
      cursor.moveToNext();
    }
    cursor.close();

    return list;
  }

  private MashStep cursorToMashStep(Cursor cursor) {
    int cid = 0;

    long id = cursor.getLong(cid);
    cid++;
    long ownerId = cursor.getLong(cid);
    cid++;
    long dbid = cursor.getLong(cid);
    cid++;
    String name = cursor.getString(cid);
    cid++;
    Integer version = cursor.getInt(cid);
    cid++;
    String type = cursor.getString(cid);
    cid++;
    double infuseAmt = cursor.getDouble(cid);
    cid++;
    double stepTemp = cursor.getDouble(cid);
    cid++;
    Double stepTime = cursor.getDouble(cid);
    cid++;
    Double rampTime = cursor.getDouble(cid);
    cid++;
    double endTemp = cursor.getDouble(cid);
    cid++;
    double waterGrainRatio = cursor.getDouble(cid);
    cid++;
    String description = cursor.getString(cid);
    cid++;
    int order = cursor.getInt(cid);
    cid++;
    double infuseTemp = cursor.getDouble(cid);
    cid++;
    double decoctAmt = cursor.getDouble(cid);
    cid++;
    int calcInfuseTemp = cursor.getInt(cid);
    cid++;
    int calcInfuseAmt = cursor.getInt(cid);
    cid++;

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
    s.setBeerXmlStandardWaterToGrainRatio(waterGrainRatio);
    s.setDescription(description);
    s.setOrder(order);
    s.setBeerXmlStandardInfuseTemp(infuseTemp);
    s.setBeerXmlDecoctAmount(decoctAmt);
    s.setAutoCalcInfuseTemp(calcInfuseTemp == 1 ? true : false);
    s.setAutoCalcInfuseAmt(calcInfuseAmt == 1 ? true : false);

    return s;
  }

  private Ingredient cursorToIngredient(Cursor cursor) throws Exception {
    int cid = 0;

    // Ingredient type agnostic stuff
    long id = cursor.getLong(cid);
    cid++;
    long recipeId = cursor.getLong(cid);
    cid++;
    long databaseId = cursor.getLong(cid);
    cid++;
    String ingType = cursor.getString(cid);
    cid++;
    String name = cursor.getString(cid);
    cid++;
    String description = cursor.getString(cid);
    cid++;
    String units = cursor.getString(cid);
    cid++;
    double amount = cursor.getDouble(cid);
    cid++;
    int time = cursor.getInt(cid);
    cid++;
    double inventory = cursor.getDouble(cid);
    cid++;

    // Get non-specific stuff before creating Ingredient objects.
    long snapshotId = cursor.getLong(ingredientColumns.indexOf(DatabaseHelper.COL_SNAPSHOT_ID));

    // Fermentable specific stuff
    if (ingType.equals(Ingredient.FERMENTABLE)) {
      String type = cursor.getString(cid);
      cid++;
      double yield = cursor.getDouble(cid);
      cid++;
      double color = cursor.getDouble(cid);
      cid++;
      int afterBoil = cursor.getInt(cid);
      cid++;
      double maxInBatch = cursor.getDouble(cid);
      cid++;
      double gravity = cursor.getDouble(cid);
      cid++;

      boolean addAfterBoil = (afterBoil == 0) ? false : true;

      Fermentable ingredient = new Fermentable(name);
      ingredient.setId(id);
      ingredient.setDatabaseId(databaseId);
      ingredient.setBeerXmlStandardInventory(inventory);
      ingredient.setRecipeId(recipeId);
      ingredient.setShortDescription(description);
      ingredient.setDisplayUnits(units);
      ingredient.setBeerXmlStandardAmount(amount);
      ingredient.setTime(time);
      ingredient.setFermentableType(type);
      ingredient.setYield(yield);
      ingredient.setLovibondColor(color);
      ingredient.setAddAfterBoil(addAfterBoil);
      ingredient.setMaxInBatch(maxInBatch);
      ingredient.setGravity(gravity);
      ingredient.setSnapshotId(snapshotId);

      return ingredient;
    }

    // Hop specific stuff
    else if (ingType.equals(Ingredient.HOP)) {
      cid += 6;

      String type = cursor.getString(cid);
      cid++;
      double alpha = cursor.getDouble(cid);
      cid++;
      String use = cursor.getString(cid);
      cid++;
      String form = cursor.getString(cid);
      cid++;
      String origin = cursor.getString(cid);
      cid++;

      Hop ingredient = new Hop(name);
      ingredient.setDatabaseId(databaseId);
      ingredient.setBeerXmlStandardInventory(inventory);
      ingredient.setId(id);
      ingredient.setRecipeId(recipeId);
      ingredient.setShortDescription(description);
      ingredient.setDisplayUnits(units);
      ingredient.setBeerXmlStandardAmount(amount);
      ingredient.setHopType(type);
      ingredient.setAlphaAcidContent(alpha);
      ingredient.setUse(use);
      ingredient.setDisplayTime(time);
      ingredient.setForm(form);
      ingredient.setOrigin(origin);
      ingredient.setSnapshotId(snapshotId);

      return ingredient;
    }

    // Yeast specific stuff
    else if (ingType.equals(Ingredient.YEAST)) {
      cid += 11;

      String type = cursor.getString(cid);
      cid++;
      String form = cursor.getString(cid);
      cid++;
      Double minTemp = cursor.getDouble(cid);
      cid++;
      Double maxTemp = cursor.getDouble(cid);
      cid++;
      Double attn = cursor.getDouble(cid);
      cid++;
      String notes = cursor.getString(cid);
      cid++;
      String bestFor = cursor.getString(cid);
      cid++;
      String lab = cursor.getString(cid);
      cid++;
      String productId = cursor.getString(cid);
      cid++;

      Yeast ingredient = new Yeast(name);
      ingredient.setDatabaseId(databaseId);
      ingredient.setBeerXmlStandardInventory(inventory);
      ingredient.setId(id);
      ingredient.setRecipeId(recipeId);
      ingredient.setShortDescription(description);
      ingredient.setDisplayUnits(units);
      ingredient.setBeerXmlStandardAmount(amount);
      ingredient.setTime(time);
      ingredient.setType(type);
      ingredient.setForm(form);
      ingredient.setMinTemp(minTemp);
      ingredient.setMaxTemp(maxTemp);
      ingredient.setAttenuation(attn);
      ingredient.setNotes(notes);
      ingredient.setBestFor(bestFor);
      ingredient.setLaboratory(lab);
      ingredient.setProductId(productId);
      ingredient.setSnapshotId(snapshotId);

      return ingredient;
    }

    if (ingType.equals(Ingredient.MISC)) {
      cid += 20;

      Misc ingredient = new Misc(name);
      String miscType = cursor.getString(cid);
      cid++;
      int version = cursor.getInt(cid);
      cid++;
      int amtIsWeight = cursor.getInt(cid);
      cid++;
      String useFor = cursor.getString(cid);
      cid++;
      String use = cursor.getString(cid);
      cid++;

      ingredient.setId(id);
      ingredient.setDatabaseId(databaseId);
      ingredient.setBeerXmlStandardInventory(inventory);
      ingredient.setRecipeId(recipeId);
      ingredient.setShortDescription(description);
      ingredient.setDisplayUnits(units);
      ingredient.setBeerXmlStandardAmount(amount);
      ingredient.setTime(time);
      ingredient.setMiscType(miscType);
      ingredient.setVersion(version);
      ingredient.setAmountIsWeight(amtIsWeight > 0 ? true : false);
      ingredient.setUse(use);
      ingredient.setUseFor(useFor);
      ingredient.setSnapshotId(snapshotId);

      return ingredient;
    }

    throw new Exception("No ingredient found");
  }
}
