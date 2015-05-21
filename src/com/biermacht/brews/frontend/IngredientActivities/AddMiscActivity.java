package com.biermacht.brews.frontend.IngredientActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.PlaceholderIngredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AddMiscActivity extends AddEditIngredientActivity {

  // Holds the currently selected misc, and misc being edited
  public Misc misc;

  // Editable rows to display
  public Spinner typeSpinner;
  public Spinner useSpinner;
  public Spinner unitsSpinner;

  // Spinner array declarations
  public ArrayList<String> typeArray;
  public ArrayList<String> useArray;
  public ArrayList<String> unitsArray;

  // Data storage for spinners
  public String type;
  public String use;
  public String units;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Initialize views and such here
    typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    unitsSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

    /************************************************************************
     ************* Add views *************************************************
     *************************************************************************/
    this.registerViews(Arrays.asList((View) useSpinner, (View) typeSpinner, (View) unitsSpinner));
    this.setViews(Arrays.asList(searchableListView, amountView, timeView, useSpinner, typeSpinner));

    // Set titles
    searchableListViewTitle.setText("Misc");

    // Set up type spinner
    typeArray = new ArrayList<String>();
    typeArray.add(Misc.TYPE_FINING);
    typeArray.add(Misc.TYPE_FLAVOR);
    typeArray.add(Misc.TYPE_HERB);
    typeArray.add(Misc.TYPE_SPICE);
    typeArray.add(Misc.TYPE_WATER_AGENT);
    typeArray.add(Misc.TYPE_OTHER);

    // Misc type spinner
    SpinnerAdapter miscTypeAdapter = new SpinnerAdapter(this, typeArray, "Misc Type");
    miscTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    typeSpinner.setAdapter(miscTypeAdapter);

    // Set up use spinner
    useArray = new ArrayList<String>();
    useArray.add(Misc.USE_BOIL);
    useArray.add(Misc.USE_BOTTLING);
    useArray.add(Misc.USE_MASH);
    useArray.add(Misc.USE_PRIMARY);
    useArray.add(Misc.USE_SECONDARY);

    // Misc use spinner.
    SpinnerAdapter miscUseAdapter = new SpinnerAdapter(this, useArray, "Misc Use");
    miscUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    useSpinner.setAdapter(miscUseAdapter);

    // Set up units spinner
    unitsArray = new ArrayList<String>();
    unitsArray.add(Units.TEASPOONS_FORMAL);
    unitsArray.add(Units.CUPS_FORMAL);
    unitsArray.add(Units.GRAMS_FORMAL);
    unitsArray.add(Units.GALLONS_FORMAL);
    unitsArray.add(Units.LITERS_FORMAL);
    unitsArray.add(Units.OUNCES_FORMAL);
    unitsArray.add(Units.ITEMS_FORMAL);
    unitsArray.add(Units.KILOGRAMS_FORMAL);
    unitsArray.add(Units.MILLILITERS_FORMAL);
    unitsArray.add(Units.PACKAGES_FORMAL);
    unitsArray.add(Units.POUNDS_FORMAL);
    unitsArray.add(Units.UNITS_FORMAL);
    Collections.sort(unitsArray);

    // Units spinner adapter
    SpinnerAdapter miscUnitsAdapter = new SpinnerAdapter(this, unitsArray, "Units");
    miscUnitsAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    unitsSpinner.setAdapter(miscUnitsAdapter);

    // Handle units selector here
    unitsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        units = unitsArray.get(position);
        Log.d("AddMiscActivity::onItemSelected", "Misc units selected: " + units);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });

    // Handle type selector here
    typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        type = typeArray.get(position);
        Log.d("AddMiscActivity::onItemSelected", "Misc type selected: " + type);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });

    // Handle use selector here
    useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        use = useArray.get(position);
        misc.setUse(use);
        Log.d("AddMiscActivity::onItemSelected", "Misc use selected: " + use);

        if (use.equals(Misc.USE_BOTTLING)) {
          timeView.setVisibility(View.GONE);
        }
        else {
          timeView.setVisibility(View.VISIBLE);
          String units = misc.getTimeUnits();
          timeViewTitle.setText(use + " Time" + " (" + units + ")");
        }
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });

    // Set button text
    submitButton.setText(R.string.add);

    // Set selections
    unitsSpinner.setSelection(0);
    typeSpinner.setSelection(0);
    useSpinner.setSelection(0);

    // Set initial position for searchable list
    setInitialSearchableListSelection();
  }

  @Override
  public void onMissedClick(View v) {
    super.onMissedClick(v);
  }

  @Override
  public void getList() {
    // Get the list of ingredients to show
    Log.d("AddMiscActivity::getList", "Getting miscs list");
    ingredientList = new ArrayList<Ingredient>();
    ingredientList.addAll(ingredientHandler.getMiscsList());

    // Add a placeholder ingredient.  When selected, allows user to create
    // a new custom ingredient.
    Log.d("AddMiscActivity::getList", "Adding placeholder ingredient");
    PlaceholderIngredient i = new PlaceholderIngredient("Create new");
    i.setShortDescription("Create a custom misc");
    ingredientList.add(0, i);
  }

  @Override
  public void createSpinner() {
    // Set up spinner
    Log.d("AddMiscActivity::createSpinner", "Creating misc spinner");
    adapter = new IngredientSpinnerAdapter(this, ingredientList, "Misc Selector", true);
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    spinnerView.setAdapter(adapter);
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(0);
  }

  @Override
  public void configureSearchableListListener() {

    searchableListListener = new OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // Handle the placeholder case
        if (filteredList.get(position).getType().equals(Ingredient.PLACEHOLDER)) {
          // Cancel the dialog
          cancelDialog();

          // Switch into AddCustom
          Intent intent = new Intent(AddMiscActivity.this, AddCustomMiscActivity.class);
          intent.putExtra(Constants.KEY_RECIPE, mRecipe);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          finish();
          return;
        }

        // Not a placeholder
        misc = (Misc) filteredList.get(position);
        setValues(misc);

        // Cancel dialog
        cancelDialog();
      }
    };
  }

  public void setValues(Misc m) {
    nameViewText.setText(m.getName());
    searchableListViewText.setText(m.getName());
    timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
    amountViewText.setText(String.format("%2.2f", m.getDisplayAmount()));
    typeSpinner.setSelection(typeArray.indexOf(m.getMiscType()));
    useSpinner.setSelection(useArray.indexOf(m.getUse()));
    unitsSpinner.setSelection(unitsArray.indexOf(Units.toFormal(m.getDisplayUnits())));
    amountViewTitle.setText("Amount (" + m.getDisplayUnits() + ")");
    timeViewTitle.setText(m.getUse() + " time");

    // Set units here
    units = Units.toFormal(m.getDisplayUnits());
  }

  @Override
  public void onFinished() {
    mRecipe.addIngredient(misc);
    mRecipe.save();
    finish();
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();

    misc.setName(name);
    misc.setTime(time);
    misc.setDisplayUnits(Units.toAbbreviation(units));
    misc.setDisplayAmount(amount, misc.getDisplayUnits());
    misc.setMiscType(type);
    misc.setUse(use);
  }

  @Override
  public void onCancelPressed() {
    finish();
  }

  @Override
  public void onDeletePressed() {

  }
}
