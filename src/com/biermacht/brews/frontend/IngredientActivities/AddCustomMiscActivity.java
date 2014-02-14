package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Units;

public class AddCustomMiscActivity extends AddMiscActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Remove views we don't want
        mainView.removeView(timeView);
        mainView.removeView(amountView);
        mainView.removeView(searchableListView);
        
        // Add those we do
        mainView.addView(nameView, 0);
        mainView.addView(unitsSpinner);
        
        // Set initial values
        misc = new Misc("Custom misc");
        setValues(misc);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        misc.setShortDescription("Custom misc");
        misc.setUseFor("Custom");
        misc.setDisplayUnits(units);
    }
    
    public void setInitialSearchableListSelection()
    {
    	// Don't set the searchable list selector.
    	// Initial values are set based on the new ingredient.
    }

    @Override
    public void onFinished()
    {
        Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, misc, Constants.MASTER_RECIPE_ID);
        finish();
    }

}
