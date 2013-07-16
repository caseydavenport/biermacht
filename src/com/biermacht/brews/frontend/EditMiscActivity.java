package com.biermacht.brews.frontend;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.biermacht.brews.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.utils.*;
import java.util.*;

import com.biermacht.brews.frontend.adapters.SpinnerAdapter;

public class EditMiscActivity extends Activity {

	IngredientHandler ingredientHandler;
	private Spinner miscUseSpinner;
	private EditText timeEditText;
	private EditText amountEditText;
	private TextView amountTitle;
	private TextView timeTitle;
	private EditText nameEditText;
	private ArrayList<String> miscUseArray;
	private Misc miscObj;
	private Recipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_misc);

		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

    	// Set up Ingredient Handler
    	ingredientHandler = MainActivity.ingredientHandler;

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, -1);
		long miscId = getIntent().getLongExtra(Utils.INTENT_INGREDIENT_ID, -1);
        mRecipe = Utils.getRecipeWithId(id);
		miscObj = (Misc) Utils.getIngredientWithId(miscId);

        // Initialize views and such here
		nameEditText = (EditText) findViewById(R.id.name_edit_text);
        timeEditText = (EditText) findViewById(R.id.time_edit_text);
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
		amountTitle = (TextView) findViewById(R.id.amount_title);
		timeTitle = (TextView) findViewById(R.id.time_title);
		
		// set values
		nameEditText.setText(miscObj.getName());
		timeEditText.setText(miscObj.getTime()+"");
		amountEditText.setText(miscObj.getDisplayAmount()+"");
		amountTitle.setText("Amount (" + miscObj.getDisplayUnits() + ")");
		
		// Set up use spinner
		miscUseSpinner = (Spinner) findViewById(R.id.misc_use_spinner);
		miscUseArray = new ArrayList<String>();
		miscUseArray.add(Misc.USE_BOIL);
		miscUseArray.add(Misc.USE_BOTTLING);
		miscUseArray.add(Misc.USE_MASH);
		miscUseArray.add(Misc.USE_PRIMARY);
		miscUseArray.add(Misc.USE_SECONDARY);

		SpinnerAdapter<String> miscUseAdapter = new SpinnerAdapter<String>(this, miscUseArray);
		miscUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		miscUseSpinner.setAdapter(miscUseAdapter);
		miscUseSpinner.setSelection(miscUseArray.indexOf(miscObj.getUse()));

   
		// Handle use selector here
        miscUseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					// Set the type to the selected type...
					miscObj.setUse(miscUseArray.get(position));
					
					timeTitle.setText(miscObj.getUse() + " time");
					if (!(miscObj.getUse().equals(Misc.USE_BOIL)||
						miscObj.getUse().equals(Misc.USE_MASH)))
					{
						timeTitle.setVisibility(View.GONE);
						timeEditText.setVisibility(View.GONE);
					}
					else
					{
						timeTitle.setVisibility(View.VISIBLE);
						timeEditText.setVisibility(View.VISIBLE);
					}
				}


				public void onNothingSelected(AdapterView<?> parentView) {
					// Blag
				}

			});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_add_miscs, menu);
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
		// if edit button pressed
		if (v.getId() == R.id.edit_grain_submit_button)
		{
			int startTime, endTime;
			String name = nameEditText.getText().toString();
			int time = Integer.parseInt(timeEditText.getText().toString());
			double amount = Double.parseDouble(amountEditText.getText().toString());

			if (miscObj.getUse().equals(Misc.USE_BOIL))
			{
				if (time > mRecipe.getBoilTime())
					time = mRecipe.getBoilTime();
				
				endTime = mRecipe.getBoilTime();
				startTime = endTime - time;
			}
			else
			{
				startTime = mRecipe.getBoilTime();
				endTime = startTime + time;	
			}
			
			miscObj.setName(name);
			miscObj.setTime(time);
			miscObj.setStartTime(startTime);
			miscObj.setEndTime(endTime);
			miscObj.setDisplayAmount(amount);
			
			Utils.updateIngredient(miscObj);
			mRecipe = Utils.getRecipeWithId(mRecipe.getId());
			mRecipe.update();
			Utils.updateRecipe(mRecipe);

			finish();
		}
		
		// If "DELETE" button pressed
		if (v.getId() == R.id.delete_button)
		{
			Utils.deleteIngredient(miscObj);

			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
