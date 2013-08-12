package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditFermentableActivity extends AddFermentableActivity {

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
        // Get ingredient list
        super.getIngredientList();
    }

    @Override
    public void setIngredientSelection()
    {
        ingredientSpinner.setSelection(ingredientList.indexOf(fermentable));
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

