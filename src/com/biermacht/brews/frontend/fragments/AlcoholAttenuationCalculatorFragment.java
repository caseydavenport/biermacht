package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

public class AlcoholAttenuationCalculatorFragment extends Fragment implements ClickableFragment
{
  private static int resource = R.layout.fragment_abv_attenuation_calculator;
  View pageView;
  Context c;

  // Views which we need to access
  EditText origGravEditText;
  EditText finalGravEditText;
  TextView abvTextView;
  TextView attnTextView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
          savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);
    setHasOptionsMenu(true);

    // Get context
    c = getActivity();

    // Get views we care about
    origGravEditText = (EditText) pageView.findViewById(R.id.original_gravity_edit_text);
    finalGravEditText = (EditText) pageView.findViewById(R.id.final_gravity_edit_text);
    abvTextView = (TextView) pageView.findViewById(R.id.calculated_abv_text_view);
    attnTextView = (TextView) pageView.findViewById(R.id.attenuation_text_view);

    return pageView;
  }

  public void calculate() {
    double og;
    double fg;
    double attn;
    double abv;

    // Acquire data.  If an invalid double is supplied, short-circuit, but don't crash.
    try {
      og = Double.parseDouble(origGravEditText.getText().toString().replace(",", "" + "."));
      fg = Double.parseDouble(finalGravEditText.getText().toString().replace(",", "."));
    } catch (Exception e) {
      return;
    }

    // Calculation only valid if original gravity is greater than final gravity, and original
    // gravity is greater than that of water (1).
    if ((og > fg) && (og > 1)) {
      attn = BrewCalculator.Attenuation(og, fg);
      abv = BrewCalculator.AlcoholByVolume(og, fg);
      attnTextView.setText("Apparent attenuation of " + String.format("%2.1f", attn) + "%");
      abvTextView.setText(String.format("%2.2f", abv) + "%");
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
  public void handleClick(View v) {
    if (v.getId() == R.id.calculate_button) {
      this.calculate();
    }
  }
}
