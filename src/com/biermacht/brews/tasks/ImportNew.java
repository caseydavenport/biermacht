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
public class ImportNew extends AsyncTask<String, Void, String> {

  private ProgressDialog progress;
  private Context context;
  private IngredientHandler ingredientHandler;
  private String message;
  private String filePath;

  // One of "ingredient", "mashprofile".
  private String type;

  public ImportNew(String type, String message, Context c, String filePath) {
    super();

    this.context = c;
    this.ingredientHandler = new IngredientHandler(c);
    this.message = message;
    this.filePath = filePath;
    this.type = type;
  }

  @Override
  protected String doInBackground(String... params) {
    Log.d("ImportNew", "Installing from: " + filePath);
    try {
      if (this.type.equals("ingredient")) {
        // Imports ingredients from the file path.
        ingredientHandler.importIngredients(filePath);
      }
      else if (this.type.equals("mashprofile")) {
        // Imports mash profiles from the file path.
        new DatabaseAPI(this.context).addMashProfileList(Constants.DATABASE_CUSTOM,
                                                         ingredientHandler.getProfilesFromXml(filePath),
                                                         Constants.OWNER_NONE);
      }
      else if (this.type.equals("style")) {
        new DatabaseAPI(this.context).addStyleList(Constants.DATABASE_PERMANENT,
                                                   ingredientHandler.getStylesFromXml(filePath),
                                                   Constants.OWNER_NONE);
      }
      else {
        Log.w("ImportNew", "Invalid type: " + this.type);
      }
    } catch (IOException e) {
      Log.e("ImportNew", e.toString());
    }

    return "Executed";
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    progress.dismiss();
    Log.d("ImportNew", "Finished importing new " + this.type);
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