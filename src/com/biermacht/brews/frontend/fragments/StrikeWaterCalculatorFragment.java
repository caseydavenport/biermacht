package com.biermacht.brews.frontend.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

public class StrikeWaterCalculatorFragment extends Fragment implements ClickableFragment {

  private static int resource = R.layout.fragment_mash_strike_calculator;
  View pageView;
  Context c;

  // Views which we need to access
  TextView targetTempTitle;
  TextView grainWeightTitle;
  TextView waterToGrainTitle;
  TextView initialTempTitle;
  TextView strikeTempTitle;

  EditText targetTempText;
  EditText grainWeightText;
  EditText initialTempText;
  EditText waterToGrainText;
  TextView strikeTempText;

  ScrollView scrollView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);
    setHasOptionsMenu(true);

    // Get context
    c = getActivity();

    // Get views
    targetTempTitle = (TextView) pageView.findViewById(R.id.target_temperature_title);
    grainWeightTitle = (TextView) pageView.findViewById(R.id.grain_weight_title);
    waterToGrainTitle = (TextView) pageView.findViewById(R.id.water_to_grain_ratio_title);
    strikeTempTitle = (TextView) pageView.findViewById(R.id.strike_water_temp_title);
    initialTempTitle = (TextView) pageView.findViewById(R.id.initial_temp_title);

    targetTempText = (EditText) pageView.findViewById(R.id.target_temperature_edit_text);
    grainWeightText= (EditText) pageView.findViewById(R.id.grain_weight_edit_text);
    waterToGrainText = (EditText) pageView.findViewById(R.id.water_to_grain_ratio_edit_text);
    initialTempText = (EditText) pageView.findViewById(R.id.initial_temp_edit_text);
    strikeTempText = (TextView) pageView.findViewById(R.id.strike_water_temp_text_view);

    scrollView = (ScrollView) pageView.findViewById(R.id.scrollview);

    // Set titles based on units
    targetTempTitle.setText("Target Temperature (" + Units.getTemperatureUnits() + ")");
    initialTempTitle.setText("Initial Temperature (" + Units.getTemperatureUnits() + ")");
    grainWeightTitle.setText("Grain Weight (" + Units.getWeightUnits() + ")");
    waterToGrainTitle.setText("Water to Grain Ratio (" + Units.getWaterToGrainUnits() + ")");

    return pageView;
  }

  public void calculate() {
    double targetTemp;
    double grainWeight;
    double waterToGrainRatio;
    double strikeTemp;
    double strikeVol;
    double initialTemp;
    String volString;

    try {
      targetTemp = Double.parseDouble(targetTempText.getText().toString().replace(",", "" + "."));
      grainWeight = Double.parseDouble(grainWeightText.getText().toString().replace(",", "" + "."));
      waterToGrainRatio = Double.parseDouble(waterToGrainText.getText().toString().replace(",", "" + "."));
      initialTemp = Double.parseDouble(initialTempText.getText().toString().replace(",", "" + "."));
      strikeVol = waterToGrainRatio * grainWeight;
    } catch (Exception e)
    {
      Log.d("StrikeWaterCalculator", "Failed to calculate strike water " + e.toString());
      return;
    }

    // Calculate
    strikeTemp = BrewCalculator.calculateStrikeTemp(waterToGrainRatio, initialTemp, targetTemp);
    volString = String.format("%2.2f", strikeVol) + Units.getStrikeVolumeUnits();

    // Set text accordingly.
    strikeTempTitle.setText("Add " + volString + " of Water at:");
    strikeTempText.setText(String.format("%2.1f", strikeTemp) + Units.getTemperatureUnits());
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
