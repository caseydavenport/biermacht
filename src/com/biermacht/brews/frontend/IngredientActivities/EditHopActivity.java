package com.biermacht.brews.frontend.IngredientActivities;

import java.util.Collections;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.*;
import com.biermacht.brews.utils.comparators.IngredientComparator;

public class EditHopActivity extends AddHopsActivity {

   // Holds the currently selected hop
    Hop selectedHop;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Enable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        // Set listeners
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                use = useArray.get(position);

                if (use.equals(Hop.USE_DRY_HOP))
                {
                    timeViewTitle.setText("Time (days)");
                }
                else
                {
                    timeViewTitle.setText("Time (mins)");
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set spinners
        formSpinner.setSelection(formArray.indexOf(form));
        useSpinner.setSelection(useArray.indexOf(use));
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();

        // Acquire Hop
        hop = getIntent().getParcelableExtra(Constants.INTENT_INGREDIENT);

        // Get the use and form
        use = hop.getUse();
        form = hop.getForm();
    }

    @Override
    public void configureSpinnerListener()
    {
        spinnerListener = new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedHop = (Hop) ingredientList.get(position);

                nameViewText.setText(selectedHop.getName());
                timeViewText.setText(String.format("%d", hop.getDisplayTime()));
                alphaAcidViewText.setText(String.format("%2.2f", selectedHop.getAlphaAcidContent()));
                amountViewText.setText(String.format("%2.2f", hop.getDisplayAmount()));
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        };
    }

    @Override
    public void getList()
    {
        super.getList();

        if (!ingredientList.contains(hop))
        {
            ingredientList.add(hop);
            Collections.sort(ingredientList, new IngredientComparator());
        }
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(ingredientList.indexOf(hop));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_hops, menu);
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

    public void onFinished()
    {
        Database.updateIngredient(hop);
        finish();
    }

    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_DEFAULT);
        finish();
    }
}
