package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;
import android.view.*;

public class AddFermentableActivity extends Activity implements OnClickListener {

    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private OnClickListener onClickListener;

    // LayoutInflater
    LayoutInflater inflater;

    // Recipe we are editing
    private Recipe mRecipe;

    // IngredientHandler to get ingredient arrays
    IngredientHandler ingredientHandler;

    // Holds the currently selected fermentable
    Fermentable fermentable;

    // Editable rows to display
    private Spinner fermentableSpinner;
    private View nameView;
    private View colorView;
    private View gravityView;
    private View amountView;
    private View timeView;

    // Titles from rows
    private TextView nameViewTitle;
    private TextView colorViewTitle;
    private TextView gravityViewTitle;
    private TextView amountViewTitle;
    private TextView timeViewTitle;

    // Content from rows
    private TextView nameViewText;
    private TextView colorViewText;
    private TextView gravityViewText;
    private TextView amountViewText;
    private TextView timeViewText;

    // Spinner array declarations
    private ArrayList<Ingredient> fermentablesArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
		
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Get the Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;

    	// Get the list of ingredients to show
    	fermentablesArray = ingredientHandler.getFermentablesList();

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);
        mRecipe = Utils.getRecipeWithId(id);

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(nameView))
                    alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
                else if (v.equals(colorView))
                    alert = alertBuilder.editTextFloatAlert(colorViewText, colorViewTitle).create();
                else if (v.equals(amountView))
                    alert = alertBuilder.editTextFloatAlert(amountViewText, amountViewTitle).create();
                else if (v.equals(timeView))
                    alert = alertBuilder.editTextIntegerAlert(timeViewText, timeViewTitle).create();
                else if (v.equals(gravityView))
                    alert = alertBuilder.editTextFloatAlert(gravityViewText, gravityViewTitle).create();
                else
                    return; // In case its none of those views...

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };
        
        // Initialize views and such here
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        nameView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        colorView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        gravityView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        amountView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        timeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        fermentableSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        nameView.setOnClickListener(onClickListener);
        colorView.setOnClickListener(onClickListener);
        gravityView.setOnClickListener(onClickListener);
        amountView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(fermentableSpinner);
        mainView.addView(nameView);
        mainView.addView(timeView);
        mainView.addView(amountView);
        mainView.addView(colorView);
        mainView.addView(gravityView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Time (mins)");

        amountViewTitle = (TextView) amountView.findViewById(R.id.title);
        amountViewTitle.setText("Amount (lbs)");

        colorViewTitle = (TextView) colorView.findViewById(R.id.title);
        colorViewTitle.setText("SRM Color");

        gravityViewTitle = (TextView) gravityView.findViewById(R.id.title);
        gravityViewTitle.setText("Gravity Contribution");

        /************************************************************************
         ************* Get content views, values set below  **********************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        timeViewText = (TextView) timeView.findViewById(R.id.text);
        amountViewText = (TextView) amountView.findViewById(R.id.text);
        colorViewText = (TextView) colorView.findViewById(R.id.text);
        gravityViewText = (TextView) gravityView.findViewById(R.id.text);

        // Set up type spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, fermentablesArray, "Fermentable");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fermentableSpinner.setAdapter(adapter);
        fermentableSpinner.setSelection(0);
        
        // Handle type spinner selections here
        fermentableSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	fermentable = (Fermentable) fermentablesArray.get(position);

                // Set weather we show boil or steep
				if (mRecipe.getType().equals(Recipe.EXTRACT))
				{	
					if (fermentable.getFermentableType().equals(Fermentable.TYPE_EXTRACT))
					{
						timeViewTitle.setText(R.string.boil_time);
						timeViewText.setText(mRecipe.getBoilTime() + "");
					}
					else if (fermentable.getFermentableType().equals(Fermentable.TYPE_GRAIN))
					{
                        timeViewTitle.setText(R.string.steep_time);
                        timeViewText.setText(15 + "");
					}
					else
					{
                        timeViewTitle.setText("Time");
					}
            	}
				else 
				{
                    // TODO: Do we ever want to enter a time for mashes?
					timeView.setVisibility(View.GONE);
				}
				
                nameViewText.setText(fermentable.getName());
                colorViewText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
                gravityViewText.setText(String.format("%2.3f", fermentable.getGravity()));
                amountViewText.setText(1 +"");
                timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });   
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

	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
            boolean readyToGo = true;
            int time, endTime, startTime;
            double color, grav, amount;
            String name;

            try
            {
                name = nameViewText.getText().toString();
                color = Double.parseDouble(colorViewText.getText().toString());
                grav = Double.parseDouble(gravityViewText.getText().toString());
                amount = Double.parseDouble(amountViewText.getText().toString());
                time = Integer.parseInt(timeViewText.getText().toString());
                endTime = mRecipe.getBoilTime();
                startTime = endTime - time;

                if (startTime > mRecipe.getBoilTime())
                    startTime = mRecipe.getBoilTime();

                fermentable.setName(name);
                fermentable.setLovibondColor(color);
                fermentable.setGravity(grav);
                fermentable.setDisplayAmount(amount);
                fermentable.setStartTime(startTime);
                fermentable.setEndTime(endTime);
            }
            catch (Exception e) {
                Log.d("AddFermentable", "Hit exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                mRecipe.addIngredient(fermentable);
                mRecipe.update();
                Utils.updateRecipe(mRecipe);

                finish();
            }
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
