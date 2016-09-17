package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

public class HydrometerTempCalculatorFragment extends Fragment implements BiermachtFragment {
  private static int resource = R.layout.fragment_hydrometer_calculator;
  View pageView;
  Context c;

  // Views which we need to access
  TextView measTempTitle;
  EditText measGravEditText;
  EditText measTempEditText;
  TextView calcGravityTextView;
  TextView calcGravityTitle;
  TextView calibTempTitle;
  EditText calibTempEditText;
  ScrollView scrollView;

  SharedPreferences preferences;

  public void setPreferences(SharedPreferences preferences) {
    this.preferences=preferences;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);
    setHasOptionsMenu(true);

    // Get context
    c = getActivity();

    // Get relevant views
    measTempTitle = (TextView) pageView.findViewById(R.id.meas_temperature_title);
    measGravEditText = (EditText) pageView.findViewById(R.id.measured_gravity_edit_text);
    measTempEditText = (EditText) pageView.findViewById(R.id.measured_temperature_edit_text);
    calcGravityTextView = (TextView) pageView.findViewById(R.id.calculated_gravity_text_view);
    calibTempTitle = (TextView) pageView.findViewById(R.id.calibrate_temperature_title);
    calibTempEditText = (EditText) pageView.findViewById(R.id.calibrate_temperature_edit_text);
    calcGravityTitle = (TextView) pageView.findViewById(R.id.calculated_gravity_title);

    scrollView = (ScrollView) pageView.findViewById(R.id.scrollview);

    // Set the correct temperature titles / hints based on unit settings.
    measTempTitle.setText("Temperature of Wort (" + Units.getTemperatureUnits() + ")");
    calibTempTitle.setText("Calibration Temperature (" + Units.getTemperatureUnits() + ")");
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      String calibTemp=preferences.getString(Constants.PREF_HYDROMETER_CALIBRATION_TEMP,"68");
      calibTempEditText.setText(calibTemp);
      measTempEditText.setHint("80");
      calcGravityTitle.setText("Gravity at "+calibTemp + Units.getTemperatureUnits());
    }
    else {
      String calibTemp=preferences.getString(Constants.PREF_HYDROMETER_CALIBRATION_TEMP,"68");
      calibTempEditText.setText(calibTemp);
      measTempEditText.setHint("27");
      calcGravityTitle.setText("Gravity at "+calibTemp + Units.getTemperatureUnits());
    }

    return pageView;
  }

  public void calculate() {
    double measGrav;
    double measTemp;
    double calibTemp;

    // Acquire data.  If an invalid double is supplied, short-circuit, but don't crash.
    try {
      measGrav = Double.parseDouble(measGravEditText.getText().toString().replace(",", "" + "."));
      measTemp = Double.parseDouble(measTempEditText.getText().toString().replace(",", "."));
      calibTemp = Double.parseDouble(calibTempEditText.getText().toString().replace(",", "."));
    } catch (Exception e) {
      return;
    }
    // save calibration temp if the field contains a valid value
    preferences.edit().putString(Constants.PREF_HYDROMETER_CALIBRATION_TEMP,calibTempEditText.getText().toString()).commit();

    if ((measGrav != 0)) {
      // Update the calculated gravity title
      calcGravityTitle.setText("Gravity at " + calibTemp + Units.getTemperatureUnits());

      // Calculation of gravity in BrewCalculator is done in imperial units, so convert
      // if we need to before performing calculation.
      if (Units.getTemperatureUnits() == Units.CELSIUS) {
        measTemp = Units.celsiusToFahrenheit(measTemp);
        calibTemp = Units.celsiusToFahrenheit(calibTemp);
      }

      // Update the calculated gravity view
      double adjustedGravity = BrewCalculator.adjustGravityForTemp(measGrav, measTemp, calibTemp);
      calcGravityTextView.setText(String.format("%1.3f", adjustedGravity));
    }
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
    return "Hydrometer";
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
