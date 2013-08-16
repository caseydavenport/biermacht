package com.biermacht.brews.frontend;

import android.app.Activity;
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
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;

public class AddMashStepActivity extends AddEditActivity {

    // Views
    public View stepTempView;
    public View waterToGrainRatioView;
    public View infuseTemperatureView;

    // Titles
    public TextView stepTempViewTitle;
    public TextView waterToGrainRatioViewTitle;
    public TextView infuseTemperatureViewTitle;

    // Content text
    public TextView stepTempViewText;
    public TextView waterToGrainRatioViewText;
    public TextView infuseTemperatureViewText;

    // mashProfile we are editing
    public MashStep step;

    // step type array
    ArrayList<String> stepTypeArray;

    // Data storage
    String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Inflate views
        stepTempView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        waterToGrainRatioView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        infuseTemperatureView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set listeners
        stepTempView.setOnClickListener(onClickListener);
        waterToGrainRatioView.setOnClickListener(onClickListener);
        infuseTemperatureView.setOnClickListener(onClickListener);

        // Get titles, set values
        stepTempViewTitle = (TextView) stepTempView.findViewById(R.id.title);
        waterToGrainRatioViewTitle = (TextView) waterToGrainRatioView.findViewById(R.id.title);
        infuseTemperatureViewTitle = (TextView) infuseTemperatureView.findViewById(R.id.title);

        // Get content views
        stepTempViewText = (TextView) stepTempView.findViewById(R.id.text);
        waterToGrainRatioViewText = (TextView) waterToGrainRatioView.findViewById(R.id.text);
        infuseTemperatureViewText = (TextView) infuseTemperatureView.findViewById(R.id.text);

        // Set titles
        amountViewTitle.setText("Water to Add (qt)");
        stepTempViewTitle.setText("Step Temperature (F)");
        waterToGrainRatioViewTitle.setText("Water to Grain Ratio (qt/lb)");
        infuseTemperatureViewTitle.setText("Water Temperature (F)");

        // Remove views we don't want

        // Add views that we want
        mainView.addView(infuseTemperatureView);
        mainView.addView(waterToGrainRatioView);
        mainView.addView(stepTempView);

        // Set values
        setValues();
    }

    @Override
    public void onRecipeNotFound()
    {
        Log.d("AddMashStepActivity", "Recipe not needed - using blank recipe");
        mRecipe = new Recipe();
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(infuseTemperatureView))
        alert = alertBuilder.editTextFloatAlert(infuseTemperatureViewText, infuseTemperatureViewTitle).create();
        else if (v.equals(stepTempView))
            alert = alertBuilder.editTextFloatAlert(stepTempViewText, stepTempViewTitle).create();
        else if (v.equals(waterToGrainRatioView))
            alert = alertBuilder.editTextFloatAlert(waterToGrainRatioViewText, waterToGrainRatioViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    public void setValues()
    {
        nameViewText.setText(step.getName());
        timeViewText.setText(String.format("%2.0f", step.getStepTime()));
        amountViewText.setText(String.format("%2.2f", step.getDisplayInfuseAmount()));
        stepTempViewText.setText(String.format("%2.2f", step.getDisplayStepTemp()));
        waterToGrainRatioViewText.setText(String.format("%2.2f", step.getDisplayWaterToGrainRatio()));
        //infuseTemperatureViewText.setText(String.format("%2.2f", step.getDisplayInfuseTemperature()));
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Create mash step
        step = new MashStep();
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

                // Decoction mashes don't have an infuse amount
                if (type.equals(MashStep.DECOCTION))
                    amountView.setVisibility(View.GONE);
                else
                    amountView.setVisibility(View.VISIBLE);

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
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinished()
    {
        Intent result = new Intent();
        result.putExtra(Constants.INTENT_MASH_STEP, step);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double stepTemp = Double.parseDouble(stepTempViewText.getText().toString());
        double infuseTemp = Double.parseDouble(infuseTemperatureViewText.getText().toString());

        step.setName(name);
        step.setBeerXmlStandardInfuseAmount(amount);
        step.setStepTime(time);
        step.setDisplayStepTemp(stepTemp);
        //step.setDisplayInfuseTemperature(infuseTemp);
    }

    @Override
    public void onCancelPressed()
    {
        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
    }

    @Override
    public void onDeletePressed()
    {

    }
}
