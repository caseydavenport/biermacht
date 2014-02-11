package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Units;

import android.view.*;

public class AddFermentableActivity extends AddEditActivity {

    // Holds the currently selected fermentable
    Fermentable fermentable;

    // Editable rows to display
    public View colorView;
    public View gravityView;

    // Titles from rows
    public TextView colorViewTitle;
    public TextView gravityViewTitle;

    // Content from rows
    public TextView colorViewText;
    public TextView gravityViewText;

    // Storage for acquired values
    double gravity;
    double color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        colorView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        gravityView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set the onClickListener for each row
        colorView.setOnClickListener(onClickListener);
        gravityView.setOnClickListener(onClickListener);

        // Add views to main view
        mainView.addView(colorView);
        mainView.addView(gravityView);

        // Set titles for views
        colorViewTitle = (TextView) colorView.findViewById(R.id.title);
        colorViewTitle.setText("SRM Color");

        gravityViewTitle = (TextView) gravityView.findViewById(R.id.title);
        gravityViewTitle.setText("Gravity Contribution");

        amountViewTitle.setText("Amount " + "(" + Units.getFermentableUnits() + ")");

        // Acquire text views
        colorViewText = (TextView) colorView.findViewById(R.id.text);
        gravityViewText = (TextView) gravityView.findViewById(R.id.text);
        
        // Set button text
        submitButton.setText(R.string.add);
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(colorView))
            alert = alertBuilder.editTextFloatAlert(colorViewText, colorViewTitle).create();
        else if (v.equals(gravityView))
            alert = alertBuilder.editTextFloatAlert(gravityViewText, gravityViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void getList()
    {
        ingredientList = new ArrayList<Ingredient>();
        ingredientList.addAll(ingredientHandler.getFermentablesList());
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(0);
    }

    @Override
    public void createSpinner()
    {
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, ingredientList, "Fermentable");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fermentable = (Fermentable) ingredientList.get(position);

                // Set whether we show boil or steep
                if (mRecipe.getType().equals(Recipe.EXTRACT))
                {
                    if (fermentable.getFermentableType().equals(Fermentable.TYPE_EXTRACT))
                    {
                        timeViewTitle.setText(R.string.boil_time);
                        timeViewText.setText(mRecipe.getBoilTime() + "");
                    }
                    else if (fermentable.getFermentableType().equals(Fermentable.TYPE_GRAIN))
                    {
                        timeViewTitle.setText(R.string.steep_time);
                        timeViewText.setText(15 + "");
                    }
                    else
                    {
                        timeViewTitle.setText("Time");
                    }
                }
                else
                {
                    // TODO: Do we ever want to enter a time for mashes?
                    timeView.setVisibility(View.GONE);
                }

                nameViewText.setText(fermentable.getName());
                colorViewText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
                gravityViewText.setText(String.format("%2.3f", fermentable.getGravity()));
                amountViewText.setText(1 +"");
                timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };
    }

    @Override
    public void onDeletePressed()
    {
        // Must be overriden
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
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

        color = Double.parseDouble(colorViewText.getText().toString());
        gravity = Double.parseDouble(gravityViewText.getText().toString());

        fermentable.setName(name);
        fermentable.setTime(time);
        fermentable.setDisplayAmount(amount);
        fermentable.setLovibondColor(color);
        fermentable.setGravity(gravity);
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(fermentable);
        Database.updateRecipe(mRecipe);
        finish();
    }
}
