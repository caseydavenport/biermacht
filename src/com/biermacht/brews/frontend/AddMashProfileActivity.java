package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.MashStepArrayAdapter;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Callbacks.Callback;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;

/**
 * This Activity is called when the user creates a new Mash Profile to be added to the Mash Profile
 * Editor.
 */
public class AddMashProfileActivity extends AddEditActivity {

  // Rows which apply to this Activity.
  public Spinner mashTypeSpinner;
  public Spinner spargeTypeSpinner;
  public View tunTempView;
  public View grainTempView;
  public View spargeTempView;
  public View spargeVolumeView;

  // Title sub-views from the above rows.
  public TextView tunTempViewTitle;
  public TextView grainTempViewTitle;
  public TextView spargeTempViewTitle;
  public TextView spargeVolumeViewTitle;

  // Sub-title sub-views from the above rows.
  public TextView tunTempViewText;
  public TextView grainTempViewText;
  public TextView spargeTempViewText;
  public TextView spargeVolumeViewText;

  // Views which compose the divider between the MashProfile settings and the MashStep list.
  public View mashStepTitleView;
  public TextView mashStepTitleViewText;

  // MashProfile object being added.
  public MashProfile mProfile;

  // Spinner listeners
  AdapterView.OnItemSelectedListener mashTypeSpinnerListener;
  AdapterView.OnItemSelectedListener spargeTypeSpinnerListener;

  // Spinner array declarations
  public ArrayList<String> mashTypeArray;
  public ArrayList<String> spargeTypeArray;

  // Special ListView and adapter to implement "swipe-to-dismiss" and "drag reordering"
  public DragSortListView dragDropListView;
  public MashStepArrayAdapter dragDropAdapter;

  // Button to add new Mash Step.
  public ImageButton addMashStepButton;

