package com.biermacht.brews.frontend;

import android.os.Bundle;
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
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire profile
        mProfile = getIntent().getParcelableExtra(Constants.KEY_PROFILE);
        mRecipe.setMashProfile(mProfile);

        // Initialize data containers
        name = mProfile.getName();
    }
    
    @Override 
    public void configureSpinnerListener()
    {
    	// Override spinner listener because we don't show the spinner
    	// in edit mash profile.
    	spinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
            	mProfile = mRecipe.getMashProfile();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
    	};
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        mRecipe.setMashProfile(mProfile);
    }

    @Override
    public void onFinished()
    {
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
