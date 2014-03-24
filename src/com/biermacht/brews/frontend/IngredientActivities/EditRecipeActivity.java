package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.AddRecipeActivity;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditRecipeActivity extends AddRecipeActivity {

    // Rows to be displayed
    private View measuredOGView;
    private View measuredFGView;

    // Titles
    private TextView measuredOGViewTitle;
    private TextView measuredFGViewTitle;

    // Contents
    private TextView measuredOGViewText;
    private TextView measuredFGViewText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize views and stuff
        measuredFGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        measuredOGView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set onClickListeners for edit text views
        measuredFGView.setOnClickListener(onClickListener);
        measuredOGView.setOnClickListener(onClickListener);

        // Add views to main view
        mainView.addView(measuredOGView);
        mainView.addView(measuredFGView);

        // Titles
        measuredFGViewTitle = (TextView) measuredFGView.findViewById(R.id.title);
        measuredFGViewTitle.setText("Measured Final Gravity");

        measuredOGViewTitle = (TextView) measuredOGView.findViewById(R.id.title);
        measuredOGViewTitle.setText("Measured Original Gravity");

        // Texts
        measuredFGViewText = (TextView) measuredFGView.findViewById(R.id.text);
        measuredFGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredFG()));

        measuredOGViewText = (TextView) measuredOGView.findViewById(R.id.text);
        measuredOGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredOG()));
        
        // Set button text
        submitButton.setText(R.string.save);
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();
        mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);
        style = mRecipe.getStyle();
        profile = mRecipe.getMashProfile();
        type = mRecipe.getType();
        efficiency = mRecipe.getEfficiency();
    }

    @Override
    public void onMissedClick(View v)
    {
        super.onMissedClick(v);

        AlertDialog alert;
        if (v.equals(measuredFGView))
            alert = alertBuilder.editTextFloatAlert(measuredFGViewText, measuredFGViewTitle).create();
        else if (v.equals(measuredOGView))
            alert = alertBuilder.editTextFloatAlert(measuredOGViewText, measuredOGViewTitle).create();
        else
            return; // In case its none of those views...

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void onFinished()
    {
    	mRecipe.save();
        finish();
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double measuredOg = Double.parseDouble(measuredOGViewText.getText().toString());
        double measuredFg = Double.parseDouble(measuredFGViewText.getText().toString());

        mRecipe.setMeasuredFG(measuredFg);
        mRecipe.setMeasuredOG(measuredOg);
    }
}
