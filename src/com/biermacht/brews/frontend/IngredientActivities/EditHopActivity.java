package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;
import java.util.Collections;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
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
import com.biermacht.brews.utils.comparators.IngredientComparator;

import android.widget.TextView;
import android.util.*;

public class EditHopActivity extends AddHopsActivity {

   // Holds the currently selected hop
    Hop selectedHop;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        // Set listeners
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                use = useArray.get(position);

                if (use.equals(Hop.USE_DRY_HOP))
                {
                    timeViewTitle.setText("Time (days)");
                }
                else
                {
                    timeViewTitle.setText("Time (mins)");
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set spinners
        formSpinner.setSelection(formArray.indexOf(form));
        useSpinner.setSelection(useArray.indexOf(use));
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire Hop
        hop = getIntent().getParcelableExtra(Constants.INTENT_INGREDIENT);

        // Get the use and form
        use = hop.getUse();
        form = hop.getForm();
    }

    @Override
    public void configureIngredientSpinnerListener()
    {
        ingredientSpinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedHop = (Hop) ingredientList.get(position);

                nameViewText.setText(selectedHop.getName());
                timeViewText.setText(String.format("%d", hop.getDisplayTime()));
                alphaAcidViewText.setText(String.format("%2.2f", selectedHop.getAlphaAcidContent()));
                amountViewText.setText(String.format("%2.2f", hop.getDisplayAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void getIngredientList()
    {
        super.getIngredientList();

        if (!ingredientList.contains(hop))
        {
            ingredientList.add(hop);
            Collections.sort(ingredientList, new IngredientComparator());
        }
    }

    @Override
    public void setIngredientSelection()
    {
        ingredientSpinner.setSelection(ingredientList.indexOf(hop));
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

    public void onFinished()
    {
        Database.updateIngredient(hop);
        finish();
    }

    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_DEFAULT);
        finish();
    }
}
