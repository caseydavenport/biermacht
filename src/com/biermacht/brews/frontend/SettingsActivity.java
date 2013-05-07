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
import com.biermacht.brews.frontend.adapters.*;
import android.widget.*;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private SettingsItemArrayAdapter mAdapter;
	private ArrayList<SettingsItem> settingsList;
	private ListView listView;
	private EditText nameEditText;
	
	// Values for possible settings
	SharedPreferences preferences;
	private String brewerName;
	private String measurementSystem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Get settings from shared preferences
        preferences = this.getSharedPreferences("com.biermacht.brews", Context.MODE_PRIVATE);
        brewerName = preferences.getString("com.biermacht.brews.settings.brewMasterName", "No name found");
        measurementSystem = preferences.getString("com.biermacht.brews.settings.measurementSystem", Units.IMPERIAL);

		// Create settings list
		listView = (ListView) findViewById(R.id.settings_list);
		settingsList = new ArrayList<SettingsItem>();
		SettingsItem brewer = new SettingsItem("Brewer", brewerName, "");
		settingsList.add(brewer);
		
		// Set adapter and view
		mAdapter = new SettingsItemArrayAdapter(this, settingsList);
		listView.setAdapter(mAdapter);
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
