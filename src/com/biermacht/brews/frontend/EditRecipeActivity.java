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

public class EditRecipeActivity extends Activity implements OnClickListener {

	// Data entry view declarations
	private Spinner beerStyleSpinner;
	private Spinner beerTypeSpinner;
	private EditText recipeNameEditText;
	private EditText recipeDescEditText;
	private EditText boilTimeEditText;
	private EditText effEditText;
	private EditText batchSizeEditText;
	private EditText boilSizeEditText;
	
	// Data storage declarations
	private String style = Utils.BEERSTYLE_OTHER.toString();
	private String type = Recipe.EXTRACT;
	private float efficiency = 100;
	
	// Recipe we are editing
	private Recipe mRecipe;
	
	// Spinner array declarations
	private ArrayList<String> beerStyleArray;
	private ArrayList<String> beerTypeArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("biermacht.brews.recipeID", 0);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and stuff
        recipeNameEditText = (EditText) findViewById(R.id.recipe_name_edit_text);
        recipeDescEditText = (EditText) findViewById(R.id.recipe_description_edit_text);
        boilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        effEditText = (EditText) findViewById(R.id.efficiency_edit_text);
        batchSizeEditText = (EditText) findViewById(R.id.batch_volume_edit_text);
        boilSizeEditText = (EditText) findViewById(R.id.boil_volume_edit_text);
        
        // Default values
        recipeNameEditText.setText(mRecipe.getRecipeName());
        recipeDescEditText.setText(mRecipe.getDescription());
        boilTimeEditText.setText(String.format("%d", mRecipe.getBoilTime()));
        effEditText.setText(String.format("%2.2f", mRecipe.getEfficiency()));
        batchSizeEditText.setText(String.format("%2.2f", mRecipe.getBatchSize()));
        boilSizeEditText.setText(String.format("%2.2f", mRecipe.getBoilSize()));
        
        //Arraylist of beer types
        beerStyleArray = Utils.getBeerStyleStringList();
        
        // Set up beer type spinner
        beerStyleSpinner = (Spinner) findViewById(R.id.beer_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, beerStyleArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerStyleSpinner.setAdapter(adapter);
        
        
        // Determine the correct selection for the style spinner
        int pos = 0;
        for (String s : beerStyleArray)
        {
        	if (s.equals(mRecipe.getStyle()))
        		break;
        	pos++;
        }
        
        beerStyleSpinner.setSelection(pos);    
        
        // Set up brew type spinner
        beerTypeSpinner = (Spinner) findViewById(R.id.brew_type_spinner);
        beerTypeArray = new ArrayList<String>();
        beerTypeArray.add("Extract");
        beerTypeArray.add("Partial Mash");
        beerTypeArray.add("All Grain");
        SpinnerAdapter<String> beerTypeAdapter = new SpinnerAdapter<String>(this, beerTypeArray);  
        beerTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerTypeSpinner.setAdapter(beerTypeAdapter);

        
        // Determine the correct selection for the brew type spinner
        pos = 0;
        for (String s : beerTypeArray)
        {
        	if (s.equals(mRecipe.getType()))
        		break;
        	pos++;
        }
        
        beerTypeSpinner.setSelection(pos); 
        
        // Handle beer style selector here
        beerStyleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                style = beerStyleArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Blag
            }

        });
        
        // Handle beer type selector here
        beerTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = beerTypeArray.get(position);
                
                if(type.equals(Recipe.EXTRACT))
                {
                	// Eventually add conditional stuff for recipe type
                }
                else
                {
					
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
			String recipeName = "Unnamed Brew";
			String description = "";
			Integer boilTime = 1;
			float batchSize = 5;
			float boilSize = 5;
			
			try {
			    recipeName = recipeNameEditText.getText().toString();
			    description = recipeDescEditText.getText().toString();
				boilTime = Integer.parseInt(boilTimeEditText.getText().toString());
				efficiency = Float.parseFloat(effEditText.getText().toString());
				batchSize = Float.parseFloat(batchSizeEditText.getText().toString());
				boilSize = Float.parseFloat(boilSizeEditText.getText().toString());
			}
			catch (Exception e)
			{
				readyToGo = false;
			}
			
			if (recipeName.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				readyToGo = false;
			
			if (readyToGo)
			{
				mRecipe.setRecipeName(recipeName);
				mRecipe.setVersion(Utils.getXmlVersion());
				mRecipe.setType(type);
				mRecipe.setStyle(style);
				mRecipe.setBrewer("Biermacht Brews");
				mRecipe.setBatchSize(batchSize);
				mRecipe.setBoilSize(boilSize);
				mRecipe.setBoilTime(boilTime);
				mRecipe.setEfficiency(efficiency);
				mRecipe.setBatchTime(1);
				mRecipe.setDescription(description);
				
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
