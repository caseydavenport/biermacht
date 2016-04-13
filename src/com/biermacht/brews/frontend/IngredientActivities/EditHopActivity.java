package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.utils.Constants;

public class EditHopActivity extends AddHopsActivity {

  // Holds the currently selected hop
  Hop selectedHop;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Set listeners
    useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        use = useArray.get(position);

        if (use.equals(Hop.USE_DRY_HOP)) {
          timeViewTitle.setText("Time (days)");
          timeView.setVisibility(View.VISIBLE);
        }
        else if (use.equals(Hop.USE_FIRST_WORT)) {
          timeView.setVisibility(View.GONE);
        }
        else {
          timeViewTitle.setText("Time (mins)");
          timeView.setVisibility(View.VISIBLE);
        }
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

    // Set spinners
    formSpinner.setSelection(formArray.indexOf(form));
    useSpinner.setSelection(useArray.indexOf(use));

    // Set button text
    submitButton.setText(R.string.save);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // Acquire Hop
    hop = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);

    // Get the use and form
    use = hop.getUse();
    form = hop.getForm();
  }

  @Override
  public void configureSearchableListListener() {
    searchableListListener = new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        selectedHop = (Hop) filteredList.get(position);

        // Set values for this hop.
        setValues(selectedHop);

        // Cancel dialog.
        if (dialog != null) {
          dialog.cancel();
          dialog = null;
        }
      }
    };
  }

  public void setValues(Hop h) {
    nameViewText.setText(h.getName());
    searchableListViewText.setText(h.getName());
    timeViewText.setText(String.format("%d", hop.getDisplayTime()));
    alphaAcidViewText.setText(String.format("%2.2f", h.getAlphaAcidContent()));
    amountViewText.setText(String.format("%2.2f", hop.getDisplayAmount()));
    descriptionViewText.setText(hop.getDescription());
  }

  public void setInitialSearchableListSelection() {
    setValues(hop);
  }

  @Override
  public void getList() {
    super.getList();
    Log.d("EditHopActivity", "Getting hops list");

    // Remove the placeholder ingredient
    Log.d("EditHopActivity", "Removing placeholder ingredient");
    ingredientList.remove(0);

    if (! ingredientList.contains(hop)) {
      Log.d("EditHopActivity", "Adding custom hop to list: " + hop.getName());
      ingredientList.add(0, hop);
    }
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(ingredientList.indexOf(hop));
  }

  public void onFinished() {
    new DatabaseAPI(this).updateIngredient(hop, Constants.DATABASE_DEFAULT);
    finish();
  }

  public void onDeletePressed() {
    new DatabaseAPI(this).deleteIngredientWithId(ingredientId, Constants.DATABASE_DEFAULT);
    finish();
  }
}
