package com.biermacht.brews.frontend;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biermacht.brews.frontend.adapters.*;
import android.view.*;
import com.biermacht.brews.utils.Units;
import android.widget.TextView;
import android.util.*;

public class EditHopActivity extends Activity {

    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private View.OnClickListener onClickListener;

    // LayoutInflater
    LayoutInflater inflater;

    // Recipe we are editing
    private Recipe mRecipe;

    // IngredientHandler to get ingredient arrays
    IngredientHandler ingredientHandler;

    // Holds the currently selected hop, and hop being edited
    Hop hop, selectedHop;

    // Editable rows to display
    private Spinner hopsSpinner;
    private Spinner useSpinner;
    private Spinner formSpinner;
    private View nameView;
    private View amountView;
    private View timeView;
    private View alphaAcidView;

    // Titles from rows
    private TextView nameViewTitle;
    private TextView amountViewTitle;
    private TextView timeViewTitle;
    private TextView alphaAcidViewTitle;

    // Content from rows
    private TextView nameViewText;
    private TextView amountViewText;
    private TextView timeViewText;
    private TextView alphaAcidViewText;

    // Spinner array declarations
    private ArrayList<Ingredient> hopsArray;
    private ArrayList<String> formArray;
    private ArrayList<String> useArray;

    // Data storage variables for spinners
    String use;
    String form;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Ingredient Handler
        ingredientHandler = MainActivity.ingredientHandler;

        // Get the list of ingredients to show
        hopsArray = new ArrayList<Ingredient>();
        hopsArray.addAll(ingredientHandler.getHopsList());

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);

        // Acquire Hop
        hop = (Hop) getIntent().getParcelableExtra(Constants.INTENT_INGREDIENT);

        // Acquire recipe
        try
        {
            mRecipe = Utils.getRecipeWithId(id);
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
            finish();
        }

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(nameView))
                    alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
                else if (v.equals(amountView))
                    alert = alertBuilder.editTextFloatAlert(amountViewText, amountViewTitle).create();
                else if (v.equals(timeView))
                    alert = alertBuilder.editTextFloatAlert(timeViewText, timeViewTitle).create();
                else if (v.equals(alphaAcidView))
                    alert = alertBuilder.editTextFloatAlert(alphaAcidViewText, alphaAcidViewTitle).create();

                else
                    return; // In case its none of those views...

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };

        // Initialize views and such here
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        nameView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        amountView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        timeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        alphaAcidView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        hopsSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        formSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        nameView.setOnClickListener(onClickListener);
        amountView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);
        alphaAcidView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(hopsSpinner);
        mainView.addView(nameView);
        mainView.addView(formSpinner);
        mainView.addView(useSpinner);
        mainView.addView(timeView);
        mainView.addView(amountView);
        mainView.addView(alphaAcidView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        amountViewTitle = (TextView) amountView.findViewById(R.id.title);
        amountViewTitle.setText("Amount (oz)");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Time");

        alphaAcidViewTitle = (TextView) alphaAcidView.findViewById(R.id.title);
        alphaAcidViewTitle.setText("Alpha Acids (%)");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        amountViewText = (TextView) amountView.findViewById(R.id.text);
        timeViewText = (TextView) timeView.findViewById(R.id.text);
        alphaAcidViewText = (TextView) alphaAcidView.findViewById(R.id.text);

        // If this yeast is not in the array, add it
        if (!hopsArray.contains(hop))
            hopsArray.add(0, hop);

        // Get the use and form
        use = hop.getUse();
        form = hop.getForm();

        // Set up hops spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, hopsArray, "Hop Selector");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        hopsSpinner.setAdapter(adapter);
        hopsSpinner.setSelection(hopsArray.indexOf(hop));

        // Hop form spinner
        formArray = new ArrayList<String>();
        formArray.add(Hop.FORM_PELLET);
        formArray.add(Hop.FORM_WHOLE);
        formArray.add(Hop.FORM_PLUG);
        SpinnerAdapter hopFormAdapter = new SpinnerAdapter(this, formArray, "Form");
        hopFormAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        formSpinner.setAdapter(hopFormAdapter);
        formSpinner.setSelection(formArray.indexOf(form));

        // Set up hop use spinner
        useArray = new ArrayList<String>();
        useArray.add(Hop.USE_BOIL);
        useArray.add(Hop.USE_AROMA);
        useArray.add(Hop.USE_DRY_HOP);
        SpinnerAdapter hopUseAdapter = new SpinnerAdapter(this, useArray, "Usage");
        hopUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        useSpinner.setAdapter(hopUseAdapter);
        useSpinner.setSelection(useArray.indexOf(use));

        // Handle hops selector here
        hopsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedHop = (Hop) hopsArray.get(position);

                nameViewText.setText(selectedHop.getName());
                timeViewText.setText(String.format("%d", hop.getDisplayTime()));
                alphaAcidViewText.setText(String.format("%2.2f", selectedHop.getAlphaAcidContent()));
                amountViewText.setText(String.format("%2.0f", hop.getDisplayAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });

        // Handle use selector here
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                use = useArray.get(position);

                if (use.equals(Hop.USE_DRY_HOP))
                    timeViewTitle.setText("Time (days)");
                else
                    timeViewTitle.setText("Time (mins)");

            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Blag
            }
        });

        // Handle form selector here
        formSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                form = formArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Blag
            }
        });
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
                    startTime = mRecipe.getBoilTime();
                    endTime = startTime + (int) time;
                }

                hop.setName(hopName);
                hop.setDisplayTime((int) time);
                hop.setStartTime(startTime);
                hop.setEndTime(endTime);
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
                Utils.updateIngredient(hop);
                finish();
            }
        }

        // If "DELETE" button pressed
        if (v.getId() == R.id.delete_button)
        {
            Utils.deleteIngredient(hop);
            finish();
        }

        // if "CANCEL" button pressed
        if (v.getId() == R.id.cancel_button)
        {
            finish();
        }
    }
}
