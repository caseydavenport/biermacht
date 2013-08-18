package com.biermacht.brews.frontend;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

    // Views
    public View tunTempView;
    public View grainTempView;
    public View spargeTempView;
    public View spargeVolumeView;

    // Titles
    public TextView tunTempViewTitle;
    public TextView grainTempViewTitle;
    public TextView spargeTempViewTitle;
    public TextView spargeVolumeViewTitle;

    // Content text
    public TextView tunTempViewText;
    public TextView grainTempViewText;
    public TextView spargeTempViewText;
    public TextView spargeVolumeViewText;

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

    // add mash step button
    public ImageButton addMashStepButton;

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
        tunTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        grainTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        spargeTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        spargeVolumeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set listeners
        tunTempView.setOnClickListener(onClickListener);
        grainTempView.setOnClickListener(onClickListener);
        spargeTempView.setOnClickListener(onClickListener);
        spargeVolumeView.setOnClickListener(onClickListener);

        // Titles here
        tunTempViewTitle = (TextView) tunTempView.findViewById(R.id.title);
        grainTempViewTitle = (TextView) grainTempView.findViewById(R.id.title);
        spargeTempViewTitle = (TextView) spargeTempView.findViewById(R.id.title);
        spargeVolumeViewTitle = (TextView) spargeVolumeView.findViewById(R.id.title);

        // Set titles
        tunTempViewTitle.setText("Tun Temperature (F)");
        grainTempViewTitle.setText("Grain Temperature (F)");
        spargeTempViewTitle.setText("Sparge Water Temperature (F)");
        spargeVolumeViewTitle.setText("Sparge Volume (gal)");

        // Get content views
        tunTempViewText = (TextView) tunTempView.findViewById(R.id.text);
        grainTempViewText = (TextView) grainTempView.findViewById(R.id.text);
        spargeTempViewText = (TextView) spargeTempView.findViewById(R.id.text);
        spargeVolumeViewText = (TextView) spargeVolumeView.findViewById(R.id.text);

        // Mash Step view Title
        mashStepTitleView = inflater.inflate(R.layout.view_title, mainView, false);
        mashStepTitleViewText = (TextView) mashStepTitleView.findViewById(R.id.title);
        mashStepTitleViewText.setText("Mash Steps");

        // Add mash step button
        addMashStepButton = (ImageButton) mashStepTitleView.findViewById(R.id.add_button);
        addMashStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add_button)
                {
                    Intent i = new Intent(getApplicationContext(), AddMashStepActivity.class);
                    startActivityForResult(i, Constants.REQUEST_NEW_MASH_STEP);
                }
            }
        });

        // Remove views we don't want
        mainView.removeView(amountView);
        mainView.removeView(timeView);

        // Add views that we want
        mainView.addView(tunTempView);
        mainView.addView(grainTempView);
        mainView.addView(spargeTempView);
        //mainView.addView(spargeVolumeViewText);

        // Add views for mash steps
        mainView.addView(mashStepTitleView);
        mainView.addView(dragDropListView);

        // Listener for mash step list
        dragDropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                Intent i = new Intent(getApplicationContext(), EditMashStepActivity.class);
                i.putExtra(Constants.KEY_MASH_STEP_ID, mashStepArray.get(pos).getId());
                i.putExtra(Constants.KEY_MASH_STEP, mashStepArray.get(pos));
                startActivityForResult(i, Constants.REQUEST_EDIT_MASH_STEP);
            }
        });

        updateMashStepList();
        setValues();
    }

    @Override
    public void onMissedClick(View v)
    {
        Log.d("AddMashProfileActivity", "Entering onMissedClick()");
        AlertDialog alert;
        if (v.equals(tunTempView))
            alert = alertBuilder.editTextFloatAlert(tunTempViewText, tunTempViewTitle).create();
        else if (v.equals(grainTempView))
            alert = alertBuilder.editTextFloatAlert(grainTempViewText, grainTempViewTitle).create();
        else if (v.equals(spargeTempView))
            alert = alertBuilder.editTextFloatAlert(spargeTempViewText, spargeTempViewTitle).create();
        else if (v.equals(spargeVolumeView))
            alert = alertBuilder.editTextFloatAlert(spargeVolumeViewText, spargeVolumeViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    public void setValues()
    {
        tunTempViewText.setText(String.format("%2.2f", mProfile.getDisplayTunTemp()));
        grainTempViewText.setText(String.format("%2.2f", mProfile.getDisplayGrainTemp()));
        spargeTempViewText.setText(String.format("%2.2f", mProfile.getDisplaySpargeTemp()));
        //spargeVolumeViewText.setText(String.format("%2.2f", mProfile.getDisplaySpargeVolume()));
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
                tunTempViewText.setText(String.format("%2.2f", mProfile.getDisplayTunTemp()));
                grainTempViewText.setText(String.format("%2.2f", mProfile.getDisplayGrainTemp()));
                spargeTempViewText.setText(String.format("%2.2f", mProfile.getDisplaySpargeTemp()));

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        MashStep s;
        long id;

        if (resultCode == Constants.RESULT_CANCELED)
            return;

        switch (requestCode)
        {
            case Constants.REQUEST_NEW_MASH_STEP:
            {
                try
                {
                    s = data.getParcelableExtra(Constants.KEY_MASH_STEP);
                } catch (Exception e)
                {
                    Log.d("AddMashProfileActivity", "No step returned, probably hit back button.");
                    return;
                }

                mashStepArray.add(s);
                updateMashStepList();
                break;
            }

            case Constants.REQUEST_EDIT_MASH_STEP:
            {
                try
                {
                    s = data.getParcelableExtra(Constants.KEY_MASH_STEP);
                    id = data.getLongExtra(Constants.KEY_MASH_STEP_ID, Constants.INVALID_ID);
                } catch (Exception e)
                {
                    Log.d("AddMashProfileActivity", "No step returned, probably hit back button.");
                    return;
                }

                // Remove step
                for (MashStep step : mashStepArray)
                    if (s.getId() == id)
                        mashStepArray.remove(step);

                // If we deleted the step, do nothing, else re-add it
                if (resultCode == Constants.RESULT_OK)
                    mashStepArray.add(s);

                updateMashStepList();
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        double spargeTemp = Double.parseDouble(spargeTempViewText.getText().toString());
        double grainTemp = Double.parseDouble(grainTempViewText.getText().toString());
        double tunTemp = Double.parseDouble(tunTempViewText.getText().toString());

        mProfile.setName(name);
        mProfile.setDisplaySpargeTemp(spargeTemp);
        mProfile.setDisplayGrainTemp(grainTemp);
        mProfile.setDisplayTunTemp(tunTemp);

        // Set new orders for all the MashSteps
        for (MashStep s : mashStepArray)
        {
            s.setOrder(mashStepArray.indexOf(s));
        }

        mProfile.setMashStepList(mashStepArray);
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
