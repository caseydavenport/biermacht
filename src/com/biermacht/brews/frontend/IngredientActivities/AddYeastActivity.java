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

    //Rows
    public View attenuationView;
    public View labView;
    public View productIdView;

    // Titles from rows
    public TextView attenuationViewTitle;
    public TextView labViewTitle;
    public TextView productIdViewTitle;

    // Content from rows
    public TextView attenuationViewText;
    public TextView labViewText;
    public TextView productIdViewText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize views and such here
        attenuationView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        labView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        productIdView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set the onClickListener for each row
        attenuationView.setOnClickListener(onClickListener);
        labView.setOnClickListener(onClickListener);
        productIdView.setOnClickListener(onClickListener);

        // Remove views we don't want
        mainView.removeView(timeView);

        // Add views we want
        mainView.addView(attenuationView);

        // Set titles
        attenuationViewTitle = (TextView) attenuationView.findViewById(R.id.title);
        labViewTitle = (TextView) labView.findViewById(R.id.title);
        productIdViewTitle = (TextView) productIdView.findViewById(R.id.title);
        
        attenuationViewTitle.setText("Attenuation (%)");
        labViewTitle.setText("Laboratory");
        productIdViewTitle.setText("Product ID");
        amountViewTitle.setText("Amount (L)");
        searchableListViewTitle.setText("Yeast");

        // Get text views
        attenuationViewText = (TextView) attenuationView.findViewById(R.id.text);
        labViewText = (TextView) labView.findViewById(R.id.text);
        productIdViewText = (TextView) productIdView.findViewById(R.id.text);
        
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
        else if (v.equals(labView))
            dialog = alertBuilder.editTextStringAlert(labViewText, labViewTitle).create();
        else if (v.equals(productIdView))
            dialog = alertBuilder.editTextStringAlert(productIdViewText, productIdViewTitle).create();
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
        labViewText.setText(y.getLaboratory());
        productIdViewText.setText(y.getProductId());
	}

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        double attenuation = Double.parseDouble(attenuationViewText.getText().toString());
        String lab = labViewText.getText().toString();
        String pid = productIdViewText.getText().toString();

        yeast.setName(name);
        yeast.setAttenuation(attenuation);
        yeast.setBeerXmlStandardAmount(amount);
        yeast.setTime(time);
        yeast.setLaboratory(lab);
        yeast.setProductId(pid);
    }

    @Override
    public void onFinished()
    {
        mRecipe.addIngredient(yeast);
        mRecipe.save();
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
