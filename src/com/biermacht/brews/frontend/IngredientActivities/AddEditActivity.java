package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.ItemNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Callbacks.Callback;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.IngredientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This abstract class is used as a basis for all Activities which add or edit an object. Some
 * examples would be Recipes, MashProfiles, or Settings.  This class defines a framework for adding
 * views, acquiring values from those views, saving or deleting objects, and more.
 */
public abstract class AddEditActivity extends ActionBarActivity implements OnClickListener {

  // Main view - holds all the rows
  public ViewGroup mainView;

  // Alert builder
  public AlertBuilder alertBuilder;

  // Important things
  public OnClickListener onClickListener;

  // LayoutInflater
  public LayoutInflater inflater;

  // Shared preferences
  public SharedPreferences preferences;

  // Recipe we are editing
  public Recipe mRecipe;

  // IDs received in intent
  public long recipeId;
  public long ingredientId;
  public long mashProfileId;

  // Array adapter for spinner
  public ArrayAdapter adapter;

  // Keeps track of all registered views
  public ArrayList<View> registeredViews;

  // IngredientHandler to get ingredient arrays
  public IngredientHandler ingredientHandler;

  // Holds the currently selected ingredient
  public Ingredient ingredient;

  // Editable rows to display
  public Spinner spinnerView;
  public View searchableListView;
  public View nameView;
  public View amountView;
  public View timeView;

  // Titles from rows
  public TextView searchableListViewTitle;
  public TextView nameViewTitle;
  public TextView amountViewTitle;
  public TextView timeViewTitle;

  // Content from rows
  public TextView searchableListViewText;
  public TextView nameViewText;
  public TextView amountViewText;
  public TextView timeViewText;

  // Storage for acquired values
  public int time;
  public double amount;
  public String name;

  // Bottom buttons
  public Button submitButton;
  public Button cancelButton;
  public Button deleteButton;

  // Spinner array declarations
  public ArrayList<Ingredient> ingredientList;

  // Listeners
  public OnItemSelectedListener spinnerListener;

  // Callback for on alertBuilder return.
  public Callback callback;

  // Flags for use by sub-classes
  public boolean saveRecipeOnSubmit;

  // Abstract methods
  public abstract void onMissedClick(View v);

  /**
   * Called when spinner views should be configured.
   */
  public abstract void createSpinner();

  /**
   * Hook to acquire a list of items relevant to this specific AddEditActivity implementation.
   */
  public abstract void getList();

  /**
   * Called when the cancel button is pressed by the user - no config should be changed.
   */
  public abstract void onCancelPressed();

  /**
   * Called when the delete button is pressed by the user - should delete the object in reference.
   */
  public abstract void onDeletePressed();

