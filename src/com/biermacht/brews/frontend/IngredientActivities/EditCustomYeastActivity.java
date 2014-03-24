package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomYeastActivity extends EditYeastActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Remove views we don't want
        mainView.removeView(amountView);
        mainView.removeView(searchableListView);
        
        // Add those we do
        mainView.addView(nameView, 0);
        mainView.addView(labView);
        mainView.addView(productIdView);
        
        // Set initial values
        setValues(yeast);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();

        yeast.setShortDescription("Custom yeast");
    }
    
    public void setInitialSearchableListSelection()
    {
    	// Don't set the searchable list selector.
    	// Initial values are set based on the ingredient we are passed
    	// through the intent.
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }

    @Override
    public void onFinished()
    {
        Database.updateIngredient(yeast, Constants.DATABASE_CUSTOM);
        finish();
    }
}
