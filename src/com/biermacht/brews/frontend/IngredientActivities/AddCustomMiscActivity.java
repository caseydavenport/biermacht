package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class AddCustomMiscActivity extends AddMiscActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
            boolean readyToGo = true;

            try {
                String name = nameViewText.getText().toString();
                int time = Integer.parseInt(timeViewText.getText().toString());
                int endTime = mRecipe.getBoilTime();
                int startTime = endTime - time;
                double amount = Double.parseDouble(amountViewText.getText().toString());

                if (time > mRecipe.getBoilTime())
                    time = mRecipe.getBoilTime();

                misc.setName(name);
                misc.setTime(time);
                misc.setDisplayAmount(amount, misc.getDisplayUnits());
                misc.setMiscType(type);
                misc.setUse(use);
            } catch (Exception e)
            {
                Log.d("AddMiscActivity", "Exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                Database.addIngredientToVirtualDatabase(Constants.INGREDIENT_DB_CUSTOM, misc, Constants.MASTER_RECIPE_ID);
                finish();
            }
		}

		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
