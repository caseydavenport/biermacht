package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
                                amountView,
                                descriptionView));

    // Override the onItemSelectedListener so that when a user is selected,
    // we don't show the time view (which is not appropriate for this activity).
    useSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        use = useArray.get(position);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

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
