package com.biermacht.brews;

import java.util.ArrayList;

import com.biermacht.brews.database.RecipeDataSource;

public class Utils {
	
	public static ArrayList<Recipe> getRecipeList(RecipeDataSource rds)
	{
		return rds.getRecipeList();
	}
}
