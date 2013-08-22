package com.biermacht.brews.frontend.IngredientActivities;

import android.os.*;
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
    public void configureSpinnerListener()
    {
        spinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMisc = (Misc) ingredientList.get(position);

                nameViewText.setText(selectedMisc.getName());

                timeViewText.setText(String.format("%d", misc.getTime()));
                amountViewText.setText(String.format("%2.2f", misc.getDisplayAmount()));

                typeSpinner.setSelection(typeArray.indexOf(selectedMisc.getMiscType()));
                useSpinner.setSelection(useArray.indexOf(selectedMisc.getUse()));

                amountViewTitle.setText("Amount (" + selectedMisc.getDisplayUnits() + ")");
                timeViewTitle.setText(selectedMisc.getUse() + " time");
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        };
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