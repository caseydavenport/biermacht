package com.biermacht.brews.frontend.IngredientActivities;

import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.biermacht.brews.*;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.utils.*;

public class EditMiscActivity extends AddMiscActivity {

    // Holds the currently selected misc
    Misc selectedMisc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        typeSpinner.setSelection(typeArray.indexOf(misc.getMiscType()));
        useSpinner.setSelection(useArray.indexOf(misc.getUse()));
        
        // Set button text
        submitButton.setText(R.string.save);
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire misc
        misc = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);
    }

    @Override
    public void getList()
    {
        super.getList();

        // If this misc is not in the array, add it
        if (!ingredientList.contains(misc))
            ingredientList.add(0, misc);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(ingredientList.indexOf(misc));
    }

    @Override
    public void configureSearchableListListener()
    {
        searchableListListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMisc = (Misc) filteredList.get(position);
                
                // Set values
                setValues(selectedMisc);
                
                // Cancel dialog.
                if (dialog != null)
                {
                	dialog.cancel();
                	dialog = null;
                }
            }
        };
    }
    
    public void setValues(Misc selMisc)
    {
        nameViewText.setText(selMisc.getName());
        searchableListViewText.setText(selMisc.getName());
        timeViewText.setText(String.format("%d", misc.getTime()));
        amountViewText.setText(String.format("%2.2f", misc.getDisplayAmount()));
        typeSpinner.setSelection(typeArray.indexOf(selMisc.getMiscType()));
        unitsSpinner.setSelection(unitsArray.indexOf(Units.toFormal(selMisc.getDisplayUnits())));
        useSpinner.setSelection(useArray.indexOf(selMisc.getUse()));
        amountViewTitle.setText("Amount (" + selMisc.getDisplayUnits() + ")");
        timeViewTitle.setText(selMisc.getUse() + " time");
        
        // Set units here
        units = Units.toFormal(selMisc.getDisplayUnits());
    }
    
    public void setInitialSearchableListSelection()
    {
    	setValues(misc);
    }

    @Override
    public void onFinished()
    {
        Database.updateIngredient(misc, Constants.DATABASE_DEFAULT);
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(misc.getId(), Constants.DATABASE_DEFAULT);
        finish();
    }
}