package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;

public class EditFermentationProfileActivity extends Activity implements OnClickListener {

	// Data entry view declarations
	private Spinner numStagesSpinner;

	// Recipe
	private Recipe mRecipe;
	
	// Arrays
	private ArrayList<String> numStagesArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fermentation_profile);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        mRecipe = Utils.getRecipeWithId(id);

        // Initialize views and stuff
     
        // Default values

		
        //Arraylist of Profiles
        numStagesArray = new ArrayList<String>();
        numStagesArray.add("1");
        numStagesArray.add("2");
        numStagesArray.add("3");

		// Set up num stages spinner
        numStagesSpinner = (Spinner) findViewById(R.id.num_stages_spinner);
		SpinnerAdapter profAdapter = new SpinnerAdapter(this, numStagesArray);
        profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        numStagesSpinner.setAdapter(profAdapter);

		// Determine the correct selection for the num stages spinner
        numStagesSpinner.setSelection(mRecipe.getFermentationStages() -1);

		// Handle mash profile selector here
        numStagesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				}

				public void onNothingSelected(AdapterView<?> parentView) {
					// Blag
				}

			});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
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
			boolean readyToGo = true;	
			try 
			{
				// Get all the values from edit texts
			}
			catch (Exception e)
			{
				readyToGo = false;
			}

			if (false)
				readyToGo = false;
			if (false)
				readyToGo = false;

			if (readyToGo)
			{	
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
