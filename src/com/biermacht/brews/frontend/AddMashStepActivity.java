package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.utils.Callbacks.BooleanCallback;
import com.biermacht.brews.utils.Callbacks.Callback;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;

public class AddMashStepActivity extends AddEditActivity {

  // Views
  public View stepTempView;
  public View waterToGrainRatioView;
  public View infuseTemperatureView;
  public View stepAmountView;
  public View rampTimeView;

  // Titles
  public TextView stepTempViewTitle;
  public TextView waterToGrainRatioViewTitle;
  public TextView infuseTemperatureViewTitle;
  public TextView stepAmountViewTitle;
  public TextView rampTimeViewTitle;

  // Content text
  public TextView stepTempViewText;
  public TextView waterToGrainRatioViewText;
  public TextView infuseTemperatureViewText;
  public TextView stepAmountViewText;
  public TextView rampTimeViewText;

  // MashStep we are editing and its ID in the database.
  public MashStep step;
  public long stepId;

  // Callbacks for auto-calculation fields.
  public BooleanCallback infuseTempCallback;
  public BooleanCallback infuseAmountCallback;
  public BooleanCallback decoctAmountCallback;

  // Stores potential MashStep types - Temperature, Infusion, Decoction.
  ArrayList<String> stepTypeArray;

  // Data storage
  String type;
  MashProfile profile;

  // Keeps track of the index of this MashStep in its MashProfile's mash step list.
  public int order;

  // Database to use when saving.
  public long database;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Indicate to the superclass that it should not save the active Recipe (mRecipe) upon
    // successful completion of this Activity.  The Recipe should not be saved by this activity,
    // instead it should be saved when the entire MashProfile is saved.
    this.saveRecipeOnSubmit = false;

    // Disable delete button for this view, as delete is not a valid operation when adding a
    // MashStep.
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Inflate views
    stepTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    waterToGrainRatioView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    infuseTemperatureView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    stepAmountView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    rampTimeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    // Set listeners
    stepTempView.setOnClickListener(onClickListener);
    waterToGrainRatioView.setOnClickListener(onClickListener);
    infuseTemperatureView.setOnClickListener(onClickListener);
    stepAmountView.setOnClickListener(onClickListener);
    rampTimeView.setOnClickListener(onClickListener);

    // Get titles, set values
    stepTempViewTitle = (TextView) stepTempView.findViewById(R.id.title);
    waterToGrainRatioViewTitle = (TextView) waterToGrainRatioView.findViewById(R.id.title);
    infuseTemperatureViewTitle = (TextView) infuseTemperatureView.findViewById(R.id.title);
    stepAmountViewTitle = (TextView) stepAmountView.findViewById(R.id.title);
    rampTimeViewTitle = (TextView) rampTimeView.findViewById(R.id.title);

    // Get content views
    stepTempViewText = (TextView) stepTempView.findViewById(R.id.text);
    waterToGrainRatioViewText = (TextView) waterToGrainRatioView.findViewById(R.id.text);
    infuseTemperatureViewText = (TextView) infuseTemperatureView.findViewById(R.id.text);
    stepAmountViewText = (TextView) stepAmountView.findViewById(R.id.text);
    rampTimeViewText = (TextView) rampTimeView.findViewById(R.id.text);

    // Set titles
    stepAmountViewTitle.setText("Water to Add (" + (Units.getUnitSystem() == Units.IMPERIAL ? "qt" : "L") + ")");
    stepTempViewTitle.setText("Step Temperature (" + Units.getTemperatureUnits() + ")");
    waterToGrainRatioViewTitle.setText("Water to Grain Ratio (" + (Units.getUnitSystem() == Units.IMPERIAL ? "qt/lb" : "L/kg") + ")");
    infuseTemperatureViewTitle.setText("Water Temperature (" + Units.getTemperatureUnits() + ")");
    rampTimeViewTitle.setText("Ramp Time");

    // Remove views we don't want
    mainView.removeView(amountView);

    // Add views that we want
    mainView.addView(stepTempView);
    mainView.addView(waterToGrainRatioView);
    mainView.addView(infuseTemperatureView);
    mainView.addView(stepAmountView);
    mainView.addView(rampTimeView);

    // Change button text to say "Add" instead of "Submit"
    submitButton.setText("Add");

