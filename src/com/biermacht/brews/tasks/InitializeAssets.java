package com.biermacht.brews.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

public class InitializeAssets extends AsyncTask<String, Void, String> {

    private IngredientHandler ingredientHandler;
    public InitializeAssets(IngredientHandler i)
    {
        this.ingredientHandler = i;
    }

    @Override
    protected String doInBackground(String... params) {
        ingredientHandler.getStylesList();
        ingredientHandler.getFermentablesList();
        ingredientHandler.getHopsList();
        ingredientHandler.getYeastsList();
        ingredientHandler.getMashProfileList();

        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GetAssetsFromXml", "Successfully retrieved all assets");
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}