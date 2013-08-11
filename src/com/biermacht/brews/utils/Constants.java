package com.biermacht.brews.utils;

import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;

public class Constants {
		
	// New Objects
	public static BeerStyle BEERSTYLE_OTHER = new BeerStyle("Other");
    public static Recipe NEW_RECIPE = new Recipe("New Recipe");

    // Master recipe - used as parent for custom ingredients
    public static long MASTER_RECIPE_ID = 1;

	// Intent put values
	public static String INTENT_RECIPE_ID = "biermacht.brews.recipe.id";
	public static String INTENT_PROFILE_ID = "biermacht.brews.profile.id";
	public static String INTENT_INGREDIENT_ID = "biermacht.brews.ingredient.id";
    public static String INTENT_INGREDIENT = "biermacht.brews.ingredient";
    public static int INVALID_ID = -1; // TODO: Throw exceptions in get methods if this is passed

    // Different databases for ingredients
    public static int INGREDIENT_DB_PERMANENT = 2;  // Imported from assets.  Save these.
    public static int INGREDIENT_DB_CUSTOM = 1;     // Custom made ingredients. Save these
    public static int INGREDIENT_DB_DEFAULT = 0;    // Used by default for regular recipes

    // No owner ID for use in database
    public static long OWNER_NONE = -1;

    // Broadcast types
    public static String BROADCAST_TIMER = "com.biermacht.brews.broadcast.timer";

    // Shared preferences Constants
    public static String PREFERENCES = "com.biermacht.brews.preferences";
    public static String PREF_USED_BEFORE = "com.biermacht.brews.usedBefore";
    public static String PREF_LAST_OPENED = "com.biermacht.brews.lastOpened";

    // Other Constants
}
