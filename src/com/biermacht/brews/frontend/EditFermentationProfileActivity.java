package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;

public class EditFermentationProfileActivity extends Activity implements OnClickListener {

	// Main Layout
	private LinearLayout mainLayout;
	private LayoutInflater inflater;
	private LinearLayout primaryLayout;
	private LinearLayout secondaryLayout;
	private LinearLayout tertiaryLayout;
	
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
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        mRecipe = Utils.getRecipeWithId(id);

        // Initialize views and stuff
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		primaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage, mainLayout, false);
		secondaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage, mainLayout, false);
		tertiaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage, mainLayout, false);
		
		// Set titles
		TextView title;
		title = (TextView) primaryLayout.findViewById(R.id.title);
		title.setText("Primary:");
		title = (TextView) secondaryLayout.findViewById(R.id.title);
		title.setText("Secondary:");
		title = (TextView) tertiaryLayout.findViewById(R.id.title);
		title.setText("Tertiary:");

        // Add sublayouts
        mainLayout.addView(inflater.inflate(R.layout.divider, mainLayout, false));
        mainLayout.addView(primaryLayout);
        mainLayout.addView(inflater.inflate(R.layout.divider, mainLayout, false));
        mainLayout.addView(secondaryLayout);
        mainLayout.addView(inflater.inflate(R.layout.divider, mainLayout, false));
        mainLayout.addView(tertiaryLayout);
        mainLayout.addView(inflater.inflate(R.layout.divider, mainLayout, false));
     
        // Default values
        View v = new View(this);
        TextView ageView;
        TextView tempView;
        for (int i = Recipe.STAGE_PRIMARY; i <= Recipe.STAGE_TERTIARY; i++)
        {
        	if (i == Recipe.STAGE_PRIMARY)
        		v = primaryLayout;
        	if (i == Recipe.STAGE_SECONDARY)
        		v = secondaryLayout;
        	if (i == Recipe.STAGE_TERTIARY)
        		v = tertiaryLayout;
        	
        	ageView = (TextView) v.findViewById(R.id.age_edit_text);
        	tempView = (TextView) v.findViewById(R.id.temp_edit_text);
        	ageView.setText(mRecipe.getFermentationAge(i) + "");
        	tempView.setText(String.format("%2.0f", mRecipe.getDisplayFermentationTemp(i)));
        }
		
        //Arraylist of Profiles
        numStagesArray = new ArrayList<String>();
        numStagesArray.add("1");
        numStagesArray.add("2");
        numStagesArray.add("3");

		// Set up num stages spinner
        numStagesSpinner = (Spinner) findViewById(R.id.num_stages_spinner);
		SpinnerAdapter profAdapter = new SpinnerAdapter(this, numStagesArray, "Num Stages");
        profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        numStagesSpinner.setAdapter(profAdapter);

		// Determine the correct selection for the num stages spinner
        numStagesSpinner.setSelection(mRecipe.getFermentationStages() -1);

		// Handle mash profile selector here
        numStagesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					
					// Set number of stages
					mRecipe.setFermentationStages(position + 1);
					
					// Figure out visibility
					secondaryLayout.setVisibility(View.VISIBLE);
					tertiaryLayout.setVisibility(View.VISIBLE);
					
					if (position == 0)
					// Only one stage
					{
						secondaryLayout.setVisibility(View.GONE);
						tertiaryLayout.setVisibility(View.GONE);
					}
					else if (position == 1)
					// Two stages
					{
						tertiaryLayout.setVisibility(View.GONE);
					}
					
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
		        View view = new View(this);
		        TextView ageView;
		        TextView tempView;
		        for (int i = Recipe.STAGE_PRIMARY; i <= mRecipe.getFermentationStages(); i++)
		        {
		        	if (i == Recipe.STAGE_PRIMARY)
		        		view = primaryLayout;
		        	if (i == Recipe.STAGE_SECONDARY)
		        		view = secondaryLayout;
		        	if (i == Recipe.STAGE_TERTIARY)
		        		view = tertiaryLayout;
		        	
		        	ageView = (TextView) view.findViewById(R.id.age_edit_text);
		        	tempView = (TextView) view.findViewById(R.id.temp_edit_text);
		        	mRecipe.setFermentationAge(i, Integer.parseInt(ageView.getText().toString()));
		        	mRecipe.setDisplayFermentationTemp(i, Double.parseDouble(tempView.getText().toString()));
		        }
			}
			catch (Exception e)
			{
				readyToGo = false;
			}

			if (readyToGo)
			{	
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
