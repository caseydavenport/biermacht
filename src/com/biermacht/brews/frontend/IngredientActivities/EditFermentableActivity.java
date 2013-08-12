package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.comparators.IngredientComparator;

import java.util.Collections;

public class EditFermentableActivity extends AddFermentableActivity {

    public Fermentable selectedFermentable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void getValuesFromIntent()
    {
        // Get the recipe from calling activity
        super.getValuesFromIntent();

        // Get the ingredient as well
        long grainId = getIntent().getLongExtra(Constants.INTENT_INGREDIENT_ID, Constants.INVALID_ID);
        fermentable = (Fermentable) Database.getIngredientWithId(grainId);
    }

    @Override
    public void getIngredientList()
    {
        super.getIngredientList();

        if (!ingredientList.contains(fermentable))
        {
            ingredientList.add(fermentable);
            Collections.sort(ingredientList, new IngredientComparator());
        }
    }

    @Override
    public void setIngredientSelection()
    {
        ingredientSpinner.setSelection(ingredientList.indexOf(fermentable));
    }

    @Override
    public void configureIngredientSpinnerListener()
    {
        ingredientSpinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedFermentable = (Fermentable) ingredientList.get(position);

                // Set whether we show boil or steep
                if (mRecipe.getType().equals(Recipe.EXTRACT))
                {
                    if (selectedFermentable.getFermentableType().equals(Fermentable.TYPE_EXTRACT))
                    {
                        timeViewTitle.setText(R.string.boil_time);
                        timeViewText.setText(mRecipe.getBoilTime() + "");
                    }
                    else if (selectedFermentable.getFermentableType().equals(Fermentable.TYPE_GRAIN))
                    {
                        timeViewTitle.setText(R.string.steep_time);
                        timeViewText.setText(15 + "");
                    }
                    else
                    {
                        timeViewTitle.setText("Time");
                    }
                }
                else
                {
                    // TODO: Do we ever want to enter a time for mashes?
                    timeView.setVisibility(View.GONE);
                }

                nameViewText.setText(fermentable.getName());
                colorViewText.setText(String.format("%2.2f", selectedFermentable.getLovibondColor()));
                gravityViewText.setText(String.format("%2.3f", selectedFermentable.getGravity()));
                amountViewText.setText(String.format("%2.2f", fermentable.getDisplayAmount()));
                timeViewText.setText(String.format("%d", fermentable.getTime()));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.INGREDIENT_DB_DEFAULT);
        finish();
    }

    @Override
    public void onFinished()
    {
        // Update the ingredient, and finish the activity
        Database.updateIngredient(fermentable);
        try
        {
            mRecipe = Database.getRecipeWithId(mRecipe.getId());
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
        }
        mRecipe.update();
        Database.updateRecipe(mRecipe);
        finish();
    }
}

