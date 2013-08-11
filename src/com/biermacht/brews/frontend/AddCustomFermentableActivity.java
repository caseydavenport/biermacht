package com.biermacht.brews.frontend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

import java.util.ArrayList;

/**
 * Subclass of AddFermentableActivity to use when adding a custom
 * fermentable that is not directly attached to a recipe.
 */
public class AddCustomFermentableActivity extends AddFermentableActivity {

    // Views for rows
    public View descriptionView;

    // Titles from rows
    public TextView descriptionViewTitle;

    // Content from rows
    public TextView descriptionViewText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the description view
        descriptionView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        descriptionView.setOnClickListener(onClickListener);
        descriptionViewText = (TextView) descriptionView.findViewById(R.id.text);
        descriptionViewTitle = (TextView) descriptionView.findViewById(R.id.title);
        descriptionViewText.setText("No Description Provided");
        descriptionViewTitle.setText("Description");

        // Remove views we don't like
        mainView.removeView(amountView);

        // Add views we do!
        mainView.addView(descriptionView);
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(descriptionView))
            alert = alertBuilder.editTextMultilineStringAlert(descriptionViewText, descriptionViewTitle).create();
        else
        {
            return;
        }

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
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
            boolean readyToGo = true;
            int time, endTime, startTime;
            double color, grav, amount;
            String name, desc;

            try
            {
                name = nameViewText.getText().toString();
                color = Double.parseDouble(colorViewText.getText().toString());
                grav = Double.parseDouble(gravityViewText.getText().toString());
                amount = Double.parseDouble(amountViewText.getText().toString());
                time = Integer.parseInt(timeViewText.getText().toString());
                endTime = mRecipe.getBoilTime();
                startTime = endTime - time;
                desc = descriptionViewText.getText().toString();

                if (startTime > mRecipe.getBoilTime())
                    startTime = mRecipe.getBoilTime();

                fermentable.setName(name);
                fermentable.setLovibondColor(color);
                fermentable.setGravity(grav);
                fermentable.setDisplayAmount(amount);
                fermentable.setTime(time);
                fermentable.setShortDescription(desc);
            }
            catch (Exception e) {
                Log.d("AddFermentable", "Hit exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                Utils.addIngredientToCustomDatabase(fermentable, Constants.MASTER_RECIPE_ID);
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
