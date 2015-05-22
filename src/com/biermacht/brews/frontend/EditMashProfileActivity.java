package com.biermacht.brews.frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditMashProfileActivity extends AddMashProfileActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Remove views we don't want.
    mainView.removeView(spinnerView);
    mainView.removeView(nameView);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // Acquire profile
    mProfile = getIntent().getParcelableExtra(Constants.KEY_PROFILE);

    // Set the profile.
    mRecipe.setMashProfile(mProfile);

    // Initialize data containers
    name = mProfile.getName();
  }

  @Override
  public void _getValuesFromIntent() {
    // When editing a mash profile, we don't need to do any special acquisition from the intent.
    // As such, override this method to do nothing.
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    mRecipe.setMashProfile(mProfile);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_edit_mash_profile, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;

      case R.id.menu_save:
        saveMashProfileAlert().show();
        return true;
    }
    return true;
  }

  @Override
  public void onFinished() {
    mProfile.save(Constants.DATABASE_DEFAULT);
    finish();
  }

  @Override
  public void onCancelPressed() {
    finish();
  }

  @Override
  public void onDeletePressed() {
    // Can't delete these!
  }

  private AlertDialog.Builder saveMashProfileAlert() {
    return new AlertDialog.Builder(this)
            .setTitle("Save Mash Profile?")
            .setMessage("Save this profile for use in other recipes?")
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                mProfile.setName("Profile from " + mRecipe.getRecipeName());
                Database.addMashProfileToVirtualDatabase(Constants.DATABASE_CUSTOM, mProfile, mProfile.getOwnerId());
                onMashProfileSavedAlert().create().show();
              }

            })

            .setNegativeButton(R.string.cancel, null);
  }

  private AlertDialog.Builder onMashProfileSavedAlert() {
    return new AlertDialog.Builder(this)
            .setTitle("Profile Saved")
            .setMessage("Your mash profile has been saved! Make changes in the Mash Profile Editor.")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
              }

            });
  }
}