  // Callback for a MashStep is dropped into a new position in the mash step list.
  public DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
    @Override
    public void drop(int oldPosition, int newPosition) {
      if (oldPosition != newPosition) {

        // Move the step in the adapter.
        MashStep step = dragDropAdapter.getItem(oldPosition);
        dragDropAdapter.remove(step);
        dragDropAdapter.insert(step, newPosition);

        // Update the MashProfile with the newly organized steps.
        mProfile.clearMashSteps();
        for (int i = 0; i < dragDropAdapter.getCount(); i++) {
          mProfile.addMashStep(dragDropAdapter.getItem(i));
          Log.e("AddMashProfile", "Adding Mash Profile with hash " + dragDropAdapter.getItem(i).hashCode());
        }
      }
    }
  };

  // Callback for when a MashStep is removed from the mash step list
  public DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
    @Override
    public void remove(int pos) {
      dragDropAdapter.remove(mProfile.getMashStepList().get(pos));
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Initialize views
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

    // Get title views
    tunTempViewTitle = (TextView) tunTempView.findViewById(R.id.title);
    grainTempViewTitle = (TextView) grainTempView.findViewById(R.id.title);
    spargeTempViewTitle = (TextView) spargeTempView.findViewById(R.id.title);
    spargeVolumeViewTitle = (TextView) spargeVolumeView.findViewById(R.id.title);

    // Set titles
    tunTempViewTitle.setText("Tun Temperature (" + Units.getTemperatureUnits() + ")");
    grainTempViewTitle.setText("Grain Temperature (" + Units.getTemperatureUnits() + ")");
    spargeTempViewTitle.setText("Sparge Water Temperature (" + Units.getTemperatureUnits() + ")");
    spargeVolumeViewTitle.setText("Sparge Volume (" + Units.getVolumeUnits() + ")");

    // Get sub-title views
    tunTempViewText = (TextView) tunTempView.findViewById(R.id.text);
    grainTempViewText = (TextView) grainTempView.findViewById(R.id.text);
    spargeTempViewText = (TextView) spargeTempView.findViewById(R.id.text);
    spargeVolumeViewText = (TextView) spargeVolumeView.findViewById(R.id.text);

    // Mash Step divider title
    mashStepTitleView = inflater.inflate(R.layout.view_title, mainView, false);
    mashStepTitleViewText = (TextView) mashStepTitleView.findViewById(R.id.title);
    mashStepTitleViewText.setText("Mash Steps");

    // Listener for mash step list.  When a MashStep is clicked, this listener
    // starts the AddMashStepActivity, passing the current Profile / Recipe.  The
    // AddMashStepActivity will, upon completion, return an updated Recipe object which includes
    // the most up-to-date MashProfile and MashStep list (with any new MashStep, if one was created)
    addMashStepButton = (ImageButton) mashStepTitleView.findViewById(R.id.button);
    addMashStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (v.getId() == R.id.button) {
          Intent i = new Intent(getApplicationContext(), AddMashStepActivity.class);
          i.putExtra(Constants.KEY_PROFILE, mProfile);
          i.putExtra(Constants.KEY_RECIPE, mRecipe);
          startActivityForResult(i, Constants.REQUEST_NEW_MASH_STEP);
        }
      }
    });

    // Set visibility based on mash profile type.
    if (mProfile.getMashType().equals(MashProfile.MASH_TYPE_BIAB)) {
      spargeTypeSpinner.setVisibility(View.GONE);
      tunTempView.setVisibility(View.GONE);
      spargeTempView.setVisibility(View.GONE);
    }

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

    // Listener for mash step list.  When a MashStep is clicked, this listener
    // starts the EditMashStepActivity, passing the current Profile / Recipe, as well as the
    // Mash Step which was clicked.  The EditMashStepActivity will, upon completion, return
    // an updated Recipe object which includes the most up-to-date MashProfile and MashStep list.
    dragDropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
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
  public void onMissedClick(View v) {
    Log.d("AddMashProfileActivity", "Entering onMissedClick()");
    AlertDialog alert;
    if (v.equals(tunTempView)) {
      alert = alertBuilder.editTextFloatAlert(tunTempViewText, tunTempViewTitle).create();
    }
    else if (v.equals(grainTempView)) {
      alert = alertBuilder.editTextFloatAlert(grainTempViewText, grainTempViewTitle).create();
    }
    else if (v.equals(spargeTempView)) {
      alert = alertBuilder.editTextFloatAlert(spargeTempViewText, spargeTempViewTitle).create();
    }
    else if (v.equals(spargeVolumeView)) {
      alert = alertBuilder.editTextFloatAlert(spargeVolumeViewText, spargeVolumeViewTitle).create();
    }
    else {
      return;
    }

    // Force keyboard open and show popup
    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    alert.show();
  }

  /**
   * Creates an Activity-wide callback object which can be called whenever values change which
   * require a mash step list update, or require setting values on the active MashProfile
   * (mProfile)
   */
  public void createCallback() {
    callback = new Callback() {
      @Override
      public void call() {
        // Update the dragDrop list, and update the profile with new values.
        Log.d("AddMashProfileActivity", "Callback hit - updating steps for profile.");
        updateMashStepList();
        try {
          acquireValues();
        } catch (Exception e) {
          Log.d("AddMashProfile", "Failed to acquire values");
        }
      }
    };
  }

  public void setValues() {
    tunTempViewText.setText(String.format("%2.2f", mProfile.getDisplayTunTemp()));
    grainTempViewText.setText(String.format("%2.2f", mProfile.getDisplayGrainTemp()));
    spargeTempViewText.setText(String.format("%2.2f", mProfile.getDisplaySpargeTemp()));
  }

  @Override
  public void setInitialValues() {
    super.setInitialValues();
    nameViewText.setText(mProfile.getName());
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();

    // This call allows each sub-class of this activity to acquire values from the intent
    // differently, while still taking advantage of the super() call to AddEditActivity.  Override
    // this method in sub-classes to change behavior.
    _getValuesFromIntent();
  }

  public void _getValuesFromIntent() {
    // We're creating a new mash profile, so we don't get it from the intent.
    // Create a blank recipe and blank profile.
    mRecipe = new Recipe();
    mProfile = new MashProfile(mRecipe);
    mRecipe.setMashProfile(mProfile);

    // Adds a single mash step, since by default a mash profile does not have a mash step.
    mProfile.addMashStep(new MashStep(mRecipe));

    // Set variables based on the profile.
    name = mProfile.getName();
  }

  @Override
  public void getList() {
    // Get mash and sparge type lists for spinner values.
    mashTypeArray = Constants.MASH_TYPES;
    spargeTypeArray = Constants.SPARGE_TYPES;
  }

  @Override
  public void createSpinner() {
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
  public void setInitialSpinnerSelection() {
    mashTypeSpinner.setSelection(mashTypeArray.indexOf(mProfile.getMashType()));
    spargeTypeSpinner.setSelection(spargeTypeArray.indexOf(mProfile.getSpargeType()));
  }

  @Override
  public void configureSpinnerListener() {

    mashTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        mProfile.setMashType(mashTypeArray.get(position));

        if (mashTypeArray.get(position).equals(MashProfile.MASH_TYPE_BIAB)) {
          // Set the spargeTypeSpinner to BIAB.
          spargeTypeSpinner.setSelection(spargeTypeArray.indexOf(MashProfile.SPARGE_TYPE_BIAB));

          // Make some undesired views disappear.
          spargeTypeSpinner.setVisibility(View.GONE);
          spargeTempView.setVisibility(View.GONE);
          tunTempView.setVisibility(View.GONE);
        }
        else {
          spargeTypeSpinner.setVisibility(View.VISIBLE);
          spargeTempView.setVisibility(View.VISIBLE);
          tunTempView.setVisibility(View.VISIBLE);
        }

        callback.call();
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    };

    spargeTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {
        mProfile.setSpargeType(spargeTypeArray.get(position));
        callback.call();
      }

      public void onNothingSelected(AdapterView<?> parentView) {
      }

    };
  }

  /**
   * Updates the visible list of MashSteps based on the current set of MashSteps in the
   * MashProfile.
   */
  public void updateMashStepList() {
    dragDropAdapter = new MashStepArrayAdapter(this, mProfile.getMashStepList());
    dragDropListView.setAdapter(dragDropAdapter);
    dragDropListView.setDropListener(onDrop);
    dragDropListView.setRemoveListener(onRemove);
    dragDropListView.setDragEnabled(true);

    // Adjust the MashStep list height.
    setListViewHeightBasedOnChildren(dragDropListView);
  }

  /**
   * This method adjusts the height of the given listView to match the combined height of all if its
   * children and the dividers between list items.  This is used to set the height of the mash step
   * list such that it does not scroll, since it is encompassed by a ScrollView.
   *
   * @param listView
   *         ListView to adjust.
   */
  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
    int totalHeight = 0;
    View view = null;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      view = listAdapter.getView(i, view, listView);
      if (i == 0) {
        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
      }

      view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
      totalHeight += view.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
    listView.requestLayout();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
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
  public void onFinished() {
    Log.d("AddMashProfileActivity", "Saving MashProfile");
    mProfile.save(getApplicationContext(), Constants.DATABASE_CUSTOM);
    finish();
  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    double spargeTemp = Double.parseDouble(spargeTempViewText.getText().toString().replace(",", "."));
    double grainTemp = Double.parseDouble(grainTempViewText.getText().toString().replace(",", "."));
    double tunTemp = Double.parseDouble(tunTempViewText.getText().toString().replace(",", "."));

    mProfile.setName(name);
    mProfile.setDisplaySpargeTemp(spargeTemp);
    mProfile.setDisplayGrainTemp(grainTemp);
    mProfile.setDisplayTunTemp(tunTemp);
  }

  @Override
  public void onCancelPressed() {
    Log.d("AddMashProfileActivity", "Cancel Pressed.");
    finish();
  }

  @Override
  public void onDeletePressed() {

  }

  /**
   * Called when the User completes an a call to Add or Edit a MashStep in the list of this
   * MashProfile.  Upon return, this method will receive an updated MashProfile, or NULL (if the
   * user canceled the activity). This method updates the current active MashProfile (mProfile), and
   * sets the profile for the active Recipe (mRecipe).  Or, if NULL is returned, this method does
   * nothing.
   *
   * @param requestCode
   *         Indicates the original intention of calling the Activity.
   * @param resultCode
   *         Indicates the result of the called Activity.
   * @param data
   *         Intent which includes the Activity result.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    MashProfile p;

    if (resultCode == Constants.RESULT_CANCELED) {
      Log.d("AddMashProfileActivity", "Add/Edit mash step activity cancelled.");
      return;
    }

    if (data == null) {
      Log.d("AddMashProfileActivity", "Null intent passed as result, returning");
      return;
    }

    // Acquire the returned profile.
    try {
      p = data.getParcelableExtra(Constants.KEY_MASH_PROFILE);
    } catch (Exception e) {
      Log.d("AddMashProfileActivity", "No profile returned, probably hit back button.");
      return;
    }

    if (p == null) {
      Log.d("AddMashProfileActivity", "No profile returned, probably hit back button.");
      return;
    }

    // Set the new profile.
    mProfile = p;
    mRecipe.setMashProfile(mProfile);
    updateMashStepList();
    super.onActivityResult(requestCode, resultCode, data);
  }
}
