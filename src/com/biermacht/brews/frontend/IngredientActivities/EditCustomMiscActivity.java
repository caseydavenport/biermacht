package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomMiscActivity extends EditMiscActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove views we don't want
        mainView.removeView(timeView);
        mainView.removeView(amountView);
        mainView.removeView(searchableListView);
        
        // Add those we do
        mainView.addView(nameView, 0);
        mainView.addView(unitsSpinner);
        
        // Set values for the given misc.
        setValues(misc);
    }
    
    public void setInitialSearchableListSelection()
    {
    	// Don't set the searchable list selector.
    	// Initial values are set based on the ingredient passed
    	// via the Intent.
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        misc.setShortDescription("Custom misc");
        misc.setUseFor("Custom");
    }

    @Override
    public void onFinished()
    {
        Database.updateIngredient(misc, Constants.DATABASE_CUSTOM);
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }
}