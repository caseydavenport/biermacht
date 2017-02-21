package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.adapters.FermentableArrayAdapter;
import com.biermacht.brews.frontend.adapters.RecipeSpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;

public class EfficiencyCalculatorFragment extends Fragment implements BiermachtFragment {
  private static int resource = R.layout.fragment_efficiency_calculator;
  View pageView;
  Context c;

  // Edit texts which the user fills out.
  EditText gravityEditText;
  EditText wortVolumeEditText;
  EditText tempEditText;

  // Titles
  TextView tempTitleView;
  TextView wortVolumeTitle;

  // Views which the calculator populates.
  TextView efficiencyView;
  TextView maxGravityView;

  // Other helper views.
  ScrollView scrollView;
  Spinner recipeSpinner;
  Switch showListSwitch;

  // Special ListView and adapter to implement "swipe-to-dismiss".
  public DragSortListView listView;
  public FermentableArrayAdapter arrayAdapter;

  Recipe selectedRecipe;

  private ArrayList<Fermentable> ingredientList;
  private AdapterView.OnItemClickListener mClickListener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);
    setHasOptionsMenu(true);

    // Get context
    c = getActivity();

    // Get views we care about
    gravityEditText = (EditText) pageView.findViewById(R.id.measured_gravity_edit_text);
    wortVolumeEditText = (EditText) pageView.findViewById(R.id.final_volume_edit_text);
    wortVolumeTitle = (TextView) pageView.findViewById(R.id.final_volume_title);
    tempEditText = (EditText) pageView.findViewById(R.id.meas_temp_edit_text);
    tempTitleView = (TextView) pageView.findViewById(R.id.meas_temp_title);
    efficiencyView = (TextView) pageView.findViewById(R.id.calculated_efficiency_view);
    maxGravityView = (TextView) pageView.findViewById(R.id.max_gravity_view);
    listView = (DragSortListView) pageView.findViewById(R.id.ingredient_list);
    scrollView = (ScrollView) pageView.findViewById(R.id.scrollview);
    recipeSpinner = (Spinner) pageView.findViewById(R.id.recipe_spinner);
    showListSwitch = (Switch) pageView.findViewById(R.id.show_list_switch);

    // Configure the switch onSetListener.
    showListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
          listView.setVisibility(View.VISIBLE);
        }
        else {
          listView.setVisibility(View.GONE);
        }

      }
    });

    // By default, show the ingredient list.
    showListSwitch.setChecked(true);

    // Set the temperature view to an appropriate default value.
    if (Units.getTemperatureUnits().equals(Units.CELSIUS)) {
      tempTitleView.setText(String.format("Temp (%s)", Units.CELSIUS));
      tempEditText.setText(String.format("%2.1f", 21.0));
      tempEditText.setHint("21.0");
    }
    else {
      tempTitleView.setText(String.format("Temp (%s)", Units.FAHRENHEIT));
      tempEditText.setText(String.format("%2.0f", 68.0));
      tempEditText.setHint("68");
    }

    // Set the volume based on configured units.
    if (Units.getVolumeUnits().equals(Units.LITERS)) {
      wortVolumeTitle.setText("Measured Volume (L)");
    } else {
      wortVolumeTitle.setText("Measured Volume (gal)");
    }

    // Initialize the ingredient list.
    ingredientList = new ArrayList<>();

    // Configure the recipe spinner adapter.
    DatabaseAPI db = new DatabaseAPI(this.getActivity().getApplicationContext());
    final ArrayList<Recipe> recipes = db.getRecipeList();

    // TODO: Better handling of case where there are no recipes.
    if (recipes.size() == 0) {
      Recipe r = new Recipe();
      recipes.add(r);
    }

    RecipeSpinnerAdapter spinnerAdapter = new RecipeSpinnerAdapter(this.getActivity().getApplicationContext(), recipes, "Recipes");
    recipeSpinner.setAdapter(spinnerAdapter);

    // Configure the recipe spinner listener.
    // Handle fermentable type selections
    recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        selectedRecipe = recipes.get(position);
        Log.d("EfficiencyCalc", "Selected recipe: " + selectedRecipe.toString());

        // Update the fermentables list and listview.
        populateFermentablesListFromRecipe();

        // Set some text.
        maxGravityView.setText(String.format("Available Gravity Points: %2.2f", BrewCalculator.GravityPoints(ingredientList)));
        wortVolumeEditText.setText(String.format("%2.2f", selectedRecipe.getDisplayBoilSize()));
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });
    recipeSpinner.setSelection(0);

    // Initialize the list view.
    populateFermentablesListFromRecipe();

    return pageView;
  }

  private void populateFermentablesListFromRecipe() {
    // If a recipe is selected, then use that ingredient list. Otherwise use an empty list.
    if (selectedRecipe != null) {
      ingredientList.removeAll(ingredientList);
      ingredientList.addAll(selectedRecipe.getFermentablesList());
    }

    arrayAdapter = new FermentableArrayAdapter(getActivity(), ingredientList);
    listView.setAdapter(arrayAdapter);
    listView.setDropListener(onDrop);
    listView.setRemoveListener(onRemove);
    listView.setDragEnabled(true);

    Utils.setListViewHeightBasedOnChildren(listView);
  }

  // Callback for when a fermentable is removed from the list.
  public DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
    @Override
    public void remove(int pos) {
      Log.d("EfficiencyCalc", "Removing ingredient at position: " + pos);
      arrayAdapter.remove(ingredientList.get(pos));
      Utils.setListViewHeightBasedOnChildren(listView);
      maxGravityView.setText(String.format("Available Gravity Points: %2.3f", BrewCalculator.GravityPoints(ingredientList)));
    }
  };

  // We don't currently support OnDrop actions here.
  public DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
    @Override
    public void drop(int oldPosition, int newPosition) {
    }
  };

  public void calculate() {
    double gravity;
    double volume;
    double temp;
    double eff = 0.0;

    // Acquire data.  If an invalid double is supplied, short-circuit, but don't crash.
    try {
      gravity = Double.parseDouble(gravityEditText.getText().toString().replace(",", "" + "."));
      volume = Double.parseDouble(wortVolumeEditText.getText().toString().replace(",", "."));
      temp = Double.parseDouble(tempEditText.getText().toString().replace(",", "."));
    } catch (Exception e) {
      return;
    }

    // If we're in metric mode, convert the volume to gallons for the calculation below.
    if (Units.getVolumeUnits().equals(Units.LITERS)) {
      volume = Units.litersToGallons(volume);
    }

    // Convert temp.
    if (Units.getTemperatureUnits().equals(Units.CELSIUS)) {
      temp = Units.celsiusToFahrenheit(temp);
    }

    // Adjust the measured gravity.
    double adjustedGravity = BrewCalculator.adjustGravityForTemp(gravity, temp, 68);

    // Determine total gravity points in the wort.
    double measuredGravityPoints = (adjustedGravity - 1) * 1000 * volume;

    // Ideal gravity points.
    double idealGravityPoints = BrewCalculator.GravityPoints(ingredientList);

    eff = (measuredGravityPoints / idealGravityPoints) * 100;
    efficiencyView.setText(String.format("%2.2f", eff));
  }

  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_efficiency_menu, menu);
  }

  //**************************************************************************
  // The following set of methods implement the Biermacht Fragment Interface
  //**************************************************************************
  @Override
  public void update() {
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }

  @Override
  public String name() {
    return "Efficiency Calculator";
  }

  @Override
  public void handleClick(View v) {
    if (v.getId() == R.id.calculate_button) {
      this.calculate();

      // The page is a bit long - scroll to the bottom so the user can see the
      // calculation output.
      scrollView.post(new Runnable() {
        @Override
        public void run() {
          scrollView.fullScroll(View.FOCUS_DOWN);
        }
      });
    }
  }
}
