package com.biermacht.brews.frontend.IngredientActivities;

import android.os.Bundle;

import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

public class AddCustomHopsActivity extends AddHopsActivity {

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

    @Override
    public void onFinished()
    {
        Database.addIngredientToVirtualDatabase(Constants.DATABASE_CUSTOM, hop, Constants.MASTER_RECIPE_ID);
        finish();
    }

}
