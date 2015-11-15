package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.database.DatabaseAPI;

import java.util.Arrays;

public class EditCustomMiscActivity extends EditMiscActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Remove the timeView, making it impossible to become visible.  It doesn't make sense
    // to ever have a timeView for this Activity.
    mainView.removeView(timeView);

    // Set views we want visible
    this.setViews(Arrays.asList(nameView, unitsSpinner, useSpinner, typeSpinner));

    // Set values for the given misc.
    setValues(misc);
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the ingredient passed
    // via the Intent.
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    misc.setShortDescription("Custom misc");
    misc.setUseFor("Custom");
  }

  @Override
  public void onFinished() {
    DatabaseAPI.updateIngredient(misc, misc.getDatabaseId());
    finish();
  }

  @Override
  public void onDeletePressed() {
    DatabaseAPI.deleteIngredientWithId(ingredientId, misc.getDatabaseId());
    finish();
  }
}
