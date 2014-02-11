package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomYeastActivity extends EditYeastActivity {


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Remove views we don't want
        mainView.removeView(amountView);
        
        // Add those we do
        mainView.addView(nameView, 0);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();

        yeast.setShortDescription("Custom yeast");
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
        Database.updateIngredient(yeast, Constants.DATABASE_CUSTOM);
        finish();
    }
}
