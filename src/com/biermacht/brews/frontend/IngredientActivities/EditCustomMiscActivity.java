package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class EditCustomMiscActivity extends EditMiscActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove views we don't want
        mainView.removeView(timeView);
        mainView.removeView(amountView);
    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();
        misc.setShortDescription("Custom misc");
        misc.setUseFor("Custom");
    }

    @Override
    public void onFinished()
    {
        Database.updateIngredient(misc, Constants.DATABASE_CUSTOM);
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        Database.deleteIngredientWithId(ingredientId, Constants.DATABASE_CUSTOM);
        finish();
    }
}