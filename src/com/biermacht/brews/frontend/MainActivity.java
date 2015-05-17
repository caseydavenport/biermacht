package com.biermacht.brews.frontend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.tasks.ImportXmlIngredientsTask;
import com.biermacht.brews.tasks.InitializeTask;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

  // Poorly done globals
  public static DatabaseInterface databaseInterface;
  public static IngredientHandler ingredientHandler;
  public static Boolean usedBefore;
  public static SharedPreferences preferences;

  // Static drawer list items
  private static String DRAWER_RECIPES = "Recipes";
  private static String DRAWER_GRAVITY_CALC = "Hydrometer Adjustment";
  private static String DRAWER_MASH_EDIT = "Mash Profile Editor";
  private static String DRAWER_EQUIP_EDIT = "Equipment Editor";
  private static String DRAWER_INGRED_EDIT = "Ingredient Editor";
  private static String DRAWER_ABV_CALC = "ABV Calculator";

  // Fragments
  ArrayList<ClickableFragment> fragmentList;

  // List to store drawer options
  private ArrayList<String> drawerItems;

  //Declare views here
  private ListView drawerListView;

  // Drawer stuff
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;

  // Selected item
  private int selectedItem;

  // Context
  private Context context;

  // Stores recipes found the the selected file
  private ArrayList<Recipe> foundImportedRecipes;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Store context
    context = this;

    // Instantiate my ingredient handler
    ingredientHandler = new IngredientHandler(getApplicationContext());

    // Get shared preferences
    preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

    // Instantiate my database interface
    databaseInterface = new DatabaseInterface(getApplicationContext());
    databaseInterface.open();

    // SHARED PREFERENCES JUNK ALL GOES HERE!
    SharedPreferences preferences = this.getSharedPreferences(Constants.PREFERENCES, Context
            .MODE_PRIVATE);
    usedBefore = preferences.getBoolean(Constants.PREF_USED_BEFORE, false);
    if (! usedBefore) {
      // We've used the app! Woo!
      preferences.edit().putBoolean(Constants.PREF_USED_BEFORE, true).commit();
      new ImportXmlIngredientsTask(this).execute("");

      // Create the master recipe - used as placeholder for stuff
      Database.createRecipeWithName("Master Recipe");
    }
    else {
      // Async Initialize Assets on startup
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
    drawerItems.add(DRAWER_GRAVITY_CALC);
    drawerItems.add(DRAWER_ABV_CALC);
    //drawerItems.add(DRAWER_EQUIP_EDIT); TODO:

    // Set the adapter and click listener for the list view
    drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.row_layout_drawer_item,
            drawerItems));
    drawerListView.setOnItemClickListener(new DrawerItemClickListener());

    // Drawer layout and ActionBarDrawerToggle
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(
            this,                  /* host Activity */
            mDrawerLayout,         /* DrawerLayout object */
            R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
            R.string.drawer_open,  /* "open drawer" description */
            R.string.drawer_close  /* "close drawer" description */) {

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed(View view) {
        getActionBar().setTitle(drawerItems.get(selectedItem));
      }

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened(View drawerView) {
        getActionBar().setTitle(R.string.drawer_open_title);
      }
    };

    // Set the drawer toggle as the DrawerListener
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // Set up fragment List
    selectedItem = 0;
    updateFragments();
  }

  @Override
  public void onResume() {
    super.onResume();
    updateFragments();
    selectItem(selectedItem);

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Pass the event to ActionBarDrawerToggle, if it returns
    // true, then it has handled the app icon touch event
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    switch (item.getItemId()) {
      case R.id.menu_new_recipe:
        Intent i = new Intent(getApplicationContext(), AddRecipeActivity.class);
        startActivity(i);
        break;

      case R.id.menu_settings:
        Intent i2 = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i2);
        break;

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
            .setTitle("Import BeerXML Recipe")
            .setMessage("Select a BeerXML file on your device to import recipes.")
            .setPositiveButton("Select", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Intent i3 = new Intent(Intent.ACTION_GET_CONTENT);
                i3.setType("file/*");
                startActivityForResult(i3, 1);
              }

            })
            .setNegativeButton(R.string.cancel, null);
  }

  private AlertDialog.Builder recipeSelectorAlert() {
    // Build view which contains the recipes to select
    final ListView v = new ListView(getApplicationContext());
    v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    v.setFitsSystemWindows(true);
    final RecipeCheckboxArrayAdapter adapter = new RecipeCheckboxArrayAdapter
            (getApplicationContext(), foundImportedRecipes);
    v.setAdapter(adapter);
    final ArrayList<Recipe> recipesToImport = new ArrayList<Recipe>();

    return new AlertDialog.Builder(this)
            .setTitle("Found " + adapter.getCount() + " Recipes")
            .setMessage("Select which recipes to import.")
            .setView(v)
            .setPositiveButton("Import", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                // Iterate recipes, import those which are selected.  If selected and a recipe
                // exists with that name, ask if we should overwrite.
                for (int i = 0; i < adapter.getCount(); i++) {
                  if (adapter.isChecked(adapter.getItem(i).getRecipeName())) {
                    recipesToImport.add(adapter.getItem(i));
                  }
                }

                // Store the recipes
                new StoreRecipes(context, ingredientHandler, recipesToImport).execute("");
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if (resultCode == RESULT_OK) {
        String path = data.getData().getPath().toString();

        if (path != null) {
          new LoadRecipes(this, path, ingredientHandler).execute("");
        }
      }
      if (resultCode == RESULT_CANCELED) {
        //Write your code on no result return
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
   * Swaps fragments in the main content view
   */
  private void selectItem(int pos) {
    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, (Fragment) fragmentList.get
            (pos)).commit();

    // Highlight the selected item, update the title, and close the drawer
    drawerListView.setItemChecked(pos, true);
    setTitle(drawerItems.get(pos));
    mDrawerLayout.closeDrawer(drawerListView);

    // Set currently selected item
    selectedItem = pos;
  }

  @Override
  public void setTitle(CharSequence title) {
    getActionBar().setTitle(title);
  }

  private void updateFragments() {
    fragmentList = new ArrayList<ClickableFragment>();
    fragmentList.add(new RecipesFragment());
    fragmentList.add(new EditIngredientsFragment());
    fragmentList.add(new EditMashProfilesFragment());
    fragmentList.add(new HydrometerTempCalculatorFragment());
    fragmentList.add(new AlcoholAttenuationCalculatorFragment());
    selectItem(selectedItem);
  }

  public void setImportedRecipes(ArrayList<Recipe> recipeList) {
    this.foundImportedRecipes = recipeList;
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
      selectItem(position);
    }
  }

  private class LoadRecipes extends AsyncTask<String, Void, String> {

    private String path;
    private IngredientHandler ingredientHandler;
    private Context context;
    private ProgressDialog progress;
    private ArrayList<Recipe> importedRecipes;

    public LoadRecipes(Context c, String path, IngredientHandler i) {
      this.path = path;
      this.ingredientHandler = i;
      this.context = c;
      this.importedRecipes = new ArrayList<Recipe>();
    }

    @Override
    protected String doInBackground(String... params) {
      try {
        importedRecipes = ingredientHandler.getRecipesFromXml(path);
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
      progress.setCancelable(true);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }

  private class StoreRecipes extends AsyncTask<String, Void, String> {

    private IngredientHandler ingredientHandler;
    private Context context;
    private ProgressDialog progress;
    private ArrayList<Recipe> list;

    public StoreRecipes(Context c, IngredientHandler i, ArrayList<Recipe> list) {
      this.ingredientHandler = i;
      this.context = c;
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
      progress = new ProgressDialog(context);
      progress.setMessage("Importing...");
      progress.setIndeterminate(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setCancelable(true);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }
}
