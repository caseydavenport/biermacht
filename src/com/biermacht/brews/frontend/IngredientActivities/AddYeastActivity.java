package com.biermacht.brews.frontend.IngredientActivities;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.ItemNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Database;

import java.util.ArrayList;

public class AddYeastActivity extends AddEditActivity implements OnClickListener {

    // Holds the currently selected yeast, and yeast being edited
    public Yeast yeast;

    private View attenuationView;

    // Titles from rows
    private TextView attenuationViewTitle;

    // Content from rows
    public TextView attenuationViewText;

    // Spinner array declarations
    private ArrayList<Ingredient> yeastsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        attenuationView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set the onClickListener for each row
        attenuationView.setOnClickListener(onClickListener);

        // Add views we want
        mainView.addView(attenuationView);

        // Set titles
        attenuationViewTitle = (TextView) attenuationView.findViewById(R.id.title);
        attenuationViewTitle.setText("Attenuation (%)");
        amountViewTitle.setText("Amount (L)");

        // Get text views
        attenuationViewText = (TextView) attenuationView.findViewById(R.id.text);
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(attenuationView))
            alert = alertBuilder.editTextFloatAlert(attenuationViewText, attenuationViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void getList()
    {
        // Get the list of ingredients to show
        yeastsArray = new ArrayList<Ingredient>();
        yeastsArray.addAll(ingredientHandler.getYeastsList());
    }

    @Override
    public void createSpinner()
    {
        // Set up type spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, yeastsArray, "Yeast");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                yeast = (Yeast) yeastsArray.get(position);

                nameViewText.setText(yeast.getName());
                attenuationViewText.setText(String.format("%2.0f", yeast.getAttenuation()));
                amountViewText.setText(String.format("%2.2f", yeast.getBeerXmlStandardAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double attenuation = Double.parseDouble(attenuationViewText.getText().toString());

        yeast.setName(name);
        yeast.setAttenuation(attenuation);
        yeast.setBeerXmlStandardAmount(amount);
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(yeast);
        Database.updateRecipe(mRecipe);
        finish();
    }

    public void onDeletePressed()
    {
        // Nothing
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }
}
