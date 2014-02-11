package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.Units;

import android.view.*;
import android.widget.TextView;

public class AddHopsActivity extends AddEditActivity {

    // Holds the currently selected hop, and hop being edited
    Hop hop;

    // Editable rows to display
    public Spinner useSpinner;
    public Spinner formSpinner;
    public View alphaAcidView;

    // Titles from rows
    public TextView alphaAcidViewTitle;

    // Content from rows
    public TextView alphaAcidViewText;

    // Spinner array declarations
    public ArrayList<String> formArray;
    public ArrayList<String> useArray;

    // Storage for acquired values
    String use;
    String form;
    double alpha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        alphaAcidView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        formSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        alphaAcidView.setOnClickListener(onClickListener);

        // Add Views to main view
        mainView.addView(formSpinner);
        mainView.addView(useSpinner);
        mainView.addView(alphaAcidView);

        // Configure titles
        alphaAcidViewTitle = (TextView) alphaAcidView.findViewById(R.id.title);
        alphaAcidViewTitle.setText("Alpha Acids (%)");
        amountViewTitle.setText("Amount " + "(" + Units.getHopUnits() + ")");

        // Get content views
        alphaAcidViewText = (TextView) alphaAcidView.findViewById(R.id.text);

        // Hop form spinner
        formArray = Constants.HOP_FORMS;
        SpinnerAdapter hopFormAdapter = new SpinnerAdapter(this, formArray, "Form");
        hopFormAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        formSpinner.setAdapter(hopFormAdapter);

        // Set up hop use spinner
        useArray = Constants.HOP_USES;
        SpinnerAdapter hopUseAdapter = new SpinnerAdapter(this, useArray, "Usage");
        hopUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        useSpinner.setAdapter(hopUseAdapter);

		// Set listeners
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                use = useArray.get(position);

                if (use.equals(Hop.USE_DRY_HOP))
                {
                    timeViewTitle.setText("Time (days)");
                    timeViewText.setText(5 + "");
                }
                else
                {
                    timeViewTitle.setText("Time (mins)");
                    timeViewText.setText(mRecipe.getBoilTime() + "");
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

	    formSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                form = formArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set selections
        formSpinner.setSelection(0);
        useSpinner.setSelection(0);
        
        // Set button text
        submitButton.setText(R.string.add);
	}

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(alphaAcidView))
            alert = alertBuilder.editTextFloatAlert(alphaAcidViewText, alphaAcidViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void getList()
    {
        ingredientList = new ArrayList<Ingredient>();
        ingredientList.addAll(ingredientHandler.getHopsList());
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(0);
    }

    @Override
    public void createSpinner()
    {
        // Ingredient Spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, ingredientList, "Hop");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                hop = (Hop) ingredientList.get(position);

                nameViewText.setText(hop.getName());
                timeViewText.setText(mRecipe.getBoilTime() + "");
                alphaAcidViewText.setText(String.format("%2.2f", hop.getAlphaAcidContent()));
                amountViewText.setText(1.0 +"");
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void onDeletePressed()
    {
        // Must be overriden
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        alpha = Double.parseDouble(alphaAcidViewText.getText().toString());

        hop.setName(name);
        hop.setAlphaAcidContent(alpha);
        hop.setDisplayAmount(amount);
        hop.setUse(use);
        hop.setForm(form);
        hop.setDisplayTime(time);
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(hop);
        mRecipe.update();
        Database.updateRecipe(mRecipe);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_hops, menu);
        return true;
    }
}
