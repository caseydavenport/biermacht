package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;

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

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.utils.Utils;

import android.widget.*;

public class EditRecipeActivity extends Activity implements OnClickListener {

    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private OnClickListener onClickListener;

    // Rows to be displayed
	private View recipeNameView;
	private View timeView;
	private View efficiencyView;
	private View batchSizeView;
	private View boilSizeView;
    private View measuredOGView;
    private View measuredFGView;
    private Spinner styleSpinner;
    private Spinner typeSpinner;
    private Spinner profileSpinner;

    // Titles
    private TextView recipeNameViewTitle;
    private TextView timeViewTitle;
    private TextView efficiencyViewTitle;
    private TextView batchSizeViewTitle;
    private TextView boilSizeViewTitle;
    private TextView measuredOGViewTitle;
    private TextView measuredFGViewTitle;

    // Contents
    private TextView recipeNameViewText;
    private TextView timeViewText;
    private TextView efficiencyViewText;
    private TextView batchSizeViewText;
    private TextView boilSizeViewText;
    private TextView measuredOGViewText;
    private TextView measuredFGViewText;

    // LayoutInflater
    LayoutInflater inflater;
	
	// Data storage declarations
	private BeerStyle style;
	private MashProfile profile;
	private String type;
	private double efficiency;
	
	// Recipe we are editing
	private Recipe mRecipe;
	
