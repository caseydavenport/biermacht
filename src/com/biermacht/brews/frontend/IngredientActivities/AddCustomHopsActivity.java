package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.utils.Constants;

import java.util.Arrays;

public class AddCustomHopsActivity extends AddHopsActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set views
    this.setViews(Arrays.asList(nameView,
                                amountView,
                                timeView,
                                formSpinner,
                                useSpinner,
                                alphaAcidView,
                                descriptionView));
    if (! haveRecipe()) {
      timeView.setVisibility(View.GONE);
      amountView.setVisibility(View.GONE);
    }
    // Set initial values
    hop = new Hop("Custom hop");
    setValues(hop);
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the new ingredient.
  }

  @Override
  public void onFinished() {
    Log.d("AddCustomHop", "Adding hop to db_custom: " + hop.getName());
    DatabaseAPI.addIngredient(Constants.DATABASE_CUSTOM, hop, Constants.MASTER_RECIPE_ID);
    if (haveRecipe()) {
      // If not master ID, update the recipe.
      Log.d("AddCustomHop", "Adding hop '" +
              hop.getName() + "' to recipe '" + mRecipe.getRecipeName() + "'");
      mRecipe.addIngredient(hop);
      mRecipe.save();
    }
    Log.d("AddCustomHop", "Closing activity");
    finish();
  }

}
