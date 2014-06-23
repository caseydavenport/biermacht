package com.biermacht.brews.frontend.IngredientActivities;

import java.util.Arrays;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomHopActivity extends EditHopActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set views
        this.setViews(Arrays.asList(nameView, amountView, timeView, formSpinner, useSpinner, alphaAcidView));
        
        // Set values for the given hop
        setValues(hop);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
    }
    
    public void setInitialSearchableListSelection()
    {
    	// Don't set the searchable list selector.
    	// Initial values are set based on the ingredient we are passed
    	// through the intent.
    }

    public void onFinished()
    {
        Database.updateIngredient(hop, Constants.DATABASE_CUSTOM);
        finish();
    }

    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }
}
