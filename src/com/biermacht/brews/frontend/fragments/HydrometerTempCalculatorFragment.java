package com.biermacht.brews.frontend.fragments;

import android.app.Fragment;
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
  EditText measGravityEditText;
  EditText measTempEditText;
  TextView calcGravityTextView;
  TextView calcGravityTitle;
  TextView calibrationTempTitle;
  EditText calibrationTempEditText;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);
    setHasOptionsMenu(true);

    // Get context
    c = getActivity();

    // Get relevant views
    measTempTitle = (TextView) pageView.findViewById(R.id.meas_temperature_title);
    measGravityEditText = (EditText) pageView.findViewById(R.id.measured_gravity_edit_text);
    measTempEditText = (EditText) pageView.findViewById(R.id.measured_temperature_edit_text);
    calcGravityTextView = (TextView) pageView.findViewById(R.id.calculated_gravity_text_view);
    calibrationTempTitle = (TextView) pageView.findViewById(R.id.calibrate_temperature_title);
    calibrationTempEditText = (EditText) pageView.findViewById(R.id.calibrate_temperature_edit_text);
    calcGravityTitle = (TextView) pageView.findViewById(R.id.calculated_gravity_title);

    // Set the correct temperature titles / hints based on unit settings.
    measTempTitle.setText("Temperature of Wort (" + Units.getTemperatureUnits() + ")");
    calibrationTempTitle.setText("Calibration Temperature (" + Units.getTemperatureUnits() + ")");
    if (Units.getTemperatureUnits().equals(Units.FAHRENHEIT)) {
      calibrationTempEditText.setText("68");
      measTempEditText.setHint("80");
      calcGravityTitle.setText("Gravity at 68" + Units.getTemperatureUnits());
    }
    else {
      calibrationTempEditText.setText("20");
      measTempEditText.setHint("27");
      calcGravityTitle.setText("Gravity at 20" + Units.getTemperatureUnits());
    }

    return pageView;
  }

  public void calculate() {
    double measGrav = Double.parseDouble(measGravityEditText.getText().toString()
            .replace(",", "" + "."));
    double measTemp = Double.parseDouble(measTempEditText.getText().toString().replace(",", "."));
    double calibTemp = Double.parseDouble(calibrationTempEditText.getText().toString()
            .replace(",", "."));

    if ((measGrav != 0)) {
      // Update the calculated gravity title
      calcGravityTitle.setText("Gravity at " + calibTemp + Units.getTemperatureUnits());

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
