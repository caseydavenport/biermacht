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

public class EditMashProfileActivity extends AddEditActivity {

    // Rows to be displayed
    private View efficiencyView;

    // Title divider
    private View mashStepTitleView;
    private TextView mashStepTitleViewText;


    // Titles
    private TextView efficiencyViewTitle;

    // Contents
    private TextView efficiencyViewText;

    // Data storage declarations
    private double efficiency;

    // mashProfile we are editing
    private MashProfile mProfile;

    // Spinner array declarations
    private ArrayList<MashProfile> profileArray;
    private ArrayList<MashStep> mashStepArray;

    // DragDrop ListView stuff
    private DragSortListView dragDropListView;
    private MashStepArrayAdapter dragDropAdapter;

    // Callback for when a view is dropped
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
    {
        @Override
        public void drop(int oldPosition, int newPosition)
        {
            if (oldPosition != newPosition)
            {
                MashStep step = dragDropAdapter.getItem(oldPosition);
                dragDropAdapter.remove(step);
                dragDropAdapter.insert(step, newPosition);
            }
        }
    };

    // Callback for when a view is removed from the list
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int pos)
        {
            dragDropAdapter.remove(dragDropAdapter.getItem(pos));

            // Set new orders for all the steps
            for (MashStep s : mashStepArray)
            {
                s.setOrder(mashStepArray.indexOf(s));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and stuff
        efficiencyView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        dragDropListView = (DragSortListView) inflater.inflate(R.layout.view_drag_drop_list, mainView, false);

        // mash step list title
        mashStepTitleView = inflater.inflate(R.layout.view_title, mainView, false);
        mashStepTitleViewText = (TextView) mashStepTitleView.findViewById(R.id.title);
        mashStepTitleViewText.setText("Mash Steps");

        // Set onClickListeners for edit text views
        efficiencyView.setOnClickListener(onClickListener);

        // Remove views we don't want
        mainView.removeView(amountView);
        mainView.removeView(timeView);

        // Add views that we want
        mainView.addView(efficiencyView);
        mainView.addView(mashStepTitleView);
        mainView.addView(dragDropListView);

        // Get titles, set values
        efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
        efficiencyViewTitle.setText("Efficiency (%)");

        // Get content, set values
        efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
        efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

        updateMashStepList();
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(efficiencyView))
            alert = alertBuilder.editTextIntegerAlert(efficiencyViewText, efficiencyViewTitle).create();
        else
                return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void setInitialValues()
    {
        super.setInitialValues();
        nameViewText.setText(mProfile.getName());
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire profile
        mProfile = getIntent().getParcelableExtra(Constants.INTENT_PROFILE);

        // Initialize data containers
        name = mProfile.getName();
        efficiency = mRecipe.getEfficiency();
    }

    @Override
    public void getList()
    {
        profileArray = MainActivity.ingredientHandler.getMashProfileList();
        mashStepArray = new ArrayList<MashStep>();
        mashStepArray.addAll(mProfile.getMashStepList());

        // If it doesn't contain the current profile
        // then it is custom and we add it to the list.
        // TODO: We should include custom stuff without having to check here
        if(!profileArray.contains(mRecipe.getMashProfile()))
            profileArray.add(mRecipe.getMashProfile());
    }

    @Override
    public void createSpinner()
    {
        MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(profileAdapter);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(profileArray.indexOf(mProfile));
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                mProfile = profileArray.get(position);
                nameViewText.setText(mProfile.getName());
                mashStepArray.removeAll(mashStepArray);
                mashStepArray.addAll(mProfile.getMashStepList());

                updateMashStepList();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    private void updateMashStepList()
    {
        // Drag list view
        dragDropAdapter = new MashStepArrayAdapter(this, mashStepArray);
        dragDropListView.setAdapter(dragDropAdapter);
        dragDropListView.setDropListener(onDrop);
        dragDropListView.setRemoveListener(onRemove);
        dragDropListView.setDragEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        efficiency = Float.parseFloat(efficiencyViewText.getText().toString());
        mProfile.setName(name);
        mRecipe.setEfficiency(efficiency);

        // Set new orders for all the MashSteps
        for (MashStep s : mashStepArray)
        {
            s.setOrder(mashStepArray.indexOf(s));
        }
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public void onDeletePressed()
    {

    }
}
