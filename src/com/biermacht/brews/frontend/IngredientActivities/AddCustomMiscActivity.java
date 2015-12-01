package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.utils.Constants;

import java.util.Arrays;

public class AddCustomMiscActivity extends AddMiscActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Remove the timeView from the mainView, as it should never be visible.
    // This prevents it from appearing based on the useSpinner selection.
    mainView.removeView(timeView);

    // Set the views that should be visible.
    this.setViews(Arrays.asList(nameView, amountView, unitsSpinner, useSpinner, typeSpinner));
    if (! haveRecipe()) {
      timeView.setVisibility(View.GONE);
      amountView.setVisibility(View.GONE);
    }

    // Set initial values
    misc = new Misc("Custom misc");
    setValues(misc);
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    misc.setShortDescription("Custom misc");
    misc.setUseFor(use);
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the new ingredient.
  }

  @Override
  public void onFinished() {
    Log.d("AddCustomMisc", "Adding misc to db_custom: " + misc.getName());
    DatabaseAPI.addIngredient(Constants.DATABASE_CUSTOM, misc, Constants.MASTER_RECIPE_ID);
    if (haveRecipe()) {
      // If not master ID, update the recipe.
      Log.d("AddCustomMisc", "Adding misc '" +
              misc.getName() + "' to recipe '" + mRecipe.getRecipeName() + "'");
      mRecipe.addIngredient(misc);
      mRecipe.save();
    }
    Log.d("AddCustomMisc", "Closing activity");
    finish();
  }
}
