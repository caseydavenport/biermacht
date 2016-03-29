package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.utils.Constants;

import java.util.Arrays;

public class AddCustomFermentableActivity extends AddFermentableActivity {

  // Views for rows
  public View descriptionView;

  // Titles from rows
  public TextView descriptionViewTitle;

  // Content from rows
  public TextView descriptionViewText;

  // Storage for acquired values
  String description;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create the description view
    descriptionView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    descriptionView.setOnClickListener(onClickListener);
    descriptionViewText = (TextView) descriptionView.findViewById(R.id.text);
    descriptionViewTitle = (TextView) descriptionView.findViewById(R.id.title);
    descriptionViewText.setText("No Description Provided");
    descriptionViewTitle.setText("Description");

    // Set views
    Log.d("AddCustomFerm", "Initializing views");
    this.registerViews(Arrays.asList(descriptionView));
    this.setViews(Arrays.asList(nameView, fermentableTypeSpinner, timeView, amountView, colorView, gravityView, descriptionView));
    if (! haveRecipe()) {
      timeView.setVisibility(View.GONE);
      amountView.setVisibility(View.GONE);
    }

    // Set initial values
    setValues();
  }

  @Override
  public void onMissedClick(View v) {
    super.onMissedClick(v);
    Log.d("AddCustomFerm", "Checking views for: " + v);

    AlertDialog alert;
    if (v.equals(descriptionView)) {
      Log.d("AddCustomFerm", "Displaying descriptionView edit alert");
      alert = alertBuilder.editTextMultilineStringAlert(descriptionViewText, descriptionViewTitle).create();
    }
    else {
      Log.d("AddCustomFerm", "View not found: " + v);
      return;
    }

    // Force keyboard open and show popup
    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    alert.show();
  }

  @Override
  public void setInitialSearchableListSelection() {
    // Override this method becaue there is no searchable list for this activity.  Since this activity
    // is meant to add new custom Fermentables, rather than select a fermentable from a list.
    Log.d("AddCustomFerm", "No searchable list for this activity");
  }

  // We need this because we don't use spinners in this activity
  public void setValues() {
    Log.d("AddCustomFerm", "Setting textEdit values");

    // Create new fermentable
    fermentable = new Fermentable("New Fermentable");

    // Set values
    nameViewText.setText(fermentable.getName());
    colorViewText.setText("5.00");
    gravityViewText.setText("1.037");
    timeViewText.setText("60");
    amountViewText.setText("1");
    fermentableTypeSpinner.setSelection(0);
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    Log.d("AddCustomFerm", "Acquiring values for: " + fermentable.getName());
    description = descriptionViewText.getText().toString();

    // Set to user provided values
    fermentable.setShortDescription(description);
  }

  @Override
  public void onFinished() {
    Log.d("AddCustomFerm", "Adding fermentable to db_custom: " + fermentable.getName());
    DatabaseAPI.addIngredient(Constants.DATABASE_CUSTOM, fermentable, Constants.MASTER_RECIPE_ID);
    if (haveRecipe()) {
      // If not master ID, update the recipe.
      Log.d("AddCustomFerm", "Adding fermentable '" +
              fermentable.getName() + "' to recipe '" + mRecipe.getRecipeName() + "'");
      mRecipe.addIngredient(fermentable);
      mRecipe.save();
    }
    Log.d("AddCustomFerm", "Closing activity");
    finish();
  }
}
