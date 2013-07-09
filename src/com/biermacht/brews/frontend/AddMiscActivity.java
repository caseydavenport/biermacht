package com.biermacht.brews.frontend;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biermacht.brews.frontend.adapters.*;
import android.util.*;
import android.view.*;

public class AddMiscActivity extends Activity {

	IngredientHandler ingredientHandler;
	private Spinner miscSpinner;
	private Spinner miscTypeSpinner;
	private EditText timeEditText;
	private EditText amountEditText;
	private ArrayList<Ingredient> miscArray;
	private ArrayList<String> miscTypeArray;
	private String misc;
	private Misc miscObj;
	private String use;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_misc);

		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;

    	// Set list of ingredients to show
    	miscArray = ingredientHandler.getMiscsList();

        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);

        // Initialize views and such here
        timeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);

        // Set up spinner
        miscSpinner = (Spinner) findViewById(R.id.misc_spinner);
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, miscArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        miscSpinner.setAdapter(adapter);
        miscSpinner.setSelection(0);    

		// Set up type spinner
       	miscTypeSpinner = (Spinner) findViewById(R.id.misc_type_spinner);
        miscTypeArray = new ArrayList<String>();
        miscTypeArray.add(Misc.TYPE_FINING);
        miscTypeArray.add(Misc.TYPE_FLAVOR);
        miscTypeArray.add(Misc.TYPE_HERB);
		miscTypeArray.add(Misc.TYPE_SPICE);
		miscTypeArray.add(Misc.TYPE_WATER_AGENT);
		miscTypeArray.add(Misc.TYPE_OTHER);
		
        SpinnerAdapter<String> miscTypeAdapter = new SpinnerAdapter<String>(this, miscTypeArray);  
        miscTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        miscTypeSpinner.setAdapter(miscTypeAdapter);
        miscTypeSpinner.setSelection(0);

        // Handle misc selector here
        miscSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					miscObj = (Misc) miscArray.get(position);
					misc = miscArray.get(position).getName();

					timeEditText.setText(mRecipe.getBoilTime() + "");
					amountEditText.setText(miscObj.getDisplayAmount() + "");
					Log.i("AddMiscActivity", miscObj.getMiscType() + " is the misc type");
					Log.i("AddMiscActivity", miscTypeArray.indexOf(miscObj.getMiscType()) + " is the index");
					miscTypeSpinner.setSelection(miscTypeArray.indexOf(miscObj.getMiscType()));
				}

				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});   

		// Handle beer type selector here
        miscTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					// Set the type to the selected type...
					use = miscTypeArray.get(position);
				}


				public void onNothingSelected(AdapterView<?> parentView) {
					// Blag
				}

			});
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_add_miscs, menu);
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
		if (v.getId() == R.id.new_grain_submit_button)
		{
			String name = miscObj.getName();
			int time = Integer.parseInt(timeEditText.getText().toString());
			int endTime = mRecipe.getBoilTime();
			int startTime = endTime - time;
			double amount = Double.parseDouble(amountEditText.getText().toString());

			if (time > mRecipe.getBoilTime())
				time = mRecipe.getBoilTime();

			Misc h = new Misc(name);
			h.setTime(time);
			h.setStartTime(startTime);
			h.setEndTime(endTime);
			h.setDisplayAmount(amount);
			h.setUse(use);

			mRecipe.addIngredient(h);
			mRecipe.update();
			Utils.updateRecipe(mRecipe);

			finish();
		}

		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
