package com.biermacht.brews.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import android.widget.TextView;
import android.util.*;

public class EditHopActivity extends Activity implements OnClickListener {
	
	// private Spinner grainTypeSpinner;
	private EditText hopNameEditText;
	private EditText hopAcidsEditText;
	private EditText hopBoilTimeEditText;
	private EditText hopWeightEditText;
	private TextView timeTitleTextView;
	private Recipe mRecipe;
	private Hop hop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hop);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        long hopId = getIntent().getLongExtra(Utils.INTENT_INGREDIENT_ID, 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        
        // Get the grain from the database
        hop = (Hop) Utils.getIngredientWithId(hopId);
        
        // Initialize views and such here
        hopNameEditText = (EditText) findViewById(R.id.hop_name_edit_text);
        hopAcidsEditText = (EditText) findViewById(R.id.hop_acid_edit_text);
        hopBoilTimeEditText = (EditText) findViewById(R.id.boil_time_edit_text);
        hopWeightEditText = (EditText) findViewById(R.id.hop_weight_edit_text);
        timeTitleTextView = (TextView) findViewById(R.id.boil_time_title);
		
        hopNameEditText.setText(hop.getName());
        hopAcidsEditText.setText(hop.getAlphaAcidContent() +"");
        hopBoilTimeEditText.setText(hop.getTime() +"");
        hopWeightEditText.setText(hop.getDisplayAmount() + "");
		
		if (hop.getUse().equals(Hop.USE_DRY_HOP))
			timeTitleTextView.setText("Time (days)");
		else if (hop.getUse().equals(Hop.USE_BOIL))
			timeTitleTextView.setText("Time (mins)");
		else
			timeTitleTextView.setText("Time");
        
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
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
			boolean readyToGo = true;
			int endTime, startTime;
			String hopName="";
			double time=0, alpha=0, weight = 0;
			try
			{
				hopName = hopNameEditText.getText().toString();
				time = Integer.parseInt(hopBoilTimeEditText.getText().toString());
				alpha = Double.parseDouble(hopAcidsEditText.getText().toString());
				weight = Double.parseDouble(hopWeightEditText.getText().toString());
			} catch (Exception e) {
				Log.d("EditHopsActivity", e.toString());
				Log.d("EditHops", "Setting ready to NO due to exception");
				readyToGo = false;
			}

		    if (!hop.getUse().equals(Hop.USE_DRY_HOP))
			{
		        endTime = mRecipe.getBoilTime();
			    startTime = endTime - (int) time;
				
				if (time > mRecipe.getBoilTime())
					time = mRecipe.getBoilTime();
			}
			else
			{
				startTime = mRecipe.getBoilTime();
				endTime = startTime + (int) time;
			}

			if (hopName.isEmpty())
				readyToGo = false;
			if (!(weight > 0))
				readyToGo = false;
			if (!(time > 0))
				readyToGo = false;

			if (readyToGo)
			{
				hop.setName(hopName);
				hop.setDisplayTime((int) time);
				hop.setStartTime(startTime);
				hop.setEndTime(endTime);
				hop.setAlphaAcidContent(alpha);
				hop.setDisplayAmount(weight);
				hop.setForm(Hop.FORM_PELLET);

				Utils.updateIngredient(hop);
				mRecipe = Utils.getRecipeWithId(mRecipe.getId());
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
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
