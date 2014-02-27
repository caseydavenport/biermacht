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
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Callbacks.BooleanCallback;
import com.biermacht.brews.utils.Callbacks.Callback;

import java.util.ArrayList;

public class AddMashStepActivity extends AddEditActivity {

    // Views
    public View stepTempView;
    public View waterToGrainRatioView;
    public View infuseTemperatureView;
    public View stepAmountView; 		// Create custom amount view for autocalc.

    // Titles
    public TextView stepTempViewTitle;
    public TextView waterToGrainRatioViewTitle;
    public TextView infuseTemperatureViewTitle;
    public TextView stepAmountViewTitle;

    // Content text
    public TextView stepTempViewText;
    public TextView waterToGrainRatioViewText;
    public TextView infuseTemperatureViewText;
    public TextView stepAmountViewText;

    // mashProfile we are editing
    public MashStep step;
    public long stepId;
    
    // Callbacks for autocalc fields.
    public BooleanCallback infuseTempCallback;
    public BooleanCallback infuseAmountCallback;

    // step type array
    ArrayList<String> stepTypeArray;

    // Data storage
    String type;
    MashProfile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Inflate views
        stepTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        waterToGrainRatioView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        infuseTemperatureView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        stepAmountView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set listeners
        stepTempView.setOnClickListener(onClickListener);
        waterToGrainRatioView.setOnClickListener(onClickListener);
        infuseTemperatureView.setOnClickListener(onClickListener);
        stepAmountView.setOnClickListener(onClickListener);

        // Get titles, set values
        stepTempViewTitle = (TextView) stepTempView.findViewById(R.id.title);
        waterToGrainRatioViewTitle = (TextView) waterToGrainRatioView.findViewById(R.id.title);
        infuseTemperatureViewTitle = (TextView) infuseTemperatureView.findViewById(R.id.title);
        stepAmountViewTitle = (TextView) stepAmountView.findViewById(R.id.title);

        // Get content views
        stepTempViewText = (TextView) stepTempView.findViewById(R.id.text);
        waterToGrainRatioViewText = (TextView) waterToGrainRatioView.findViewById(R.id.text);
        infuseTemperatureViewText = (TextView) infuseTemperatureView.findViewById(R.id.text);
        stepAmountViewText = (TextView) stepAmountView.findViewById(R.id.text);


        // Set titles
        stepAmountViewTitle.setText("Water to Add (" + (Units.getUnitSystem() == Units.IMPERIAL ? "qt" : "L") + ")");
        stepTempViewTitle.setText("Step Temperature (" + Units.getTemperatureUnits() + ")");
        waterToGrainRatioViewTitle.setText("Water to Grain Ratio (" + (Units.getUnitSystem() == Units.IMPERIAL ? "qt/lb" : "L/kg") + ")");
        infuseTemperatureViewTitle.setText("Water Temperature (" + Units.getTemperatureUnits() + ")");

        // Remove views we don't want
        mainView.removeView(amountView);

        // Add views that we want
        mainView.addView(stepTempView);
        mainView.addView(waterToGrainRatioView);
        mainView.addView(infuseTemperatureView);
        mainView.addView(stepAmountView);
        
        // Change button text to say "Add" instead of "Submit"
        submitButton.setText("Add");

