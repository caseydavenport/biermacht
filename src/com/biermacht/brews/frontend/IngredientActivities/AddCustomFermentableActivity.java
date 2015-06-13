package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

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
    Log.d("AddCustomFermentableActivity::onCreate", "Initializing views");
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
    Log.d("AddCustomFermentableActivity::onMissedClick", "Checking views for: " + v);

    AlertDialog alert;
    if (v.equals(descriptionView)) {
      Log.d("AddCustomFermentableActivity::onMissedClick", "Displaying descriptionView edit alert");
      alert = alertBuilder.editTextMultilineStringAlert(descriptionViewText, descriptionViewTitle).create();
    }
    else {
      Log.d("AddCustomFermentableActivity::onMissedClick", "View not found: " + v);
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
    Log.d("AddCustomFermentableActivity", "No searchable list for this activity");
  }

  // We need this because we don't use spinners in this activity
  public void setValues() {
    Log.d("AddCustomFermentableActivity::setValues", "Setting textEdit values");

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
    Log.d("AddCustomFermentableActivity::acquireValues", "Acquiring values for: " + fermentable.getName());
    description = descriptionViewText.getText().toString();

    // Set to user provided values
    fermentable.setShortDescription(description);
  }

  @Override
  public void onFinished() {
    Log.d("AddCustomFermentableActivity::onFinished", "Adding fermentable to db_custom: " + fermentable.getName());
    Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, fermentable, Constants.MASTER_RECIPE_ID);
    if (haveRecipe()) {
      // If not master ID, update the recipe.
      Log.d("AddCustomFermentableActivity::onFinished", "Adding fermentable '" +
              fermentable.getName() + "' to recipe '" + mRecipe.getRecipeName() + "'");
      mRecipe.addIngredient(fermentable);
      mRecipe.save();
    }
    Log.d("AddCustomFermentableActivity::onFinished", "Closing activity");
    finish();
  }
}
