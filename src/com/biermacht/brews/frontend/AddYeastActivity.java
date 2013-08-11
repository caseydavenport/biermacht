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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

import java.util.ArrayList;

public class AddYeastActivity extends Activity implements OnClickListener {

    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private OnClickListener onClickListener;

    // LayoutInflater
    LayoutInflater inflater;

    // Recipe we are editing
    public Recipe mRecipe;

    // IngredientHandler to get ingredient arrays
    public IngredientHandler ingredientHandler;

    // Holds the currently selected yeast, and yeast being edited
    public Yeast yeast;

    // Editable rows to display
    private Spinner yeastSpinner;
    private View nameView;
    private View amountView;
    private View attenuationView;

    // Titles from rows
    private TextView nameViewTitle;
    private TextView amountViewTitle;
    private TextView attenuationViewTitle;

    // Content from rows
    public TextView nameViewText;
    public TextView amountViewText;
    public TextView attenuationViewText;

    // Spinner array declarations
    private ArrayList<Ingredient> yeastsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Ingredient Handler
        ingredientHandler = MainActivity.ingredientHandler;

        // Get the list of ingredients to show
        yeastsArray = new ArrayList<Ingredient>();
        yeastsArray.addAll(ingredientHandler.getYeastsList());

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Get recipe and yeast from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);

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
                else if (v.equals(attenuationView))
                    alert = alertBuilder.editTextFloatAlert(attenuationViewText, attenuationViewTitle).create();
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
        attenuationView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        yeastSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        nameView.setOnClickListener(onClickListener);
        amountView.setOnClickListener(onClickListener);
        attenuationView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(yeastSpinner);
        mainView.addView(nameView);
        mainView.addView(amountView);
        mainView.addView(attenuationView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        amountViewTitle = (TextView) amountView.findViewById(R.id.title);
        amountViewTitle.setText("Amount (L)");

        attenuationViewTitle = (TextView) attenuationView.findViewById(R.id.title);
        attenuationViewTitle.setText("Attenuation (%)");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        amountViewText = (TextView) amountView.findViewById(R.id.text);
        attenuationViewText = (TextView) attenuationView.findViewById(R.id.text);

        // Set up type spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, yeastsArray, "Yeast");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        yeastSpinner.setAdapter(adapter);
        yeastSpinner.setSelection(0);

        // Handle type spinner selections here
        yeastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                yeast = (Yeast) yeastsArray.get(position);

                nameViewText.setText(yeast.getName());
                attenuationViewText.setText(String.format("%2.0f", yeast.getAttenuation()));
                amountViewText.setText(String.format("%2.2f", yeast.getBeerXmlStandardAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });
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
            try
            {
                String name = nameViewText.getText().toString();
                double attenuation = Double.parseDouble(attenuationViewText.getText().toString());
                double amount = Double.parseDouble(amountViewText.getText().toString());

                yeast.setName(name);
                yeast.setAttenuation(attenuation);
                yeast.setBeerXmlStandardAmount(amount);
            } catch (Exception e)
            {
                Log.d("EditYeastActivity", "Exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                mRecipe.addIngredient(yeast);
                mRecipe.update();
                Utils.updateRecipe(mRecipe);

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
