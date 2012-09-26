package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class EditGrainActivity extends Activity implements OnClickListener {
	
	private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private ArrayList<String> grainTypeArray = Utils.getFermentablesStringList();
	private Recipe mRecipe;
	private Grain grain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grain);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeID", 0);
        long grainId = getIntent().getLongExtra("com.biermacht.brews.grainID", 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        
        // Get the grain from the database
        grain = (Grain) Utils.getIngredientWithId(grainId);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        
        grainNameEditText.setText(grain.getName());
        grainColorEditText.setText(grain.getLovibondColor() +"");
        grainGravEditText.setText(grain.getGravity() +"");
        grainWeightEditText.setText(grain.getAmount() + "");
        
        
        // Set up grain type spinner
        grainTypeSpinner = (Spinner) findViewById(R.id.grain_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, grainTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        grainTypeSpinner.setAdapter(adapter);
        grainTypeSpinner.setSelection(0);
        grainTypeSpinner.setVisibility(View.GONE);
        
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
    
    @Override
    public void onBackPressed()
    {
	    Intent intent = new Intent(EditGrainActivity.this, DisplayRecipeActivity.class);
	    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
	    startActivity(intent);				
    }

	public void onClick(View v) {
		// If "EDIT" button pressed
		if (v.getId() == R.id.grain_submit_button)
		{
			String grainName = grainNameEditText.getText().toString();
			double color = Double.parseDouble(grainColorEditText.getText().toString());
			double grav = Double.parseDouble(grainGravEditText.getText().toString());
			double weight = Double.parseDouble(grainWeightEditText.getText().toString());
			
			grain.setName(grainName);
			grain.setLovibondColor(color);
			grain.setGravity(grav);
			grain.setWeight(weight);
			grain.setGrainType(Grain.GRAIN);
			grain.setUnit("lbs");
			grain.setEfficiency(1);
			
			Utils.updateIngredient(grain);
			mRecipe = Utils.getRecipeWithId(mRecipe.getId());
			mRecipe.update();
			Utils.updateRecipe(mRecipe);

		    Intent intent = new Intent(EditGrainActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);	
		}
		
		// If "DELETE" button pressed
		if (v.getId() == R.id.grain_delete_button)
		{
			Utils.deleteIngredient(grain);
			
		    Intent intent = new Intent(EditGrainActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);	
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
		    Intent intent = new Intent(EditGrainActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);	
		}
		
	}
}
