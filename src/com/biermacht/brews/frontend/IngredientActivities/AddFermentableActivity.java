package com.biermacht.brews.frontend.IngredientActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.PlaceholderIngredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFermentableActivity extends AddEditIngredientActivity {

  // Holds the currently selected fermentable
  Fermentable fermentable;

  // Editable rows to display
  public View colorView;
  public View gravityView;
  public Spinner fermentableTypeSpinner;

  // Titles from rows
  public TextView colorViewTitle;
  public TextView gravityViewTitle;

  // Content from rows
  public TextView colorViewText;
  public TextView gravityViewText;

  // Storage for acquired values
  double gravity;
  double color;
  String type;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Initialize views and such here
    colorView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    gravityView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    /************************************************************************
     ************* Add views *************************************************
     *************************************************************************/
    this.registerViews(Arrays.asList(fermentableTypeSpinner, colorView, gravityView));
    this.setViews(Arrays.asList(searchableListView, timeView, amountView, colorView, gravityView));

    /************************************************************************
     ************* Get titles, set values   **********************************
     *************************************************************************/
    colorViewTitle = (TextView) colorView.findViewById(R.id.title);
    colorViewTitle.setText("SRM Color");

    gravityViewTitle = (TextView) gravityView.findViewById(R.id.title);
    gravityViewTitle.setText("Gravity Contribution");

    searchableListViewTitle.setText("Fermentable");
    amountViewTitle.setText("Amount " + "(" + Units.getFermentableUnits() + ")");

    // Acquire text views
    colorViewText = (TextView) colorView.findViewById(R.id.text);
    gravityViewText = (TextView) gravityView.findViewById(R.id.text);

    // Set button text
    submitButton.setText(R.string.add);

    // Set initial position for searchable list
    setInitialSearchableListSelection();
  }

  @Override
  public void onMissedClick(View v) {
    super.onMissedClick(v);
    Log.d("AddFermentableActivity", "Checking views for: " + v);

    if (v.equals(colorView)) {
      dialog = alertBuilder.editTextFloatAlert(colorViewText, colorViewTitle).create();
    }
    else if (v.equals(gravityView)) {
      dialog = alertBuilder.editTextFloatAlert(gravityViewText, gravityViewTitle).create();
    }
    else {
      Log.d("AddFermentableActivity", "View not found: " + v);
      return;
    }

    // Force keyboard open and show popup
    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    dialog.show();
  }

  @Override
  public void getList() {
    Log.d("AddFermentableActivityt", "Getting fermentables list");
    ingredientList = new ArrayList<Ingredient>();
    ingredientList.addAll(ingredientHandler.getFermentablesList());

    // Add a placeholder ingredient.  When selected, allows user to create
    // a new custom ingredient.
    Log.d("AddFermentableActivity", "Adding placeholder ingredient");
    PlaceholderIngredient i = new PlaceholderIngredient("Create new");
    i.setShortDescription("Create a custom fermentable");
    ingredientList.add(0, i);
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(0);
  }

  @Override
  public void createSpinner() {
    this.adapter = new IngredientSpinnerAdapter(this, ingredientList, "Fermentable", true);
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    spinnerView.setAdapter(this.adapter);

    fermentableTypeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView,
                                                        false);
    SpinnerAdapter adapter = new SpinnerAdapter(this, Constants.FERMENTABLE_TYPES, "Type");
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    fermentableTypeSpinner.setAdapter(adapter);

    // Handle fermentable type selections
    fermentableTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        type = Constants.FERMENTABLE_TYPES.get(position);
        Log.d("AddFermentableActivity", "Fermentable type selcted: " + type);
        if (mRecipe.getType().equals(Recipe.EXTRACT)) {
          timeView.setVisibility(View.VISIBLE);
          if (type.equals(Fermentable.TYPE_EXTRACT) || type.equals(Fermentable.TYPE_SUGAR)) {
            gravityViewText.setText("1.044");
            timeViewTitle.setText(R.string.boil_time);
            timeViewText.setText(mRecipe.getBoilTime() + "");
          }
          else if (type.equals(Fermentable.TYPE_GRAIN)) {
            gravityViewText.setText("1.037");
            timeViewTitle.setText(R.string.steep_time);
            timeViewText.setText(15 + "");
          }
          else {
            timeViewTitle.setText("Time");
          }
        }
        else {
          // TODO: Do we ever want to enter a time for mashes?
          timeView.setVisibility(View.GONE);
        }
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });
  }

  @Override
  public void configureSearchableListListener() {
    searchableListListener = new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position,
                              long id) {
        // Handle the placeholder case
        if (filteredList.get(position).getType().equals(Ingredient.PLACEHOLDER)) {
          // Cancel the dialog
          cancelDialog();

          // Switch into AddCustomFermentableActivity
          Intent intent = new Intent(AddFermentableActivity.this, AddCustomFermentableActivity
                  .class);
          intent.putExtra(Constants.KEY_RECIPE, mRecipe);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          finish();
          return;
        }

        // Not a placeholder
        fermentable = (Fermentable) filteredList.get(position);

        // Set whether we show boil or steep
        if (mRecipe.getType().equals(Recipe.EXTRACT)) {
          if (fermentable.getFermentableType().equals(Fermentable.TYPE_EXTRACT)) {
            timeViewTitle.setText(R.string.boil_time);
            timeViewText.setText(mRecipe.getBoilTime() + "");
          }
          else if (fermentable.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
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

        nameViewText.setText(fermentable.getName());
        searchableListViewText.setText(fermentable.getName());
        colorViewText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
        gravityViewText.setText(String.format("%2.3f", fermentable.getGravity()));
        amountViewText.setText(1 + "");
        timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
        type = fermentable.getFermentableType();

        // Cancel dialog
        cancelDialog();
      }
    };
  }

  @Override
  public void onDeletePressed() {
    // Must be overriden
  }

  @Override
  public void onCancelPressed() {
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    Log.d("AddFermentableActivity", "Acquiring values for: " + fermentable.getName());

    color = Double.parseDouble(colorViewText.getText().toString().replace(",", "."));
    gravity = Double.parseDouble(gravityViewText.getText().toString().replace(",", "."));

    fermentable.setName(name);
    fermentable.setTime(time);
    fermentable.setDisplayAmount(amount);
    fermentable.setLovibondColor(color);
    fermentable.setGravity(gravity);
    fermentable.setFermentableType(type);
  }

  @Override
  public void onFinished() {
    mRecipe.addIngredient(fermentable);
    mRecipe.save();
    finish();
  }
}
