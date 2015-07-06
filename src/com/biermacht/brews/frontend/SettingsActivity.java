package com.biermacht.brews.frontend;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.xml.RecipeXmlWriter;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AddEditActivity {

  // Views to display
  public Spinner preferredUnitsSpinner;
  public View deleteAllRecipesView;
  public View exportRecipesView;
  public View resetIngredientsView;

  // View titles
  public TextView deleteAllRecipesViewTitle;
  public TextView exportRecipesViewTitle;
  public TextView resetIngredientsViewTitle;

  // View contents
  public TextView preferredUnitsViewText;
  public TextView deleteAllRecipesViewText;
  public TextView exportRecipesViewText;
  public TextView resetIngredientsViewText;

  // Lists for spinners
  public ArrayList<String> unitSystemsArray;

  // Data storage
  public String unitSystem;
  public Context appContext;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create our views
    preferredUnitsSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView,
                                                       false);
    deleteAllRecipesView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    exportRecipesView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    resetIngredientsView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    // Set click listeners for views
    deleteAllRecipesView.setOnClickListener(onClickListener);
    exportRecipesView.setOnClickListener(onClickListener);
    resetIngredientsView.setOnClickListener(onClickListener);

    // Store context for use in async tasks
    appContext = this;

    // Remove views we don't want
    mainView.removeView(spinnerView);
    mainView.removeView(amountView);
    mainView.removeView(timeView);

    // Add views we do want
    mainView.addView(preferredUnitsSpinner);
    mainView.addView(deleteAllRecipesView);
    mainView.addView(exportRecipesView);
    mainView.addView(resetIngredientsView);

    // Get titles and set correct text
    deleteAllRecipesViewTitle = (TextView) deleteAllRecipesView.findViewById(R.id.title);
    deleteAllRecipesViewTitle.setText("Delete all recipes");
    nameViewTitle.setText("Brewmaster");

    exportRecipesViewTitle = (TextView) exportRecipesView.findViewById(R.id.title);
    exportRecipesViewTitle.setText("Export recipes");

    resetIngredientsViewTitle = (TextView) resetIngredientsView.findViewById(R.id.title);
    resetIngredientsViewTitle.setText("Reset Ingredients");

    // Get content views
    deleteAllRecipesViewText = (TextView) deleteAllRecipesView.findViewById(R.id.text);
    exportRecipesViewText = (TextView) exportRecipesView.findViewById(R.id.text);
    resetIngredientsViewText = (TextView) resetIngredientsView.findViewById(R.id.text);

    // Set content view values
    deleteAllRecipesViewText.setText("Permanently delete local recipes");
    nameViewText.setText(preferences.getString(Constants.PREF_BREWER_NAME, "No name provided"));
    exportRecipesViewText.setText("Export recipes to XML file.");
    resetIngredientsViewText.setText("Restore default ingredient database");

    // Configure spinner for preferred units
    SpinnerAdapter unitsAdapter = new SpinnerAdapter(this, unitSystemsArray, "Preferred units");
    unitsAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    preferredUnitsSpinner.setAdapter(unitsAdapter);
    preferredUnitsSpinner.setSelection(unitSystemsArray.indexOf(preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL)));

    // Handle type selector here
    preferredUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        unitSystem = unitSystemsArray.get(position);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });
  }

  @Override
  public void onRecipeNotFound() {
    // We don't need a recipe for this, so do nothing.
    Log.d("SettingsActivity", "Recipe not needed, continuing");
    mRecipe = new Recipe();
  }

  @Override
  public void onMissedClick(View v) {
    if (v.equals(deleteAllRecipesView)) {
      deleteAllRecipes().show();
      return;
    }
    else if (v.equals(exportRecipesView)) {
      exportRecipes().show();
      return;
    }
    else if (v.equals(resetIngredientsView)) {
      resetIngredients().show();
      return;
    }
    else {
      return;
    }
  }

  @Override
  public void getList() {
    unitSystemsArray = Constants.UNIT_SYSTEMS;
  }

  @Override
  public void createSpinner() {
    // Do nothing here, we don't use the default spinner
  }

  @Override
  public void configureSpinnerListener() {
    spinnerListener = new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    };
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();

    preferences.edit().putString(Constants.PREF_MEAS_SYSTEM, unitSystem).commit();
    preferences.edit().putString(Constants.PREF_BREWER_NAME, name).commit();
  }

  @Override
  public void onFinished() {
    finish();
  }

  @Override
  public void onCancelPressed() {
    finish();
  }

  @Override
  public void onDeletePressed() {
    // Should never be pressed. We disable delete button for this activity
  }

  // Custom dialogs for this activity only
  private Builder deleteAllRecipes() {
    return new AlertDialog.Builder(this)
            .setTitle("Delete all recipes")
            .setMessage("Delete all local recipes from this device? This action cannot be undone." +
                                "  Remote recipes will be unaffected.")
            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                new DeleteRecipes().execute("");
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  private Builder exportRecipes() {
    return new AlertDialog.Builder(this)
            .setTitle("Export all recipes")
            .setMessage("Export all recipes to BeerXML.")
            .setPositiveButton(R.string.export, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                new ExportRecipes().execute("");
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  private Builder finishedExporting(String pathToFile) {
    return new AlertDialog.Builder(this)
            .setTitle("Complete")
            .setMessage("Finished exporting recipes to: \n" + pathToFile)
            .setPositiveButton(R.string.done, null);
  }

  /**
   * Async task to export all recipes to BeerXML file.
   */
  private class ExportRecipes extends AsyncTask<String, Void, String> {

    private ProgressDialog progress;
    private RecipeXmlWriter xmlWriter;

    @Override
    protected String doInBackground(String... params) {
      xmlWriter = new RecipeXmlWriter(SettingsActivity.this);
      xmlWriter.writeRecipes(Database.getRecipeList(MainActivity.databaseInterface), "recipes-");
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
      progress = new ProgressDialog(appContext);
      progress.setMessage("Exporting all recipes...");
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
   * Async task to delete all recipes.
   */
  private class DeleteRecipes extends AsyncTask<String, Void, String> {

    private ProgressDialog progress;

    @Override
    protected String doInBackground(String... params) {
      Database.deleteAllRecipes();
      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      Log.d("DeleteAllRecipes", "Finished deleting recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(appContext);
      progress.setMessage("Deleting all recipes...");
      progress.setIndeterminate(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setCancelable(false);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
  }

  private Builder resetIngredients() {
    return new AlertDialog.Builder(this)
            .setTitle("Reset Ingredients")
            .setMessage("Reset default ingredient list? This will not affect any custom made ingredients.")
            .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                new ResetIngredients().execute("");
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  /**
   * Async task to reset the default ingredient database.
   */
  private class ResetIngredients extends AsyncTask<String, Void, String> {

    private ProgressDialog progress;

    @Override
    protected String doInBackground(String... params) {
      Log.d("ResetIngredients", "Deleting all 'permanent' ingredients");
      for (Ingredient ing : Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_PERMANENT)) {
        Database.deleteIngredientWithId(ing.getId(), ing.getDatabaseId());
      }

      Log.d("ResetIngredients", "Re-initializing ingredient assets");
      try {
        ingredientHandler.importIngredients();
      } catch (IOException e) {
        Log.e("ResetIngredients", e.toString());
      }

      return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      Log.d("ResetIngredients", "Finished exporting recipes");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(appContext);
      progress.setMessage("Resetting ingredient database...");
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
