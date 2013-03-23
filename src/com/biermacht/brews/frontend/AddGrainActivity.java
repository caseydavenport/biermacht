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
import android.widget.TextView;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;
import android.view.*;

public class AddGrainActivity extends Activity implements OnClickListener {
	
	IngredientHandler ingredientHandler;
	
	private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private EditText grainTimeEditText;
	private TextView grainTypeTextView;
	private TextView boilSteepTextView;
	private ArrayList<String> grainTypeArray;
	private String grainType;
	private Recipe mRecipe;
	Fermentable fermentable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grain);
		
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;
    	
    	// Set list of ingredients to show
    	grainTypeArray = Utils.getIngredientStringList(ingredientHandler.getFermentablesList());
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        grainTimeEditText = (EditText) findViewById(R.id.time_edit_text);
        grainTypeTextView = (TextView) findViewById(R.id.fermentable_type_view);
		boilSteepTextView = (TextView) findViewById(R.id.time_title);	
		
        // Set up grain type spinner
        grainTypeSpinner = (Spinner) findViewById(R.id.grain_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, grainTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        grainTypeSpinner.setAdapter(adapter);
        grainTypeSpinner.setSelection(0);    
        
        // Handle beer type selector here
        grainTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	fermentable = new Fermentable("");
            	grainType = grainTypeArray.get(position);
                
                for (Ingredient i : ingredientHandler.getFermentablesList())
                {
                	if (grainType.equals(i.toString()))
                		fermentable = (Fermentable) i;
                }
                
                // Set whether we want to show name field
                if (fermentable.getName().equals("Custom Fermentable"))
                {
                	grainNameEditText.setVisibility(View.VISIBLE);
                	findViewById(R.id.grain_name_title).setVisibility(View.VISIBLE);
                }
                else
                {
                	grainNameEditText.setVisibility(View.GONE);
                	findViewById(R.id.grain_name_title).setVisibility(View.GONE);
                }
                // Set weather we show boil or steep
				if (mRecipe.getType().equals(Recipe.EXTRACT))
				{	
					if (fermentable.getFermentableType().equals(Fermentable.EXTRACT))
					{
						boilSteepTextView.setText(R.string.boil_time);
						grainTimeEditText.setText(mRecipe.getBoilTime() + "");
					}
					else if (fermentable.getFermentableType().equals(Fermentable.GRAIN))
					{
						boilSteepTextView.setText(R.string.steep_time);
						grainTimeEditText.setText(15 + "");
					}
					else
					{
						boilSteepTextView.setText("Time");
					}
            	}
				else 
				{
					// TODO: Partial Mash / All Grain
				}
				
                grainNameEditText.setText(grainType);
                grainColorEditText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
                grainGravEditText.setText(String.format("%2.3f", fermentable.getGravity()));
                grainWeightEditText.setText(1 +"");
                grainTimeEditText.setText(String.format("%d", mRecipe.getBoilTime()));
				grainTypeTextView.setText(fermentable.getFermentableType());
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // This is never reached?
            }

        });   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
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
			String grainName = grainNameEditText.getText().toString();
			double color = Double.parseDouble(grainColorEditText.getText().toString());
			double grav = Double.parseDouble(grainGravEditText.getText().toString());
			double weight = Double.parseDouble(grainWeightEditText.getText().toString());
			int cookTime = Integer.parseInt(grainTimeEditText.getText().toString());
			int endTime = mRecipe.getBoilTime();
			int startTime = endTime - cookTime;
			String fermType = grainTypeTextView.getText().toString();
			
			if (startTime > mRecipe.getBoilTime())
				startTime = mRecipe.getBoilTime();
			
			Fermentable g = fermentable;
			g.setName(grainName);
			g.setLovibondColor(color);
			g.setGravity(grav);
			g.setAmount(weight);
			g.setFermentableType(fermType);
			g.setStartTime(startTime);
			g.setEndTime(endTime);
			
			mRecipe.addIngredient(g);
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
