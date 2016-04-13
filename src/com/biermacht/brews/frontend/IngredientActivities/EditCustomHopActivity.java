package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.database.DatabaseAPI;

import java.util.Arrays;

public class EditCustomHopActivity extends EditHopActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set views
    this.setViews(Arrays.asList(nameView,
                                formSpinner,
                                useSpinner,
                                alphaAcidView,
                                descriptionView));

    // Set values for the given hop
    setValues(hop);
  }

  @Override
  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the ingredient we are passed
    // through the intent.
  }

  @Override
  public void onFinished() {
    new DatabaseAPI(this).updateIngredient(hop, hop.getDatabaseId());
    finish();
  }

  @Override
  public void onDeletePressed() {
    new DatabaseAPI(this).deleteIngredientWithId(ingredientId, hop.getDatabaseId());
    finish();
  }
}
