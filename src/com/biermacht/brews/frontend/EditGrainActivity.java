package com.biermacht.brews.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class EditGrainActivity extends Activity implements OnClickListener {
	
	// private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private EditText grainBoilTimeEditText;
	// private ArrayList<String> grainTypeArray = Utils.getFermentablesStringList();
	private Recipe mRecipe;
	private Fermentable fermentable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grain);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeID", 0);
        long grainId = getIntent().getLongExtra("com.biermacht.brews.grainID", 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        
        // Get the grain from the database
        fermentable = (Fermentable) Utils.getIngredientWithId(grainId);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        grainBoilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        
        grainNameEditText.setText(fermentable.getName());
        grainColorEditText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
        grainGravEditText.setText(String.format("%2.3f", fermentable.getGravity()));
        grainWeightEditText.setText(String.format("%2.2f", fermentable.getAmount()));
        grainBoilTimeEditText.setText(String.format("%d", fermentable.getStartTime()));
 
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
			String grainName = grainNameEditText.getText().toString();
			double color = Double.parseDouble(grainColorEditText.getText().toString());
			double grav = Double.parseDouble(grainGravEditText.getText().toString());
			double weight = Double.parseDouble(grainWeightEditText.getText().toString());
			int startTime = Integer.parseInt(grainBoilTimeEditText.getText().toString());
			
			if (startTime > mRecipe.getBoilTime())
				startTime = mRecipe.getBoilTime();
			
			fermentable.setName(grainName);
			fermentable.setLovibondColor(color);
			fermentable.setGravity(grav);
			fermentable.setAmount(weight);
			fermentable.setFermentableType(Fermentable.GRAIN);
			fermentable.setStartTime(startTime);
			
			Utils.updateIngredient(fermentable);
			mRecipe = Utils.getRecipeWithId(mRecipe.getId());
			mRecipe.update();
			Utils.updateRecipe(mRecipe);

			finish();
		}
		
		// If "DELETE" button pressed
		if (v.getId() == R.id.delete_button)
		{
			Utils.deleteIngredient(fermentable);
			
			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
		
	}
}
