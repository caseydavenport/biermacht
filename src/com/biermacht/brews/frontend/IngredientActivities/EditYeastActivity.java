package com.biermacht.brews.frontend.IngredientActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.ItemNotFoundException;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.IngredientSpinnerAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.IngredientHandler;

import java.util.ArrayList;

public class EditYeastActivity extends AddYeastActivity implements OnClickListener {

    public Yeast selectedYeast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

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

        if (!yeastsArray.contains(yeast))
            yeastsArray.add(0, yeast);
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedYeast = (Yeast) yeastsArray.get(position);

                nameViewText.setText(selectedYeast.getName());
                attenuationViewText.setText(String.format("%2.0f", selectedYeast.getAttenuation()));
                amountViewText.setText(String.format("%2.2f", yeast.getBeerXmlStandardAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(yeastsArray.indexOf(yeast));
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
