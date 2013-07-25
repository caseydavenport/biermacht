package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import com.biermacht.brews.R;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.frontend.adapters.*;
import android.widget.*;
import android.app.*;
import android.content.*;
import android.view.*;
import com.biermacht.brews.utils.*;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private SettingsItemArrayAdapter mAdapter;
	private ArrayList<SettingsItem> settingsList;
	private ListView listView;
	private EditText nameEditText;
	private OnItemClickListener mClickListener;
	
	// Values for possible settings
	SharedPreferences preferences;
	private String brewerName;
	private String measurementSystem;
	
	// Settings
	public static String TITLE_BREWER = "Brewmaster";
	public static String TITLE_DELETE_ALL = "Delete All Recipes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Get settings from shared preferences
        preferences = this.getSharedPreferences("com.biermacht.brews", Context.MODE_PRIVATE);
        brewerName = preferences.getString("com.biermacht.brews.settings.brewMasterName", "No name found");
        measurementSystem = preferences.getString("com.biermacht.brews.settings.measurementSystem", Units.IMPERIAL);

		updateSettings();
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
			preferences.edit().putString("com.biermacht.brews.settings.measurementSystem", measurementSystem).commit();
			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
	
	public void updateSettings()
	{
		// Create settings list
		listView = (ListView) findViewById(R.id.settings_list);
		settingsList = new ArrayList<SettingsItem>();
		SettingsItem brewer = new SettingsItem(TITLE_BREWER, brewerName, "");
		SettingsItem deleteAllRecipes = new SettingsItem(TITLE_DELETE_ALL, "Permanently delete all local recipes.", "");
		settingsList.add(brewer);
		settingsList.add(deleteAllRecipes);

		// Set up the onClickListener
        mClickListener = new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id)
			{	
			    if(settingsList.get(pos).getTitle().equals(TITLE_BREWER))
				{
					setBrewer().show();
				}
				else if (settingsList.get(pos).getTitle().equals(TITLE_DELETE_ALL))
				{
					deleteAllRecipes().show();
				}
			}
        };

		// Set adapter and view
		mAdapter = new SettingsItemArrayAdapter(this, settingsList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(mClickListener);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
            case android.R.id.home:
        		finish();
        		return true;

        }
        return super.onOptionsItemSelected(item);
    }
	
	
	// BELOW GO ALL THE ALERT CLASSES NEEDED FOR EACH SETTING		
	private Builder setBrewer()
	{
		LayoutInflater factory = LayoutInflater.from(this);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
		editText.setHint("Enter your name");
		editText.setText(brewerName);

		return new AlertDialog.Builder(this)
			.setTitle(TITLE_BREWER)
			.setView(alertView)
			.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {	
					brewerName = editText.getText().toString();
					preferences.edit().putString("com.biermacht.brews.settings.brewMasterName", brewerName).commit();
					updateSettings();
				}

		    })

		    .setNegativeButton(R.string.cancel, null);
	}
	
	private Builder deleteAllRecipes()
	{
		return new AlertDialog.Builder(this)
			.setTitle(TITLE_DELETE_ALL)
			.setMessage("Delete all local recipes from this device? This action cannot be undone.  Remote recipes will be unaffected.")
			.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Utils.deleteAllRecipes();
				}

		    })

		    .setNegativeButton(R.string.cancel, null);
	}
}
