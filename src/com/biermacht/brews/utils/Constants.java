package com.biermacht.brews.utils;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

  // New Objects
  public static final BeerStyle BEERSTYLE_OTHER = new BeerStyle("Other");

  // Master recipe's ID.  This is the ID of the dummy recipe we create on first use.
  // This recipe is used as a placeholder when a user-created recipe is not available.
  // For example, when a custom ingredient is added to the database, but is not in a recipe.
  public static final long MASTER_RECIPE_ID = 1;

  // Keys for passing objects
  public static final String KEY_RECIPE_ID = "biermacht.brews.recipe.id";
  public static final String KEY_RECIPE = "biermacht.brews.recipe";
  public static final String KEY_PROFILE_ID = "biermacht.brews.mash.profile.id";
  public static final String KEY_PROFILE = "biermacht.brews.mash.profile";
  public static final String KEY_INGREDIENT_ID = "biermacht.brews.ingredient.id";
  public static final String KEY_INGREDIENT = "biermacht.brews.ingredient";
  public static final String KEY_MASH_STEP = "biermacht.brews.mash.step";
  public static final String KEY_MASH_STEP_ID = "biermacht.brews.mash.step.id";
  public static final String KEY_MASH_STEP_LIST = "biermacht.brews.mash.profile.steps";
  public static final String KEY_MASH_PROFILE = "biermacht.brews.mash.profile";
  public static final String KEY_INSTRUCTION = "biermacht.brews.instruction";
  public static final String KEY_DATABASE_ID = "biermacht.brews.database.id";
  public static final String KEY_SECONDS = "biermacht.brews.remaining.seconds";
  public static final String KEY_TITLE = "biermacht.brews.title";
  public static final String KEY_COMMAND = "biermacht.brews.command";
  public static final String KEY_STEP_NUMBER = "biermacht.brews.stepnumber";
  public static final String KEY_TIMER_STATE = "biermacht.brews.timer.state";
  public static final String KEY_STYLE = "biermacht.brews.style";


  public static final int INVALID_ID = - 1;

  public static final String BREW_DATE_FMT = "dd MMM yyyy";

  // Indicates if a recipe should be displayed after it is created.
  public static final String DISPLAY_ON_CREATE = "biermacht.brews.recipe.display.on.create";

  // Valid commands
  public static final String COMMAND_START = "biermacht.brews.commands.start";
  public static final String COMMAND_STOP = "biermacht.brews.commands.stop";
  public static final String COMMAND_PAUSE = "biermacht.brews.commands.pause";
  public static final String COMMAND_QUERY = "biermacht.brews.commands.query";
  public static final String COMMAND_STOP_ALARM = "biermacht.brews.commands.stop.alarm";

  // Database identifiers used to slice SQLite database into
  // multiple zones, each with a different purpose.

  // System DB - imported from assets.  Contains the selection of "default" ingredients /
  // profiles / styles / etc that come with the application.
  public static final long DATABASE_SYSTEM_RESOURCES = 2;

  // User DB - custom resources added by the user. Constains the selection of ingredients / profiles
  // styles / etc that a user has specifically added to the application.
  public static final long DATABASE_USER_RESOURCES = 1;

  // User DB - stores recipes and specific instances of the "rubber-stamp"
  // ingredients / profiles / styles / etc from one of SYSTEM_ADDED or USER_ADDED
  // that are related to those recipes.
  public static final long DATABASE_USER_RECIPES = 0;

  // No owner ID for use in database
  public static final long OWNER_NONE = - 1;

  // Broadcast types
  public static final String BROADCAST_TIMER = "com.biermacht.brews.broadcast.timer";
  public static final String BROADCAST_REMAINING_TIME = "com.biermacht.brews.broadcast.timer.remaining";
  public static final String BROADCAST_TIMER_CONTROLS = "com.biermacht.brews.broadcast.timer.controls";
  public static final String BROADCAST_QUERY_RESP = "com.biermacht.brews.broadcast.query.resp";

  // Shared preferences Constants
  public static final String PREFERENCES = "com.biermacht.brews.preferences";
  public static final String PREF_USED_BEFORE = "com.biermacht.brews.usedBefore";
  public static final String PREF_LAST_OPENED = "com.biermacht.brews.lastOpened";
  public static final String PREF_BREWER_NAME = "com.biermacht.brews.brewerName";
  public static final String PREF_MEAS_SYSTEM = "com.biermacht.brews.measurementSystem";
  public static final String PREF_FIXED_RATIOS = "com.biermacht.brews.waterToGrainRatiosFixed";
  public static final String PREF_HYDROMETER_CALIBRATION_TEMP = "com.biermacht.brews.hydrometerCalibrationTemp";
  // Value of this preference indicates the last time db contents were updated.
  public static final String PREF_NEW_CONTENTS_VERSION = "com.biermacht.brews.newIngredientsVersion";

  //  Incremented when new database contents are added.
  public static int NEW_DB_CONTENTS_VERSION = 5;

  // Activity for result return codes
  public static final int RESULT_DELETED = 1;
  public static final int RESULT_OK = 2;
  public static final int RESULT_CANCELED = 3;

  // Request codes for activities
  public static final int REQUEST_NEW_MASH_STEP = 1;
  public static final int REQUEST_EDIT_MASH_STEP = 2;
  public static final int REQUEST_EDIT_RECIPE = 3;
  public static final int REQUEST_IMPORT_FILE = 4;
  public static final int REQUEST_CONNECT_TO_DRIVE = 5;
  public static final int REQUEST_DRIVE_FILE_OPEN = 6;
  public static final int REQUEST_DRIVE_FILE_CREATE = 7;

  // Possible timer states
  public static int PAUSED = 0;
  public static int RUNNING = 1;
  public static int STOPPED = 2;

  // Constant messages
  public static String MESSAGE_AUTO_CALC_W2GR = "Water-to-grain ratio is calculated automatically for this step.";

  // Other Constants
  private static final String[] hop_uses = {Hop.USE_BOIL, Hop.USE_AROMA, Hop.USE_DRY_HOP, Hop.USE_FIRST_WORT};
  private static final String[] hop_forms = {Hop.FORM_PELLET, Hop.FORM_WHOLE, Hop.FORM_PLUG};
  private static final String[] ferm_types = {Fermentable.TYPE_GRAIN, Fermentable.TYPE_EXTRACT, Fermentable.TYPE_SUGAR, Fermentable.TYPE_ADJUNCT};
  private static final String[] step_types = {MashStep.INFUSION, MashStep.DECOCTION, MashStep.TEMPERATURE};
  private static final String[] unit_systems = {Units.IMPERIAL, Units.METRIC};
  private static final String[] mash_types = {MashProfile.MASH_TYPE_DECOCTION, MashProfile.MASH_TYPE_INFUSION, MashProfile.MASH_TYPE_TEMPERATURE, MashProfile.MASH_TYPE_BIAB};
  private static final String[] sparge_types = {MashProfile.SPARGE_TYPE_BATCH, MashProfile.SPARGE_TYPE_FLY, MashProfile.SPARGE_TYPE_BIAB};

  public static final ArrayList<String> HOP_USES = new ArrayList<String>(Arrays.asList(hop_uses));
  public static final ArrayList<String> HOP_FORMS = new ArrayList<String>(Arrays.asList(hop_forms));
  public static final ArrayList<String> FERMENTABLE_TYPES = new ArrayList<String>(Arrays.asList(ferm_types));
  public static final ArrayList<String> MASH_STEP_TYPES = new ArrayList<String>(Arrays.asList(step_types));
  public static final ArrayList<String> UNIT_SYSTEMS = new ArrayList<String>(Arrays.asList(unit_systems));
  public static final ArrayList<String> MASH_TYPES = new ArrayList<String>(Arrays.asList(mash_types));
  public static final ArrayList<String> SPARGE_TYPES = new ArrayList<String>(Arrays.asList(sparge_types));
}
