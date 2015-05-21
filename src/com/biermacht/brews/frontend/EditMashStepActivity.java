package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;

public class EditMashStepActivity extends AddMashStepActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Change submit button to say "save"
    submitButton.setText("Save");
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    profile.removeMashStep(order);
    profile.addMashStep(order, step);
  }

  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(stepTypeArray.indexOf(step.getType()));
  }

  @Override
  public void onCancelPressed() {
    setResult(Constants.RESULT_CANCELED, new Intent());
    finish();
  }
}
