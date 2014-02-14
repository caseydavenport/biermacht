package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditYeastActivity extends AddYeastActivity {

    public Yeast selectedYeast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
        
        // Set button text
        submitButton.setText(R.string.save);
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
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();
        yeast = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);
    }

    @Override
    public void getList()
    {
        super.getList();

        if (!ingredientList.contains(yeast))
            ingredientList.add(0, yeast);
    }

    @Override
    public void configureSearchableListListener()
    {
        searchableListListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedYeast = (Yeast) filteredList.get(position);
                
                setValues(selectedYeast);
                
                // Cancel dialog.
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
	    amountViewText.setText(String.format("%2.2f", yeast.getBeerXmlStandardAmount()));
    }
    
    public void setInitialSearchableListSelection()
    {
    	setValues(yeast);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(ingredientList.indexOf(yeast));
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(yeast.getId(), Constants.DATABASE_DEFAULT);
        finish();
    }

    @Override
    public void onFinished()
    {
        Database.updateIngredient(yeast, Constants.DATABASE_DEFAULT);
        finish();
    }
}
