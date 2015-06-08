package com.biermacht.brews.frontend.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.biermacht.brews.*;
import com.biermacht.brews.database.*;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.frontend.IngredientActivities.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.utils.*;
import com.biermacht.brews.utils.interfaces.*;
import com.biermacht.brews.xml.*;
import java.util.*;


public class RecipesFragment extends Fragment implements ClickableFragment {

  // Layout resource
  private static int resource = R.layout.fragment_recipes;

  // Static menu items
  private static String EDIT_RECIPE = "Edit Recipe";
  private static String SCALE_RECIPE = "Scale Recipe";
  private static String COPY_RECIPE = "Copy Recipe";
  private static String DELETE_RECIPE = "Delete Recipe";
  private static String EDIT_FERM = "Edit Fermentation Profile";
  private static String EDIT_MASH = "Edit Mash Profile";
  private static String EXPORT_RECIPE = "Export as BeerXML";
  private static String BREW_TIMER = "Brew Timer";
  private static String RECIPE_NOTES = "Recipe Notes";

  ViewGroup pageView;

  // Recipe List stuff
  private RecipeArrayAdapter mAdapter;
  private AdapterView.OnItemClickListener mClickListener;
  private ArrayList<Recipe> recipeList;

  // Database Interface
  private DatabaseInterface databaseInterface;

  // Context menu items
  private ArrayList<String> menuItems;

  // Holds the selected recipe
  private Recipe selectedRecipe;

  // Context
  private Context c;

  // Declare views here
  private ListView listView;
  private TextView noRecipesView;
  private LinearLayout detailsView;
  
  // Fields used when running on a tablet
  public boolean isTablet = false;
  private DisplayRecipeCollectionPagerAdapter cpAdapter;
  private ViewPager mViewPager;
  public int currentSelectedIndex = 0;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Log.d("RecipesFragment", "Starting onCreateView()");
	
    // Get views
    pageView = (RelativeLayout) inflater.inflate(resource, container, false);
    listView = (ListView) pageView.findViewById(R.id.recipe_list);
    noRecipesView = (TextView) pageView.findViewById(R.id.no_recipes_view);

	// Get Context
	c = getActivity();

	// Create recipe adapter
	recipeList = new ArrayList<Recipe>();
	mAdapter = new RecipeArrayAdapter(c, recipeList, this);

	// Set adapter for listView
	listView.setAdapter(mAdapter);
	
	// Search for the details view.  If it exists, it means we're 
	// running on a tablet, and we should inflate the details.
	detailsView = (LinearLayout) pageView.findViewById(R.id.details_view);
	if (detailsView != null) {
		Log.d("RecipesFragment", "Found detailsView - running on tablet");
	  mViewPager = (ViewPager) detailsView.findViewById(R.id.pager);
		isTablet = true;		
	}
	
  // Get database Interface
  databaseInterface = MainActivity.databaseInterface;

