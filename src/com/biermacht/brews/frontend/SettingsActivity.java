package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.utils.Units;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private Spinner unitsSpinner;
	private EditText nameEditText;
	private ArrayList<String> measurementSystemsList;
	
	// Settings values
	SharedPreferences preferences;
	private String brewerName;
	private String measurementSystem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Get settings from shared preferences
        preferences = this.getSharedPreferences("com.biermacht.brews", Context.MODE_PRIVATE);
        brewerName = preferences.getString("com.biermacht.brews.settings.brewMasterName", "");
        measurementSystem = preferences.getString("com.biermacht.brews.settings.measurementSystem", Units.IMPERIAL);

        // Views and junk
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        
        // Set values
        nameEditText.setText(brewerName);
        
        // Create list of measurement systems
        measurementSystemsList = new ArrayList<String>();
        measurementSystemsList.add(Units.METRIC);
        measurementSystemsList.add(Units.IMPERIAL);
        
        // Set up grain type spinner
        unitsSpinner = (Spinner) findViewById(R.id.units_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, measurementSystemsList);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        unitsSpinner.setAdapter(adapter);
        
        int pos = 0;
        for (String s : measurementSystemsList)
        {
        	if (s.equals(measurementSystem))
        		break;
        	pos++;
        }
        unitsSpinner.setSelection(pos);    
        
        // Handle beer type selector here
        unitsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
            {
            	measurementSystem = measurementSystemsList.get(position);            
    	    }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
    
    public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{	
			brewerName = nameEditText.getText().toString();
			
	        preferences.edit().putString("com.biermacht.brews.settings.brewMasterName", brewerName).commit();
	        preferences.edit().putString("com.biermacht.brews.settings.measurementSystem", measurementSystem).commit();
			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
