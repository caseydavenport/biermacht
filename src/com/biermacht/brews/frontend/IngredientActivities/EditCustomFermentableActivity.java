package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomFermentableActivity extends AddCustomFermentableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        // Handle fermentable type selections
        fermentableTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                type = typeList.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();
        fermentable = getIntent().getParcelableExtra(Constants.KEY_INGREDIENT);
    }

    @Override
    public void getList()
    {
        // Get ingredient list
        super.getList();
    }

    @Override
    public void setValues()
    {
        nameViewText.setText(fermentable.getName());
        colorViewText.setText(String.format("%2.2f", fermentable.getLovibondColor()));
        gravityViewText.setText(String.format("%2.3f", fermentable.getGravity()));
        descriptionViewText.setText(fermentable.getShortDescription());
        timeViewText.setText("60");
        amountViewText.setText("1");
        fermentableTypeSpinner.setSelection(typeList.indexOf(fermentable.getFermentableType()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }

    @Override
    public void onFinished()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.MASTER_RECIPE_ID);
        Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, fermentable, Constants.MASTER_RECIPE_ID);
        finish();
    }
}

