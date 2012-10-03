package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class AddNewRecipeActivity extends Activity implements OnClickListener {

	private Spinner beerTypeSpinner;
	private EditText recipeNameEditText;
	private EditText recipeDescEditText;
	private EditText boilTimeEditText;
	private EditText effEditText;
	private EditText batchSizeEditText;
	private String beerType = Utils.BEERTYPE_OTHER.toString();
	private ArrayList<String> beerTypeArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);
        
        // Initialize views and stuff
        recipeNameEditText = (EditText) findViewById(R.id.recipe_name_edit_text);
        recipeDescEditText = (EditText) findViewById(R.id.recipe_description_edit_text);
        boilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        effEditText = (EditText) findViewById(R.id.efficiency_edit_text);
        batchSizeEditText = (EditText) findViewById(R.id.batch_volume_edit_text);
        
        // Default values
        boilTimeEditText.setText(60 +"");
        effEditText.setText(100 +"");
        batchSizeEditText.setText(5.0 +"");
        
        //Arraylist of beer types
        beerTypeArray = Utils.getBeerStyleStringList();
        
        // Set up beer type spinner
        beerTypeSpinner = (Spinner) findViewById(R.id.beer_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, beerTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerTypeSpinner.setAdapter(adapter);
        beerTypeSpinner.setSelection(0);    
        
        // Handle beer type selector here
        beerTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                beerType = beerTypeArray.get(position);
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
		if (v.getId() == R.id.new_recipe_submit_button)
		{
			String recipeName = recipeNameEditText.getText().toString();
			String recipeDesc = recipeDescEditText.getText().toString();
			int boilTime = Integer.parseInt(boilTimeEditText.getText().toString());
			float eff = Float.parseFloat(effEditText.getText().toString());
			float batchSize = Float.parseFloat(batchSizeEditText.getText().toString());
			Recipe r = Utils.createRecipeWithName(recipeName);
			
			if (!recipeDesc.isEmpty())
				r.setDescription(recipeDesc);
			
			if (!recipeName.isEmpty())
			{
				r.setBeerType(beerType);
				r.setBoilTime(boilTime);
				r.setEfficiency(eff);
				r.setVolume(batchSize);
				Utils.updateRecipe(r);
				finish();
			}
		}
	}
}
