package com.biermacht.brews.frontend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.utils.Constants;

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
    // onFinished is called when the User has chosen to save changes made to the active profile.
    // Save the profile, then exit the activity.
    mProfile.save(Constants.DATABASE_DEFAULT);
    finish();
  }

  @Override
  public void onCancelPressed() {
    // If cancel is pressed, do not save the current profile.  Just finish the activity
    // without committing any changes.
    finish();
  }

  @Override
  public void onDeletePressed() {
    // This activity is for editing a MashProfile which is attached to a User's recipe.
    // As such, these can not be deleted (the user should change their recipe to be extract
    // if their recipe does not use a mash profile).
  }

  /**
   * Returns a Builder for an AlertDialog which prompts the user if they would like to save the
   * current recipe's mash profile to be used in future recipes.
   *
   * @return AlertDialog.Builder
   */
  private AlertDialog.Builder saveMashProfileAlert() {
    return new AlertDialog.Builder(this)
            .setTitle("Save Mash Profile?")
            .setMessage("Save this profile for use in other recipes?")
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                mProfile.setName(mRecipe.getRecipeName() + "'s profile");
                DatabaseAPI.addMashProfileToVirtualDatabase(Constants.DATABASE_CUSTOM, mProfile, mProfile.getRecipeId(), Constants.SNAPSHOT_NONE);
                onMashProfileSavedAlert().create().show();
              }
            })
            .setNegativeButton(R.string.cancel, null);
  }

  /**
   * Returns a Builder for an AlertDialog which informs the User that the active Mash Profile has
   * been saved for use in other recipes.
   *
   * @return AlertDialog.Builder
   */
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
