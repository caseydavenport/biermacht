package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.Utils;

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
        mProfile = getIntent().getParcelableExtra(Constants.INTENT_PROFILE);

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
