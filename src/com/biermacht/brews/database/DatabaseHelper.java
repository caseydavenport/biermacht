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
	public static final String REC_COL_DESC = "description";
	public static final String REC_COL_TYPE = "beertype";
	public static final String REC_COL_TIME = "batchTime";
	public static final String REC_COL_VOL = "Volume";
	public static final String REC_COL_GRAV = "gravity";
	public static final String REC_COL_ABV = "ABV";
	public static final String REC_COL_BITTER = "bitterness";
	public static final String REC_COL_COLOR = "color";
	
	// Column name defines for INGREDIENTS
	public static final String ING_COL_ID = "_id";
	public static final String ING_COL_OWNER_ID = "ownerId";
	public static final String ING_COL_TYPE = "type";
	public static final String ING_COL_NAME = "name";
	public static final String ING_COL_UNIT = "units";
	public static final String ING_COL_AMT = "amount";
	public static final String ING_COL_BOIL_END_TIME = "endtime";
	public static final String ING_COL_BOIL_START_TIME = "starttime";
	
	public static final String ING_GR_COL_WEIGHT = "weight";
	public static final String ING_GR_COL_COLOR = "color";
	public static final String ING_GR_COL_GRAV = "gravity";
	public static final String ING_GR_COL_TYPE = "grainType";
	public static final String ING_GR_COL_EFF = "efficiency";
	
	public static final String ING_HP_COL_DESC = "description";
	public static final String ING_HP_COL_ACID = "acids";
	public static final String ING_HP_COL_TYPE = "hopType";
	
	
	// Table Creation Queries
	private static final String CREATE_RECIPE_TABLE = "create table " +
		TABLE_RECIPES 
		+ "("
			+ REC_COL_ID + " integer primary key autoincrement, "
			+ REC_COL_NAME + " text not null, " 
			+ REC_COL_DESC + " text not null, "
			+ REC_COL_TYPE + " text not null, " 
			+ REC_COL_TIME + " int not null, "
			+ REC_COL_VOL + " float not null, "
			+ REC_COL_GRAV + " float not null, " 
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
				+ ING_COL_UNIT + " text not null, "
				+ ING_COL_AMT + " float not null, " 
				+ ING_COL_BOIL_START_TIME + " float not null, "
				+ ING_COL_BOIL_END_TIME + " float not null, "
				+ ING_GR_COL_WEIGHT + " float, "
				+ ING_GR_COL_COLOR + " float, " 
				+ ING_GR_COL_GRAV + " float, " 
				+ ING_GR_COL_TYPE + " text, " 
				+ ING_GR_COL_EFF + " float, "
				+ ING_HP_COL_DESC + " text, "
				+ ING_HP_COL_ACID + " float, "
				+ ING_HP_COL_TYPE + " text"
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
