package com.biermacht.brews.frontend.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.AddRecipeActivity;
import com.biermacht.brews.frontend.BrewTimerActivity;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.frontend.DisplayStyleActivity;
import com.biermacht.brews.frontend.EditFermentationProfileActivity;
import com.biermacht.brews.frontend.EditMashProfileActivity;
import com.biermacht.brews.frontend.EditRecipeNotesActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddHopsActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddYeastActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditRecipeActivity;
import com.biermacht.brews.frontend.adapters.DisplayRecipeCollectionPagerAdapter;
import com.biermacht.brews.frontend.adapters.RecipeArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.DriveActivity;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.utils.comparators.RecipeDateComparator;
import com.biermacht.brews.utils.comparators.RecipeNameComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;
import com.biermacht.brews.xml.RecipeXmlWriter;

import java.util.ArrayList;
import java.util.Collections;

public class RecipesFragment extends Fragment implements BiermachtFragment {

  // Layout resource
  private static int resource = R.layout.fragment_recipes;

  // The parent view group.
  ViewGroup pageView;

  // Recipe List stuff
  private RecipeArrayAdapter mAdapter;
  private AdapterView.OnItemClickListener mClickListener;
  private AdapterView.OnItemLongClickListener mLongClickListener;
  private ArrayList<Recipe> recipeList;

  // Context menu items
  private ArrayList<String> menuItems;

  // Holds the recipe for which the current context menu (long press)
  // action is being performed.
  private Recipe contextActionRecipe;

  // Context
  private Context context;
  private DatabaseAPI databaseApi;

  // Declare views here
  private ListView listView;
  private TextView noRecipesView;
  private LinearLayout detailsView;

  // Currently displayed AlertDialog
  private AlertDialog currentAlert;

  // Fields used when running on a tablet
  public boolean isTablet = false;
  private DisplayRecipeCollectionPagerAdapter cpAdapter;
  private ViewPager mViewPager;
  public int currentSelectedIndex = 0;

  // Contextual action bar (CAB) stuff
  ActionMode.Callback mActionModeCallback;
  ActionMode mActionMode;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("RecipesFragment", "Starting onCreateView()");

    // Get views
    pageView = (RelativeLayout) inflater.inflate(resource, container, false);
    listView = (ListView) pageView.findViewById(R.id.recipe_list);
    noRecipesView = (TextView) pageView.findViewById(R.id.no_recipes_view);

    // Get Context
    context = getActivity();
    databaseApi = new DatabaseAPI(context);

    // Create recipe adapter.
    recipeList = new ArrayList<Recipe>();
    mAdapter = new RecipeArrayAdapter(context, recipeList, this);

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

    // Callback which handles the creation and operation of the contextual action bar when
    // a Recipe is long-pressed.
    mActionModeCallback = new ActionMode.Callback() {

      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Called when the action mode is created; startActionMode() was called
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_recipe_selected, menu);
        return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        return false; // Return false if nothing is done
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Declare Intent used to start new apps
        Intent i;

        // Get the recipe here, in case it has changed since the user long pressed the list item.
        contextActionRecipe = recipeList.get(currentSelectedIndex);

