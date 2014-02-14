package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.Database;

import java.util.ArrayList;

public class AddYeastActivity extends AddEditIngredientActivity {

    // Holds currently selected yeast.
    public Yeast yeast;

    public View attenuationView;

    // Titles from rows
    public TextView attenuationViewTitle;

    // Content from rows
    public TextView attenuationViewText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        attenuationView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set the onClickListener for each row
        attenuationView.setOnClickListener(onClickListener);

        // Remove views we don't want
        mainView.removeView(timeView);

        // Add views we want
        mainView.addView(attenuationView);

        // Set titles
        attenuationViewTitle = (TextView) attenuationView.findViewById(R.id.title);
        attenuationViewTitle.setText("Attenuation (%)");
        amountViewTitle.setText("Amount (L)");
        searchableListViewTitle.setText("Yeast");

        // Get text views
        attenuationViewText = (TextView) attenuationView.findViewById(R.id.text);
        
        // Set button text
        submitButton.setText(R.string.add);
        
        // Set initial position for searchable list
        setInitialSearchableListSelection();
    }

    @Override
    public void onMissedClick(View v)
    {
    	super.onMissedClick(v);
    	
        if (v.equals(attenuationView))
            dialog = alertBuilder.editTextFloatAlert(attenuationViewText, attenuationViewTitle).create();
        else
            return;

        // Force keyboard open and show popup
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    @Override
    public void getList()
    {
        // Get the list of ingredients to show
        ingredientList = new ArrayList<Ingredient>();
        ingredientList.addAll(ingredientHandler.getYeastsList());
    }

    @Override
    public void createSpinner()
    {
        // Set up type spinner
        adapter = new IngredientSpinnerAdapter(this, ingredientList, "Yeast", true);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerView.setAdapter(adapter);
    }

	@Override
	public void configureSearchableListListener() 
	{
        searchableListListener = new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) 
            {
                yeast = (Yeast) filteredList.get(position);
                setValues(yeast);
                
                // Cancel dialog
                if (dialog != null)
                {
                	dialog.cancel();
                	dialog = null;
                }
            }
        };
    }
	
	public void setValues(Yeast y)
	{
        nameViewText.setText(y.getName());
        searchableListViewText.setText(y.getName());
        attenuationViewText.setText(String.format("%2.0f", y.getAttenuation()));
        amountViewText.setText(String.format("%2.2f", y.getBeerXmlStandardAmount()));
	}

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double attenuation = Double.parseDouble(attenuationViewText.getText().toString());

        yeast.setName(name);
        yeast.setAttenuation(attenuation);
        yeast.setBeerXmlStandardAmount(amount);
        yeast.setTime(time);
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(yeast);
        Database.updateRecipe(mRecipe);
        finish();
    }

    public void onDeletePressed()
    {
        // Nothing
    }

    @Override
    public void onCancelPressed()
    {
        finish();
    }
}
