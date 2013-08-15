package com.biermacht.brews.frontend;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;

public class EditMashProfileActivity extends Activity implements OnClickListener {


    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private OnClickListener onClickListener;

    // Rows to be displayed
    private Spinner spinner;
    private View nameView;
    private View efficiencyView;

    // Title divider
    private View mashStepTitleView;
    private TextView mashStepTitleViewText;


    // Titles
    private TextView nameViewTitle;
    private TextView efficiencyViewTitle;


    // Contents
    private TextView nameViewText;
    private TextView efficiencyViewText;

    // LayoutInflater
    LayoutInflater inflater;

    // Data storage declarations
    private double efficiency;
    private String name;

    // Recipe + mashProfile we are editing
    private Recipe mRecipe;
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

                // Set new orders for all the steps
                for (MashStep s : mashStepArray)
                {
                    s.setOrder(mashStepArray.indexOf(s));
                }
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
        setContentView(R.layout.activity_add_edit);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);

        // Acquire recipe
        try
        {
            mRecipe = Database.getRecipeWithId(id);
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
            finish();
        }

        // Acquire profile
        mProfile = mRecipe.getMashProfile();
		
		// Initialize data containers
		name = mProfile.getName();
		efficiency = mRecipe.getEfficiency();

        // Get lists
        profileArray = MainActivity.ingredientHandler.getMashProfileList();
        mashStepArray = new ArrayList<MashStep>();
        mashStepArray.addAll(mProfile.getMashStepList());

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(nameView))
                    alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
                else if (v.equals(efficiencyView))
                    alert = alertBuilder.editTextIntegerAlert(efficiencyViewText, efficiencyViewTitle).create();
                else
                    return; // In case its none of those views...

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };

        // Initialize views and stuff
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        nameView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        efficiencyView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        spinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        dragDropListView = (DragSortListView) inflater.inflate(R.layout.view_drag_drop_list, mainView, false);

        // mash step list title
        mashStepTitleView = inflater.inflate(R.layout.view_title, mainView, false);
        mashStepTitleViewText = (TextView) mashStepTitleView.findViewById(R.id.title);
        mashStepTitleViewText.setText("Mash Steps");

        // Set onClickListeners for edit text views
        nameView.setOnClickListener(onClickListener);
        efficiencyView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(spinner);
        mainView.addView(nameView);
        mainView.addView(efficiencyView);
        mainView.addView(mashStepTitleView);
        mainView.addView(dragDropListView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
        efficiencyViewTitle.setText("Efficiency (%)");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        nameViewText.setText(mProfile.getName());

        efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
        efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

        // If it doesn't contain the current profile
        // then it is custom and we add it to the list.
        // TODO: We should include custom stuff without having to check here
        if(!profileArray.contains(mRecipe.getMashProfile()))
            profileArray.add(mRecipe.getMashProfile());

        // Set up mash profile spinner
        MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(profileAdapter);
        spinner.setSelection(profileArray.indexOf(mProfile));

        // Handle mash profile selector here
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        });

        updateMashStepList();
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

	public void onClick(View v) {

		// Cancel Button Pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}

		// Submit Button pressed
		if (v.getId() == R.id.submit_button)
		{
			boolean readyToGo = true;	
			try
            {
				efficiency = Float.parseFloat(efficiencyViewText.getText().toString());
			    name = nameViewText.getText().toString();
			}
			catch (Exception e)
			{
				readyToGo = false;
			}

			if (name.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				readyToGo = false;

			if (readyToGo)
			{
				mProfile.setName(name);
				mRecipe.setEfficiency(efficiency);
                mProfile.setMashStepList(mashStepArray);
				mRecipe.setMashProfile(mProfile);
                Database.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
