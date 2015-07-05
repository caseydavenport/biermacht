package com.biermacht.brews.frontend;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;

public class EditCustomMashProfileActivity extends EditMashProfileActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

    // Add fields we want
    mainView.addView(nameView, 0);
  }

  @Override
  public void onFinished() {
    mProfile.save(Constants.DATABASE_CUSTOM);
    finish();
  }

  @Override
  public void onDeletePressed() {
    mProfile.delete(Constants.DATABASE_CUSTOM);
    finish();
  }

  /**
   * Override the options menu so that nothing is displayed.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

}
