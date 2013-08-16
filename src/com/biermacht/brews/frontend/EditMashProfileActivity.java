package com.biermacht.brews.frontend;

import android.os.Bundle;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditMashProfileActivity extends AddMashProfileActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire profile
        mProfile = getIntent().getParcelableExtra(Constants.INTENT_PROFILE);

        // Initialize data containers
        name = mProfile.getName();
    }

    @Override
    public void onFinished()
    {
        mProfile.setMashStepList(mashStepArray);
        mRecipe.setMashProfile(mProfile);
        Database.updateRecipe(mRecipe);
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
        Database.deleteMashProfileFromDatabase(mashProfileId, Constants.DATABASE_DEFAULT);
        finish();
    }
}
