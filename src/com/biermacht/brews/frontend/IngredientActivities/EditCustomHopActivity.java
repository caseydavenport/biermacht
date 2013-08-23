package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomHopActivity extends EditHopActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Remove views we don't want
        mainView.removeView(timeView);
        mainView.removeView(amountView);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        hop.setShortDescription("Custom hop");
    }

    public void onFinished()
    {
        Database.updateIngredient(hop, Constants.DATABASE_CUSTOM);
        finish();
    }

    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }
}
