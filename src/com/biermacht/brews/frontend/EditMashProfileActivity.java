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
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;
import android.widget.*;

public class EditMashProfileActivity extends Activity implements OnClickListener {

	// Data entry view declarations
	private Spinner mashProfileSpinner;
	private EditText nameEditText;
	private EditText effEditText;
	
	// Data storage declarations
	private double efficiency;
	private String name;

	// Recipe
	private Recipe mRecipe;
	private MashProfile mashProfile;
	
	// Arrays
	private ArrayList<MashProfile> mashProfileArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        mRecipe = Utils.getRecipeWithId(id);
		mashProfile = mRecipe.getMashProfile();
		
		// Initialize data containers
		name = mashProfile.getName();
		efficiency = mRecipe.getEfficiency();

        // Initialize views and stuff
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        effEditText = (EditText) findViewById(R.id.efficiency_edit_text);
     
        // Default values
        effEditText.setText(String.format("%2.2f", mRecipe.getEfficiency()));
     	nameEditText.setText(mashProfile.getName());
		
        //Arraylist of Profiles
        mashProfileArray = MainActivity.ingredientHandler.getMashProfileList();

		// If it doesn't contain the current recipes profile,
		// then it is custom and we add it to the list.
		if(!mashProfileArray.contains(mRecipe.getMashProfile()))
			mashProfileArray.add(mRecipe.getMashProfile());

		// Set up mash profile spinner
		mashProfileSpinner = (Spinner) findViewById(R.id.mash_profile_spinner);
		MashProfileSpinnerAdapter<MashProfile> profAdapter = new MashProfileSpinnerAdapter<MashProfile>(this, mashProfileArray);
        profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		mashProfileSpinner.setAdapter(profAdapter);

		// Determine the correct selection for the mash profile spinner
        int pos = 0;
        for (MashProfile p : mashProfileArray)
        {
        	if (p.equals(mRecipe.getMashProfile()))
        		break;
        	pos++;
        }
		mashProfileSpinner.setSelection(pos);

		// Handle mash profile selector here
        mashProfileSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					mashProfile = mashProfileArray.get(position);
					nameEditText.setText(mashProfile.getName());
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
			try {
				efficiency = Float.parseFloat(effEditText.getText().toString());
			    name = nameEditText.getText().toString();
			}
			catch (Exception e)
			{
				readyToGo = false;
			}

			if (name.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				readyToGo = false;

			if (readyToGo)
			{
				mashProfile.setName(name);
				
				mRecipe.setEfficiency(efficiency);
				mRecipe.setMashProfile(mashProfile);
				
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
