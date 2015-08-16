package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

import java.util.Arrays;

public class EditFermentableActivity extends AddFermentableActivity {

  public Fermentable selectedFermentable;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Set views
    this.setViews(Arrays.asList(searchableListView, timeView, amountView,
                                colorView, gravityView));

    // Set button text
    submitButton.setText(R.string.save);

    // Set initial selection
    this.setInitialSearchableListSelection();
  }

  @Override
  public void getValuesFromIntent() {
    // Get the recipe from calling activity
    super.getValuesFromIntent();

    // Get the ingredient as well
    long grainId = getIntent().getLongExtra(Constants.KEY_INGREDIENT_ID, Constants.INVALID_ID);
    fermentable = (Fermentable) DatabaseAPI.getIngredientWithId(grainId);
    Log.d("EditFermentableActivity::getValuesFromIntent", "Retrieved fermentable: " + fermentable.getName());
  }

  @Override
  public void getList() {
    super.getList();
    Log.d("EditFermentableActivity::getList", "Getting fermentables list");

    // Remove the placeholder ingredient
    Log.d("EditFermentableActivity::getList", "Removing placeholder ingredient");
    ingredientList.remove(0);

    if (! ingredientList.contains(fermentable)) {
      Log.d("EditFermentableActivity::getList", "Adding custom fermentable to list: " + fermentable.getName());
      ingredientList.add(0, fermentable);
    }
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(ingredientList.indexOf(fermentable));
  }

  @Override
  public void configureSearchableListListener() {
    searchableListListener = new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        selectedFermentable = (Fermentable) filteredList.get(position);

        // Set whether we show boil or steep
        if (mRecipe.getType().equals(Recipe.EXTRACT)) {
          if (selectedFermentable.getFermentableType().equals(Fermentable.TYPE_EXTRACT)) {
            timeViewTitle.setText(R.string.boil_time);
            timeViewText.setText(mRecipe.getBoilTime() + "");
          }
          else if (selectedFermentable.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
            timeViewTitle.setText(R.string.steep_time);
            timeViewText.setText(15 + "");
          }
          else {
            timeViewTitle.setText("Time");
          }
        }
        else {
          // TODO: Support extract / adjunct times for all-grain recipes.
          timeView.setVisibility(View.GONE);
        }

        nameViewText.setText(selectedFermentable.getName());
        searchableListViewText.setText(selectedFermentable.getName());
        colorViewText.setText(String.format("%2.2f", selectedFermentable.getLovibondColor()));
        gravityViewText.setText(String.format("%2.3f", selectedFermentable.getGravity()));
        amountViewText.setText(String.format("%2.2f", fermentable.getDisplayAmount()));
        timeViewText.setText(String.format("%d", fermentable.getTime()));
        type = selectedFermentable.getFermentableType();

        // Cancel dialog.
        if (dialog != null) {
          dialog.cancel();
          dialog = null;
        }
      }
    };
  }

  public void setInitialSearchableListSelection() {
    // Get the index of the ingredient and set it.
    int index = ingredientList.indexOf(fermentable);
    searchableListListener.onItemClick(null, null, index, 1);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
    return true;
  }

  @Override
  public void onDeletePressed() {
    DatabaseAPI.deleteIngredientWithId(ingredientId, Constants.DATABASE_DEFAULT);
    finish();
  }

  @Override
  public void onFinished() {
    // Update the ingredient, and finish the activity
    DatabaseAPI.updateIngredient(fermentable, Constants.DATABASE_DEFAULT);
    finish();
  }
}

