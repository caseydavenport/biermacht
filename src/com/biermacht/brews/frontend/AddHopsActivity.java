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
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddHopsActivity extends Activity {
	
	IngredientHandler ingredientHandler;
	private Spinner hopSpinner;
	private EditText hopNameEditText;
	private EditText hopBoilTimeEditText;
	private EditText hopAcidEditText;
	private EditText hopWeightEditText;
	private ArrayList<Ingredient> hopTypeArray;
	private String hopType;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hops);
        
    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;
    	
    	// Set list of ingredients to show
    	hopTypeArray = ingredientHandler.getHopsList();
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and such here
        hopNameEditText = (EditText) findViewById(R.id.hop_name_edit_text);
        hopBoilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        hopAcidEditText = (EditText) findViewById(R.id.hop_acid_edit_text);
        hopWeightEditText = (EditText) findViewById(R.id.hop_weight_edit_text);
        
        // Set up hop type spinner
        hopSpinner = (Spinner) findViewById(R.id.hop_type_spinner);
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, hopTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        hopSpinner.setAdapter(adapter);
        hopSpinner.setSelection(0);    
        
        // Handle beer type selector here
        hopSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Hop hopObj = new Hop("");
            	hopType = hopTypeArray.get(position).getName();
                
                for (Ingredient i : ingredientHandler.getHopsList())
                {
                	if (hopType.equals(i.toString()))
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_hops, menu);
        return true;
    }
    
	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.new_grain_submit_button)
		{
			String hopName = hopNameEditText.getText().toString();
			int time = Integer.parseInt(hopBoilTimeEditText.getText().toString());
			int endTime = mRecipe.getBoilTime();
			int startTime = endTime - time;
			double alpha = Double.parseDouble(hopAcidEditText.getText().toString());
			double weight = Double.parseDouble(hopWeightEditText.getText().toString());
			
			if (time > mRecipe.getBoilTime())
				time = mRecipe.getBoilTime();
			
			Hop h = new Hop(hopName);
			h.setTime(time);
			h.setStartTime(startTime);
			h.setEndTime(endTime);
			h.setAlphaAcidContent(alpha);
			h.setAmount(weight);
			h.setForm(Hop.FORM_PELLET);
			
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
