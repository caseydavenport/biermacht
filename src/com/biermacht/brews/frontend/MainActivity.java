package com.biermacht.brews.frontend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
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
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.tasks.ImportXmlIngredientsTask;
import com.biermacht.brews.tasks.InitializeTask;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

  // Globals
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
  private static String DRAWER_STRIKE_CALC = "Strike Temperature";

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
      // Async Initialize Assets on startup.
      new InitializeTask(ingredientHandler).execute("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BEGIN BAD HACKY TEMPORARY FIX.
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Temporary fix - initial mash profiles which came with early version of Biermacht had
    // bad water-to-grain ratios for their Mash In steps.  This procedure re-sets them to the correct
    // values. This should be removed once enough of the user-base has upgraded to this SW version.
    boolean fixedRatios = preferences.getBoolean(Constants.PREF_FIXED_RATIOS, false);
    if (! fixedRatios) {
      // Get all profiles from the Custom database.  Iterate through, change the water-to-grain
      // ratio for those what are wrong, and save them.
      preferences.edit().putBoolean(Constants.PREF_FIXED_RATIOS, true).commit();
      ArrayList<MashProfile> l = new ArrayList<MashProfile>();
      l.addAll(Database.getMashProfilesFromVirtualDatabase(Constants.DATABASE_CUSTOM));
      MashStep s;
      Recipe masterRecipe;
      try {
        masterRecipe = Database.getRecipeWithId(Constants.MASTER_RECIPE_ID);
      } catch (Exception e) {
        masterRecipe = new Recipe();
      }
      ArrayList<String> nameList = new ArrayList<String>();
      nameList.add("Decoction Mash");
      nameList.add("Infusion, Full Body");
      nameList.add("Infusion, Light Body");
      nameList.add("Infusion, Medium Body");

      for (MashProfile mp : l) {
        masterRecipe.setMashProfile(mp);
        if (nameList.contains(mp.getName())) {
          s = mp.getMashStepList().get(0);
          if (s.getBeerXmlStandardWaterToGrainRatio() > 4.2) {
            if (mp.getName().contains("Infusion")) {
              s.setBeerXmlStandardWaterToGrainRatio(2.607);
            }
            else {
              s.setBeerXmlStandardWaterToGrainRatio(4.1727);
            }
            mp.save(Constants.DATABASE_CUSTOM);
          }
        }
      }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // END BAD HACKY TEMPORARY FIX.
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
    // Inflater to inflate custom alert view.
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Inflate our custom layout
    View alertView = inflater.inflate(R.layout.alert_view_select_list, null);

    // Get the listView from the layout
    final ListView v = (ListView) alertView.findViewById(R.id.list);

    // Get the checkBox from the layout
    final CheckBox checkBox = (CheckBox) alertView.findViewById(R.id.checkbox);

    // Create checkbox array adapter to hold recipes, and set it as the adapter for the listView.
    final RecipeCheckboxArrayAdapter adapter =
            new RecipeCheckboxArrayAdapter(getApplicationContext(), foundImportedRecipes);
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
        // Action to get recipes was cancelled - do nothing.
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

  @Override
  public void setTitle(CharSequence title) {
    getActionBar().setTitle(title);
  }

  private void updateFragments() {
    fragmentList.get(selectedItem).update();
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
      progress.setCancelable(false);
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
      progress.setCancelable(false);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }
}