    // Set values
    setValues();
  }

  @Override
  public void onMissedClick(View v) {
    AlertDialog alert;
    if (v.equals(infuseTemperatureView)) {
      alert = alertBuilder.editTextFloatCheckBoxAlert(infuseTemperatureViewText,
                                                      infuseTemperatureViewTitle,
                                                      step.getAutoCalcInfuseTemp(),
                                                      infuseTempCallback).create();
    }
    else if (v.equals(stepTempView)) {
      alert = alertBuilder.editTextFloatAlert(stepTempViewText, stepTempViewTitle).create();
    }
    else if (v.equals(waterToGrainRatioView)) {
      if (step.firstInList()) {
        alert = alertBuilder.editTextFloatAlert(waterToGrainRatioViewText,
                                                waterToGrainRatioViewTitle).create();
      }
      else {
        alert = alertBuilder.editTextDisabled(waterToGrainRatioViewText,
                                              waterToGrainRatioViewTitle,
                                              Constants.MESSAGE_AUTO_CALC_W2GR).create();
      }
    }
    else if (v.equals(stepAmountView)) {
      if (step.getType().equals(MashStep.DECOCTION)) {
        alert = alertBuilder.editTextFloatCheckBoxAlert(stepAmountViewText,
                                                        stepAmountViewTitle,
                                                        step.getAutoCalcDecoctAmt(),
                                                        decoctAmountCallback).create();
      }
      else {
        alert = alertBuilder.editTextFloatCheckBoxAlert(stepAmountViewText,
                                                        stepAmountViewTitle,
                                                        step.getAutoCalcInfuseAmt(),
                                                        infuseAmountCallback).create();
      }
    }
    else if (v.equals(rampTimeView)) {
      alert = alertBuilder.editTextIntegerAlert(rampTimeViewText, rampTimeViewTitle).create();
    }
    else {
      return;
    }

    // Force keyboard open and show the chosen alert.
    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    alert.show();
  }

  @Override
  public void createCallback() {
    // Default callback, called when alertBuilders are finished.
    // Allows us to update fields that are dependent on other field values.
    callback = new Callback() {
      @Override
      public void call() {
        try {
          // Get new values
          acquireValues();
        } catch (Exception e) {
          Log.d("AddMashStepActivity", "Exception in callback from alert dialog");
          e.printStackTrace();
        }
        // Update field values
        setValues();
      }
    };

    // Called when the "auto-calculate" checkbox is pressed for Infuse temperature.
    infuseTempCallback = new BooleanCallback() {
      @Override
      public void call(boolean b) {
        Log.d("AddMashStepActivity", "Infuse temp autocalc checkbox pressed, set to: " + b);
        step.setAutoCalcInfuseTemp(b);
        infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
        stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
        waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
      }
    };

    // Called when the "auto-calculate" checkbox is pressed for Infuse amount.
    infuseAmountCallback = new BooleanCallback() {
      @Override
      public void call(boolean b) {
        Log.d("AddMashStepActivity", "Infuse amount autocalc checkbox pressed, set to: " + b);
        step.setAutoCalcInfuseAmt(b);
        stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
        infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
        waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
      }
    };

    // Called when the "auto-calculate" checkbox is pressed for decoction amount.
    decoctAmountCallback = new BooleanCallback() {
      @Override
      public void call(boolean b) {
        Log.d("AddMashStepActivity", "Decoct amount autocalc checkbox pressed, set to: " + b);
        step.setAutoCalcDecoctAmt(b);
        stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
        waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
      }
    };
  }

  public void setValues() {
    Log.d("AddMashStepActivity", "Setting TextView values based on the MashStep");
    nameViewText.setText(step.getName());
    timeViewText.setText(String.format("%d", (int) step.getStepTime()));
    stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
    stepTempViewText.setText(String.format("%2.2f", step.getDisplayStepTemp()));
    waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
    infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
    rampTimeViewText.setText(String.format("%2.2f", step.getRampTime()));
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // Get the profile from the intent
    profile = getIntent().getParcelableExtra(Constants.KEY_PROFILE);

    // Set the recipes profile
    mRecipe.setMashProfile(profile);

    // Attempt to get the MashStep from the intent which started this Activity.  When adding a
    // mash step, we do not expect to find one in the intent.  When editing a mash step, we do.
    try {
      step = getIntent().getParcelableExtra(Constants.KEY_MASH_STEP);
      step.setRecipe(mRecipe);
    } catch (Exception e) {
      // If the step was not found, create a new one.  Call out to onStepNotFound() to do this work.
      Log.d("AddMashStepActivity", "Mash step not provided in Intent, call onStepNotFound()");
      onStepNotFound();
    }

    // Store off this step's order in the mash step list.
    order = step.getOrder();
  }

  /**
   * Called when a MashStep is not provided via the Intent which started this Activity.  Creates a
   * new MashStep and adds it to the active profile.
   */
  public void onStepNotFound() {
    step = new MashStep(mRecipe);
    profile.addMashStep(step);

    // Get step id.
    stepId = getIntent().getLongExtra(Constants.KEY_MASH_STEP_ID, Constants.INVALID_ID);

    // Get the database to save to.  This varies based on whether we are editing a floating
    // custom MashProfile, or if this MashProfile is attached to a user's recipe.
    database = getIntent().getLongExtra(Constants.KEY_DATABASE_ID, Constants.DATABASE_DEFAULT);

    // Since we're adding a new step, we should increment the step temperature, unless this is
    // the first step in the list.  If this is not the first step in the list, increment the
    // temperature by 2.77C (5F).
    try {
      step.setBeerXmlStandardStepTemp(step.getPreviousStep().getBeerXmlStandardStepTemp() + 2.7777);
      Log.d("AddMashStepActivity", "Step is not first in list, increment temperature.");
    } catch (Exception e) {
      Log.d("AddMashStepActivity", "Step is first in list, using own step temp.");
    }
  }

  @Override
  public void getList() {
    stepTypeArray = Constants.MASH_STEP_TYPES;
  }

  @Override
  public void createSpinner() {
    // Configure the spinner adapter for the MashStep type spinner.
    SpinnerAdapter adapter = new SpinnerAdapter(this, stepTypeArray, "Type");
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    spinnerView.setAdapter(adapter);
  }

  @Override
  public void setInitialSpinnerSelection() {
    // Do nothing.  Since we're adding a new MashStep, there is no required
    // initial position.  When editing a MashStep, this method is overridden to initialize the
    // spinner to the active MashStep's type.
  }

  @Override
  public void configureSpinnerListener() {
    spinnerListener = new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        type = stepTypeArray.get(position);
        step.setType(type);

        // We need to set the correct titles and visible views based on the selected MashStep type.
        if (type.equals(MashStep.DECOCTION)) {
          stepAmountViewTitle.setText("Amount to decoct (" + Units.getVolumeUnits() + ")");
          infuseTemperatureView.setVisibility(View.GONE);
          waterToGrainRatioView.setVisibility(View.GONE);
          stepAmountView.setVisibility(View.VISIBLE);
          rampTimeView.setVisibility(View.GONE);
        }
        else if (type.equals(MashStep.TEMPERATURE)) {
          infuseTemperatureView.setVisibility(View.GONE);
          stepAmountView.setVisibility(View.GONE);
          waterToGrainRatioView.setVisibility(View.GONE);
          rampTimeView.setVisibility(View.VISIBLE);
        }
        else {
          stepAmountViewTitle.setText("Water to add (" + Units.getVolumeUnits() + ")");
          infuseTemperatureView.setVisibility(View.VISIBLE);
          waterToGrainRatioView.setVisibility(View.VISIBLE);
          stepAmountView.setVisibility(View.VISIBLE);
          rampTimeView.setVisibility(View.GONE);
        }

        // Update selected values.
        setValues();
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }
    };
  }

  /**
   * Override this method so that we can return a RESULT_CANCELED code when the user pressed the
   * home or back button.
   *
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        setResult(Constants.RESULT_CANCELED, new Intent());
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Acquire values from user-input EditTexts.  Invalid-values should throw an Exception, indicating
   * to the superclass that the activity cannot be finished, since the user has input invalid
   * values.
   *
   * @throws Exception
   */
  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    double stepTemp = Double.parseDouble(stepTempViewText.getText().toString().replace(",", "."));
    double infuseTemp = Double.parseDouble(infuseTemperatureViewText.getText().toString().replace(",", "."));
    double waterToGrainRatio = Double.parseDouble(waterToGrainRatioViewText.getText().toString().replace(",", "."));
    amount = Double.parseDouble(stepAmountViewText.getText().toString().replace(",", "."));
    double rampTime = Double.parseDouble(rampTimeViewText.getText().toString().replace(",", "."));

    // Make sure the type is set.
    step.setType(type);

    // Set the amount based on the type of step.
    if (step.getType().equals(MashStep.INFUSION)) {
      step.setDisplayInfuseAmount(amount);
    }
    else if (step.getType().equals(MashStep.DECOCTION)) {
      step.setDisplayDecoctAmount(amount);
    }
    else if (step.getType().equals(MashStep.TEMPERATURE)) {
      step.setDisplayInfuseAmount(0);
      step.setDisplayDecoctAmount(0);
      step.setRampTime(rampTime);
    }

    // Set common fields.
    step.setStepTime(time);
    step.setDisplayStepTemp(stepTemp);
    step.setDisplayWaterToGrainRatio(waterToGrainRatio);
    step.setDisplayInfuseTemp(infuseTemp);
    step.setName(name);
  }

  /**
   * The user has chosen to save this MashStep.  Return the active MashProfile and step to the
   * calling activity.
   */
  @Override
  public void onFinished() {
    Intent result = new Intent();
    result.putExtra(Constants.KEY_MASH_STEP, step);
    result.putExtra(Constants.KEY_MASH_STEP_ID, stepId);
    result.putExtra(Constants.KEY_MASH_PROFILE, profile);
    setResult(Constants.RESULT_OK, result);
    finish();
  }

  /**
   * The user has pressed the Cancel button.  Return to the MashProfile activity, indicating the
   * result to be RESULT_CANCELED.  The receiving activity will use this code to determine the
   * result of this activity.  Do not return the active MashProfile, since it has not been updated.
   */
  @Override
  public void onCancelPressed() {
    setResult(Constants.RESULT_CANCELED, new Intent());
    finish();
  }

  /**
   * Since we are adding a new MashStep, delete is an invalid operation.  The delete button is not
   * shown for this Activity.
   */
  @Override
  public void onDeletePressed() {
  }
}
