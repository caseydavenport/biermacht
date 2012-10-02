package com.biermacht.brews.frontend;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.R.layout;
import com.biermacht.brews.R.menu;
import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Hop;
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddHopsActivity extends Activity {
	
	private Spinner hopSpinner;
	private EditText hopNameEditText;
	private EditText hopBoilTimeEditText;
	private EditText hopAcidEditText;
	private EditText hopWeightEditText;
	private ArrayList<String> hopTypeArray = Utils.getHopsStringList();
	private String hopType;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hops);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and such here
        hopNameEditText = (EditText) findViewById(R.id.hop_name_edit_text);
        hopBoilTimeEditText = (EditText) findViewById(R.id.hop_boil_time_edit_text);
        hopAcidEditText = (EditText) findViewById(R.id.hop_acid_edit_text);
        hopWeightEditText = (EditText) findViewById(R.id.hop_weight_edit_text);
        
        // Set up grain type spinner
        hopSpinner = (Spinner) findViewById(R.id.hop_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, hopTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        hopSpinner.setAdapter(adapter);
        hopSpinner.setSelection(0);    
        
        // Handle beer type selector here
        hopSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Hop hopObj = new Hop("");
            	hopType = hopTypeArray.get(position);
                
                for (Ingredient i : Utils.getHopsList())
                {
                	if (hopType.equals(i.toString()))
                		hopObj = (Hop) i;
                }
            	
                hopNameEditText.setText(hopObj.getName());
                hopBoilTimeEditText.setText(hopObj.getBoilStartTime() +"");
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
    
    @Override
    public void onBackPressed()
    {
	    Intent intent = new Intent(AddHopsActivity.this, DisplayRecipeActivity.class);
	    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
	    startActivity(intent);				
    }
    
	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.new_grain_submit_button)
		{
			String hopName = hopNameEditText.getText().toString();
			double boilTime = Double.parseDouble(hopBoilTimeEditText.getText().toString());
			double aAcid = Double.parseDouble(hopAcidEditText.getText().toString());
			double weight = Double.parseDouble(hopWeightEditText.getText().toString());
			
			Hop h = new Hop(hopName);
			h.setBoilStartTime(boilTime);
			h.setAlphaAcidContent(aAcid);
			h.setWeight(weight);
			h.setHopType(Hop.TYPE_PELLET);
			h.setUnit("oz");
			
			
			
			mRecipe.addIngredient(h);
			mRecipe.update();
			Utils.updateRecipe(mRecipe);
			
		    Intent intent = new Intent(AddHopsActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);		
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
		    Intent intent = new Intent(AddHopsActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);	
		}
	}
}
