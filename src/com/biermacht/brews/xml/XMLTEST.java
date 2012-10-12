package com.biermacht.brews.xml;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.IngredientHandler;

public class XMLTEST extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmltest);
        
        IngredientHandler ih = new IngredientHandler(this);
        ArrayList<Ingredient> list = ih.getFermentablesList();
        
        for (Ingredient i : list)
        {
        	Log.e("XMLTEST", i.getName() + " : " + i.getAmount() + " : " + i.getType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_xmltest, menu);
        return true;
    }
}
