package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.MashProfileSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.MashStepArrayAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import java.util.ArrayList;

public class AddMashProfileActivity extends AddEditActivity {

    // Title divider
    public View mashStepTitleView;
    public TextView mashStepTitleViewText;

    // mashProfile we are editing
    public MashProfile mProfile;

    // Spinner array declarations
    public ArrayList<MashProfile> profileArray;
    public ArrayList<MashStep> mashStepArray;

    // DragDrop ListView stuff
    public DragSortListView dragDropListView;
    public MashStepArrayAdapter dragDropAdapter;

    // Callback for when a view is dropped
    public DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
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
    public DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
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
        dragDropListView = (DragSortListView) inflater.inflate(R.layout.view_drag_drop_list, mainView, false);

        // mash step list title
        mashStepTitleView = inflater.inflate(R.layout.view_title, mainView, false);
        mashStepTitleViewText = (TextView) mashStepTitleView.findViewById(R.id.title);
        mashStepTitleViewText.setText("Mash Steps");

        // Remove views we don't want
        mainView.removeView(amountView);
        mainView.removeView(timeView);

        // Add views that we want
        mainView.addView(mashStepTitleView);
        mainView.addView(dragDropListView);

        updateMashStepList();
    }

    @Override
    public void onMissedClick(View v)
    {
        // Nothing to do here!
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
        mProfile = new MashProfile();

        // Initialize data containers
        name = mProfile.getName();
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

    public void updateMashStepList()
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
        //getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
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
        Database.addMashProfileToVirtualDatabase(Constants.DATABASE_CUSTOM, mProfile, Constants.MASTER_RECIPE_ID);
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        mProfile.setName(name);

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
