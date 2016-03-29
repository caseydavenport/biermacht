package com.biermacht.brews.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;

import java.io.IOException;

/**
 * Async task to reset the default ingredient database.
 */
public class ResetIngredients extends AsyncTask<String, Void, String> {

  private ProgressDialog progress;
  private Context context;
  private IngredientHandler ingredientHandler;
  private String message;

  public ResetIngredients(Context c, String message) {
    super();

    this.context = c;
    this.ingredientHandler = new IngredientHandler(c);
    this.message = message;
  }

  @Override
  protected String doInBackground(String... params) {
    Log.d("ResetIngredients", "Deleting all 'permanent' ingredients");
    for (Ingredient ing : DatabaseAPI.getIngredients(Constants.DATABASE_PERMANENT)) {
      DatabaseAPI.deleteIngredientWithId(ing.getId(), ing.getDatabaseId());
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