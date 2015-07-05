package com.biermacht.brews.frontend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;

public class EditFermentationProfileActivity extends ActionBarActivity implements OnClickListener {

  // Main Layout
  private LinearLayout mainLayout;
  private LayoutInflater inflater;
  private LinearLayout primaryLayout;
  private LinearLayout secondaryLayout;
  private LinearLayout tertiaryLayout;

  // Data entry view declarations
  private Spinner numStagesSpinner;

  // Recipe
  private Recipe mRecipe;

  // Arrays
  private ArrayList<String> numStagesArray;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_edit);
    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get recipe from calling activity
    long id = getIntent().getLongExtra(Constants.KEY_RECIPE_ID, Constants.INVALID_ID);
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

    // Set icon as back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Initialize views and stuff
    mainLayout = (LinearLayout) findViewById(R.id.main_layout);
    numStagesSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainLayout, false);
    primaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage,
                                                    mainLayout, false);
    secondaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage,
                                                      mainLayout, false);
    tertiaryLayout = (LinearLayout) inflater.inflate(R.layout.view_edit_fermentation_stage,
                                                     mainLayout, false);

    // Set units appropriately
    TextView tempUnitsView;
    tempUnitsView = (TextView) primaryLayout.findViewById(R.id.temperature_units);
    tempUnitsView.setText(Units.getTemperatureUnits());
    tempUnitsView = (TextView) secondaryLayout.findViewById(R.id.temperature_units);
    tempUnitsView.setText(Units.getTemperatureUnits());
    tempUnitsView = (TextView) tertiaryLayout.findViewById(R.id.temperature_units);
    tempUnitsView.setText(Units.getTemperatureUnits());

    // Set titles
    TextView title;
    title = (TextView) primaryLayout.findViewById(R.id.title);
    title.setText("Primary:");
    title = (TextView) secondaryLayout.findViewById(R.id.title);
    title.setText("Secondary:");
    title = (TextView) tertiaryLayout.findViewById(R.id.title);
    title.setText("Tertiary:");

    // Add sublayouts
    mainLayout.addView(numStagesSpinner);
    mainLayout.addView(primaryLayout);
    mainLayout.addView(secondaryLayout);
    mainLayout.addView(tertiaryLayout);

    // Default values
    View v = new View(this);
    TextView ageView;
    TextView tempView;
    for (int i = Recipe.STAGE_PRIMARY; i <= Recipe.STAGE_TERTIARY; i++) {
      if (i == Recipe.STAGE_PRIMARY) {
        v = primaryLayout;
      }
      if (i == Recipe.STAGE_SECONDARY) {
        v = secondaryLayout;
      }
      if (i == Recipe.STAGE_TERTIARY) {
        v = tertiaryLayout;
      }

      ageView = (TextView) v.findViewById(R.id.age_edit_text);
      tempView = (TextView) v.findViewById(R.id.temp_edit_text);
      ageView.setText(mRecipe.getFermentationAge(i) + "");
      tempView.setText(String.format("%2.0f", mRecipe.getDisplayFermentationTemp(i)));
    }

    // Arraylist of Profiles
    numStagesArray = new ArrayList<String>();
    numStagesArray.add("1");
    numStagesArray.add("2");
    numStagesArray.add("3");

    // Set up num stages spinner
    SpinnerAdapter profAdapter = new SpinnerAdapter(this, numStagesArray, "Number of Stages");
    profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    numStagesSpinner.setAdapter(profAdapter);

    // Determine the correct selection for the num stages spinner
    numStagesSpinner.setSelection(mRecipe.getFermentationStages() - 1);

    // Handle mash profile selector here
    numStagesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                 long id) {

        // Set number of stages
        mRecipe.setFermentationStages(position + 1);

        // Figure out visibility
        secondaryLayout.setVisibility(View.VISIBLE);
        tertiaryLayout.setVisibility(View.VISIBLE);

        if (position == 0)
        // Only one stage
        {
          secondaryLayout.setVisibility(View.GONE);
          tertiaryLayout.setVisibility(View.GONE);
        }
        else if (position == 1)
        // Two stages
        {
          tertiaryLayout.setVisibility(View.GONE);
        }

      }

      public void onNothingSelected(AdapterView<?> parentView) {
        // Blag
      }

    });
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
    if (v.getId() == R.id.cancel_button) {
      finish();
    }

    // Submit Button pressed
    if (v.getId() == R.id.submit_button) {
      boolean readyToGo = true;
      try {
        View view = new View(this);
        TextView ageView;
        TextView tempView;
        for (int i = Recipe.STAGE_PRIMARY; i <= mRecipe.getFermentationStages(); i++) {
          if (i == Recipe.STAGE_PRIMARY) {
            view = primaryLayout;
          }
          if (i == Recipe.STAGE_SECONDARY) {
            view = secondaryLayout;
          }
          if (i == Recipe.STAGE_TERTIARY) {
            view = tertiaryLayout;
          }

          ageView = (TextView) view.findViewById(R.id.age_edit_text);
          tempView = (TextView) view.findViewById(R.id.temp_edit_text);
          mRecipe.setFermentationAge(i, Integer.parseInt(ageView.getText().toString()));
          mRecipe.setDisplayFermentationTemp(i, Double.parseDouble(tempView.getText().toString()
                                                                           .replace(",", ".")));
        }
      } catch (Exception e) {
        readyToGo = false;
      }

      if (readyToGo) {
        Database.updateRecipe(mRecipe);
        finish();
      }
    }
  }
}
