package com.biermacht.brews;

import java.util.ArrayList;

public class Utils {
	
	public static ArrayList<Recipe> getRecipeList()
	{
        // Create a bunch of test brews here
        Recipe brew1 = new Recipe("Arizona Pale Ale");
        Recipe brew2 = new Recipe("Panther Stout");
        Recipe brew3 = new Recipe("Chattanooga Cherry Weissbier");
        Recipe brew4 = new Recipe("Sherman Light");
        
        brew1.setBeerType(Recipe.BEERTYPE_IPA);
        brew2.setBeerType(Recipe.BEERTYPE_STOUT);
        brew3.setBeerType(Recipe.BEERTYPE_HEFEWEIZEN);
        
        // Add the test brews to the list
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        recipeList.add(brew1);
        recipeList.add(brew2);
        recipeList.add(brew3);
        recipeList.add(brew4);
        
        return recipeList;
		
	}

}
