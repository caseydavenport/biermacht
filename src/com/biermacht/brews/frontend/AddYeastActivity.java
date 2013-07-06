package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;
import android.view.*;

public class AddYeastActivity extends Activity implements OnClickListener {
	
	IngredientHandler ingredientHandler;
	
	private Spinner yeastSpinner;
	private EditText yeastNameEditText;
	private EditText amountEditText;
	private EditText attenuationEditText;
	private TextView bestForTextView;
	private ArrayList<String> yeastArray;
	private String yeastFormSpinnerValue;
	private Recipe mRecipe;
	Yeast yeast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_yeast);
		
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;
    	
    	// Set lists here
    	yeastArray = Utils.getIngredientStringList(ingredientHandler.getYeastsList());

        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeId", -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Initialize views and such here
        yeastNameEditText = (EditText) findViewById(R.id.name_edit_text);
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        attenuationEditText = (EditText) findViewById(R.id.attenuation_edit_text);
        bestForTextView = (TextView) findViewById(R.id.best_for_text);
        
        // Set some values
        amountEditText.setText(".120");
        
        // Set up yeast spinner
        yeastSpinner = (Spinner) findViewById(R.id.ingredient_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, yeastArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        yeastSpinner.setAdapter(adapter);
        yeastSpinner.setSelection(0);
                
        // Handle yeast selector here
        yeastSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	yeast = new Yeast("");
            	yeastFormSpinnerValue = yeastArray.get(position);
                
                for (Ingredient i : ingredientHandler.getYeastsList())
                {
                	if (yeastFormSpinnerValue.equals(i.toString()))
                	{
                		yeast = (Yeast) i;
                		attenuationEditText.setText(yeast.getAttenuation() +"");
                		bestForTextView.setText(yeast.getBestFor());
                	}
                }
                
                // Set whether we want to show name field
                if (yeast.getName().equals("Custom Yeast"))
                {
                	yeastNameEditText.setVisibility(View.VISIBLE);
                	findViewById(R.id.name_title).setVisibility(View.VISIBLE);
                }
                else
                {
                	yeastNameEditText.setVisibility(View.GONE);
                	findViewById(R.id.name_title).setVisibility(View.GONE);
                }
            	
                yeastNameEditText.setText(yeastFormSpinnerValue);
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
		if (v.getId() == R.id.submit_button)
		{
			String name = yeastNameEditText.getText().toString();
			double attenuation = Double.parseDouble(attenuationEditText.getText().toString());
			double amount = Double.parseDouble(amountEditText.getText().toString());
			
			Yeast y = yeast;
			y.setName(name);
			y.setAttenuation(attenuation);
			y.setDisplayAmount(amount);
			
			mRecipe.addIngredient(y);
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
