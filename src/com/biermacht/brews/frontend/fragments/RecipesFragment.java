package com.biermacht.brews.frontend.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
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
import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.BrewTimerActivity;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.frontend.EditFermentationProfileActivity;
import com.biermacht.brews.frontend.EditMashProfileActivity;
import com.biermacht.brews.frontend.EditRecipeNotesActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditRecipeActivity;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.RecipeArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

import java.util.ArrayList;

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
  //Declare views here
  private ListView listView;
  private TextView noRecipesView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    // Get views
    pageView = (RelativeLayout) inflater.inflate(resource, container, false);
    listView = (ListView) pageView.findViewById(R.id.recipe_list);
    noRecipesView = (TextView) pageView.findViewById(R.id.no_recipes_view);

    // Get database Interface
    databaseInterface = MainActivity.databaseInterface;

    // Get Context
    c = getActivity();

    // Create adapter
    recipeList = new ArrayList<Recipe>();
    mAdapter = new RecipeArrayAdapter(c, recipeList);

    // Set adapter for listView
    listView.setAdapter(mAdapter);

    // Set up the onClickListener
    mClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos,
                              long id) {
        Intent intent = new Intent(c, DisplayRecipeActivity.class);
        intent.putExtra(Constants.KEY_RECIPE, recipeList.get(pos));
        startActivity(intent);
      }
    };

    // Set up my listView with title and ArrayAdapter
    new GetRecipeListFromDatabaseTask(getActivity()).execute("");
    listView.setOnItemClickListener(mClickListener);
    registerForContextMenu(listView);

    // Turn on options menu
    setHasOptionsMenu(true);

    return pageView;
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
    }
    else {
      noRecipesView.setVisibility(View.GONE);
      listView.setVisibility(View.VISIBLE);
    }
  }

  private AlertDialog.Builder deleteAlert(final Recipe r) {
    return new AlertDialog.Builder(c)
            .setTitle("Confirm Delete")
            .setMessage("Do you really want to delete '" + r.getRecipeName() + "'")
            .setIcon(android.R.drawable.ic_delete)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("RecipesFragment::deleteAlert", "Deleting recipe: " + r);
                recipeList.remove(r);
                mAdapter.notifyDataSetChanged();
                Database.deleteRecipe(r);
                setCorrectView();
                Log.d("RecipesFragment::deleteAlert", "Recipe deleted");
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

  }

  private class GetRecipeListFromDatabaseTask extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progress;

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
      //progress.dismiss();
      mAdapter.notifyDataSetChanged();
      setCorrectView();
      Log.d("readRecipesFromDatabase", "Finished reading recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(context);
      progress.setMessage("Importing...");
      progress.setIndeterminate(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setCancelable(true);
      //progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }
}
