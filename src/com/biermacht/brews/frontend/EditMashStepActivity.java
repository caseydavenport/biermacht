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

    // Enable delete button for this view, allowing users to delete an existing MashStep.  This
    // button is disabled by the superclass, since delete is an invalid operation when adding a
    // new MashStep.
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Since we are editing an existing MashStep, change the submit button to say "Done".
    // MashSteps will be saved only when the entire MashProfile is saved.
    submitButton.setText("Done");
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    profile.removeMashStep(order);
    profile.addMashStep(order, step);
  }

  /**
   * Set the initial MashStep type spinner to be that of the current MashStep.  This is called at
   * start-of-day, so that the TextView displays the correct MashStep type (Infusion, Decoction,
   * Temperature).
   */
  @Override
  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(stepTypeArray.indexOf(step.getType()));
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
   * The user has chosen to delete this mash step from its MashProfile.  Remove the step from the
   * profile, and then return the modified MashProfile in an Intent back to the calling Activity.
   */
  @Override
  public void onDeletePressed() {
    profile.removeMashStep(order);
    Intent result = new Intent();
    result.putExtra(Constants.KEY_MASH_STEP, step);
    result.putExtra(Constants.KEY_MASH_PROFILE, profile);
    result.putExtra(Constants.KEY_MASH_STEP_ID, stepId);
    setResult(Constants.RESULT_DELETED, result);
    finish();
  }
}
