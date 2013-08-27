package com.biermacht.brews.frontend;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.*;
import android.content.*;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.utils.*;

public class SettingsActivity extends AddEditActivity {

    // Views to display
    public Spinner preferredUnitsSpinner;
    public View deleteAllRecipesView;

    // View titles
    public TextView deleteAllRecipesViewTitle;

    // View contents
    public TextView preferredUnitsViewText;
    public TextView deleteAllRecipesViewText;

    // Lists for spinners
    public ArrayList<String> unitSystemsArray;

    // Data storage
    public String unitSystem;
    public Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our views
        preferredUnitsSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        deleteAllRecipesView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set click listeners for views
        deleteAllRecipesView.setOnClickListener(onClickListener);

        // Get context for async tasks
        appContext = this;

        // Remove views we don't want
        mainView.removeView(spinnerView);
        mainView.removeView(amountView);
        mainView.removeView(timeView);

        // Add views we do want
        mainView.addView(preferredUnitsSpinner);
        mainView.addView(deleteAllRecipesView);

        // Get titles and set correct text
        deleteAllRecipesViewTitle = (TextView) deleteAllRecipesView.findViewById(R.id.title);
        deleteAllRecipesViewTitle.setText("Delete all recipes");
        nameViewTitle.setText("Brewmaster");

        // Get content views
        deleteAllRecipesViewText = (TextView) deleteAllRecipesView.findViewById(R.id.text);

        // Set content view values
        deleteAllRecipesViewText.setText("Permanently delete local recipes");
        nameViewText.setText(preferences.getString(Constants.PREF_BREWER_NAME, "No name provided"));

        // Configure spinner for preferred units
        SpinnerAdapter unitsAdapter = new SpinnerAdapter(this, unitSystemsArray, "Preferred units");
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        preferredUnitsSpinner.setAdapter(unitsAdapter);
        preferredUnitsSpinner.setSelection(unitSystemsArray.indexOf(preferences.getString(Constants.PREF_MEAS_SYSTEM, Units.IMPERIAL)));

        // Handle type selector here
        preferredUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                unitSystem = unitSystemsArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });
    }

    @Override
    public void onRecipeNotFound()
    {
        // We don't need a recipe for this, so do nothing.
        Log.d("SettingsActivity", "Recipe not needed, continuing");
        mRecipe = new Recipe();
    }

    @Override
    public void onMissedClick(View v)
    {
        //AlertDialog alert;
        if (v.equals(deleteAllRecipesView))
        {
            deleteAllRecipes().show();
            return;
        }
        else
            return;

        // Force keyboard open and show popup
        //alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //alert.show();
    }

    @Override
    public void getList()
    {
        unitSystemsArray = Constants.UNIT_SYSTEMS;
    }

    @Override
    public void createSpinner()
    {
        // Do nothing here, we don't use the default spinner
    }

    @Override
    public void configureSpinnerListener()
    {
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
    public void acquireValues() throws Exception
    {
        super.acquireValues();

        preferences.edit().putString(Constants.PREF_MEAS_SYSTEM, unitSystem).commit();
        preferences.edit().putString(Constants.PREF_BREWER_NAME, name).commit();
    }

    @Override
    public void onFinished()
    {
        finish();
    }


    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        // Should never be pressed. We disable delete button for this activity
    }

    /************************************************************************************
    /*  Extra stuff goes below, unrelated to AddEditActivity methods.
    /*      Contains builders and tasks for this activity only.
    /************************************************************************************/

    // Custom dialogs for this activity only
	private Builder deleteAllRecipes()
	{
		return new AlertDialog.Builder(this)
			.setTitle("Delete all recipes")
			.setMessage("Delete all local recipes from this device? This action cannot be undone.  Remote recipes will be unaffected.")
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
                    new DeleteRecipes().execute("");
				}

		    })

		    .setNegativeButton(R.string.cancel, null);
	}

    // Async task to delete all recipes
    private class DeleteRecipes extends AsyncTask<String, Void, String> {

        private ProgressDialog progress;

        @Override
        protected String doInBackground(String... params)
        {
            Database.deleteAllRecipes();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progress.dismiss();
            Log.d("DeleteAllRecipes", "Finished deleting recipes");
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(appContext);
            progress.setMessage("Deleting all recipes...");
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
