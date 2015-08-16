package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

public class EditMiscActivity extends AddMiscActivity {

  // Holds the currently selected misc
  Misc selectedMisc;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    typeSpinner.setSelection(typeArray.indexOf(misc.getMiscType()));
    useSpinner.setSelection(useArray.indexOf(misc.getUse()));

    // Set button text
    submitButton.setText(R.string.save);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // Acquire misc
    misc = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);
  }

  @Override
  public void getList() {
    super.getList();
    Log.d("EditMiscActivity::getList", "Getting miscs list");

    // Remove the placeholder ingredient
    Log.d("EditMiscActivity::getList", "Removing placeholder ingredient");
    ingredientList.remove(0);

    // If this misc is not in the array, add it
    if (! ingredientList.contains(misc)) {
      Log.d("EditMiscActivity::getList", "Adding custom misc to list: " + misc.getName());
      ingredientList.add(0, misc);
    }
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(ingredientList.indexOf(misc));
  }

  @Override
  public void configureSearchableListListener() {
    searchableListListener = new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        selectedMisc = (Misc) filteredList.get(position);

        // Set values
        setValues(selectedMisc);

        // Cancel dialog.
        if (dialog != null) {
          dialog.cancel();
          dialog = null;
        }
      }
    };
  }

  public void setValues(Misc selMisc) {
    nameViewText.setText(selMisc.getName());
    searchableListViewText.setText(selMisc.getName());
    timeViewText.setText(String.format("%d", misc.getTime()));
    amountViewText.setText(String.format("%2.2f", misc.getDisplayAmount()));
    typeSpinner.setSelection(typeArray.indexOf(selMisc.getMiscType()));
    unitsSpinner.setSelection(unitsArray.indexOf(Units.toFormal(selMisc.getDisplayUnits())));
    useSpinner.setSelection(useArray.indexOf(selMisc.getUse()));
    amountViewTitle.setText("Amount (" + selMisc.getDisplayUnits() + ")");
    timeViewTitle.setText(selMisc.getUse() + " time");

    // Set units here
    units = Units.toFormal(selMisc.getDisplayUnits());
  }

  public void setInitialSearchableListSelection() {
    setValues(misc);
  }

  @Override
  public void onFinished() {
    DatabaseAPI.updateIngredient(misc, Constants.DATABASE_DEFAULT);
    finish();
  }

  @Override
  public void onDeletePressed() {
    DatabaseAPI.deleteIngredientWithId(misc.getId(), Constants.DATABASE_DEFAULT);
    finish();
  }
}