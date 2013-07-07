package com.biermacht.brews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database / Table Data Defines
	public static final String DATABASE_NAME = "BiermachtBrews";
	public static final int DATABASE_VERSION = 1;
	
	// Tables
	public static final String TABLE_RECIPES = "RecipeTable";
	public static final String TABLE_INGREDIENTS = "IngredientTable";
	public static final String TABLE_STYLES = "StyleTable";
	public static final String TABLE_PROFILES="MashProfileTable";
	public static final String TABLE_STEPS="MashStepTable";
	
	// Column name defines for RECIPES
	public static final String REC_COL_ID = "_id";
	public static final String REC_COL_NAME = "name";
	public static final String REC_COL_VER = "xmlversion";
	public static final String REC_COL_TYPE = "type";
	public static final String REC_COL_STYLE = "style";
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
	
	// Column name defines for INGREDIENTS
	public static final String ING_COL_ID = "_id";
	public static final String ING_COL_OWNER_ID = "ownerId";
	public static final String ING_COL_TYPE = "ingType";
	public static final String ING_COL_NAME = "name";
	public static final String ING_COL_DESC = "description";
	public static final String ING_COL_UNITS = "units";
	public static final String ING_COL_AMT = "amount";
	public static final String ING_COL_START_TIME = "startTime";
	public static final String ING_COL_END_TIME = "endTime";
	
	public static final String ING_FR_COL_TYPE = "ferType";
	public static final String ING_FR_COL_YIELD = "yield";
	public static final String ING_FR_COL_COLOR = "color";
	public static final String ING_FR_COL_ADD_AFTER_BOIL = "addAfterBoil";
	public static final String ING_FR_COL_MAX_IN_BATCH = "maxInBatch";
	public static final String ING_FR_COL_GRAV = "gravity";
	
	public static final String ING_HP_COL_TYPE = "hopType";
	public static final String ING_HP_COL_ALPHA = "alpha";
	public static final String ING_HP_COL_USE = "use";
	public static final String ING_HP_COL_TIME = "time";
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
	
	// Column define names for styles
	public static final String STY_COL_ID = "_id";
	public static final String STY_COL_OWNER_ID = "styleOwnerId";
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
	
	// Column define names for Mash Steps
	public static final String STE_COL_ID = "_id";
	public static final String STE_COL_OWNER_ID = "stepOwnerId";
	public static final String STE_COL_NAME = "stepName";
	public static final String STE_COL_VERSION = "stepVersion";
	public static final String STE_COL_TYPE = "stepType";
	public static final String STE_COL_INFUSE_AMT = "stepInfuseAmount";
	public static final String STE_COL_STEP_TEMP = "stepStepTemp";
	public static final String STE_COL_STEP_TIME = "stepStepTime";
	public static final String STE_COL_RAMP_TIME = "stepRampTime";
	public static final String STE_COL_END_TEMP = "stepEndTemp";
	
	// Create table strings
	private static final String CREATE_RECIPE_TABLE = "create table " +
		TABLE_RECIPES 
		+ "("
			+ REC_COL_ID + " integer primary key autoincrement, "
			+ REC_COL_NAME + " text not null, " 
			+ REC_COL_VER + " int not null, "
			+ REC_COL_TYPE + " text not null, " 
			+ REC_COL_STYLE + " text not null, "
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
			+ REC_COL_COLOR + " float not null"

		+ ");";
	
	private static final String CREATE_INGREDIENT_TABLE = "create table " +
			TABLE_INGREDIENTS 
			+ "("
				+ ING_COL_ID + " integer primary key autoincrement, "
				+ ING_COL_OWNER_ID + " long not null, "
				+ ING_COL_TYPE + " text not null, "
				+ ING_COL_NAME + " text not null, "
				+ ING_COL_DESC + " text not null, "
				+ ING_COL_UNITS + " text not null, "
				+ ING_COL_AMT + " float not null, "
				+ ING_COL_START_TIME + " int not null, "
				+ ING_COL_END_TIME + " int not null, "
				
				+ ING_FR_COL_TYPE + " text, "
				+ ING_FR_COL_YIELD + " float, "
				+ ING_FR_COL_COLOR + " float, " 
				+ ING_FR_COL_ADD_AFTER_BOIL + " int, "
				+ ING_FR_COL_MAX_IN_BATCH + " float, "
				+ ING_FR_COL_GRAV + " float, " 
				
				+ ING_HP_COL_TYPE + " text, "
				+ ING_HP_COL_ALPHA + " float, "
				+ ING_HP_COL_USE + " text, "
				+ ING_HP_COL_TIME + " int, "
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
				+ ING_MC_COL_USE_FOR + " text"
				
				+ ");";
			
	public static final String CREATE_STYLE_TABLE = "create table " +
		TABLE_STYLES
		+ "("
			+ STY_COL_ID + " integer primary key autoincrement, "
			+ STY_COL_OWNER_ID + " long not null, "
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
			+ PRO_COL_NAME + " text not null, "
			+ PRO_COL_VERSION + " integer not null, "
			+ PRO_COL_GRAIN_TEMP + " float, "
			+ PRO_COL_NOTES + " text, "
			+ PRO_COL_TUN_TEMP + " float, "
			+ PRO_COL_SPARGE_TEMP + " float, "
			+ PRO_COL_PH + " float, "
			+ PRO_COL_TUN_WEIGHT + " float, "
			+ PRO_COL_TUN_SPEC_HEAT + " float, "
			+ PRO_COL_TUN_EQUIP_ADJ + " integer"
	        + ");";
	
	public static final String CREATE_STEP_TABLE = "create table " +
	    TABLE_STEPS
		+ "("
		    + STE_COL_ID + " integer primary key autoincrement, "
			+ STE_COL_OWNER_ID + " long not null, "
			+ STE_COL_NAME + " text not null, "
			+ STE_COL_VERSION + " integer not null, "
			+ STE_COL_TYPE + " text not null, "
			+ STE_COL_INFUSE_AMT + " float, "
			+ STE_COL_STEP_TEMP + " float, "
			+ STE_COL_STEP_TIME + " int, "
			+ STE_COL_RAMP_TIME + " int, "
			+ STE_COL_END_TEMP + " float"
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
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STYLES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
	    onCreate(db);
	}
}