  /**
   * Called when the save/create button is pressed by the user.  Should save the object in
   * reference.
   */
  public abstract void onFinished();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_edit);

    // By default, save the current recipe when submit is pressed.
    this.saveRecipeOnSubmit = true;

    // Set icon as back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Get the Ingredient Handler
    ingredientHandler = MainActivity.ingredientHandler;

    // Get the inflater
    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Create alert builder
    createCallback();
    alertBuilder = new AlertBuilder(this, callback);

    // Get shared preferences
    preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

    // Disable delete button
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Get recipe and other ids from calling activity
    Log.d("AddEditActivity", "Calling getValuesFromIntent()");
    getValuesFromIntent();

    // Get the list to show
    Log.d("AddEditActivity", "Calling getList()");
    getList();

    // On click listener
    onClickListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        /************************************************************
         * Options for clicking on each of the editable views
         ************************************************************/
        AlertDialog alert;
        if (v.equals(nameView)) {
          alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
        }
        else if (v.equals(amountView)) {
          alert = alertBuilder.editTextFloatAlert(amountViewText, amountViewTitle).create();
        }
        else if (v.equals(timeView)) {
          alert = alertBuilder.editTextIntegerAlert(timeViewText, timeViewTitle).create();
        }
        else {
          Log.d("AddEditActivity", "View not found for click, calling onMissedClick()");
          onMissedClick(v);
          return;
        }

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams
                                                   .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
      }
    };

    // Get buttons
    submitButton = (Button) findViewById(R.id.submit_button);
    cancelButton = (Button) findViewById(R.id.cancel_button);
    deleteButton = (Button) findViewById(R.id.delete_button);

    // Initialize views and such here
    mainView = (ViewGroup) findViewById(R.id.main_layout);
    searchableListView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    nameView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    amountView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    timeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
    spinnerView = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

    /************************************************************************
     ************* Add views *************************************************
     *************************************************************************/
    this.registerViews(Arrays.asList(spinnerView, nameView, timeView, amountView));
    this.setViews(Arrays.asList(spinnerView, nameView, timeView, amountView));

    /************************************************************************
     ************* Get titles, set values   **********************************
     *************************************************************************/
    searchableListViewTitle = (TextView) searchableListView.findViewById(R.id.title);
    searchableListViewTitle.setText("Select");

    nameViewTitle = (TextView) nameView.findViewById(R.id.title);
    nameViewTitle.setText("Name");

    timeViewTitle = (TextView) timeView.findViewById(R.id.title);
    timeViewTitle.setText("Time (mins)");

    amountViewTitle = (TextView) amountView.findViewById(R.id.title);
    amountViewTitle.setText("Amount (lbs)");

    /************************************************************************
     ************* Get content views, values set below  **********************
     *************************************************************************/
    searchableListViewText = (TextView) searchableListView.findViewById(R.id.text);
    nameViewText = (TextView) nameView.findViewById(R.id.text);
    timeViewText = (TextView) timeView.findViewById(R.id.text);
    amountViewText = (TextView) amountView.findViewById(R.id.text);

    // Set any initial values here.
    Log.d("AddEditActivity", "Calling setInitialValues()");
    setInitialValues();

    // Set up type spinner
    Log.d("AddEditActivity", "Calling createSpinner()");
    createSpinner();
    Log.d("AddEditActivity", "Calling setInitialSpinnerSelection()");
    setInitialSpinnerSelection();

    // Handle type spinner selections here
    Log.d("AddEditActivity", "Calling configureSpinnerListener()");
    configureSpinnerListener();
    spinnerView.setOnItemSelectedListener(spinnerListener);
  }

  public void configureSpinnerListener() {
    Log.d("AddEditActivity", "Configuring default spinner listener");
    spinnerListener = new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Log.d("AddEditActivity", "Item selected using default listener");
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
      }
    };
  }

  public void createCallback() {
    callback = new Callback() {
      @Override
      public void call() {
        Log.d("AddEditActivity", "Entering default callback");
      }
    };
  }

  public void getValuesFromIntent() {
    recipeId = getIntent().getLongExtra(Constants.KEY_RECIPE_ID, Constants.INVALID_ID);
    ingredientId = getIntent().getLongExtra(Constants.KEY_INGREDIENT_ID, Constants.INVALID_ID);
    mashProfileId = getIntent().getLongExtra(Constants.KEY_PROFILE_ID, Constants.INVALID_ID);

    // Acquire recipe
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

    if (mRecipe == null) {
      Log.d("AddEditActivity", "NULL Recipe passed via Intent");
      if (recipeId != Constants.INVALID_ID) {
        Log.d("AddEditActivity", "Found recipe ID, Trying database");
        try {
          mRecipe = Database.getRecipeWithId(recipeId);
          Log.d("AddEditActivity", "Found recipe in database");
        } catch (ItemNotFoundException e) {
          e.printStackTrace();
          Log.d("AddEditActivity", "Database lookup failed, calling onRecipeNotFound()");
          onRecipeNotFound();
          return;
        }
      }
      else {
        Log.d("AddEditActivity", "No recipe ID passed via Intent, Calling onRecipeNotFound()");
        onRecipeNotFound();
        return;
      }
    }
    Log.d("AddEditActivity", "Successfully retrieved basic values from intent!");
  }

  public void onRecipeNotFound() {
    Log.d("AddEditActivity", "Recipe not found, finishing");
    finish();
  }

  public void setInitialSpinnerSelection() {
    spinnerView.setSelection(0);
  }

  public void setInitialValues() {
    amountViewText.setText("1.0");
    timeViewText.setText("60");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
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

  public void acquireValues() throws Exception {
    Log.d("AddEditActivity", "Acquiring values");
    name = nameViewText.getText().toString();
    amount = Double.parseDouble(amountViewText.getText().toString().replace(",", "."));
    time = Integer.parseInt(timeViewText.getText().toString());
  }

  public void onSubmitPressed() {
    boolean readyToGo = true;
    try {
      acquireValues();
    } catch (Exception e) {
      Log.d("AddEditActivity", "Could not acquire values: " + e.toString());
      e.printStackTrace();
      readyToGo = false;
    }

    if (readyToGo) {
      Log.d("AddEditActivity", "Successfully acquired values, validate.");
      if (this.validateValues()) {
        Log.d("AddEditActivity", "Values valid, save");

        // Some activities don't want to save the recipe on submit.  Such an activity can set this
        // flag to false, thus not saving the recipe when the submit button is pressed.
        if (this.saveRecipeOnSubmit) {
          mRecipe.save();
        }
        onFinished();
      }
    }
  }

  public boolean validateValues() {
    // Validates the values stored in acquireValues.  Returns true if values are all valid,
    // false otherwise.
    return true;
  }

  public void onClick(View v) {
    // if "SUBMIT" button pressed
    if (v.getId() == R.id.submit_button) {
      Log.d("AddEditActivity", "Submit Pressed");
      onSubmitPressed();
    }

    // If "DELETE" button pressed
    if (v.getId() == R.id.delete_button) {
      Log.d("AddEditActivity", "Delete Pressed");
      onDeletePressed();
    }

    // if "CANCEL" button pressed
    if (v.getId() == R.id.cancel_button) {
      Log.d("AddEditActivity", "Cancel Pressed");
      onCancelPressed();
    }
  }

  public void registerViews(List<View> views) {
    if (this.registeredViews == null) {
      Log.d("AddEditActivity", "Initializing registeredViews");
      this.registeredViews = new ArrayList<View>();
    }

    this.registeredViews.addAll(views);
    for (View v : views) {
      Log.d("AddEditActivity", "Registering view");
      mainView.addView(v);
      if (! (v instanceof Spinner)) {
        // Register any text row views with the onClickListener so that they can receive
        // notification when the view is clicked.
        v.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * Helper method to define the set of visible row Views.
   *
   * @param views
   *         List of Views which should be displayed.
   */
  public void setViews(List<View> views) {
    Log.d("AddEditActivity", "Setting visible views");
    for (View v : this.registeredViews) {
      if (views.contains(v)) {
        v.setVisibility(View.VISIBLE);
      }
      else {
        v.setVisibility(View.GONE);
      }
    }

    // Remove and re-add each view so that ordering matches the given list.
    for (View v : views) {
      mainView.removeView(v);
      mainView.addView(v);
    }
  }
}
