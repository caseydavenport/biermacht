package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

public class HydrometerTempCalculatorFragment extends Fragment implements ClickableFragment {

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

    // Set the correct temperature titles / hints based on unit settings.
    measTempTitle.setText("Temperature of Wort (" + Units.getTemperatureUnits() + ")");
    calibTempTitle.setText("Calibration Temperature (" + Units.getTemperatureUnits() + ")");
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      calibTempEditText.setText("68");
      measTempEditText.setHint("80");
      calcGravityTitle.setText("Gravity at 68" + Units.getTemperatureUnits());
    }
    else {
      calibTempEditText.setText("20");
      measTempEditText.setHint("27");
      calcGravityTitle.setText("Gravity at 20" + Units.getTemperatureUnits());
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

  @Override
  public void handleClick(View v) {
    if (v.getId() == R.id.calculate_button) {
      this.calculate();
    }
  }
}
