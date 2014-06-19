package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class AddCustomFermentableActivity extends AddFermentableActivity {

    // Views for rows
    public View descriptionView;

    // Titles from rows
    public TextView descriptionViewTitle;

    // Content from rows
    public TextView descriptionViewText;

    // Storage for acquired values
    String description;
    String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the description view
        descriptionView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        descriptionView.setOnClickListener(onClickListener);
        descriptionViewText = (TextView) descriptionView.findViewById(R.id.text);
        descriptionViewTitle = (TextView) descriptionView.findViewById(R.id.title);
        descriptionViewText.setText("No Description Provided");
        descriptionViewTitle.setText("Description");

        // Remove views we don't like
        mainView.removeView(amountView);
        mainView.removeView(timeView);
        mainView.removeView(spinnerView);
        mainView.removeView(searchableListView);
        mainView.removeView(nameView);

        // Add views we do!
        mainView.addView(nameView, 0);
        mainView.addView(descriptionView);

        // Set initial values
        setValues();
    }

    @Override
    public void onMissedClick(View v)
    {
        super.onMissedClick(v);

        AlertDialog alert;
        if (v.equals(descriptionView))
            alert = alertBuilder.editTextMultilineStringAlert(descriptionViewText, descriptionViewTitle).create();
        else
        {
            return;
        }

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    // We need this because we don't use spinners in this activity
    public void setValues()
    {
        // Create new fermentable
        fermentable = new Fermentable("New Fermentable");

        // Set values
        nameViewText.setText(fermentable.getName());
        colorViewText.setText("5.00");
        gravityViewText.setText("1.037");
        timeViewText.setText("60");
        amountViewText.setText("1");
        fermentableTypeSpinner.setSelection(0);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        description = descriptionViewText.getText().toString();

        // Set to user provided values
        fermentable.setShortDescription(description);
        fermentable.setFermentableType(type);
    }

    @Override
    public void onFinished()
    {
            Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, fermentable, Constants.MASTER_RECIPE_ID);
            finish();
    }
}
