package com.biermacht.brews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {
	
	// Database / Table Data Defines
	public static final String DATABASE_NAME = "BiermachtBres";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "RecipeTable";
	
	// Column Name Defines
	public static final String COL_ID = "_id";
	public static final String COL_NAME = "name";
	public static final String COL_DESC = "description";
	public static final String COL_TYPE = "beertype";
	public static final String COL_TIME = "batchTime";
	public static final String COL_GRAV = "gravity";
	public static final String COL_ABV = "ABV";
	public static final String COL_BITTER = "bitterness";
	public static final String COL_COLOR = "color";
	public static final String COL_INGRED = "ingredientID";
	public static final String COL_INSTRUC = "instructionID";
	
	// Database Creation Query
	private static final String CREATE_DB = "create table " +
		TABLE_NAME 
		+ "("
			+ COL_ID + " integer primary key autoincrement, "
			+ COL_NAME + " text not null, " 
			+ COL_DESC + " text not null, "
			+ COL_TYPE + " text not null, " 
			+ COL_TIME + " int not null, "
			+ COL_GRAV + " float not null, " 
			+ COL_ABV + " float not null, " 
			+ COL_BITTER + " float not null, " 
			+ COL_COLOR + " float not null, "
			+ COL_INGRED + " int not null, "
			+ COL_INSTRUC + " int not null"
		+ ");";
	
	// Public Constructor
	public RecipeDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create the table
		database.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
}
