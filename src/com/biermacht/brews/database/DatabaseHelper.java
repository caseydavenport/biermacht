package com.biermacht.brews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database / Table Data Defines
	public static final String DATABASE_NAME = "BiermachtBrews";
	public static final int DATABASE_VERSION = 1;
	
	// Tab;es
	public static final String TABLE_RECIPES = "RecipeTable";
	public static final String TABLE_INGREDIENTS = "IngredientTable";
	
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
	
	
	// Table Creation Queries
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
				+ ING_FR_COL_ADD_AFTER_BOIL + " int, "   // SQLITE does not have bool type
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
				+ ING_YS_COL_BEST_FOR + " text"
			+ ");";
	
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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
	    onCreate(db);
	}
}
