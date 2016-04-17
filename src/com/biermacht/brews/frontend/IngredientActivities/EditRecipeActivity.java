package com.biermacht.brews.frontend.IngredientActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.AddRecipeActivity;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditRecipeActivity extends AddRecipeActivity {

  // Rows to be displayed
  private View brewDateView;
  private View measuredOGView;
  private View measuredFGView;
  private View measuredBatchSizeView;

  // Titles
  private TextView brewDateViewTitle;
  private TextView measuredOGViewTitle;
  private TextView measuredFGViewTitle;
  private TextView measuredBatchSizeViewTitle;

  // Contents
  public static TextView brewDateViewText;
  private TextView measuredOGViewText;
  private TextView measuredFGViewText;
  private TextView measuredBatchSizeViewText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize views and stuff
    brewDateView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    measuredFGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    measuredOGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    measuredBatchSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    // Set onClickListeners for edit text views
    brewDateView.setOnClickListener(onClickListener);
    measuredFGView.setOnClickListener(onClickListener);
    measuredOGView.setOnClickListener(onClickListener);
    measuredBatchSizeView.setOnClickListener(onClickListener);

    // Add views to main view
    mainView.addView(brewDateView);
    mainView.addView(measuredOGView);
    mainView.addView(measuredFGView);
    mainView.addView(measuredBatchSizeView);

    // Titles
    brewDateViewTitle = (TextView) brewDateView.findViewById(R.id.title);
    brewDateViewTitle.setText("Brew Date");

    measuredFGViewTitle = (TextView) measuredFGView.findViewById(R.id.title);
    measuredFGViewTitle.setText("Measured Final Gravity");

    measuredOGViewTitle = (TextView) measuredOGView.findViewById(R.id.title);
    measuredOGViewTitle.setText("Measured Original Gravity");

    measuredBatchSizeViewTitle = (TextView) measuredBatchSizeView.findViewById(R.id.title);
    measuredBatchSizeViewTitle.setText("Measured Batch Size (" + Units.getVolumeUnits() + ")");

    // Texts
    brewDateViewText = (TextView) brewDateView.findViewById(R.id.text);
    brewDateViewText.setText(mRecipe.getBrewDate());

    measuredFGViewText = (TextView) measuredFGView.findViewById(R.id.text);
    measuredFGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredFG()));

    measuredOGViewText = (TextView) measuredOGView.findViewById(R.id.text);
    measuredOGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredOG()));

    measuredBatchSizeViewText = (TextView) measuredBatchSizeView.findViewById(R.id.text);
    measuredBatchSizeViewText.setText(String.format("%2.3f", mRecipe.getDisplayMeasuredBatchSize
            ()));

    // Set button text
    submitButton.setText(R.string.save);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);
    style = mRecipe.getStyle();
    profile = mRecipe.getMashProfile();
    type = mRecipe.getType();
    efficiency = mRecipe.getEfficiency();
  }

  @Override
  public void onMissedClick(View v) {
    super.onMissedClick(v);

    AlertDialog alert;
    if (v.equals(measuredFGView)) {
      alert = alertBuilder.editTextFloatAlert(measuredFGViewText, measuredFGViewTitle).create();
    }
    else if (v.equals(measuredOGView)) {
      alert = alertBuilder.editTextFloatAlert(measuredOGViewText, measuredOGViewTitle).create();
    }
    else if (v.equals(measuredBatchSizeView)) {
      alert = alertBuilder.editTextFloatAlert(measuredBatchSizeViewText,
                                              measuredBatchSizeViewTitle).create();
    }
    else if (v.equals(brewDateView)) {
      // Show the brew date picker and return.
      DialogFragment newFragment = new DatePickerFragment();
      Bundle args = new Bundle();
      newFragment.setArguments(args);
      newFragment.show(this.getSupportFragmentManager(), "datePicker");
      return;
    }
    else {
      Log.d("EditRecipeActivity", "onMissedClick did not handle the clicked view");
      return; // In case its none of those views...
    }

    // Force keyboard open and show popup
    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    alert.show();
  }

  @Override
  public void onFinished() {
    mRecipe.save(this);
    finish();
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    double measuredOg = Double.parseDouble(measuredOGViewText.getText().toString()
                                                   .replace(",", "."));
    double measuredFg = Double.parseDouble(measuredFGViewText.getText().toString()
                                                   .replace(",", "."));
    double measuredBatchSize = Double.parseDouble(measuredBatchSizeViewText.getText().toString()
                                                          .replace(",", "."));
    String brewDate = brewDateViewText.getText().toString();

    mRecipe.setBrewDate(brewDate);
    mRecipe.setMeasuredFG(measuredFg);
    mRecipe.setMeasuredOG(measuredOg);
    mRecipe.setDisplayMeasuredBatchSize(measuredBatchSize);
  }

  /**
   * DatePicker dialog for when the user decides to modify the brew date.
   */
  public static class DatePickerFragment extends DialogFragment
          implements DatePickerDialog.OnDateSetListener {

    public DatePickerFragment() {
      // This fragment has no options menu.
      setHasOptionsMenu(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      // Parse the date.
      Date date = new GregorianCalendar(year, month, day).getTime();
      String dateString = new SimpleDateFormat(Constants.BREW_DATE_FMT).format(date);

      // Update the text view.
      brewDateViewText.setText(dateString);
    }
  }
}
