package com.biermacht.brews.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import android.widget.*;

public class EditGrainActivity extends Activity implements OnClickListener {
	
	// private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private EditText grainBoilTimeEditText;
	private TextView boilSteepTimeTextView;
	
	private Recipe mRecipe;
	private Fermentable fermentable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grain);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        long grainId = getIntent().getLongExtra(Utils.INTENT_INGREDIENT_ID, 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        
        // Get the grain from the database
        fermentable = (Fermentable) Utils.getIngredientWithId(grainId);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        grainBoilTimeEditText = (EditText) findViewById(R.id.time_edit_text);
        boilSteepTimeTextView = (TextView) findViewById(R.id.time_title);
		
        grainNameEditText.setText(fermentable.getName());
        grainColorEditText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
        grainGravEditText.setText(String.format("%2.3f", fermentable.getGravity()));
        grainWeightEditText.setText(String.format("%2.2f", fermentable.getDisplayAmount()));
        grainBoilTimeEditText.setText(String.format("%d", fermentable.getTime()));
 		
		
		// Set boil vs steep accordingly
		if (mRecipe.getType().equals(Recipe.EXTRACT))
		{
			if (fermentable.getFermentableType().equals(Fermentable.EXTRACT))
			{
				boilSteepTimeTextView.setText(R.string.boil_time);	
 			}
			else if (fermentable.getFermentableType().equals(Fermentable.GRAIN))
			{
				boilSteepTimeTextView.setText(R.string.steep_time);
			}
			else
			{
				boilSteepTimeTextView.setText("Time");
			}
		}
		else
		{
			// TODO
		}
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
			int cookTime = Integer.parseInt(grainBoilTimeEditText.getText().toString());
			int endTime = mRecipe.getBoilTime();
			int startTime = endTime - cookTime;
			
			if (startTime > mRecipe.getBoilTime())
				startTime = mRecipe.getBoilTime();
			
			fermentable.setName(grainName);
			fermentable.setLovibondColor(color);
			fermentable.setGravity(grav);
			fermentable.setDisplayAmount(weight);
			//fermentable.setFermentableType(//TODO);
			fermentable.setStartTime(startTime);
			fermentable.setEndTime(endTime);
			
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