        // Determine which context item was selected.
        switch (item.getItemId()) {
          case R.id.menu_delete_recipe:
            deleteAlert(contextActionRecipe).show();
            return true;

          case R.id.menu_scale_recipe:
            scaleAlert(contextActionRecipe).show();
            return true;

          case R.id.menu_copy_recipe:
            // TODO: Ask user for new name, animate new recipe entering list.
            Recipe copy = databaseApi.createRecipeFromExisting(contextActionRecipe);
            copy.setRecipeName(contextActionRecipe.getRecipeName() + " - Copy");
            recipeList.add(mAdapter.getPosition(contextActionRecipe) + 1, copy);
            mAdapter.notifyDataSetChanged();
            Snackbar.make(listView, R.string.recipe_copied, Snackbar.LENGTH_LONG).show();
            copy.save(context);
            return true;

          case R.id.edit_recipe:
            i = new Intent(context, EditRecipeActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, contextActionRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, contextActionRecipe);
            startActivity(i);
            return true;

          case R.id.export_recipe:
            exportAlert(contextActionRecipe).show();
            return true;

          case R.id.edit_fermentation_profile:
            i = new Intent(context, EditFermentationProfileActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, contextActionRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, contextActionRecipe);
            startActivity(i);
            return true;

          case R.id.brew_timer:
            i = new Intent(context, BrewTimerActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, contextActionRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, contextActionRecipe);
            startActivity(i);
            return true;

          case R.id.edit_mash_profile:
            i = new Intent(context, EditMashProfileActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, contextActionRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, contextActionRecipe);
            i.putExtra(Constants.KEY_PROFILE_ID, contextActionRecipe.getMashProfile().getId());
            i.putExtra(Constants.KEY_PROFILE, contextActionRecipe.getMashProfile());
            startActivity(i);
            return true;

          case R.id.recipe_notes:
            i = new Intent(context, EditRecipeNotesActivity.class);
            i.putExtra(Constants.KEY_RECIPE, contextActionRecipe);
            i.putExtra(Constants.KEY_RECIPE_ID, contextActionRecipe.getId());
            startActivity(i);
            return true;
        }
        return false;
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {
        // Called when the user exits the action mode

        // Nullify the CAB.
        mActionMode = null;

        // Clear any selections in the list.
        mAdapter.clearChecks();
        mAdapter.notifyDataSetChanged();
      }
    };

    // Set up the onClickListener for when a Recipe is selected
    // in the main recipe list.
    mClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {

        // If the contextual action bar is shown, and we've clicked another recipe,
        // while we're in tablet mode, we need to cancel the CAB so that it isn't
        // confusing.  The other option would be to update contextActionRecipe.
        if (mActionMode != null && isTablet) {
          mActionMode.finish();
        }

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
          Intent intent = new Intent(context, DisplayRecipeActivity.class);
          intent.putExtra(Constants.KEY_RECIPE, recipeList.get(pos));
          startActivity(intent);
        }
      }
    };

    // Set up the onLongClickListener for when a Recipe is long clicked.
    mLongClickListener = new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
        // Mark this list item and only this list item as selected.
        mAdapter.clearChecks();
        mAdapter.setChecked(pos, true);
        mAdapter.notifyDataSetChanged();
        currentSelectedIndex = pos;

        // If the Contextual action bar is already being displayed, don't start a new action bar.
        if (mActionMode != null) {
          // Return true so that onItemClick is not invoked.
          return true;
        }

        // Otherwise, start the CAB.
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);

        // Return true to indicate we have handled the action.  This prevents the onClickListener from
        // being called after the onItemLongClick event.
        return true;
      }
    };

    // Set up listView with title and ArrayAdapter
    updateRecipesFromDatabase();
    listView.setOnItemClickListener(mClickListener);
    listView.setOnItemLongClickListener(mLongClickListener);

    // Turn on options menu
    setHasOptionsMenu(true);

    Log.d("RecipesFragment", "Exiting onCreateView()");
    return pageView;
  }

  public void recipesExportedSnackbar() {
    Snackbar.make(listView, R.string.recipe_exported, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.activity_main, menu);
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

  public void updateTabletDetailsView(Recipe r) {
    // ViewPager and pagerAdapter for Slidy tabs!
    cpAdapter = new DisplayRecipeCollectionPagerAdapter(getChildFragmentManager(), r, context);

    // Set Adapter
    mViewPager = (ViewPager) detailsView.findViewById(R.id.pager);
    mViewPager.setAdapter(cpAdapter);

    // Set to the first page - the ingredients list.
    mViewPager.setCurrentItem(0);
  }

  /**
   * Returns a builder for an alert which prompts the user if they would like to delete the given
   * Recipe.  If the user selects yes, the Recipe will be deleted.  If the user cancels the alert,
   * the Recipe will not be deleted.
   */
  private AlertDialog.Builder deleteAlert(final Recipe r) {
    return new AlertDialog.Builder(context)
            .setTitle("Confirm Delete")
            .setMessage("Do you really want to delete '" + r.getRecipeName() + "'?")
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("RecipesFragment", "Deleting recipe: " + r);
                recipeList.remove(r);
                mAdapter.notifyDataSetChanged();
                databaseApi.deleteRecipe(r);
                if (isTablet) {
                  // If we're running on a tablet, we should set the current selected item to 0
                  // and update the details view.  Otherwise, the details view will remain stuck on the 
                  // current (now deleted) recipe.
                  currentSelectedIndex = (currentSelectedIndex > 0) ? currentSelectedIndex - 1 : 0;
                  if (recipeList.size() != 0) {
                    // Only update the details view if there is a recipe to show.
                    updateTabletDetailsView(recipeList.get(currentSelectedIndex));
                  }
                  else {
                    // Otherwise, set the correct views to show.
                    setCorrectView();
                  }
                }

                // If the last recipe was just deleted, we need to update which
                // view is being displayed.
                setCorrectView();

                // Dismiss the CAB
                mActionMode.finish();
                Log.d("RecipesFragment", "Recipe deleted");

                // Show Snackbar to indicate deletion.
                Snackbar.make(listView, R.string.recipe_deleted, Snackbar.LENGTH_LONG).show();
              }

            })

            .setNegativeButton(android.R.string.no, null);
  }

  private AlertDialog.Builder scaleAlert(final Recipe r) {
    LayoutInflater factory = LayoutInflater.from(context);
    final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_scale, null);
    final EditText editText = (EditText) alertView.findViewById(R.id.new_volume_edit_text);

    return new AlertDialog.Builder(context)
            .setTitle("Scale Recipe")
            .setView(alertView)
            .setPositiveButton(R.string.scale, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                double newVolume = Double.parseDouble(editText.getText().toString()
                                                              .replace(",", "."));
                Utils.scaleRecipe(getActivity(), r, newVolume);
                mAdapter.notifyDataSetChanged();

                // Dismiss the CAB
                mActionMode.finish();

                // Show Snackbar to indicate completion.
                Snackbar.make(listView, R.string.recipe_scaled, Snackbar.LENGTH_LONG).show();
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  @Override
  public void handleClick(View v) {
    // Currently, the handleClick method is only valid when running on
    // a tablet.  If we hit this method when not running on a tablet,
    // we should just return.
    if (! isTablet) {
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
      currentAlert = ingredientSelectAlert().show();
    }
    else if (v.getId() == R.id.add_fermentable_button) {
      // User has chosen to add a fermentable - start the 
      // add fermentable activity.
      Intent i = new Intent(getActivity(), AddFermentableActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);

      // Dismiss the currentAlert, which should be the ingredientSelectorAlert()
      currentAlert.dismiss();
    }
    else if (v.getId() == R.id.add_hops_button) {
      // User has chosen to add a hop - start the add hop activity.
      Intent i = new Intent(getActivity(), AddHopsActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);

      // Dismiss the currentAlert, which should be the ingredientSelectorAlert()
      currentAlert.dismiss();
    }
    else if (v.getId() == R.id.add_yeast_button) {
      // User has chosen to add a yeast - start the add yeast activity.
      Intent i = new Intent(getActivity(), AddYeastActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);

      // Dismiss the currentAlert, which should be the ingredientSelectorAlert()
      currentAlert.dismiss();
    }
    else if (v.getId() == R.id.add_misc_button) {
      // User has chosen to add a misc - start the add misc activity.
      Intent i = new Intent(getActivity(), AddMiscActivity.class);
      i.putExtra(Constants.KEY_RECIPE, r);
      startActivity(i);

      // Dismiss the currentAlert, which should be the ingredientSelectorAlert()
      currentAlert.dismiss();
    }
    else if (v.getId() == R.id.display_style_button) {
      // Called when the display style button is pressed on the profile fragment.
      // Launches the DisplayStyleActivity.
      Intent i = new Intent(getActivity(), DisplayStyleActivity.class);
      i.putExtra(Constants.KEY_STYLE, r.getStyle());
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
        i.putExtra(Constants.DISPLAY_ON_CREATE, ! isTablet);
        startActivity(i);
        return true;
    }

    // Return false if the item was unhandled.
    return false;
  }

  @Override
  public String name() {
    return "Recipes";
  }

  @Override
  public void update() {
    Log.d("RecipesFragment", "Updating RecipesFragment UI");
    if (recipeList != null && context != null) {
      updateRecipesFromDatabase();
    }
  }

  public void updateRecipesFromDatabase() {
    Log.d("RecipesFragment", "updateRecipesFromDatabase()");

    // Load all recipes from database.
    ArrayList<Recipe> loadedRecipes = databaseApi.getRecipeList();

    // Update the recipe list with the loaded recipes.
    recipeList.removeAll(recipeList);
    recipeList.addAll(loadedRecipes);

    // Sort the recipe list according to the configured strategy.
    sortRecipes();

    // Update the adapter and UI.
    mAdapter.notifyDataSetChanged();
    setCorrectView();

    // If we're running in tablet mode, try to set the details view to
    // display the most recently selected receipe.
    if (isTablet) {
      if (currentSelectedIndex < recipeList.size()) {
        Log.d("RecipesFragment", "Found recipes, set pageView");
        updateTabletDetailsView(recipeList.get(currentSelectedIndex));
      }
    }
  }

  private void sortRecipes() {
    String sortStrategy = this.getActivity().
            getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).
            getString(Constants.PREF_SORT_STRATEGY, Constants.SORT_STRATEGY_ALPHABETICAL);
    Log.d("RecipesFragment", "Sorting recipes using strategy: " + sortStrategy);

    if (sortStrategy.equals(Constants.SORT_STRATEGY_ALPHABETICAL)) {
      Collections.sort(recipeList, new RecipeNameComparator<Recipe>());
    }
    else if (sortStrategy.equals(Constants.SORT_STRATEGY_REV_ALPHABETICAL)) {
      Collections.sort(recipeList, Collections.<Recipe>reverseOrder(new RecipeNameComparator<Recipe>()));
    }
    else if (sortStrategy.equals(Constants.SORT_STRATEGY_BREW_DATE)) {
      Collections.sort(recipeList, new RecipeDateComparator<Recipe>());
    }
    else if (sortStrategy.equals(Constants.SORT_STRATEGY_REV_BREW_DATE)) {
      Collections.sort(recipeList, Collections.reverseOrder(new RecipeDateComparator<Recipe>()));
    }
    else {
      Log.w("RecipesList", "Unrecognized sort strategy: " + sortStrategy);
    }
  }

  private AlertDialog.Builder exportAlert(final Recipe r) {
    return new AlertDialog.Builder(getActivity())
            .setTitle("Export recipe")
            .setMessage("Export '" + r.getRecipeName() + "' to BeerXML file?")
            .setPositiveButton(R.string.local_storage, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                new ExportRecipe(r).execute("");
                mActionMode.finish();
                Snackbar.make(listView, R.string.recipe_exported, Snackbar.LENGTH_LONG).show();
              }

            })
            .setNeutralButton(R.string.cancel, null)
            .setNegativeButton(R.string.drive_button, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                ArrayList<Recipe> l = new ArrayList<Recipe>();
                l.add(r);
                ((DriveActivity) getActivity()).writeFile(l);
                mActionMode.finish();
              }

            });
  }

  private AlertDialog.Builder finishedExporting(String pathToFile) {
    return new AlertDialog.Builder(getActivity())
            .setTitle("Complete")
            .setMessage("Finished exporting recipe to: \n" + pathToFile)
            .setPositiveButton(R.string.done, null);
  }

  /**
   * Returns a builder to display an alert to the User which asks them which type of ingredient to
   * add to the current selected recipe. Selections made in this alert should be handled by the
   * handleClick() method.
   */
  private AlertDialog.Builder ingredientSelectAlert() {
    // Inflater to inflate custom alert view.
    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Inflate our custom layout
    View v = inflater.inflate(R.layout.alert_view_select_ingredient_type, null);

    return new AlertDialog.Builder(getActivity())
            .setTitle("Select Ingredient Type")
            .setView(v)
            .setNegativeButton(R.string.cancel, null);
  }

  // Async task to export a single recipe.
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
      progress.setCancelable(false);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }
}
