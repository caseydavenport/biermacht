package com.biermacht.brews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database / Table Data Defines
	public static final String DATABASE_NAME = "BiermachtBrews";

    // Versioning information for the database
    public static final int DATABASE_ALPHA = 1; // Database used in development stages
    public static final int DATABASE_BETA = 2;

    // Current database version
    public static final int DATABASE_VERSION = DATABASE_BETA;
	
	// Tables
	public static final String TABLE_RECIPES = "RecipeTable";
	public static final String TABLE_INGREDIENTS = "IngredientTable";
	public static final String TABLE_STYLES = "StyleTable";
	public static final String TABLE_PROFILES="MashProfileTable";
	public static final String TABLE_STEPS="MashStepTable";
	
	// Column name defines for RECIPES
	public static final String REC_COL_ID = "_id";
    public static final String REC_COL_DB_ID = "databaseId";
	public static final String REC_COL_NAME = "name";
	public static final String REC_COL_VER = "xmlversion";
	public static final String REC_COL_TYPE = "type";
	public static final String REC_COL_BREWER = "brewer";
	public static final String REC_COL_BATCH_SIZE = "batchSize";
	public static final String REC_COL_BOIL_SIZE = "boilSize";
	public static final String REC_COL_BOIL_TIME = "boilTime";
	public static final String REC_COL_BOIL_EFF = "efficiency";
	public static final String REC_COL_OG = "OG";
	public static final String REC_COL_FG = "FG";
	public static final String REC_COL_STAGES = "fermStages";
	public static final String REC_COL_DESC = "description";
	public static final String REC_COL_BATCH_TIME = "batchTime";
	public static final String REC_COL_ABV = "ABV";
	public static final String REC_COL_BITTER = "bitterness";
	public static final String REC_COL_COLOR = "color";
	public static final String REC_COL_MEAS_OG = "measuredOG";
	public static final String REC_COL_MEAS_FG = "measuredFG";
	public static final String REC_COL_PRIMARY_TEMP = "primaryTemp";
	public static final String REC_COL_PRIMARY_AGE = "primaryAge";
	public static final String REC_COL_SECONDARY_TEMP = "secondaryTemp";
	public static final String REC_COL_SECONDARY_AGE = "secondaryAge";
	public static final String REC_COL_TERTIARY_TEMP = "tertiaryTemp";
	public static final String REC_COL_TERTIARY_AGE = "tertiaryAge";
    public static final String REC_COL_TASTE_NOTES = "tasteNotes";
    public static final String REC_COL_TASTE_RATING = "tasteRating";
    public static final String REC_COL_BOTTLE_AGE = "bottleAge";
    public static final String REC_COL_BOTTLE_TEMP = "bottleTemp";
    public static final String REC_COL_BREW_DATE = "brewDate";
    public static final String REC_COL_CARBONATION = "carbonation";
    public static final String REC_COL_FORCED_CARB = "forcedCarb";
    public static final String REC_COL_PRIMING_SUGAR_NAME = "primingSugarName";
    public static final String REC_COL_CARB_TEMP = "carbonationTemp";
    public static final String REC_COL_PRIMING_SUGAR_EQUIV = "primingSugarEquiv";
    public static final String REC_COL_KEG_PRIMING_FACTOR = "kegPrimingFactor";
    public static final String REC_COL_CALORIES = "calories";
    public static final String REC_COL_CALC_BOIL_VOL = "calculateBoilVolume";
    public static final String REC_COL_CALC_STRIKE_TEMP = "calculateStrikeTemp";
    public static final String REC_COL_CALC_STRIKE_VOL = "calculateStrikeVolume";
	
	// Column name defines for INGREDIENTS
	public static final String ING_COL_ID = "_id";
	public static final String ING_COL_OWNER_ID = "ownerId";
    public static final String ING_COL_DB_ID = "databaseId";
	public static final String ING_COL_TYPE = "ingType";
	public static final String ING_COL_NAME = "name";
	public static final String ING_COL_DESC = "description";
	public static final String ING_COL_UNITS = "units";
	public static final String ING_COL_AMT = "amount";
	public static final String ING_COL_TIME = "time";
    public static final String ING_COL_INVENTORY = "inventory";
	
	public static final String ING_FR_COL_TYPE = "ferType";
	public static final String ING_FR_COL_YIELD = "yield";
	public static final String ING_FR_COL_COLOR = "color";
	public static final String ING_FR_COL_ADD_AFTER_BOIL = "addAfterBoil";
	public static final String ING_FR_COL_MAX_IN_BATCH = "maxInBatch";
	public static final String ING_FR_COL_GRAV = "gravity";
	
	public static final String ING_HP_COL_TYPE = "hopType";
	public static final String ING_HP_COL_ALPHA = "alpha";
	public static final String ING_HP_COL_USE = "use";
	public static final String ING_HP_COL_FORM = "form";
	public static final String ING_HP_COL_ORIGIN = "origin";
	
	public static final String ING_YS_COL_TYPE = "yeastType";
	public static final String ING_YS_COL_FORM = "yeastForm";
	public static final String ING_YS_COL_MIN_TEMP = "minTemp";
	public static final String ING_YS_COL_MAX_TEMP = "maxTempt";
	public static final String ING_YS_COL_ATTENUATION = "attenuation";
	public static final String ING_YS_COL_NOTES = "notes";
	public static final String ING_YS_COL_BEST_FOR = "bestFor";
	
	public static final String ING_MC_COL_TYPE = "miscType";
	public static final String ING_MC_COL_VERSION = "miscVersion";
	public static final String ING_MC_COL_AMT_IS_WEIGHT = "amountIsWeight";
	public static final String ING_MC_COL_USE_FOR = "useFor";
	public static final String ING_MC_COL_USE = "miscUse";
	
	// Column define names for styles
	public static final String STY_COL_ID = "_id";
	public static final String STY_COL_OWNER_ID = "styleOwnerId";
    public static final String STY_COL_DB_ID = "databaseId";
	public static final String STY_COL_NAME = "styleName";
	public static final String STY_COL_CATEGORY = "styleCategory";
	public static final String STY_COL_CAT_NUM = "styleCategoryNumber";
	public static final String STY_COL_STY_LETTER = "styleLetter";
	public static final String STY_COL_STY_GUIDE = "styleGuide";
	public static final String STY_COL_TYPE = "styleType";
	public static final String STY_COL_OG_MIN = "styleOgMin";
	public static final String STY_COL_OG_MAX = "styleOgMax";
	public static final String STY_COL_FG_MIN = "styleFgMin";
	public static final String STY_COL_FG_MAX = "styleFgMax";
	public static final String STY_COL_IBU_MIN = "styleIbuMin";
	public static final String STY_COL_IBU_MAX = "styleIbuMax";
	public static final String STY_COL_SRM_MIN = "styleSrmMin";
	public static final String STY_COL_SRM_MAX = "styleSrmMax";
	public static final String STY_COL_CARB_MIN = "styleCarbMin";
	public static final String STY_COL_CARB_MAX = "styleCarbMax";
	public static final String STY_COL_ABV_MIN = "styleAbvMin";
	public static final String STY_COL_ABV_MAX = "styleAbvMax";
	public static final String STY_COL_NOTES = "styleNotes";
	public static final String STY_COL_PROFILE = "styleProfile";
	public static final String STY_COL_INGREDIENTS = "styleIngredients";
	public static final String STY_COL_EXAMPLES = "styleExamples";
	
	// Column define names for Mash Profiles
	public static final String PRO_COL_ID = "_id";
	public static final String PRO_COL_OWNER_ID = "profileOwnerId";
    public static final String PRO_COL_DB_ID = "databaseId";
	public static final String PRO_COL_NAME = "profileName";
	public static final String PRO_COL_VERSION = "profileVersion";
	public static final String PRO_COL_GRAIN_TEMP = "profileGrainTemp";
	public static final String PRO_COL_NOTES = "profileNotes";
	public static final String PRO_COL_TUN_TEMP = "profileTunTemp";
	public static final String PRO_COL_SPARGE_TEMP = "profileSpargeTemp";
	public static final String PRO_COL_PH = "profilePH";
	public static final String PRO_COL_TUN_WEIGHT = "profileTunWeight";
	public static final String PRO_COL_TUN_SPEC_HEAT = "profileTunSpecificHeat";
	public static final String PRO_COL_TUN_EQUIP_ADJ = "profileEquipmentAdjust";
    public static final String PRO_COL_MASH_TYPE = "profileMashType";
    public static final String PRO_COL_SPARGE_TYPE = "profileSpargeType";
	
	// Column define names for Mash Steps
	public static final String STE_COL_ID = "_id";
	public static final String STE_COL_OWNER_ID = "stepOwnerId";
    public static final String STE_COL_DB_ID = "databaseId";
	public static final String STE_COL_NAME = "stepName";
	public static final String STE_COL_VERSION = "stepVersion";
	public static final String STE_COL_TYPE = "stepType";
	public static final String STE_COL_INFUSE_AMT = "stepInfuseAmount";
	public static final String STE_COL_STEP_TEMP = "stepStepTemp";
	public static final String STE_COL_STEP_TIME = "stepStepTime";
	public static final String STE_COL_RAMP_TIME = "stepRampTime";
	public static final String STE_COL_END_TEMP = "stepEndTemp";
    public static final String STE_COL_WATER_GRAIN_RATIO = "stepWaterGrainRatio";
    public static final String STE_COL_DESCRIPTION = "stepDescription";
    public static final String STE_COL_ORDER = "stepOrder";
    public static final String STE_COL_INFUSE_TEMP = "stepInfuseTemp";
    public static final String STE_COL_DECOCT_AMT = "decoctionAmount";
	
	// Create table strings
	private static final String CREATE_RECIPE_TABLE = "create table " +
		TABLE_RECIPES 
		+ "("
			+ REC_COL_ID + " integer primary key autoincrement, "
            + REC_COL_DB_ID + " long not null, "
			+ REC_COL_NAME + " text not null, " 
			+ REC_COL_VER + " int not null, "
			+ REC_COL_TYPE + " text not null, "
			+ REC_COL_BREWER + " text not null, "
			+ REC_COL_BATCH_SIZE + " float not null, "
			+ REC_COL_BOIL_SIZE + " float not null, "
			+ REC_COL_BOIL_TIME + " int not null, "
			+ REC_COL_BOIL_EFF + " float not null, "
			+ REC_COL_OG + " float not null, "
			+ REC_COL_FG + " float not null, "
			+ REC_COL_STAGES + " int not null, "
			+ REC_COL_DESC + " text not null, "
			+ REC_COL_BATCH_TIME + " int not null, "
			+ REC_COL_ABV + " float not null, " 
			+ REC_COL_BITTER + " float not null, " 
			+ REC_COL_COLOR + " float not null, "
			+ REC_COL_MEAS_OG + " float, "
			+ REC_COL_MEAS_FG + " float, "
			+ REC_COL_PRIMARY_TEMP + " float, "
			+ REC_COL_PRIMARY_AGE + " integer, "
			+ REC_COL_SECONDARY_TEMP + " float, "
			+ REC_COL_SECONDARY_AGE + " integer, "
			+ REC_COL_TERTIARY_TEMP + " float, "
			+ REC_COL_TERTIARY_AGE + " integer, "
            + REC_COL_TASTE_NOTES + " string, "
            + REC_COL_TASTE_RATING + " integer, "
            + REC_COL_BOTTLE_AGE + " integer, "
            + REC_COL_BOTTLE_TEMP + " float, "
            + REC_COL_BREW_DATE + " string, "
            + REC_COL_CARBONATION + " float, "
            + REC_COL_FORCED_CARB + " integer, "
            + REC_COL_PRIMING_SUGAR_NAME + " string, "
            + REC_COL_CARB_TEMP + " float, "
            + REC_COL_PRIMING_SUGAR_EQUIV + " float, "
            + REC_COL_KEG_PRIMING_FACTOR + " float, "
            + REC_COL_CALORIES + " integer, "
            + REC_COL_CALC_BOIL_VOL + " integer, "
            + REC_COL_CALC_STRIKE_TEMP + " integer, "
            + REC_COL_CALC_STRIKE_VOL + " integer"
		+ ");";
	
	private static final String CREATE_INGREDIENT_TABLE = "create table " +
			TABLE_INGREDIENTS 
			+ "("
				+ ING_COL_ID + " integer primary key autoincrement, "
				+ ING_COL_OWNER_ID + " long not null, "
                + ING_COL_DB_ID + " long not null, "
				+ ING_COL_TYPE + " text not null, "
				+ ING_COL_NAME + " text not null, "
				+ ING_COL_DESC + " text not null, "
				+ ING_COL_UNITS + " text not null, "
				+ ING_COL_AMT + " float not null, "
				+ ING_COL_TIME + " int not null, "
                + ING_COL_INVENTORY + " float, "
				
				+ ING_FR_COL_TYPE + " text, "
				+ ING_FR_COL_YIELD + " float, "
				+ ING_FR_COL_COLOR + " float, " 
				+ ING_FR_COL_ADD_AFTER_BOIL + " int, "
				+ ING_FR_COL_MAX_IN_BATCH + " float, "
				+ ING_FR_COL_GRAV + " float, " 
				
				+ ING_HP_COL_TYPE + " text, "
				+ ING_HP_COL_ALPHA + " float, "
				+ ING_HP_COL_USE + " text, "
				+ ING_HP_COL_FORM + " text, "
				+ ING_HP_COL_ORIGIN + " text, "
				
				+ ING_YS_COL_TYPE + " text, "
				+ ING_YS_COL_FORM + " text, "
				+ ING_YS_COL_MIN_TEMP + " float, "
				+ ING_YS_COL_MAX_TEMP + " float, "
				+ ING_YS_COL_ATTENUATION + " float, "
				+ ING_YS_COL_NOTES + " text, "
				+ ING_YS_COL_BEST_FOR + " text, "
				
				+ ING_MC_COL_TYPE + " text, "
				+ ING_MC_COL_VERSION + " int, "
				+ ING_MC_COL_AMT_IS_WEIGHT + " int, "
				+ ING_MC_COL_USE_FOR + " text, "
				+ ING_MC_COL_USE + " text"
				+ ");";
			
	public static final String CREATE_STYLE_TABLE = "create table " +
		TABLE_STYLES
		+ "("
			+ STY_COL_ID + " integer primary key autoincrement, "
			+ STY_COL_OWNER_ID + " long not null, "
            + STY_COL_DB_ID + " long not null, "
			+ STY_COL_NAME + " text not null, "
			+ STY_COL_CATEGORY + " text not null, "
			+ STY_COL_CAT_NUM + " text not null, "
			+ STY_COL_STY_LETTER + " text not null, "
			+ STY_COL_STY_GUIDE + " text not null, "
			+ STY_COL_TYPE + " text not null, "
			+ STY_COL_OG_MIN + " float not null, "
			+ STY_COL_OG_MAX + " float not null, "
			+ STY_COL_FG_MIN + " float not null, "
			+ STY_COL_FG_MAX + " float not null, "
			+ STY_COL_IBU_MIN + " float not null, "
			+ STY_COL_IBU_MAX + " float not null, "
			+ STY_COL_SRM_MIN + " float not null, "
			+ STY_COL_SRM_MAX + " float not null, "
			+ STY_COL_CARB_MIN + " float, "
			+ STY_COL_CARB_MAX + " float, "
			+ STY_COL_ABV_MIN + " float, "
			+ STY_COL_ABV_MAX + " float, "
			+ STY_COL_NOTES + " text, "
			+ STY_COL_PROFILE + " text, "
			+ STY_COL_INGREDIENTS + " text, "
			+ STY_COL_EXAMPLES + " text"
			+ ");";
			
	public static final String CREATE_PROFILE_TABLE = "create table " +
	    TABLE_PROFILES
	    + "("
			+ PRO_COL_ID + " integer primary key autoincrement, "
	        + PRO_COL_OWNER_ID + " long not null, "
            + PRO_COL_DB_ID + " long not null, "
			+ PRO_COL_NAME + " text not null, "
			+ PRO_COL_VERSION + " integer not null, "
			+ PRO_COL_GRAIN_TEMP + " float, "
			+ PRO_COL_NOTES + " text, "
			+ PRO_COL_TUN_TEMP + " float, "
			+ PRO_COL_SPARGE_TEMP + " float, "
			+ PRO_COL_PH + " float, "
			+ PRO_COL_TUN_WEIGHT + " float, "
			+ PRO_COL_TUN_SPEC_HEAT + " float, "
			+ PRO_COL_TUN_EQUIP_ADJ + " integer, "
            + PRO_COL_MASH_TYPE + " text, "
            + PRO_COL_SPARGE_TYPE + " text"
	        + ");";
	
	public static final String CREATE_STEP_TABLE = "create table " +
	    TABLE_STEPS
		+ "("
		    + STE_COL_ID + " integer primary key autoincrement, "
            + STE_COL_DB_ID + " long not null, "
			+ STE_COL_OWNER_ID + " long not null, "
			+ STE_COL_NAME + " text not null, "
			+ STE_COL_VERSION + " integer not null, "
			+ STE_COL_TYPE + " text not null, "
			+ STE_COL_INFUSE_AMT + " float, "
			+ STE_COL_STEP_TEMP + " float, "
			+ STE_COL_STEP_TIME + " float, "
			+ STE_COL_RAMP_TIME + " float, "
			+ STE_COL_END_TEMP + " float, "
            + STE_COL_WATER_GRAIN_RATIO + " float, "
            + STE_COL_DESCRIPTION + " text, "
            + STE_COL_ORDER + " integer, "
            + STE_COL_INFUSE_TEMP + " float, "
            + STE_COL_DECOCT_AMT + " float "
			+");";
	
	// Public Constructor
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create the table
		database.execSQL(CREATE_RECIPE_TABLE);
		database.execSQL(CREATE_INGREDIENT_TABLE);
		database.execSQL(CREATE_STYLE_TABLE);
		database.execSQL(CREATE_PROFILE_TABLE);
		database.execSQL(CREATE_STEP_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int currentVersion, int newVersion)
    {
        Log.d("DatabaseHelper", "Upgrading database from version " + currentVersion + " to " + newVersion);
        String sql = "";
        // Allows for incremental upgrades in case a user has skipped an upgrade.
        // Add a case block for each upgrade over time.
        while (currentVersion < newVersion)
        {
            // Increment currentVersion so that the value it stores is
            // actually the next version we're upgrading to.
            currentVersion++;
            switch (currentVersion)
            {
                case DATABASE_BETA:
                    // Upgrade from ALPHA to BETA.  Our first ever upgrade!  We are testing
                    // Database upgrades using the new infusion water temperature for mash steps.
                    Log.d("DatabaseHelper", "Upgrading database from ALPHA to BETA");
                    sql = "ALTER TABLE " + TABLE_STEPS + " ADD COLUMN " + DatabaseHelper.STE_COL_INFUSE_TEMP +
                            " float";
                    db.execSQL(sql);
                    break;
            }
        }
	}
}