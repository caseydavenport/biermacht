package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.BeerStyleSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.MashProfileSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Callbacks.BooleanCallback;
import com.biermacht.brews.utils.Callbacks.Callback;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddRecipeActivity extends AddEditActivity {

  // Rows to be displayed
  public View efficiencyView;
  public View batchSizeView;
  public View boilSizeView;
  public Spinner styleSpinner;
  public Spinner typeSpinner;
  public Spinner profileSpinner;

  // Titles
  public TextView efficiencyViewTitle;
  public TextView batchSizeViewTitle;
  public TextView boilSizeViewTitle;

  // Contents
  public TextView efficiencyViewText;
  public TextView batchSizeViewText;
  public TextView boilSizeViewText;

  // Data storage declarations
  public BeerStyle style;
  public MashProfile profile;
  public String type;
  public double efficiency;

  // Spinner array declarations
  public ArrayList<BeerStyle> styleArray;
  public ArrayList<String> typeArray;
  public ArrayList<MashProfile> profileArray;

  // Callbacks
  public BooleanCallback boilVolumeCallback;

  // Whether or not to open the DisplayRecipeActivity when the new 
  // recipe is saved.  This defaults to true.
  public boolean displayOnCreate = true;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Create the ingredient handler.
    this.ingredientHandler = new IngredientHandler(AddRecipeActivity.this);

    // Initialize each of the rows to be displayed
    mainView = (ViewGroup) findViewById(R.id.main_layout);
    efficiencyView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    batchSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    ;
    boilSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    // Set onClickListeners for edit text views
    efficiencyView.setOnClickListener(onClickListener);
    batchSizeView.setOnClickListener(onClickListener);
    boilSizeView.setOnClickListener(onClickListener);

    /************************************************************************
     ************* Add views to main view  **********************************
     *************************************************************************/
    mainView.removeAllViews();
    mainView.addView(nameView);
    mainView.addView(styleSpinner);
    mainView.addView(typeSpinner);
    mainView.addView(profileSpinner);
    mainView.addView(timeView);
    mainView.addView(efficiencyView);
    mainView.addView(batchSizeView);
    mainView.addView(boilSizeView);

    /************************************************************************
     ************* Get titles, set values   **********************************
     *************************************************************************/
    nameViewTitle.setText("Recipe Name");
    timeViewTitle.setText("Boil Time (mins)");

    efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
    efficiencyViewTitle.setText("Predicted Efficiency");

    batchSizeViewTitle = (TextView) batchSizeView.findViewById(R.id.title);
    batchSizeViewTitle.setText("Batch Size (" + Units.getVolumeUnits() + ")");

    boilSizeViewTitle = (TextView) boilSizeView.findViewById(R.id.title);
    boilSizeViewTitle.setText("Boil Size (" + Units.getVolumeUnits() + ")");

    /************************************************************************
     ************* Get content views, set values   **************************
     *************************************************************************/
    nameViewText.setText(mRecipe.getRecipeName());
    timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));

    efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
    efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

    batchSizeViewText = (TextView) batchSizeView.findViewById(R.id.text);
    batchSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBatchSize()));

    boilSizeViewText = (TextView) boilSizeView.findViewById(R.id.text);
    boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
  }

  @Override
  public void onMissedClick(View v) {
    AlertDialog alert;
    if (v.equals(efficiencyView)) {
      alert = alertBuilder.editTextFloatAlert(efficiencyViewText, efficiencyViewTitle).create();
    }
    else if (v.equals(batchSizeView)) {
      alert = alertBuilder.editTextFloatAlert(batchSizeViewText, batchSizeViewTitle).create();
    }
    else if (v.equals(boilSizeView)) {
      alert = alertBuilder.editTextFloatCheckBoxAlert(boilSizeViewText, boilSizeViewTitle,
                                                      mRecipe.getCalculateBoilVolume(),
                                                      boilVolumeCallback).create();
    }
    else {
      return;
    }

    // Force keyboard open and show popup
    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    alert.show();
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // Determine if we should display the recipe, or just finish upon save.
    displayOnCreate = getIntent().getBooleanExtra(Constants.DISPLAY_ON_CREATE, true);
  }

  @Override
  public void createCallback() {
    // Default callback, called when alertBuilders are finished.
    // Allows us to update fields that are dependent on other fields which may have been changed
    // by the user..
    callback = new Callback() {
      @Override
      public void call() {
        try {
          // Get new values
          Log.d("AddRecipeActivity", "Calling callback.");
          acquireValues();
        } catch (Exception e) {
          Log.d("AddRecipeActivity", "Exception in callback from alert dialog");
          e.printStackTrace();
        }
        // Update fields here
        boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
      }
    };

    // Callback for when boilVolume is updated.  We need to check if the option to auto-calc
    // boil volume has changed via user selection.
    boilVolumeCallback = new BooleanCallback() {
      @Override
      public void call(boolean b) {
        Log.d("AddRecipeActivity", "Boil volume checkbox pressed.");
        mRecipe.setCalculateBoilVolume(b);
        boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
      }
    };
  }

  @Override
  public void onRecipeNotFound() {
    Log.d("AddRecipeActivity", "onRecipeNotFound because we're creating one!");
    // Set values
    mRecipe = new Recipe("New Recipe");
    style = mRecipe.getStyle();
    profile = mRecipe.getMashProfile();
    type = mRecipe.getType();
    efficiency = mRecipe.getEfficiency();
  }

  @Override
  public void createSpinner() {
    // Set up style spinner here
    styleSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    BeerStyleSpinnerAdapter styleAdapter = new BeerStyleSpinnerAdapter(this, styleArray);
    styleAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    styleSpinner.setAdapter(styleAdapter);

    // Set up mash profile spinner
    profileSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
    profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    profileSpinner.setAdapter(profileAdapter);

    // Set up brew type spinner
    typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
    SpinnerAdapter typeAdapter = new SpinnerAdapter(this, typeArray, "Recipe Type");
    typeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    typeSpinner.setAdapter(typeAdapter);
  }

  @Override
  public void setInitialSpinnerSelection() {
    typeSpinner.setSelection(typeArray.indexOf(mRecipe.getType()));
    profileSpinner.setSelection(profileArray.indexOf(mRecipe.getMashProfile()));
    styleSpinner.setSelection(styleArray.indexOf(mRecipe.getStyle()));
  }

  @Override
  public void configureSpinnerListener() {
    // Handle beer style selector here
    styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        style = styleArray.get(position);
        callback.call();
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });

    // Handle mash profile selector here
    profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        profile = profileArray.get(position);
        Log.d("AddRecipeActivity", "Got profile: " + profile.getName());
        callback.call();
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });

    // Handle type selector here
    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        type = typeArray.get(position);
        callback.call();

        if (type.equals(Recipe.EXTRACT)) {
          profileSpinner.setVisibility(View.GONE);
          efficiencyView.setVisibility(View.GONE);
        }
        else {
          profileSpinner.setVisibility(View.VISIBLE);
          efficiencyView.setVisibility(View.VISIBLE);
        }
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });
  }

  @Override
  public void getList() {
    // Get array of styles and mash profiles
    styleArray = this.ingredientHandler.getStylesList();
    profileArray = this.ingredientHandler.getMashProfileList();
    typeArray = new ArrayList<String>();
    typeArray.add(Recipe.ALL_GRAIN);
    typeArray.add(Recipe.PARTIAL_MASH);
    typeArray.add(Recipe.EXTRACT);

    // If it doesn't contain the current recipes style / profile,
    // then it is custom and we add it to the list.
    if (! styleArray.contains(mRecipe.getStyle())) {
      styleArray.add(0, mRecipe.getStyle());
    }
    if (! profileArray.contains(mRecipe.getMashProfile())) {
      profileArray.add(mRecipe.getMashProfile());
    }
  }

  @Override
  public void onCancelPressed() {
    setResult(Constants.RESULT_CANCELED, new Intent());
    finish();
  }

  @Override
  public void onDeletePressed() {

  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();

    efficiency = Double.parseDouble(efficiencyViewText.getText().toString().replace(",", "."));
    double batchSize = Double.parseDouble(batchSizeViewText.getText().toString().replace(",", "."));
    double boilSize = Double.parseDouble(boilSizeViewText.getText().toString().replace(",", "."));
    // Notes are no longer edited in the recipe details dialog
    //String notes = "";
    String brewDate = new SimpleDateFormat(Constants.BREW_DATE_FMT).format(new Date());

    mRecipe.setRecipeName(name);
    mRecipe.setBrewDate(brewDate);
    mRecipe.setVersion(Utils.getXmlVersion());
    mRecipe.setType(type);
    mRecipe.setStyle(style);
    mRecipe.setMashProfile(profile);
    mRecipe.setBrewer("Unknown Brewer"); // TODO
    mRecipe.setDisplayBatchSize(batchSize);
    mRecipe.setDisplayBoilSize(boilSize);
    mRecipe.setBoilTime(time);
    mRecipe.setEfficiency(efficiency);
    mRecipe.setBatchTime(1);
    //mRecipe.setNotes(notes);
    //mRecipe.setTasteNotes(notes);
  }

  @Override
  public boolean validateValues() {
    if (mRecipe.getDisplayBatchSize() <= 0) {
      Log.d("AddRecipeActivity", "Recipe batch size <= 0 - invalid input");
      new AlertDialog.Builder(this)
              .setTitle("Invalid Batch Size")
              .setMessage("Recipe batch size must be greater than zero.")
              .setPositiveButton("OK", null).show();
      return false;
    }

    // Values are all valid.
    return true;
  }

  @Override
  public void onFinished() {
    // Create the recipe in the database.
    Recipe r = new DatabaseAPI(getApplicationContext()).createRecipeFromExisting(mRecipe);

    if (this.displayOnCreate) {
      // Open up the display recipe activity.
      Intent intent = new Intent(AddRecipeActivity.this, DisplayRecipeActivity.class);
      intent.putExtra(Constants.KEY_RECIPE, r);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
    }
    else {
      // Otherwise, just finish the activity.
      finish();
    }
  }
}
