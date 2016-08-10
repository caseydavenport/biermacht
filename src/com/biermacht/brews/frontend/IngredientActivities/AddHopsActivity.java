package com.biermacht.brews.frontend.IngredientActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.PlaceholderIngredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;
import java.util.Arrays;

public class AddHopsActivity extends AddEditIngredientActivity {

  // Editable rows to display
  public Spinner useSpinner;
  public Spinner formSpinner;
  public View alphaAcidView;
  public View descriptionView;

  // Titles from rows
  public TextView alphaAcidViewTitle;
  public TextView descriptionViewTitle;

  // Content from rows
  public TextView alphaAcidViewText;
  public TextView descriptionViewText;

  // Spinner array declarations
  public ArrayList<String> formArray;
  public ArrayList<String> useArray;

  // Holds the currently selected hop, and hop being edited
  Hop hop;

  // Storage for acquired values
  String use;
  String form;
  double alpha;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Initialize views and such here
    alphaAcidView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    descriptionView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    formSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

    /************************************************************************
     ************* Add views *************************************************
     *************************************************************************/
    this.registerViews(Arrays.asList(alphaAcidView, descriptionView, useSpinner, formSpinner));
    this.setViews(Arrays.asList(searchableListView, amountView, timeView, formSpinner,
                                useSpinner, alphaAcidView));

    // Inflate title views.
    alphaAcidViewTitle = (TextView) alphaAcidView.findViewById(R.id.title);
    descriptionViewTitle = (TextView) descriptionView.findViewById(R.id.title);

    // Configure titles
    alphaAcidViewTitle.setText("Alpha Acids (%)");
    amountViewTitle.setText("Amount " + "(" + Units.getHopUnits() + ")");
    searchableListViewTitle.setText("Hop");
    descriptionViewTitle.setText("Description");

    // Get content views
    alphaAcidViewText = (TextView) alphaAcidView.findViewById(R.id.text);
    descriptionViewText = (TextView) descriptionView.findViewById(R.id.text);

    // Hop form spinner
    formArray = Constants.HOP_FORMS;
    SpinnerAdapter hopFormAdapter = new SpinnerAdapter(this, formArray, "Form");
    hopFormAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    formSpinner.setAdapter(hopFormAdapter);

    // Set up hop use spinner
    useArray = Constants.HOP_USES;
    SpinnerAdapter hopUseAdapter = new SpinnerAdapter(this, useArray, "Usage");
    hopUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    useSpinner.setAdapter(hopUseAdapter);

    // Set listeners
    useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        use = useArray.get(position);

        if (use.equals(Hop.USE_DRY_HOP)) {
          timeViewTitle.setText("Time (days)");
          timeViewText.setText(5 + "");
          timeView.setVisibility(View.VISIBLE);
        }
        else if (use.equals(Hop.USE_FIRST_WORT)) {
          // First wort hops are included in the full boil.
          timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
          timeView.setVisibility(View.GONE);
        }
        else {
          timeViewTitle.setText("Time (mins)");
          timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
          timeView.setVisibility(View.VISIBLE);
        }
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

    formSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        form = formArray.get(position);
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    });

    // Set selections
    formSpinner.setSelection(0);
    useSpinner.setSelection(0);

    // Set button text
    submitButton.setText(R.string.add);

    // Set initial position for searchable list
    setInitialSearchableListSelection();
  }

  @Override
  public void onMissedClick(View v) {
    super.onMissedClick(v);

    if (v.equals(alphaAcidView)) {
      dialog = alertBuilder.editTextFloatAlert(alphaAcidViewText, alphaAcidViewTitle).create();
    }
    else if (v.equals(descriptionView)) {
      dialog = alertBuilder.editTextStringAlert(descriptionViewText, descriptionViewTitle).create();
    }
    else {
      return;
    }

    // Force keyboard open and show popup
    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    dialog.show();
  }

  @Override
  public void getList() {
    Log.d("AddHopsActivity", "Getting hops list");
    ingredientList = new ArrayList<Ingredient>();
    ingredientList.addAll(ingredientHandler.getHopsList());
    Log.d("AddHopsActivity", "Found " + ingredientList.size() + " hop(s)");

    // Add a placeholder ingredient.  When selected, allows user to create
    // a new custom ingredient.
    Log.d("AddHopsActivity", "Adding placeholder ingredient");
    PlaceholderIngredient i = new PlaceholderIngredient("Create new");
    i.setShortDescription("Create a custom hop");
    ingredientList.add(0, i);
    Log.d("AddHopsActivity", "Total with placeholder: " + ingredientList.size() + " hop(s)");
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(0);
  }

  @Override
  public void createSpinner() {
    // Ingredient Spinner
    adapter = new IngredientSpinnerAdapter(this, ingredientList, "Hop", true);
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    spinnerView.setAdapter(adapter);
  }

  @Override
  public void configureSpinnerListener() {
    // We don't use the spinner for this activity.
  }

  @Override
  public void configureSearchableListListener() {
    searchableListListener = new OnItemClickListener() {

      public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position,
                              long id) {
        // Handle the placeholder case
        if (filteredList.get(position).getType().equals(Ingredient.PLACEHOLDER)) {
          // Cancel the dialog
          cancelDialog();

          // Switch into AddCustom
          Intent intent = new Intent(AddHopsActivity.this, AddCustomHopsActivity.class);
          intent.putExtra(Constants.KEY_RECIPE, mRecipe);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          finish();
          return;
        }

        // Not a placeholder
        hop = (Hop) filteredList.get(position);

        setValues(hop);

        // Cancel dialog
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
    timeViewText.setText(mRecipe.getBoilTime() + "");
    alphaAcidViewText.setText(String.format("%2.2f", h.getAlphaAcidContent()));
    descriptionViewText.setText(h.getDescription());

    // If an amount has been specified then show it.  Otherewise,
    // default to 1.0oz.
    if (hop.getDisplayAmount() != 0) {
      amountViewText.setText(String.format("%2.2f", hop.getDisplayAmount()));
    }
    else {
      amountViewText.setText("1.0");
    }
  }

  @Override
  public void onDeletePressed() {
    // Must be overriden.  Add activities don't have a delete button.
  }

  @Override
  public void onCancelPressed() {
    finish();
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    alpha = Double.parseDouble(alphaAcidViewText.getText().toString().replace(",", "."));
    String description = descriptionViewText.getText().toString();

    hop.setName(name);
    hop.setAlphaAcidContent(alpha);
    hop.setDisplayAmount(amount);
    hop.setUse(use);
    hop.setForm(form);
    hop.setDisplayTime(time);
    hop.setDescription(description);
  }

  @Override
  public void onFinished() {
    mRecipe.addIngredient(hop);
    mRecipe.save(AddHopsActivity.this);
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_add_hops, menu);
    return true;
  }
}
