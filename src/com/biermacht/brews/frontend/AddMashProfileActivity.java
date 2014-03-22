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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.MashProfileSpinnerAdapter;
import com.biermacht.brews.frontend.adapters.MashStepArrayAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.utils.Callbacks.Callback;

import java.util.ArrayList;
import java.util.Collections;

public class AddMashProfileActivity extends AddEditActivity {

    // Views
    public Spinner mashTypeSpinner;
    public Spinner spargeTypeSpinner;
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

    // Spinner listeners
    AdapterView.OnItemSelectedListener mashTypeSpinnerListener;
    AdapterView.OnItemSelectedListener spargeTypeSpinnerListener;

    // Spinner array declarations
    public ArrayList<MashProfile> profileArray;
    public ArrayList<String> mashTypeArray;
    public ArrayList<String> spargeTypeArray;

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
            	// Move the step
                MashStep step = dragDropAdapter.getItem(oldPosition);
                dragDropAdapter.remove(step);
                dragDropAdapter.insert(step, newPosition);

                // Set the new list in the profile
                mProfile.clearMashSteps();
                for (int i = 0; i < dragDropAdapter.getCount(); i++)
                {
                	mProfile.addMashStep(dragDropAdapter.getItem(i));
                }
            }
        }
    };

    // Callback for when a view is removed from the list
    public DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int pos)
        {
        	dragDropAdapter.remove(mProfile.getMashStepList().get(pos));
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
        tunTempViewTitle.setText("Tun Temperature (" + Units.getTemperatureUnits() + ")");
        grainTempViewTitle.setText("Grain Temperature (" + Units.getTemperatureUnits() + ")");
        spargeTempViewTitle.setText("Sparge Water Temperature (" + Units.getTemperatureUnits() + ")");
        spargeVolumeViewTitle.setText("Sparge Volume (" + Units.getVolumeUnits() + ")");

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
        addMashStepButton = (ImageButton) mashStepTitleView.findViewById(R.id.button);
        addMashStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button)
                {
                    Intent i = new Intent(getApplicationContext(), AddMashStepActivity.class);
                    i.putExtra(Constants.KEY_PROFILE, mProfile);
                    i.putExtra(Constants.KEY_RECIPE, mRecipe);
                    startActivityForResult(i, Constants.REQUEST_NEW_MASH_STEP);
                }
            }
        });

        // Remove views we don't want
        mainView.removeView(amountView);
        mainView.removeView(timeView);
        mainView.removeView(spinnerView);

        // Add views that we want
        mainView.addView(mashTypeSpinner);
        mainView.addView(spargeTypeSpinner);
        mainView.addView(spargeTempView);
        mainView.addView(tunTempView);
        mainView.addView(grainTempView);

        // Add views for mash steps
        mainView.addView(mashStepTitleView);
        mainView.addView(dragDropListView);

        // Listener for mash step list
        dragDropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                Intent i = new Intent(getApplicationContext(), EditMashStepActivity.class);
                i.putExtra(Constants.KEY_MASH_STEP_ID, mProfile.getMashStepList().get(pos).getId());
                i.putExtra(Constants.KEY_MASH_STEP, mProfile.getMashStepList().get(pos));
                i.putExtra(Constants.KEY_PROFILE, mProfile);
                i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivityForResult(i, Constants.REQUEST_EDIT_MASH_STEP);
            }
        });

        updateMashStepList();
        setValues();
        spargeTypeSpinner.setOnItemSelectedListener(spargeTypeSpinnerListener);
        mashTypeSpinner.setOnItemSelectedListener(mashTypeSpinnerListener);
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
    
    public void createCallback()
    {
        callback = new Callback() {
            @Override
            public void call() 
            {
                Log.d("AddMashProfileActivity", "Callback hit - updating steps for profile.");
                updateMashStepList();
            }
        };
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
        
        setTempVals();

        // Initialize data containers
        name = mProfile.getName();
    }
    
    public void setTempVals()
    {
        // Add some temporary fields to the objects so that
        // the mash profile has stuff to work with for calculating
        // temps, volumes, etc.
        mProfile.addMashStep(new MashStep(mRecipe));
    }

    @Override
    public void getList()
    {
        profileArray = MainActivity.ingredientHandler.getMashProfileList();

        // If it doesn't contain the current profile
        // then it is custom and we add it to the list.
        if(!profileArray.contains(mRecipe.getMashProfile()))
            profileArray.add(mRecipe.getMashProfile());

        // Get mash and sparge type lists for spinners.
        mashTypeArray = Constants.MASH_TYPES;
        spargeTypeArray = Constants.SPARGE_TYPES;
    }

    @Override
    public void createSpinner()
    {
        MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(profileAdapter);

        mashTypeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        SpinnerAdapter mashTypeAdapter = new SpinnerAdapter(this, mashTypeArray, "Mash Type");
        mashTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mashTypeSpinner.setAdapter(mashTypeAdapter);

        spargeTypeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        SpinnerAdapter spargeTypeAdapter = new SpinnerAdapter(this, spargeTypeArray, "Sparge Type");
        spargeTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spargeTypeSpinner.setAdapter(spargeTypeAdapter);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(profileArray.indexOf(mProfile));
        mashTypeSpinner.setSelection(mashTypeArray.indexOf(mProfile.getMashType()));
        spargeTypeSpinner.setSelection(spargeTypeArray.indexOf(mProfile.getSpargeType()));
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

                updateMashStepList();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };

        mashTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                mProfile.setMashType(mashTypeArray.get(position));
                callback.call();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };

        spargeTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                mProfile.setSpargeType(spargeTypeArray.get(position));
                callback.call();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    public void updateMashStepList()
    {    	
        // Layout parameters for listView
        // We have trouble setting the size in XML, so we dynamically do it here based on
        // the number of steps. Each step is 60dip tall.
        int height = mProfile.getMashStepList().size() * 140;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);

        // Drag list view
        dragDropAdapter = new MashStepArrayAdapter(this, mProfile.getMashStepList());
        dragDropListView.setAdapter(dragDropAdapter);
        dragDropListView.setDropListener(onDrop);
        dragDropListView.setRemoveListener(onRemove);
        dragDropListView.setDragEnabled(true);
        dragDropListView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    	mProfile.save(Constants.DATABASE_CUSTOM);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        MashProfile p;

        if (resultCode == Constants.RESULT_CANCELED)
            return;

        if (data == null)
        {
            Log.d("AddMashProfileActivity", "Null intent passed as result, returning");
            return;
        }
        
        // Acquire the returned profile.
        try { p = data.getParcelableExtra(Constants.KEY_MASH_PROFILE); } 
        catch (Exception e){
            Log.d("AddMashProfileActivity", "No profile returned, probably hit back button.");
            return;
        }
        
        if (p == null)
        {
            Log.d("AddMashProfileActivity", "No profile returned, probably hit back button.");
            return;
        }
        
        // Set the new profile.
        mProfile = p;
        updateMashStepList();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
