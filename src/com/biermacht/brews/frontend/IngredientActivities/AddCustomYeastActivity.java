package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.Constants;

public class AddCustomYeastActivity extends AddYeastActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Remove views we don't want
    mainView.removeView(amountView);
    mainView.removeView(searchableListView);

    // Add those we do.
    mainView.addView(labView);
    mainView.addView(productIdView);

    // Set initial values
    yeast = new Yeast("Custom yeast");
    setValues(yeast);
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();

    yeast.setShortDescription("Custom yeast");
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the new ingredient.
  }

  @Override
  public void onFinished() {
    DatabaseAPI.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, yeast, Constants.MASTER_RECIPE_ID, Constants.SNAPSHOT_NONE);
    finish();
  }
}
