package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Database;

public class AddCustomHopsActivity extends AddHopsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_hops, menu);
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
			int endTime, startTime;
			String hopName="";
			double time=0, alpha=0, weight = 0;
			try
			{
				hopName = nameViewText.getText().toString();
				time = Integer.parseInt(timeViewText.getText().toString());
				alpha = Double.parseDouble(alphaAcidViewText.getText().toString());
				weight = Double.parseDouble(amountViewText.getText().toString());

                if (hopName.isEmpty())
                    readyToGo = false;

                if (!use.equals(Hop.USE_DRY_HOP))
                {
                    endTime = mRecipe.getBoilTime();
                    startTime = endTime - (int) time;

                    if (time > mRecipe.getBoilTime())
                        time = mRecipe.getBoilTime();
                }
                else
                {
                    time = Units.daysToMinutes(time);
                    startTime = mRecipe.getBoilTime();
                    endTime = startTime + (int) time;
                }

                hop.setName(hopName);
                hop.setDisplayTime((int) time);
                hop.setAlphaAcidContent(alpha);
                hop.setDisplayAmount(weight);
                hop.setUse(use);
                hop.setForm(form);

			} catch (Exception e)
            {
				Log.d("AddHopsActivity", "Exception on submit: " + e.toString());
				readyToGo = false;
			}
			
			if (readyToGo)
			{
                Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, hop, Constants.MASTER_RECIPE_ID);
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
