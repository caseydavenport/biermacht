package com.biermacht.brews.frontend;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.IngredientActivities.AddCustomFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddCustomHopsActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddCustomMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddCustomYeastActivity;
import com.biermacht.brews.frontend.adapters.RecipeCheckboxArrayAdapter;
import com.biermacht.brews.frontend.fragments.AlcoholAttenuationCalculatorFragment;
import com.biermacht.brews.frontend.fragments.EditIngredientsFragment;
import com.biermacht.brews.frontend.fragments.EditMashProfilesFragment;
import com.biermacht.brews.frontend.fragments.HydrometerTempCalculatorFragment;
import com.biermacht.brews.frontend.fragments.RecipesFragment;
import com.biermacht.brews.frontend.fragments.StrikeWaterCalculatorFragment;
import com.biermacht.brews.frontend.fragments.ViewStylesFragment;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.tasks.ImportNew;
import com.biermacht.brews.tasks.ImportXmlIngredientsTask;
import com.biermacht.brews.tasks.InitializeTask;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.DriveActivity;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends DriveActivity {

  // Globals, referenced outside of this Activity.
  // TODO: These should not be use globally - can cause null reference when application is killed and re-started.
  public static IngredientHandler ingredientHandler;
  public static Boolean usedBefore;

  // For accessing the database.
  private DatabaseAPI databaseApi;

  // Static drawer list items
  private static String DRAWER_RECIPES = "Recipes";
  private static String DRAWER_GRAVITY_CALC = "Hydrometer Adjustment";
  private static String DRAWER_MASH_EDIT = "Mash Profile Editor";
  private static String DRAWER_INGRED_EDIT = "Ingredient Editor";
  private static String DRAWER_STYLE_VIEW = "Style Viewer";
  private static String DRAWER_ABV_CALC = "ABV Calculator";
  private static String DRAWER_STRIKE_CALC = "Strike Temperature";

  // List to store drawer option names.
  private ArrayList<String> drawerItems;

  // List of Fragments which can be shown in the main view.  These correspond to the drawer items
  // in the above ArrayList<String> drawerItems
  ArrayList<BiermachtFragment> fragmentList;

  // Drawer related variables.
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private ListView drawerListView;

  // Currently selected drawer item - for use as an index in drawerItems and fragmentList.
  private int selectedItem;

  // Stores all recipes found the the selected file.
  private ArrayList<Recipe> foundImportedRecipes;

  // Stores recipes to be imported from the selected file.
  private ArrayList<Recipe> recipesToImport;

  // Stores the file name and extension when importing a recipe.
  private String fileName;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Instantiate ingredient handler
    ingredientHandler = new IngredientHandler(getApplicationContext());

    // Instantiate my database interface
    this.databaseApi = new DatabaseAPI(this);

    // Check for important shared preferences flags and perform any required actions.
    SharedPreferences preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    usedBefore = preferences.getBoolean(Constants.PREF_USED_BEFORE, false);
    if (! usedBefore) {
      // This is the first time the app has been used.  Mark that the app has been opened, and
      // perform first-time use setup task.
      preferences.edit().putBoolean(Constants.PREF_USED_BEFORE, true).commit();
      preferences.edit().putInt(Constants.PREF_NEW_CONTENTS_VERSION, Constants.NEW_DB_CONTENTS_VERSION).commit();
      new ImportXmlIngredientsTask(this).execute("");

      // Create the master recipe - used as placeholder for stuff
      databaseApi.createRecipeWithName("Master Recipe");
    }
    else {
      // Async Initialize Assets on startup.  This loads styles and mash profiles for faster
      // access later.
      new InitializeTask(ingredientHandler).execute("");

      // Check if we need to update the ingredients database with new entries.
      // This occurs when new ingredients are added to the app.
      int lastVersion = preferences.getInt(Constants.PREF_NEW_CONTENTS_VERSION, 0);

      Log.d("MainActivity", "DB Contents version, was: " + lastVersion + ", now: " + Constants.NEW_DB_CONTENTS_VERSION);
      while (lastVersion < Constants.NEW_DB_CONTENTS_VERSION) {
        // Increment the version.
        lastVersion++;

        // Perform and actions for this version.
        switch (lastVersion) {
          case 1:
            Log.d("MainActivity", "Importing new dry yeasts");
            new ImportNew("ingredient", "Importing new ingredients",
                          this, "Yeasts/dry-yeasts-01.xml").execute("");
            break;
          case 2:
            Log.d("MainActivity", "Importing BIAB mash profiles");
            new ImportNew("mashprofile", "Importing new mash profiles",
                          this, "Profiles/biab-01.xml").execute("");
            break;
          case 3:
            Log.d("MainActivity", "Importing mangrove yeasts");
            new ImportNew("ingredient", "Importing new ingredients",
                          this, "Yeasts/mangrove-01.xml").execute("");
            break;
          case 4:
            Log.d("MainActivity", "Importing mosaic / el dorado");
            new ImportNew("ingredient", "Importing new hops",
                          this, "Hops/new-hops-1.xml").execute("");
            break;
          case 5:
            Log.d("MainActivity", "Updating style database");
            new ImportNew("style", "Updating style database",
                          this, "Styles/styles.xml").execute("");
          default:
            Log.w("MainActivity", "No action for version: " + lastVersion);
            break;
        }
      }

      // Update shared preferences.
      preferences.edit().putInt(Constants.PREF_NEW_CONTENTS_VERSION, Constants.NEW_DB_CONTENTS_VERSION).commit();
    }

    // Initialize storage for imported recipes
    foundImportedRecipes = new ArrayList<Recipe>();
    recipesToImport = new ArrayList<Recipe>();

    // Initialize views
    drawerListView = (ListView) findViewById(R.id.drawer_list);

    // Create list for drawer
    drawerItems = new ArrayList<String>();
    drawerItems.add(DRAWER_RECIPES);
    drawerItems.add(DRAWER_INGRED_EDIT);
    drawerItems.add(DRAWER_MASH_EDIT);
    drawerItems.add(DRAWER_STYLE_VIEW);
    drawerItems.add(DRAWER_STRIKE_CALC);
    drawerItems.add(DRAWER_GRAVITY_CALC);
    drawerItems.add(DRAWER_ABV_CALC);

    // Set the adapter and click listener for the list view
    drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.row_layout_drawer_item,
                                                       drawerItems));
    drawerListView.setOnItemClickListener(new DrawerItemClickListener());

    // Drawer layout and ActionBarDrawerToggle
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(
            this,                  /* host Activity */
            mDrawerLayout,         /* DrawerLayout object */
            R.string.drawer_open,  /* "open drawer" description */
            R.string.drawer_close  /* "close drawer" description */) {

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed(View view) {
        getSupportActionBar().setTitle(drawerItems.get(selectedItem));
      }

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened(View drawerView) {
        getSupportActionBar().setTitle(R.string.drawer_open_title);
      }
    };

    // Set the drawer toggle as the DrawerListener
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    // Set up fragment List to contain the correct fragments.
    selectedItem = 0;
    fragmentList = new ArrayList<BiermachtFragment>();
    fragmentList.add(new RecipesFragment());
    fragmentList.add(new EditIngredientsFragment());
    fragmentList.add(new EditMashProfilesFragment());
    fragmentList.add(new ViewStylesFragment());
    fragmentList.add(new StrikeWaterCalculatorFragment());
    HydrometerTempCalculatorFragment hydrometerTempCalculatorFragment = new HydrometerTempCalculatorFragment();
    hydrometerTempCalculatorFragment.setPreferences(preferences);
    fragmentList.add(hydrometerTempCalculatorFragment);
    fragmentList.add(new AlcoholAttenuationCalculatorFragment());

    // Select the recipe fragment
    selectItem(0);
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d("MainActivity", "onResume() called");
    updateFragments();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Pass the event to ActionBarDrawerToggle, if it returns
    // true, then it has handled the app icon touch event
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    // Pass the event to the active fragment.  If it returns true,
    // then it has handled the touch event.
    if (fragmentList.get(selectedItem).onOptionsItemSelected(item)) {
      Log.d("MainActivity", "Current frag handled item selection");
      return true;
    }

    // The option selection is unhandled.  See if we can handle it.
    // TODO: Some of these should be handled by the underlying fragment, rather than this Activity.
    Intent i;
    switch (item.getItemId()) {
      case R.id.menu_settings:
        Intent i2 = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i2);
        return true;

      case R.id.menu_import_recipe:
        importRecipeAlert().show();
        break;

      case R.id.add_fermentable:
        i = new Intent(this, AddCustomFermentableActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        startActivity(i);
        break;

      case R.id.new_mash_profile:
        i = new Intent(this, AddMashProfileActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        startActivity(i);
        break;

      case R.id.add_hop:
        i = new Intent(this, AddCustomHopsActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        startActivity(i);
        break;

      case R.id.add_yeast:
        i = new Intent(this, AddCustomYeastActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        startActivity(i);
        break;

      case R.id.add_misc:
        i = new Intent(this, AddCustomMiscActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        startActivity(i);
        break;
    }
    return true;
  }

  public void onClick(View v) {
    // Pass the event to the currently active fragment.
    this.fragmentList.get(selectedItem).handleClick(v);
  }

  private AlertDialog.Builder importRecipeAlert() {
    Log.d("MainActivity", "importRecipeAlert() triggered");
    return new AlertDialog.Builder(this)
            .setTitle(R.string.import_recipes_title)
            .setMessage(R.string.import_recipes_msg)
            .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("MainActivity", "Browse pressed, launching GET_CONTENT intent");
                try {
                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("*/*");
                  startActivityForResult(intent, Constants.REQUEST_IMPORT_FILE);
                } catch (android.content.ActivityNotFoundException e) {
                  new AlertDialog.Builder(MainActivity.this)
                          .setTitle("No File Browser Found")
                          .setMessage("Please install a file browser from the Play Store")
                          .setPositiveButton("OK", null).show();
                }
              }
            })
            .setNegativeButton(R.string.drive_button, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                Log.d("MainActivity", "Drive button pressed, lauching Drive file picker");
                pickFile();
              }
            })
            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MainActivity", "Cancel pressed, not importing any files");
              }
            });
  }

  private AlertDialog.Builder existingRecipesAlert(final Recipe r) {
    return new AlertDialog.Builder(this)
            .setTitle("Recipe Exists")
            .setMessage("The recipe '" + r.getRecipeName() + "' already exists.")
            .setPositiveButton("Overwrite", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("MainActivity", "Overwrite pressed for recipe: " + r.getRecipeName());
                // Delete any existing recipe with the same name.
                for (Recipe tmpR : databaseApi.getRecipeList()) {
                  if (tmpR.getRecipeName().equals(r.getRecipeName())) {
                    databaseApi.deleteRecipe(tmpR);
                  }
                }

                // Add this recipe.
                ArrayList<Recipe> imports = new ArrayList<Recipe>();
                imports.add(r);
                new StoreRecipes(imports, findViewById(R.id.drawer_layout)).execute("");
              }

            })
            .setNegativeButton("Skip", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                Log.d("MainActivity", "Skip pressed for recipe: " + r.getRecipeName());
              }
            })
            .setNeutralButton("Duplicate", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("MainActivity", "Duplicate pressed for recipe: " + r.getRecipeName());
                // Add copy of this recipe.
                r.setRecipeName(r.getRecipeName() + " - Import");
                Recipe copy = databaseApi.createRecipeFromExisting(r);

                // Show SnackBar and update the fragments to display the new recipes.
                String snack = "1 Recipe(s) Imported";
                Snackbar.make(findViewById(R.id.drawer_layout), snack, Snackbar.LENGTH_LONG).show();
                updateFragments();
              }
            });
  }

  /**
   * Called after the user selects a BeerXML file on their device to import recipes.  This method
   * gets the chosen file path and loads the recipes therein.
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    Log.d("MainActivity", "onActivityResult " + String.format("requestCode: %d, resultCode: %d", requestCode, resultCode));
    if (requestCode == Constants.REQUEST_IMPORT_FILE) {
      if (resultCode == RESULT_OK) {
        Log.d("MainActivity", "User selected a file and result was OK");
        Uri uri = data.getData();
        String path = uri.getPath().toString();
        Log.d("MainActivity", "URI: " + uri.toString());
        Log.d("MainActivity", "Path: " + path);

        ContentResolver cR = getContentResolver();
        Log.d("MainActivity", "MIME type: " + cR.getType(uri));

        if (path != null) {
          try {
            // Create an InputStream from the path.
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            fileName = uri.getPath().toString();

            // Load the recipes.
            new LoadRecipes(inputStream, fileName, ingredientHandler).execute("");
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
      if (resultCode == RESULT_CANCELED) {
        // Action to get recipes was cancelled - do nothing.
        Log.d("MainActivity", "Load recipes from file cancelled by user");
      }
    }
    else {
      // If none of the above handle the response, see if the super class handles it.
      // The DriveActivity superclass handles responses to Google Drive intents.
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Called by the DriveActivity when a drive file has been selected by a user.
   *
   * @param data
   */
  public void onDriveFilePicked(Intent data) {
    Log.d("MainActivity", "A Google Drive file has been selected");
    DriveId driveId = data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

    // Only grab the file if we're still connected to Drive APIs.
    // If we've somehow been disconnected, warn the user and return.
    if (! driveClient.isConnected()) {
      Log.e("MainActivity", "Drive client became unexpected disconnected");
      String msg = "Unexpected disconnect from Google Drive";
      new AlertDialog.Builder(MainActivity.this)
              .setTitle("Disconnected from Google Drive")
              .setMessage(msg)
              .setPositiveButton(R.string.ok, null)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .show();
      return;
    }

    // Get the file.
    final DriveFile file = Drive.DriveApi.getFile(driveClient, driveId);

    // Callback for when the file has been opened.  Checks for success and extracts the contents
    // of the file.
    final ResultCallback resultCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
      @Override
      public void onResult(DriveApi.DriveContentsResult result) {
        Log.d("MainActivity", "Entering callback for Drive file open");
        if (! result.getStatus().isSuccess()) {
          Log.e("MainActivity", "Failed to open drive file");
          // TODO: Display an error saying file can't be opened
          return;
        }
        // DriveContents object contains pointers to the actual byte stream.
        DriveContents contents = result.getDriveContents();

        // Load the recipes in the file.
        new LoadRecipes(contents.getInputStream(), fileName, ingredientHandler).execute("");
      }
    };

    // Callback for when file Metadata has been returned - extracts the file name
    // and opens the file.
    ResultCallback metadataCallback = new ResultCallback<DriveResource.MetadataResult>() {
      @Override
      public void onResult(DriveResource.MetadataResult result) {
        Log.d("MainActivity", "Entering callback for Drive metadata returned");
        if (! result.getStatus().isSuccess()) {
          Log.e("MainActivity", "Failed to get Drive metadata");
          // TODO: Display an error saying file can't be opened
          return;
        }
        // Set fileName for use in LoadRecipes.
        String title = result.getMetadata().getTitle();
        String extension = result.getMetadata().getFileExtension();
        Log.d("MainActivity", "Metadata.title: " + title + " Metadata.ext: " + extension);
        fileName = title + "." + extension;

        // Open the file.
        Log.d("MainActivity", "About to open drive file");
        file.open(driveClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(resultCallback);
      }
    };

    // Get Metadata for the file.
    Log.d("MainActivity", "Initiating request for Drive metadata");
    file.getMetadata(driveClient).setResultCallback(metadataCallback);
  }

  @Override
  public void onDriveFileWritten(Intent data) {
    Log.d("MainActivity", "File written to drive");

    // Show a snack bar showing that it was exported.
    ((RecipesFragment) fragmentList.get(0)).recipesExportedSnackbar();
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  /**
   * Private class which handles selections in the app drawer and selects the appropriate Fragment
   * to display.
   */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
      selectItem(position);
    }
  }

  /**
   * Swaps fragments in the main content view so that the Fragment as position 'pos' is displayed.
   */
  private void selectItem(int pos) {
    // Insert the fragment by replacing any existing fragment.
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment f = (Fragment) fragmentList.get(pos);
    fragmentManager.beginTransaction().replace(R.id.content_frame, f).commit();

    // Highlight the selected item, update the title, and close the drawer
    drawerListView.setItemChecked(pos, true);
    setTitle(drawerItems.get(pos));
    mDrawerLayout.closeDrawer(drawerListView);

    // Set currently selected item
    selectedItem = pos;
  }

  /**
   * Calls through to the udpate() method of the currently active Fragment.  Call this method after
   * making data changes so that the active Fragment can update its UI.
   */
  private void updateFragments() {
    fragmentList.get(selectedItem).update();
  }

  /**
   * Called by the LoadRecipes task to store off the loaded recipes.  Referenced by the recipe
   * selector alert.
   *
   * @param recipeList
   */
  public void setImportedRecipes(ArrayList<Recipe> recipeList) {
    this.foundImportedRecipes = recipeList;
  }

  /**
   * This AsyncTask loads recipes from the given InputStream and then displays the recipe selector
   * alert which allows the user to select which recipes they would like to import.
   */
  private class LoadRecipes extends AsyncTask<String, Void, String> {

    private InputStream inputStream;
    private String fileName;
    private IngredientHandler ingredientHandler;
    private ProgressDialog progress;
    private ArrayList<Recipe> importedRecipes;
    private Exception storedException;

    public LoadRecipes(InputStream is, String fileName, IngredientHandler i) {
      Log.d("MainActivity", "Loading Recipes from file: " + fileName);
      this.inputStream = is;
      this.fileName = fileName;
      this.ingredientHandler = i;
      this.importedRecipes = new ArrayList<Recipe>();
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        Log.d("MainActivity", "Importing recipes from file: " + this.fileName + " Input stream: " + this.inputStream);
        importedRecipes = ingredientHandler.getRecipesFromXml(this.inputStream, this.fileName);
      } catch (Exception e) {
        Log.e("LoadRecipes", "Error parsing recipes, storing exception: " + e.toString());
        this.storedException = e;
      }
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      if (this.storedException == null) {
        // All is good - show the recipe selector.
        Log.d("MainActivity", "Successfully parsed file: " + this.fileName);
        setImportedRecipes(importedRecipes);
        recipeSelectorAlert().show();
        updateFragments();
      }
      else {
        // Failed to load recipes.  Display the exception.
        Log.e("MainActivity", "Failed to parse file: " + this.fileName);
        String stackTrace = Log.getStackTraceString(this.storedException);
        String msg = "Parsing failed.  This may be due to an invalid file, or something else.  " +
                "Please report the follwing message to " +
                "the developer: \n\n" + this.storedException.toString() + "\n\n" + stackTrace;
        Log.e("MainActivity", "Alerting user  of failure: \n\n" + stackTrace);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error parsing file")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
      }
      Log.d("LoadRecipes", "Finished loading recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Log.d("MainActivity", "About to load recipes from file - starting progress spinner");
      progress = new ProgressDialog(MainActivity.this);
      progress.setMessage("Loading...");
      progress.setIndeterminate(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setCancelable(false);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }

  public void displayStyle(View v) {
    // Pass directly to the selected fragment.
    // This method gets called in tablet mode when a user opens
    // the style for the currently selected recipe.
    this.fragmentList.get(selectedItem).handleClick(v);
  }

  /**
   * Creates the Builder for the Recipe selector, which is shown after Recipes have been loaded from
   * a user-chosen XML resource.
   *
   * @return
   */
  private AlertDialog.Builder recipeSelectorAlert() {
    Log.d("MainActivity", "Building recipe selector alert after loading file");

    // Inflater to inflate custom alert view.
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Inflate our custom layout
    View alertView = inflater.inflate(R.layout.alert_view_select_list, null);

    // Get the listView from the layout
    final ListView v = (ListView) alertView.findViewById(R.id.list);

    // Get the checkBox from the layout
    final CheckBox checkBox = (CheckBox) alertView.findViewById(R.id.checkbox);

    // Sort the found recipes by name.
    Collections.sort(foundImportedRecipes, new ToStringComparator());

    // Create checkbox array adapter to hold recipes, and set it as the adapter for the listView.
    final RecipeCheckboxArrayAdapter adapter = new RecipeCheckboxArrayAdapter(getApplicationContext(), foundImportedRecipes);
    v.setAdapter(adapter);
    recipesToImport = new ArrayList<Recipe>();

    // Set a listener for the checkbox so that we can select / unselect items in the list.
    checkBox.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (checkBox.isChecked()) {
          // Checkbox is checked - select all recipes.
          adapter.selectAll();
        }
        else {
          // Checkbox is not checked - deselect all recipes.
          adapter.deselectAll();
        }
      }
    });

    return new AlertDialog.Builder(this)
            .setTitle("Found " + adapter.getCount() + " Recipes")
            .setView(alertView)
            .setPositiveButton("Import", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                // Iterate recipes, import those which are selected.
                Log.d("MainActivity", "Import button presses, importing recipes");
                for (int i = 0; i < adapter.getCount(); i++) {
                  if (adapter.isChecked(adapter.getItem(i).getRecipeName())) {
                    recipesToImport.add(adapter.getItem(i));
                  }
                }

                // Get all recipes, check for name clashes.
                Iterator<Recipe> iterator = recipesToImport.iterator();
                ArrayList<Recipe> allRecipes = databaseApi.getRecipeList();
                ArrayList<Recipe> clashes = new ArrayList<Recipe>();
                while (iterator.hasNext()) {
                  Recipe newRecipe = iterator.next();
                  for (Recipe existingRecipe : allRecipes) {
                    if (newRecipe.getRecipeName().equals(existingRecipe.getRecipeName())) {
                      if (! clashes.contains(newRecipe)) {
                        // Add to the clashes list.
                        clashes.add(newRecipe);

                        // Remove from the "ok to import" list.
                        iterator.remove();
                      }
                    }
                  }
                }

                // If there are any clashes, we need to show an alert asking what to do.
                // The alert will choose which recipes should be imported, duplicated, skipped.
                if (clashes.size() != 0) {
                  Log.d("MainActivity", "Recipe clashes found: " + clashes.toString());
                  for (Recipe r : clashes) {
                    existingRecipesAlert(r).show();
                  }
                }

                // Store all recipes which do not have conflicts.
                Log.d("MainActivity", "Launching StoreRecipes: " + recipesToImport.toString());
                new StoreRecipes(recipesToImport, findViewById(R.id.drawer_layout)).execute("");
              }

            })
            .setNegativeButton(R.string.cancel, null);
  }

  /**
   * This AsyncTask stores the given list of Recipes to the Recipes database.
   */
  private class StoreRecipes extends AsyncTask<String, Void, String> {

    private ProgressDialog progress;
    private ArrayList<Recipe> list;
    private View mainView;

    public StoreRecipes(ArrayList<Recipe> list, View mainView) {
      this.list = list;
      this.mainView = mainView;
    }

    @Override
    protected String doInBackground(String... params) {
      Log.d("MainActivity", "Storing recipes in database");
      for (Recipe r : list) {
        Log.d("MainActivity", "\tWriting recipe to database: " + r.toString());
        r.update();
        databaseApi.createRecipeFromExisting(r);
      }
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      Log.d("MainActivity", "Storing recipes complete - dismiss progress dialog");
      progress.dismiss();

      // Show SnackBar and update the fragments to display the new recipes.
      String snack = list.size() + " Recipe(s) Imported";
      Snackbar.make(mainView, snack, Snackbar.LENGTH_LONG).show();
      updateFragments();
      Log.d("StoreRecipes", "Finished importing recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Log.d("MainActivity", "About to store recipes, showing progress dialog");
      progress = new ProgressDialog(MainActivity.this);
      progress.setMessage("Importing...");
      progress.setIndeterminate(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setCancelable(false);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }
}