	// Spinner array declarations
	private ArrayList<BeerStyle> styleArray;
	private ArrayList<String> typeArray;
	private ArrayList<MashProfile> profileArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);

        // Acquire recipe
        try
        {
            mRecipe = Database.getRecipeWithId(id);
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
            finish();
        }

        // Get values from recipe
        style = mRecipe.getStyle();
        profile = mRecipe.getMashProfile();
        type = mRecipe.getType();
        efficiency = mRecipe.getEfficiency();

        // Get array of styles and mash profiles
        styleArray = MainActivity.ingredientHandler.getStylesList();
        profileArray = MainActivity.ingredientHandler.getMashProfileList();
        typeArray = new ArrayList<String>();
        typeArray.add(Recipe.EXTRACT);
        typeArray.add(Recipe.PARTIAL_MASH);
        typeArray.add(Recipe.ALL_GRAIN);

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(recipeNameView))
                    alert = alertBuilder.editTextStringAlert(recipeNameViewText, recipeNameViewTitle).create();
                else if (v.equals(timeView))
                    alert = alertBuilder.editTextIntegerAlert(timeViewText, timeViewTitle).create();
                else if (v.equals(efficiencyView))
                    alert = alertBuilder.editTextFloatAlert(efficiencyViewText, efficiencyViewTitle).create();
                else if (v.equals(batchSizeView))
                    alert = alertBuilder.editTextFloatAlert(batchSizeViewText, batchSizeViewTitle).create();
                else if (v.equals(boilSizeView))
                    alert = alertBuilder.editTextFloatAlert(boilSizeViewText, boilSizeViewTitle).create();
                else if (v.equals(measuredFGView))
                    alert = alertBuilder.editTextFloatAlert(measuredFGViewText, measuredFGViewTitle).create();
                else if (v.equals(measuredOGView))
                    alert = alertBuilder.editTextFloatAlert(measuredOGViewText, measuredOGViewTitle).create();
                else
                    return; // In case its none of those views...

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };
        
        // Initialize views and stuff
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        recipeNameView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        styleSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        profileSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        timeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        efficiencyView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        batchSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);;
        boilSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        measuredFGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        measuredOGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set onClickListeners for edit text views
        recipeNameView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);
        efficiencyView.setOnClickListener(onClickListener);
        batchSizeView.setOnClickListener(onClickListener);
        boilSizeView.setOnClickListener(onClickListener);
        measuredFGView.setOnClickListener(onClickListener);
        measuredOGView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(recipeNameView);
        mainView.addView(styleSpinner);
        mainView.addView(typeSpinner);
        mainView.addView(profileSpinner);
        mainView.addView(timeView);
        mainView.addView(efficiencyView);
        mainView.addView(batchSizeView);
        mainView.addView(boilSizeView);
        mainView.addView(measuredOGView);
        mainView.addView(measuredFGView);

        /************************************************************************
        ************* Get titles, set values   **********************************
        *************************************************************************/
        recipeNameViewTitle = (TextView) recipeNameView.findViewById(R.id.title);
        recipeNameViewTitle.setText("Recipe Name");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Boil Time (mins)");

        efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
        efficiencyViewTitle.setText("Predicted Efficiency");

        batchSizeViewTitle = (TextView) batchSizeView.findViewById(R.id.title);
        batchSizeViewTitle.setText("Batch Size (gal)");

        boilSizeViewTitle = (TextView) boilSizeView.findViewById(R.id.title);
        boilSizeViewTitle.setText("Boil Size (gal)");

        measuredFGViewTitle = (TextView) measuredFGView.findViewById(R.id.title);
        measuredFGViewTitle.setText("Measured Final Gravity");

        measuredOGViewTitle = (TextView) measuredOGView.findViewById(R.id.title);
        measuredOGViewTitle.setText("Measured Original Gravity");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        recipeNameViewText = (TextView) recipeNameView.findViewById(R.id.text);
        recipeNameViewText.setText(mRecipe.getRecipeName());

        timeViewText = (TextView) timeView.findViewById(R.id.text);
        timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));

        efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
        efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

        batchSizeViewText = (TextView) batchSizeView.findViewById(R.id.text);
        batchSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBatchSize()));

        boilSizeViewText = (TextView) boilSizeView.findViewById(R.id.text);
        boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));

        measuredFGViewText = (TextView) measuredFGView.findViewById(R.id.text);
        measuredFGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredFG()));

        measuredOGViewText = (TextView) measuredOGView.findViewById(R.id.text);
        measuredOGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredOG()));
		
		// If it doesn't contain the current recipes style / profile,
		// then it is custom and we add it to the list.
        // TODO: We should include custom stuff without having to check here
		if(!styleArray.contains(mRecipe.getStyle()))
			styleArray.add(mRecipe.getStyle());
		if(!profileArray.contains(mRecipe.getMashProfile()))
			profileArray.add(mRecipe.getMashProfile());
        
        // Set up style spinner here
        BeerStyleSpinnerAdapter styleAdapter = new BeerStyleSpinnerAdapter(this, styleArray);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        styleSpinner.setAdapter(styleAdapter);
        styleSpinner.setSelection(styleArray.indexOf(mRecipe.getStyle()));
		
		// Set up mash profile spinner
		MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		profileSpinner.setAdapter(profileAdapter);
        profileSpinner.setSelection(profileArray.indexOf(mRecipe.getMashProfile()));
        
        // Set up brew type spinner
        SpinnerAdapter typeAdapter = new SpinnerAdapter(this, typeArray, "Recipe Type");
        typeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(typeArray.indexOf(mRecipe.getType()));
        
        // Handle beer style selector here
        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                style = styleArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });

		// Handle mash profile selector here
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
				profile = profileArray.get(position);
			}

			public void onNothingSelected(AdapterView<?> parentView)
            {
			}

		});

        // Handle type selector here
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = typeArray.get(position);
                
                if(type.equals(Recipe.EXTRACT))
                	profileSpinner.setVisibility(View.GONE);
                else
					profileSpinner.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
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

	public void onClick(View v) {
		
		// Cancel Button Pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
		
		// Submit Button pressed
		if (v.getId() == R.id.submit_button)
		{
            Log.d("EditRecipe", "Submit button pressed");

			boolean readyToGo = true;
			String recipeName = "Unnamed Brew";
			Integer boilTime = 1;
			float batchSize = 5;
			float boilSize = 5;
            float measuredOg = 1;
            float measuredFg = 1;
			
			try {
			    recipeName = recipeNameViewText.getText().toString();
				boilTime = Integer.parseInt(timeViewText.getText().toString());
				efficiency = Float.parseFloat(efficiencyViewText.getText().toString());
				batchSize = Float.parseFloat(batchSizeViewText.getText().toString());
				boilSize = Float.parseFloat(boilSizeViewText.getText().toString());
                measuredOg = Float.parseFloat(measuredOGViewText.getText().toString());
                measuredFg = Float.parseFloat(measuredFGViewText.getText().toString());
			}
			catch (Exception e)
			{
                Log.d("EditRecipe", "Hit Exception: " + e.toString());
				readyToGo = false;
			}
			
			if (recipeName.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				efficiency = 100;
			
			if (readyToGo)
			{
				mRecipe.setRecipeName(recipeName);
				mRecipe.setVersion(Utils.getXmlVersion());
				mRecipe.setType(type);
				mRecipe.setStyle(style);
				mRecipe.setMashProfile(profile);
				mRecipe.setDisplayBatchSize(batchSize);
				mRecipe.setDisplayBoilSize(boilSize);
				mRecipe.setBoilTime(boilTime);
				mRecipe.setEfficiency(efficiency);
				mRecipe.setBatchTime(1);
                mRecipe.setMeasuredFG(measuredFg);
                mRecipe.setMeasuredOG(measuredOg);
				
				mRecipe.update();
				Database.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
