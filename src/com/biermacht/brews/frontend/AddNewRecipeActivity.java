package com.biermacht.brews.frontend;

import java.util.ArrayList;

import com.biermacht.brews.R;
import com.biermacht.brews.R.id;
import com.biermacht.brews.R.layout;
import com.biermacht.brews.R.menu;
import com.biermacht.brews.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewRecipeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);
        
        // View holders
        Spinner beerTypeSpinner;
        EditText recipeNameEditText;
        EditText recipeDescriptionEditText;
        
        //Arraylist of beer types
        ArrayList<String> beerTypeArray = Utils.getBeerTypeList();
        
        // Set up beer type spinner
        beerTypeSpinner = (Spinner) findViewById(R.id.beer_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, beerTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerTypeSpinner.setAdapter(adapter);
        beerTypeSpinner.setSelection(0); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }
}
