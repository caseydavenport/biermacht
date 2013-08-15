package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import java.util.ArrayList;

public class AddCustomFermentableActivity extends AddFermentableActivity {

    // Views for rows
    public View descriptionView;
    public Spinner fermentableTypeSpinner;

    // Titles from rows
    public TextView descriptionViewTitle;

    // Content from rows
    public TextView descriptionViewText;

    // Arrays
    public ArrayList<String> typeList;

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

        // Get possible fermentable type list
        typeList = Constants.FERMENTABLE_TYPES;

        // Create spinner for fermentable type
        fermentableTypeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        SpinnerAdapter adapter = new SpinnerAdapter(this, typeList, "Type");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fermentableTypeSpinner.setAdapter(adapter);

        // Handle fermentable type selections
        fermentableTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                type = typeList.get(position);
                if (type.equals(Fermentable.TYPE_EXTRACT) || type.equals(Fermentable.TYPE_SUGAR))
                    gravityViewText.setText("1.044");
                else
                    gravityViewText.setText("1.037");
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Remove views we don't like
        mainView.removeView(amountView);
        mainView.removeView(timeView);
        mainView.removeView(spinnerView);

        // Add views we do!
        mainView.addView(fermentableTypeSpinner);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
		
    	switch (item.getItemId())
		{
            case android.R.id.home:
        		finish();
        		return true; 
        }
        return super.onOptionsItemSelected(item);
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
