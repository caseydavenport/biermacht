package com.biermacht.brews.frontend;

import android.os.Bundle;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomMashProfileActivity extends AddMashProfileActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire profile
        mProfile = getIntent().getParcelableExtra(Constants.KEY_PROFILE);

        // Initialize data containers
        name = mProfile.getName();
    }

    @Override
    public void onFinished()
    {
        Database.deleteMashProfileFromDatabase(mashProfileId, Constants.DATABASE_CUSTOM);
        Database.addMashProfileToVirtualDatabase(Constants.DATABASE_CUSTOM, mProfile, Constants.MASTER_RECIPE_ID);
        finish();
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteMashProfileFromDatabase(mashProfileId, Constants.DATABASE_CUSTOM);
        finish();
    }
}
