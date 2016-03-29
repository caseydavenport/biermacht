package com.biermacht.brews.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;

import java.io.IOException;

/**
 * Async task to reset the default ingredient database.
 */
public class ImportNewIngredients extends AsyncTask<String, Void, String> {

  private ProgressDialog progress;
  private Context context;
  private IngredientHandler ingredientHandler;
  private String message;
  private String filePath;

  public ImportNewIngredients(Context c, String filePath) {
    super();

    this.context = c;
    this.ingredientHandler = new IngredientHandler(c);
    this.message = "Importing new ingredients";
    this.filePath = filePath;
  }

  @Override
  protected String doInBackground(String... params) {
    Log.d("ResetIngredients", "Installing ingredients from: " + filePath);
    try {
      // Imports ingredients from the file path.
      ingredientHandler.importIngredients(filePath);
    } catch (IOException e) {
      Log.e("ResetIngredients", e.toString());
    }

    return "Executed";
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    progress.dismiss();
    Log.d("ResetIngredients", "Finished resetting ingredients");
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progress = new ProgressDialog(this.context);
    progress.setMessage(this.message);
    progress.setIndeterminate(false);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setCancelable(false);
    progress.show();
  }

  @Override
  protected void onProgressUpdate(Void... values) {
  }
}