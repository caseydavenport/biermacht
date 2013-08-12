package com.biermacht.brews.frontend.IngredientActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Database;

import java.util.ArrayList;

public abstract class IngredientActivity extends Activity implements OnClickListener {

    // Main view - holds all the rows
    public ViewGroup mainView;

    // Alert builder
    public AlertBuilder alertBuilder;

    // Important things
    public OnClickListener onClickListener;

    // LayoutInflater
    LayoutInflater inflater;

    // Recipe we are editing
    public Recipe mRecipe;

    // IDs received in intent
    long recipeId;
    long ingredientId;

    // IngredientHandler to get ingredient arrays
    IngredientHandler ingredientHandler;

    // Holds the currently selected ingredient
    Ingredient ingredient;

    // Editable rows to display
    public Spinner ingredientSpinner;
    public View nameView;
    public View amountView;
    public View timeView;

    // Titles from rows
    public TextView nameViewTitle;
    public TextView amountViewTitle;
    public TextView timeViewTitle;

    // Content from rows
    public TextView nameViewText;
    public TextView amountViewText;
    public TextView timeViewText;

    // Storage for acquired values
    int time;
    double amount;
    String name;

    // Spinner array declarations
    public ArrayList<Ingredient> ingredientList;

    // Listeners
    OnItemSelectedListener ingredientSpinnerListener;

    // Abstract methods
    public abstract void onMissedClick(View v);
    public abstract void createIngredientSpinner();
    public abstract void configureIngredientSpinnerListener();
    public abstract void getIngredientList();
    public abstract void onCancelPressed();
    public abstract void onDeletePressed();
    public abstract void onFinished();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

    	// Get the Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Get recipe from calling activity
        getValuesFromIntent();

        // Get the list of ingredients to show.
        getIngredientList();
        Log.d("IngredientActivity", "Received " + ingredientList.size() + " ingredients");

        // On click listener
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(nameView))
                    alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
                else if (v.equals(amountView))
                    alert = alertBuilder.editTextFloatAlert(amountViewText, amountViewTitle).create();
                else if (v.equals(timeView))
                    alert = alertBuilder.editTextIntegerAlert(timeViewText, timeViewTitle).create();
                else
                {
                    onMissedClick(v);
                    return;
                }

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };

        // Initialize views and such here
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        nameView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        amountView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        timeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        ingredientSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        nameView.setOnClickListener(onClickListener);
        amountView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(ingredientSpinner);
        mainView.addView(nameView);
        mainView.addView(timeView);
        mainView.addView(amountView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Time (mins)");

        amountViewTitle = (TextView) amountView.findViewById(R.id.title);
        amountViewTitle.setText("Amount (lbs)");

        /************************************************************************
         ************* Get content views, values set below  **********************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        timeViewText = (TextView) timeView.findViewById(R.id.text);
        amountViewText = (TextView) amountView.findViewById(R.id.text);

        // Set up type spinner
        createIngredientSpinner();
        setIngredientSelection();

        // Handle type spinner selections here
        configureIngredientSpinnerListener();
        ingredientSpinner.setOnItemSelectedListener(ingredientSpinnerListener);
    }

    public void getValuesFromIntent()
    {
        recipeId = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);
        ingredientId = getIntent().getLongExtra(Constants.INTENT_INGREDIENT_ID, Constants.INVALID_ID);

        // Acquire recipe
        try
        {
            mRecipe = Database.getRecipeWithId(recipeId);
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
            finish();
        }
    }

    public void setIngredientSelection()
    {
        ingredientSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId())
		{
            case android.R.id.home:
        		finish();
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void acquireValues() throws Exception
    {
        name = nameViewText.getText().toString();
        amount = Double.parseDouble(amountViewText.getText().toString());
        time = Integer.parseInt(timeViewText.getText().toString());
    }

    public void onSubmitPressed()
    {
        boolean readyToGo = true;
        try
        {
            acquireValues();
        }
        catch (Exception e)
        {
            Log.d("AddIngredient", "Could not acquire values: " + e.toString());
            e.printStackTrace();
            readyToGo = false;
        }

        if (readyToGo)
        {
            onFinished();
        }
    }

	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
            onSubmitPressed();
		}

        // If "DELETE" button pressed
        if (v.getId() == R.id.delete_button)
        {
            onDeletePressed();
        }
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
            onCancelPressed();
		}
	}
}