        // Set values
        setValues();
    }

    @Override
    public void onRecipeNotFound()
    {
        Log.d("AddMashStepActivity", "Recipe not found - using blank recipe");
        mRecipe = new Recipe();
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(infuseTemperatureView))
        	alert = alertBuilder.editTextFloatCheckBoxAlert(infuseTemperatureViewText, infuseTemperatureViewTitle, 
        			                                        step.getAutoCalcInfuseTemp(), infuseTempCallback).create();
        else if (v.equals(stepTempView))
            alert = alertBuilder.editTextFloatAlert(stepTempViewText, stepTempViewTitle).create();
        else if (v.equals(waterToGrainRatioView))
        {
        	if (step.firstInList())
        		alert = alertBuilder.editTextFloatAlert(waterToGrainRatioViewText, waterToGrainRatioViewTitle).create();
        	else
        		alert = alertBuilder.editTextDisabled(waterToGrainRatioViewText, waterToGrainRatioViewTitle, Constants.MESSAGE_AUTO_CALC_W2GR).create();
        }
        else if (v.equals(stepAmountView))
        {
        	if (step.getType().equals(MashStep.DECOCTION))
                alert = alertBuilder.editTextFloatAlert(stepAmountViewText, stepAmountViewTitle).create();
        	else
            	alert = alertBuilder.editTextFloatCheckBoxAlert(stepAmountViewText, stepAmountViewTitle, 
                        step.getAutoCalcInfuseAmt(), infuseAmountCallback).create();
        }
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }
    
    @Override
    public void createCallback()
    {
        // Default callback, called when alertBuilders are finished.
        // Allows us to update fields that are dependent on other fields.
        callback = new Callback()
        {
            @Override
            public void call()
            {
                try
                {
                    // Get new values
                	Log.d("AddMashStepActivity", "Calling callback.");
                    acquireValues();
                }
                catch (Exception e)
                {
                    Log.d("AddMashStepActivity", "Exception in callback from alert dialog");
                    e.printStackTrace();
                }
                // Update field values
                setValues();
            }
        };

        // Callbacks for autocalculation.
        infuseTempCallback = new BooleanCallback()
        {
            @Override
            public void call(boolean b)
            {
            	Log.d("AddMashStepActivity", "Infuse temp autocalc checkbox pressed, set to: " + b);
            	step.setAutoCalcInfuseTemp(b);
                infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
                stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
                waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
            }
        };
        
        // Callbacks for autocalculation.
        infuseAmountCallback = new BooleanCallback()
        {
            @Override
            public void call(boolean b)
            {
            	Log.d("AddMashStepActivity", "Infuse amount autocalc checkbox pressed, set to: " + b);
            	step.setAutoCalcInfuseAmt(b);
                stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
                infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
                waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
            }
        };
    }

    public void setValues()
    {
        nameViewText.setText(step.getName());
        timeViewText.setText(String.format("%d", (int) step.getStepTime()));
        stepAmountViewText.setText(String.format("%2.2f", step.getDisplayAmount()));
        stepTempViewText.setText(String.format("%2.2f", step.getDisplayStepTemp()));
        waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
        infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemp()));
        Log.d("SetValsAddMashStep", "Got amount: " + step.getDisplayAmount());
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Create mash step
        profile = getIntent().getParcelableExtra(Constants.KEY_PROFILE);
        mRecipe.setMashProfile(profile);
        step = new MashStep(mRecipe);
        step.setOrder(profile.getNumberOfSteps());
        profile.addMashStep(step);
    }

    @Override
    public void getList()
    {
        stepTypeArray = Constants.MASH_STEP_TYPES;
    }

    @Override
    public void createSpinner()
    {
        SpinnerAdapter adapter = new SpinnerAdapter(this, stepTypeArray, "Type");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        //
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                type = stepTypeArray.get(position);

                // Decoction mashes don't have an infuse amount, but they do have 
                // a decoction amount.
                if (type.equals(MashStep.DECOCTION))
                {
                    stepAmountViewTitle.setText("Amount to decoct (" + Units.getVolumeUnits() + ")");
                	infuseTemperatureView.setVisibility(View.GONE);
                	waterToGrainRatioView.setVisibility(View.GONE);
                	
                }
                else
                {
                    stepAmountViewTitle.setText("Water to add (" + Units.getVolumeUnits() + ")");
                	infuseTemperatureView.setVisibility(View.VISIBLE);
                	waterToGrainRatioView.setVisibility(View.VISIBLE);
                }
                
                // Update selected values.
                setValues();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(Constants.RESULT_CANCELED, new Intent());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinished()
    {
        Intent result = new Intent();
        result.putExtra(Constants.KEY_MASH_STEP, step);
        setResult(Constants.RESULT_OK, result);
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double stepTemp = Double.parseDouble(stepTempViewText.getText().toString());
        double infuseTemp = Double.parseDouble(infuseTemperatureViewText.getText().toString());
        double waterToGrainRatio = Double.parseDouble(waterToGrainRatioViewText.getText().toString());

        step.setName(name);
        step.setDisplayInfuseAmount(amount);
        step.setStepTime(time);
        step.setType(type);
        step.setDisplayStepTemp(stepTemp);
        step.setDisplayWaterToGrainRatio(waterToGrainRatio);
        step.setDisplayInfuseTemp(infuseTemp);
    }

    @Override
    public void onCancelPressed()
    {
        setResult(Constants.RESULT_CANCELED, new Intent());
        profile.removeMashStep(step);
        finish();
    }

    @Override
    public void onDeletePressed()
    {

    }
}
