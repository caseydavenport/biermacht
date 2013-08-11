package com.biermacht.brews.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Utils;

import java.util.ArrayList;

public class AddCustomYeastActivity extends AddYeastActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

    public void onClick(View v) {
        // if "SUBMIT" button pressed
        if (v.getId() == R.id.submit_button)
        {
            boolean readyToGo = true;
            try
            {
                String name = nameViewText.getText().toString();
                double attenuation = Double.parseDouble(attenuationViewText.getText().toString());
                double amount = Double.parseDouble(amountViewText.getText().toString());

                yeast.setName(name);
                yeast.setAttenuation(attenuation);
                yeast.setBeerXmlStandardAmount(amount);
            } catch (Exception e)
            {
                Log.d("EditYeastActivity", "Exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                Utils.addIngredientToCustomDatabase(yeast, Constants.MASTER_RECIPE_ID);
                finish();
            }
        }

        // if "CANCEL" button pressed
        if (v.getId() == R.id.cancel_button)
        {
            finish();
        }
    }
}
