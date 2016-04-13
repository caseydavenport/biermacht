package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.utils.Constants;

public class EditCustomFermentableActivity extends AddCustomFermentableActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Handle fermentable type selections
    fermentableTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        type = Constants.FERMENTABLE_TYPES.get(position);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

    // Set button text
    submitButton.setText(R.string.save);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();
    fermentable = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);
  }

  public void setInitialSearchableListSelection() {
    // Don't set the searchable list selector.
    // Initial values are set based on the fermentable we are passed
    // through the intent.
  }

  @Override
  public void getList() {
    // Get ingredient list
    super.getList();
  }

  @Override
  public void setValues() {
    nameViewText.setText(fermentable.getName());
    colorViewText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
    gravityViewText.setText(String.format("%2.3f", fermentable.getGravity()));
    descriptionViewText.setText(fermentable.getShortDescription());
    timeViewText.setText("60");
    amountViewText.setText("1");
    fermentableTypeSpinner.setSelection(Constants.FERMENTABLE_TYPES.indexOf(fermentable.getFermentableType()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
    return true;
  }

  @Override
  public void onDeletePressed() {
    new DatabaseAPI(this).deleteIngredientWithId(ingredientId, fermentable.getDatabaseId());
    finish();
  }

  @Override
  public void onFinished() {
    new DatabaseAPI(this).updateIngredient(fermentable, fermentable.getDatabaseId());
    finish();
  }
}

