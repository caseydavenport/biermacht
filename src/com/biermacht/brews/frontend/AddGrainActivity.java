package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class AddGrainActivity extends Activity implements OnClickListener {
	
	private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private EditText grainBoilStartTimeEditText;
	private ArrayList<String> grainTypeArray = Utils.getFermentablesStringList();
	private String grainType;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grain);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        Log.e("AddGrain", "ID: " + id);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        grainBoilStartTimeEditText = (EditText) findViewById(R.id.start_time_edit_text);
        
        // Set up grain type spinner
        grainTypeSpinner = (Spinner) findViewById(R.id.grain_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, grainTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        grainTypeSpinner.setAdapter(adapter);
        grainTypeSpinner.setSelection(0);    
        
        // Handle beer type selector here
        grainTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Fermentable grainObj = new Fermentable("");
            	grainType = grainTypeArray.get(position);
                
                for (Ingredient i : Utils.getFermentablesList())
                {
                	if (grainType.equals(i.toString()))
                		grainObj = (Fermentable) i;
                }
            	
                grainNameEditText.setText(grainType);
                grainColorEditText.setText(grainObj.getLovibondColor() +"");
                grainGravEditText.setText(grainObj.getGravity() +"");
                grainWeightEditText.setText(5 +"");
                grainBoilStartTimeEditText.setText(0 + "");
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }
    
    @Override
    public void onBackPressed()
    {
	    Intent intent = new Intent(AddGrainActivity.this, DisplayRecipeActivity.class);
	    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
	    startActivity(intent);				
    }

	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.new_grain_submit_button)
		{
			String grainName = grainNameEditText.getText().toString();
			double color = Double.parseDouble(grainColorEditText.getText().toString());
			double grav = Double.parseDouble(grainGravEditText.getText().toString());
			double weight = Double.parseDouble(grainWeightEditText.getText().toString());
			int startTime = Integer.parseInt(grainBoilStartTimeEditText.getText().toString());
			int endTime = mRecipe.getBoilTime();
			
			if (startTime > mRecipe.getBoilTime())
				startTime = mRecipe.getBoilTime();
			
			Fermentable g = new Fermentable(grainName);
			g.setLovibondColor(color);
			g.setGravity(grav);
			g.setAmount(weight);
			g.setFermentableType(Fermentable.GRAIN);
			g.setEfficiency(1);
			g.setStartTime(startTime);
			g.setEndTime(endTime);
			
			mRecipe.addIngredient(g);
			mRecipe.update();
			Utils.updateRecipe(mRecipe);
			
		    Intent intent = new Intent(AddGrainActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);		
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
		    Intent intent = new Intent(AddGrainActivity.this, DisplayRecipeActivity.class);
		    intent.putExtra("biermacht.brews.recipeID", mRecipe.getId());
		    startActivity(intent);	
		}
	}
}
