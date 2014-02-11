package com.biermacht.brews.frontend.IngredientActivities;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.biermacht.brews.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.utils.*;
import java.util.*;

import com.biermacht.brews.frontend.adapters.SpinnerAdapter;

public class AddMiscActivity extends AddEditActivity {

    // Holds the currently selected misc, and misc being edited
    public Misc misc;

    // Editable rows to display
    public Spinner typeSpinner;
    public Spinner useSpinner;

    // Spinner array declarations
    public ArrayList<String> typeArray;
    public ArrayList<String> useArray;

    // Data storage for spinners
	public String type;
    public String use;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Add views to main view
        mainView.addView(useSpinner);
        mainView.addView(typeSpinner);

		// Set up type spinner
        typeArray = new ArrayList<String>();
        typeArray.add(Misc.TYPE_FINING);
        typeArray.add(Misc.TYPE_FLAVOR);
        typeArray.add(Misc.TYPE_HERB);
        typeArray.add(Misc.TYPE_SPICE);
        typeArray.add(Misc.TYPE_WATER_AGENT);
        typeArray.add(Misc.TYPE_OTHER);
		
        SpinnerAdapter miscTypeAdapter = new SpinnerAdapter(this, typeArray, "Misc Type");
        miscTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(miscTypeAdapter);
        typeSpinner.setSelection(0);

		// Set up use spinner
		useArray = new ArrayList<String>();
        useArray.add(Misc.USE_BOIL);
        useArray.add(Misc.USE_BOTTLING);
        useArray.add(Misc.USE_MASH);
        useArray.add(Misc.USE_PRIMARY);
        useArray.add(Misc.USE_SECONDARY);
		
		SpinnerAdapter miscUseAdapter = new SpinnerAdapter(this, useArray, "Misc Use");
		miscUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        useSpinner.setAdapter(miscUseAdapter);
		useSpinner.setSelection(0);

		// Handle type selector here
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
					type = typeArray.get(position);
				}

				public void onNothingSelected(AdapterView<?> parentView)
                {
				}

			});

		// Handle use selector here
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
					use = useArray.get(position);

                    if (use.equals(Misc.USE_BOTTLING))
                    {
                        timeView.setVisibility(View.GONE);
                    }
                    else
                    {
                        timeView.setVisibility(View.VISIBLE);
                        String units = "";
                        if (use.equals(Misc.USE_PRIMARY)||use.equals(Misc.USE_SECONDARY))
                            units = Units.DAYS;
                        else if (use.equals(Misc.USE_BOIL)||use.equals(Misc.USE_MASH))
                            units = Units.MINUTES;
                        else
                            units = Units.MINUTES;

                        timeViewTitle.setText(use + " Time" + " (" + units + ")");
                    }
				}

				public void onNothingSelected(AdapterView<?> parentView)
                {
				}

			});
        
        // Set button text
        submitButton.setText(R.string.add);
    }

    @Override
    public void onMissedClick(View v)
    {

    }

    @Override
    public void getList()
    {
        // Get the list of ingredients to show
        ingredientList = new ArrayList<Ingredient>();
        ingredientList.addAll(ingredientHandler.getMiscsList());
    }

    @Override
    public void createSpinner()
    {
        // Set up spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, ingredientList, "Misc Selector");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(0);
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                misc = (Misc) ingredientList.get(position);

                nameViewText.setText(misc.getName());

                timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
                amountViewText.setText(String.format("%2.2f", misc.getDisplayAmount()));

                typeSpinner.setSelection(typeArray.indexOf(misc.getMiscType()));
                useSpinner.setSelection(useArray.indexOf(misc.getUse()));

                amountViewTitle.setText("Amount (" + misc.getDisplayUnits() + ")");
                timeViewTitle.setText(misc.getUse() + " time");
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(misc);
        mRecipe.update();
        Database.updateRecipe(mRecipe);
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();

        misc.setName(name);
        misc.setTime(time);
        misc.setDisplayAmount(amount, misc.getDisplayUnits());
        misc.setMiscType(type);
        misc.setUse(use);
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public void onDeletePressed()
    {

    }
}
