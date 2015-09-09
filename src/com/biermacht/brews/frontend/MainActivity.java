package com.biermacht.brews.frontend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseInterface;
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
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.tasks.ImportXmlIngredientsTask;
import com.biermacht.brews.tasks.InitializeTask;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends ActionBarActivity {

  // Globals, for reference outside of this Activity.
  public static DatabaseInterface databaseInterface;
  public static IngredientHandler ingredientHandler;
  public static Boolean usedBefore;
  public static SharedPreferences preferences;

  // Static drawer list items
  private static String DRAWER_RECIPES = "Recipes";
  private static String DRAWER_GRAVITY_CALC = "Hydrometer Adjustment";
  private static String DRAWER_MASH_EDIT = "Mash Profile Editor";
  private static String DRAWER_INGRED_EDIT = "Ingredient Editor";
  private static String DRAWER_ABV_CALC = "ABV Calculator";
  private static String DRAWER_STRIKE_CALC = "Strike Temperature";

  // List to store drawer option names.
  private ArrayList<String> drawerItems;

  // List of Fragments which can be shown in the main view.  These correspond to the drawer items
  // in the above ArrayList<String> drawerItems
  ArrayList<ClickableFragment> fragmentList;

  // Drawer related variables.
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;
  private ListView drawerListView;

  // Currently selected drawer item - for use as an index in drawerItems and fragmentList.
  private int selectedItem;

  // Stores recipes found the the selected file
  private ArrayList<Recipe> foundImportedRecipes;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Instantiate ingredient handler
    ingredientHandler = new IngredientHandler(getApplicationContext());

    // Get shared preferences
    preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

    // Instantiate my database interface
    databaseInterface = new DatabaseInterface(getApplicationContext());
    databaseInterface.open();

    // Check for important shared preferences flags and perform any required actions.
    SharedPreferences preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    usedBefore = preferences.getBoolean(Constants.PREF_USED_BEFORE, false);
    if (! usedBefore) {
      // This is the first time the app has been used.  Mark that the app has been opened, and
      // perform first-time use setup task.
      preferences.edit().putBoolean(Constants.PREF_USED_BEFORE, true).commit();
      new ImportXmlIngredientsTask(this).execute("");

      // Create the master recipe - used as placeholder for stuff
      Database.createRecipeWithName("Master Recipe");
    }
    else {
      // Async Initialize Assets on startup.  This loads styles and mash profiles for faster
      // access later.
      new InitializeTask(ingredientHandler).execute("");
    }

    // Initialize storage for imported recipes
    foundImportedRecipes = new ArrayList<Recipe>();

    // Initialize views
    drawerListView = (ListView) findViewById(R.id.drawer_list);

    // Create list for drawer
    drawerItems = new ArrayList<String>();
    drawerItems.add(DRAWER_RECIPES);
    drawerItems.add(DRAWER_INGRED_EDIT);
    drawerItems.add(DRAWER_MASH_EDIT);
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
    fragmentList = new ArrayList<ClickableFragment>();
    fragmentList.add(new RecipesFragment());
    fragmentList.add(new EditIngredientsFragment());
    fragmentList.add(new EditMashProfilesFragment());
    fragmentList.add(new StrikeWaterCalculatorFragment());
    fragmentList.add(new HydrometerTempCalculatorFragment());
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
    return new AlertDialog.Builder(this)
            .setTitle("Import Recipe(s) from XML")
            .setMessage("Select a BeerXML (.xml) or BeerSmith2 (.bsmx) file on your device to import recipes.")
            .setPositiveButton("Select", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                try {
                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  String[] mimeTypes = {"file/*.xml", "file/*.bsmx", "text/*"};
                  intent.setType("*/*");
                  intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                  startActivityForResult(intent, Constants.REQUEST_IMPORT_FILE);
                } catch (android.content.ActivityNotFoundException e) {
                  new AlertDialog.Builder(getApplicationContext())
                          .setTitle("No File Browser Found")
                          .setMessage("Please install a file browser from the Play Store")
                          .setPositiveButton("OK", null).show();
                }
              }

            })
            .setNegativeButton(R.string.cancel, null);
  }

  /**
   * Called after the user selects a BeerXML file on their device to import recipes.  This method
   * gets the chosen file path and loads the recipes therein.
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == Constants.REQUEST_IMPORT_FILE) {
      if (resultCode == RESULT_OK) {
        Log.d("MainActivity::onActivityResult", "User selected a file.");
        Uri uri = data.getData();
        String path = uri.getPath().toString();
        Log.d("MainActivity::onActivityResult", "URI: " + uri.toString());
        Log.d("MainActivity::onActivityResult", "Path: " + path);

        if (path != null) {
          new LoadRecipes(this, uri, ingredientHandler).execute("");
        }
      }
      if (resultCode == RESULT_CANCELED) {
        // Action to get recipes was cancelled - do nothing.
        Log.d("MainActivity::onActivityResult", "Load recipes from file cancelled by user");
      }
    }
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
   * This AsyncTask loads recipes from the given BeerXML file path and then displays the recipe
   * selector alert which allows the user to select which recipes they would like to import.
   */
  private class LoadRecipes extends AsyncTask<String, Void, String> {

    private Uri uri;
    private IngredientHandler ingredientHandler;
    private Context context;
    private ProgressDialog progress;
    private ArrayList<Recipe> importedRecipes;

    public LoadRecipes(Context c, Uri uri, IngredientHandler i) {
      Log.d("MainActivity", "Loading URI: " + uri.toString());
      this.uri = uri;
      this.ingredientHandler = i;
      this.context = c;
      this.importedRecipes = new ArrayList<Recipe>();
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        importedRecipes = ingredientHandler.getRecipesFromXml(uri);
      } catch (IOException e) {
        Log.e("LoadRecipes", e.toString());
      }
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      setImportedRecipes(importedRecipes);
      recipeSelectorAlert().show();
      updateFragments();
      Log.d("LoadRecipes", "Finished loading recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(context);
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

  /**
   * Creates the Builder for the Recipe selector, which is shown after Recipes have been loaded from
   * a user-chosen XML resource.
   *
   * @return
   */
  private AlertDialog.Builder recipeSelectorAlert() {
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
    final ArrayList<Recipe> recipesToImport = new ArrayList<Recipe>();

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
                // TODO: Check if recipe already exists, ask to overwrite.
                for (int i = 0; i < adapter.getCount(); i++) {
                  if (adapter.isChecked(adapter.getItem(i).getRecipeName())) {
                    recipesToImport.add(adapter.getItem(i));
                  }
                }

                // Store the recipes
                new StoreRecipes(recipesToImport).execute("");
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

    public StoreRecipes(ArrayList<Recipe> list) {
      this.list = list;
    }

    @Override
    protected String doInBackground(String... params) {
      for (Recipe r : list) {
        r.update();
        Database.createRecipeFromExisting(r);
      }
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      updateFragments();
      Log.d("StoreRecipes", "Finished importing recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
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
