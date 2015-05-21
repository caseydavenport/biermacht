package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import java.util.Arrays;

public class AddCustomHopsActivity extends AddHopsActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set views
    this.setViews(Arrays.asList(nameView, amountView, timeView, formSpinner, useSpinner, alphaAcidView));
    if (! haveRecipe()) {
      timeView.setVisibility(View.GONE);
      amountView.setVisibility(View.GONE);
    }
    // Set initial values
    hop = new Hop("Custom hop");
    setValues(hop);
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    hop.setShortDescription("Custom hop");
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the new ingredient.
  }

  @Override
  public void onFinished() {
    Log.d("AddCustomHop::onFinished", "Adding hop to db_custom: " + hop.getName());
    Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, hop, Constants.MASTER_RECIPE_ID);
    if (haveRecipe()) {
      // If not master ID, update the recipe.
      Log.d("AddCustomHop::onFinished", "Adding hop '" +
              hop.getName() + "' to recipe '" + mRecipe.getRecipeName() + "'");
      mRecipe.addIngredient(hop);
      mRecipe.save();
    }
    Log.d("AddCustomHop::onFinished", "Closing activity");
    finish();
  }

}
