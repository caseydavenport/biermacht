package com.biermacht.brews.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class EditHopActivity extends Activity implements OnClickListener {
	
	// private Spinner grainTypeSpinner;
	private EditText hopNameEditText;
	private EditText hopAcidsEditText;
	private EditText hopBoilTimeEditText;
	private EditText hopWeightEditText;
	// private ArrayList<String> grainTypeArray = Utils.getFermentablesStringList();
	private Recipe mRecipe;
	private Hop hop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hop);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeID", 0);
        long hopId = getIntent().getLongExtra("com.biermacht.brews.grainID", 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        
        // Get the grain from the database
        hop = (Hop) Utils.getIngredientWithId(hopId);
        
        // Initialize views and such here
        hopNameEditText = (EditText) findViewById(R.id.hop_name_edit_text);
        hopAcidsEditText = (EditText) findViewById(R.id.hop_acid_edit_text);
        hopBoilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        hopWeightEditText = (EditText) findViewById(R.id.hop_weight_edit_text);
        
        hopNameEditText.setText(hop.getName());
        hopAcidsEditText.setText(hop.getAlphaAcidContent() +"");
        hopBoilTimeEditText.setText(hop.getTime() +"");
        hopWeightEditText.setText(hop.getAmount() + "");
        
        
        /*
        // Set up hop type spinner
        grainTypeSpinner = (Spinner) findViewById(R.id.grain_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, grainTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        grainTypeSpinner.setAdapter(adapter);
        grainTypeSpinner.setSelection(0);
        grainTypeSpinner.setVisibility(View.GONE);
        */
        
        /*
        // Handle beer type selector here
        grainTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	
                grainNameEditText.setText(grain.getName());
                grainColorEditText.setText(grain.getLovibondColor() +"");
                grainGravEditText.setText(grain.getGravity() +"");
                grainWeightEditText.setText(grain.getAmount() + "");
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            	
            }

        });  
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }
   
    public void onResume()
    {
    	super.onResume();
    }

	public void onClick(View v) {
		// If "EDIT" button pressed
		if (v.getId() == R.id.submit_button)
		{	
			String name = hopNameEditText.getText().toString();
			double acids = Double.parseDouble(hopAcidsEditText.getText().toString());
			int boilTime = Integer.parseInt(hopBoilTimeEditText.getText().toString());
			int boilStartTime = hop.getEndTime() - boilTime;
			double weight = Double.parseDouble(hopWeightEditText.getText().toString());
			
			if (boilTime > mRecipe.getBoilTime())
				boilTime = mRecipe.getBoilTime();
			
			hop.setName(name);
			hop.setAlphaAcidContent(acids);
			hop.setStartTime(boilStartTime);
			hop.setTime(boilTime);
			hop.setAmount(weight);
			
			Utils.updateIngredient(hop);
			mRecipe = Utils.getRecipeWithId(mRecipe.getId());
			mRecipe.update();
			Utils.updateRecipe(mRecipe);
			
			finish();
		}
		
		// If "DELETE" button pressed
		if (v.getId() == R.id.delete_button)
		{
			Utils.deleteIngredient(hop);
			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
		
	}
}
