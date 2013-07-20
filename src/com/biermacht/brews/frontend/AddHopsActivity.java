package com.biermacht.brews.frontend;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biermacht.brews.frontend.adapters.*;
import android.view.*;
import com.biermacht.brews.utils.Units;
import android.widget.TextView;
import android.util.*;

public class AddHopsActivity extends Activity {
	
	IngredientHandler ingredientHandler;
	private Spinner hopSpinner;
	private Spinner hopTypeSpinner;
	private EditText hopNameEditText;
	private EditText hopBoilTimeEditText;
	private TextView hopTimeTitle;
	private EditText hopAcidEditText;
	private EditText hopWeightEditText;
	private ArrayList<Ingredient> hopArray;
	private ArrayList<String> hopTypeArray;
	private String hop;
	private String use;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hops);
		
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;
    	
    	// Set list of ingredients to show
    	hopArray = ingredientHandler.getHopsList();
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and such here
        hopNameEditText = (EditText) findViewById(R.id.hop_name_edit_text);
        hopBoilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        hopAcidEditText = (EditText) findViewById(R.id.hop_acid_edit_text);
        hopWeightEditText = (EditText) findViewById(R.id.hop_weight_edit_text);
        hopTimeTitle = (TextView) findViewById(R.id.boil_time_title);
		
        // Set up hop spinner
        hopSpinner = (Spinner) findViewById(R.id.hop_spinner);
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, hopArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        hopSpinner.setAdapter(adapter);
        hopSpinner.setSelection(0);    
		
		// Set up hop type spinner
       	hopTypeSpinner = (Spinner) findViewById(R.id.hop_type_spinner);
        hopTypeArray = new ArrayList<String>();
        hopTypeArray.add(Hop.USE_BOIL);
        hopTypeArray.add(Hop.USE_AROMA);
        hopTypeArray.add(Hop.USE_DRY_HOP);
        SpinnerAdapter hopTypeAdapter = new SpinnerAdapter(this, hopTypeArray);  
        hopTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        hopTypeSpinner.setAdapter(hopTypeAdapter);
        hopTypeSpinner.setSelection(0);
        
        // Handle hops selector here
        hopSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Hop hopObj = new Hop("");
            	hop = hopArray.get(position).getName();
                
                for (Ingredient i : ingredientHandler.getHopsList())
                {
                	if (hop.equals(i.toString()))
                		hopObj = (Hop) i;
                }
            	
                hopNameEditText.setText(hopObj.getName());
                hopBoilTimeEditText.setText(mRecipe.getBoilTime() + "");
                hopAcidEditText.setText(hopObj.getAlphaAcidContent() +"");
                hopWeightEditText.setText(1.0 +"");
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });   
		
		// Handle beer type selector here
        hopTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Set the type to the selected type...
				use = hopTypeArray.get(position);
				
				if (use.equals(Hop.USE_DRY_HOP))
				{
					hopTimeTitle.setText("Time (days)");
					hopBoilTimeEditText.setText(5 + "");
				}
				else
				{
					hopTimeTitle.setText("Time (mins)");
					hopBoilTimeEditText.setText(mRecipe.getBoilTime() + "");
				}
			}


			public void onNothingSelected(AdapterView<?> parentView) {
				// Blag
			}

		});
    }
	
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_hops, menu);
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
			boolean readyToGo = true;
			int endTime, startTime;
			String hopName="";
			double time=0, alpha=0, weight = 0;
			try
			{
				hopName = hopNameEditText.getText().toString();
				time = Integer.parseInt(hopBoilTimeEditText.getText().toString());
				alpha = Double.parseDouble(hopAcidEditText.getText().toString());
				weight = Double.parseDouble(hopWeightEditText.getText().toString());
			} catch (Exception e) {
				Log.d("AddHopsActivity", e.toString());
				readyToGo = false;
			}
		
		    if (!use.equals(Hop.USE_DRY_HOP))
			{
		        endTime = mRecipe.getBoilTime();
			    startTime = endTime - (int) time;
				
				if (time > mRecipe.getBoilTime())
					time = mRecipe.getBoilTime();
			}
			else
			{
				time = Units.daysToMinutes(time);
				startTime = mRecipe.getBoilTime();
				endTime = startTime + (int) time;
			}
						
			if (hopName.isEmpty())
				readyToGo = false;
			if (!(weight > 0))
				readyToGo = false;
			if (!(time > 0))
				readyToGo = false;
			
			if (readyToGo)
			{
				Hop h = new Hop(hopName);
				h.setDisplayTime((int) time);
				h.setStartTime(startTime);
				h.setEndTime(endTime);
				h.setAlphaAcidContent(alpha);
				h.setDisplayAmount(weight);
				h.setUse(use);
				h.setForm(Hop.FORM_PELLET);
				
				mRecipe.addIngredient(h);
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