  // Set up the onClickListener for when a Recipe is selected 
	// in the main recipe list.
  mClickListener = new AdapterView.OnItemClickListener() {
    public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
		  // If we're running on a tablet, update the details view.
		  // Otherwise, open the DisplayRecipeActivity to display the recipe.
		  if (isTablet) {
		    Log.d("RecipesFragment", "Running on tablet - update details");
		    currentSelectedIndex = pos;
		    updateTabletDetailsView(recipeList.get(pos));
        mAdapter.notifyDataSetChanged();
	  	}
		  else {
		    Log.d("RecipesFragment", "Launching DisplayRecipeActivity");
        Intent intent = new Intent(c, DisplayRecipeActivity.class);
        intent.putExtra(Constants.KEY_RECIPE, recipeList.get(pos));
        startActivity(intent);
		  }
    }
  };

  // Set up my listView with title and ArrayAdapter
  new GetRecipeListFromDatabaseTask(getActivity()).execute("");
  listView.setOnItemClickListener(mClickListener);
  registerForContextMenu(listView);

  // Turn on options menu
  setHasOptionsMenu(true);
	
	Log.d("RecipesFragment", "Exiting onCreateView()");
    return pageView;
  }
  
  @Override
  public void onResume() {	
    super.onResume();
	  Log.d("RecipesFragment", "onResume: currentSelectedIndex = " + currentSelectedIndex);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.activity_main, menu);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    if (v == listView) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
      String title = recipeList.get(info.position).getRecipeName();

      selectedRecipe = recipeList.get(info.position);

      menu.setHeaderTitle(title);
      menuItems = new ArrayList<String>();

      // Build menu items
      menuItems.add(EDIT_RECIPE);
      if (! selectedRecipe.getType().equals(Recipe.EXTRACT)) {
        menuItems.add(EDIT_MASH);
      }
      menuItems.add(EDIT_FERM);
      menuItems.add(BREW_TIMER);
      menuItems.add(RECIPE_NOTES);
      menuItems.add(EXPORT_RECIPE);
      menuItems.add(SCALE_RECIPE);
      menuItems.add(COPY_RECIPE);
      menuItems.add(DELETE_RECIPE);

      for (int i = 0; i < menuItems.size(); i++) {
        menu.add(Menu.NONE, i, i, menuItems.get(i));
      }
    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    String selected = menuItems.get(item.getItemId());

    // Edit recipe selected
    if (selected.equals(EDIT_RECIPE)) {
      Intent i = new Intent(c, EditRecipeActivity.class);
      i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
      i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
      startActivity(i);
    }

    // Scale recipe selected
    else if (selected.equals(SCALE_RECIPE)) {
      scaleAlert(selectedRecipe).show();
    }

    // Export recipe selected
    else if (selected.equals(EXPORT_RECIPE)) {
      exportAlert(selectedRecipe).show();
    }

    // Copy recipe selected
    else if (selected.equals(COPY_RECIPE)) {
      Recipe copy = Database.createRecipeFromExisting(selectedRecipe);
      copy.setRecipeName(selectedRecipe.getRecipeName() + " - Copy");
      copy.save();
      recipeList.add(mAdapter.getPosition(selectedRecipe) + 1, copy);
      mAdapter.notifyDataSetChanged();
    }

    // Delete recipe selected
    else if (selected.equals(DELETE_RECIPE)) {
      deleteAlert(selectedRecipe).show();
    }

    // Edit fermentation selected
    else if (selected.equals(EDIT_FERM)) {
      Intent i = new Intent(c, EditFermentationProfileActivity.class);
      i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
      i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
      startActivity(i);
    }

    //  Brew timer
    else if (selected.equals(BREW_TIMER)) {
      Intent i = new Intent(c, BrewTimerActivity.class);
      i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
      i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
      startActivity(i);
    }

    // Edit Mash profile
    else if (selected.equals(EDIT_MASH)) {
      Intent i = new Intent(c, EditMashProfileActivity.class);
      i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
      i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
      i.putExtra(Constants.KEY_PROFILE_ID, selectedRecipe.getMashProfile().getId());
      i.putExtra(Constants.KEY_PROFILE, selectedRecipe.getMashProfile());
      startActivity(i);
    }

    else if (selected.equals(RECIPE_NOTES)) {
      Intent i = new Intent(c, EditRecipeNotesActivity.class);
      i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
      i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
      startActivity(i);
    }

    return true;
  }

  public void setCorrectView() {
    if (recipeList.size() == 0) {
      noRecipesView.setVisibility(View.VISIBLE);
      listView.setVisibility(View.GONE);
      if (isTablet) {
        // The view pager may not always be defined.
        mViewPager.setVisibility(View.GONE);
      }
    }
    else {
      noRecipesView.setVisibility(View.GONE);
      listView.setVisibility(View.VISIBLE);
      if (isTablet) {
        mViewPager.setVisibility(View.VISIBLE);
      }
    }
  }
  
  public void updateTabletDetailsView(Recipe r)
  {
	  // ViewPager and pagerAdapter for Slidy tabs!
	  cpAdapter = new DisplayRecipeCollectionPagerAdapter(getChildFragmentManager(), r, c);

	  // Set Adapter
	  mViewPager = (ViewPager) detailsView.findViewById(R.id.pager);
	  mViewPager.setAdapter(cpAdapter);

	  // Set to the first page - the ingredients list.
	  mViewPager.setCurrentItem(0);
	  //mViewPager.setOnPageChangeListener(pageListener);
  }

  private AlertDialog.Builder deleteAlert(final Recipe r) {
    return new AlertDialog.Builder(c)
            .setTitle("Confirm Delete")
            .setMessage("Do you really want to delete '" + r.getRecipeName() + "'")
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("RecipesFragment", "Deleting recipe: " + r);
                recipeList.remove(r);
                mAdapter.notifyDataSetChanged();
                Database.deleteRecipe(r);
                setCorrectView();
                if (isTablet)
                {
                  // If we're running on a tablet, we should set the current selected item to 0
                  // and update the details view.  Otherwise, the details view will remain stuck on the 
                  // current (now deleted) recipe.
                  currentSelectedIndex = (currentSelectedIndex > 0) ? currentSelectedIndex - 1 : 0;
                  updateTabletDetailsView(recipeList.get(currentSelectedIndex));
                }
                Log.d("RecipesFragment", "Recipe deleted");
              }

            })

            .setNegativeButton(android.R.string.no, null);
  }

  private AlertDialog.Builder scaleAlert(final Recipe r) {
    LayoutInflater factory = LayoutInflater.from(c);
    final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_scale, null);
    final EditText editText = (EditText) alertView.findViewById(R.id.new_volume_edit_text);

    return new AlertDialog.Builder(c)
            .setTitle("Scale Recipe")
            .setView(alertView)
            .setPositiveButton(R.string.scale, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                double newVolume = Double.parseDouble(editText.getText().toString()
                                                              .replace(",", "."));
                Utils.scaleRecipe(r, newVolume);
                mAdapter.notifyDataSetChanged();
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  @Override
  public void handleClick(View v) {
    // Currently, the handleClick method is only valid when running on
    // a tablet.  If we hit this method when not running on a tablet,
    // we should just return.
    if (!isTablet) {
      Log.d("RecipesFragment", "Not on tablet, do nothing");
      return;
    }
    
    // Get the current selected recipe.
    Recipe r = recipeList.get(currentSelectedIndex);
    
    if (v.getId() == R.id.add_ingredient_button) {
      // The user has pressed the add-ingredient button.  Display
      // options for which type of ingredient to add.  This button should 
      // only be present on tablets.
      Log.d("RecipesFragment", "User pressed add-ingredient button");
      ingredientSelectAlert().show();
    }
    else if (v.getId() == R.id.add_fermentable_button) {
      // User has chosen to add a fermentable - start the 
      // add fermentable activity.
      Intent i = new Intent(getActivity(), AddFermentableActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);
    }
    else if (v.getId() == R.id.add_hops_button) {
      // User has chosen to add a hop - start the add hop activity.
      Intent i = new Intent(getActivity(), AddHopsActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);
    }
    else if (v.getId() == R.id.add_yeast_button) {
      // User has chosen to add a yeast - start the add hop activity.
      Intent i = new Intent(getActivity(), AddYeastActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);
    }
    else if (v.getId() == R.id.add_misc_button) {
      // User has chosen to add a misc - start the add hop activity.
      Intent i = new Intent(getActivity(), AddMiscActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);
    }
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Check if we can handle this options item.  The recipes fragment
    // can handle the following events: New Recipe
    switch (item.getItemId()) {
      case R.id.menu_new_recipe:
        // The add recipe button was pressed - start the intent, indicating
        // that the display recipe activity should be started if we're 
        // not running on a tablet.
        Intent i = new Intent(getActivity(), AddRecipeActivity.class);
        i.putExtra(Constants.DISPLAY_ON_CREATE, !isTablet);
        startActivity(i);
        return true;
    }
    
    // Return false if the item was unhandled.
    return false;
  }
  
  @Override
  public void update() {
      Log.d("RecipesFragment", "Updating RecipesFragment UI");
      if (recipeList != null && c != null) {
        new GetRecipeListFromDatabaseTask(getActivity()).execute("");
      }
  }

  private class GetRecipeListFromDatabaseTask extends AsyncTask<String, Void, String> {

    private Context context;
    //private ProgressDialog progress;

    public GetRecipeListFromDatabaseTask(Context c) {
      this.context = c;
    }

    @Override
    protected String doInBackground(String... params) {
      // Get recipes to display
      recipeList.removeAll(recipeList);
      recipeList.addAll(Database.getRecipeList(databaseInterface));

      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      mAdapter.notifyDataSetChanged();
      setCorrectView();
      Log.d("readRecipesFromDatabase", "Finished reading recipes from database");
	  
	    // If we're running in tablet mode, try to set the details view to
      // display the most recently selected receipe.
	    if (isTablet) {
        if (currentSelectedIndex < recipeList.size()) {
          Log.d("RecipesFragment", "Found recipes, set pageView");
          updateTabletDetailsView(recipeList.get(currentSelectedIndex));
        }
	    }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }

  private AlertDialog.Builder exportAlert(final Recipe r) {
    return new AlertDialog.Builder(getActivity())
            .setTitle("Export recipe")
            .setMessage("Export '" + r.getRecipeName() + "' to BeerXML file?")
            .setPositiveButton(R.string.export, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                new ExportRecipe(r).execute("");
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  private AlertDialog.Builder finishedExporting(String pathToFile) {
    return new AlertDialog.Builder(getActivity())
            .setTitle("Complete")
            .setMessage("Finished exporting recipe to: \n" + pathToFile)
            .setPositiveButton(R.string.done, null);
  }
  
  private AlertDialog.Builder ingredientSelectAlert() {
    // Inflater to inflate custom alert view.
    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Inflate our custom layout
    View v = inflater.inflate(R.layout.select_ingredient_type, null);
    
    return new AlertDialog.Builder(getActivity())
      .setTitle("Select Ingredient Type")
      .setView(v)
      .setNegativeButton(R.string.done, null);
  }

  // Async task to export all recipes
  private class ExportRecipe extends AsyncTask<String, Void, String> {

    private ProgressDialog progress;
    private RecipeXmlWriter xmlWriter;
    private Context context;
    private Recipe r;
    private String filePrefix;

    private ExportRecipe(Recipe r) {
      this.context = getActivity();
      this.r = r;
      this.filePrefix = r.getRecipeName().replaceAll("\\s", "") + "-";
    }

    @Override
    protected String doInBackground(String... params) {
      xmlWriter = new RecipeXmlWriter(context);
      xmlWriter.writeRecipe(r, filePrefix);
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      finishedExporting(xmlWriter.getSavedFileLocation()).show();
      Log.d("ExportAllRecipes", "Finished exporting recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(context);
      progress.setMessage("Exporting " + r.getRecipeName() + "...");
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
