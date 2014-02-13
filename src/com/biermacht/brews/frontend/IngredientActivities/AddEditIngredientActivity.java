package com.biermacht.brews.frontend.IngredientActivities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;

public abstract class AddEditIngredientActivity extends AddEditActivity {

    // Editable rows to display
    public View searchableListView;

    // Titles from rows
    public TextView searchableListViewTitle;

    // Content from rows
    public TextView searchableListViewText;
    
    // Listener for searchable list
    AdapterView.OnItemClickListener searchableListListener;
    
    // Method called in subclass to configure the searchable list listener.
    public abstract void configureSearchableListListener();
    
    // Store the current dialog
    public AlertDialog dialog;
    
    // Textwatcher for searchable list view
    TextWatcher textWatcher;
    
    // Filtered lsit from which the ingredient was chosen.
    ArrayList<Ingredient> filteredList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize views and such here
        searchableListView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        
        // Initialize dialog to null
        dialog = null;

        // Set the onClickListener for each row
        searchableListView.setOnClickListener(onClickListener);

        // Titles
        searchableListViewTitle  = (TextView) searchableListView.findViewById(R.id.title);
        searchableListViewTitle.setText("Select");

        // Text views
        searchableListViewText = (TextView) searchableListView.findViewById(R.id.text);
        
        // Remove views we don't want
        mainView.removeView(nameView);
        mainView.removeView(spinnerView);
        
        // Add views to main view
        mainView.addView(searchableListView, 0);
        
        // Configure the searchable list listener
        configureSearchableListListener();
        
        textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s){}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) 
            {
            	filteredList = new ArrayList<Ingredient>();
            	for (Ingredient i : ingredientList)
            	{
            		if (i.toString().toLowerCase().contains(s.toString().toLowerCase()))
            		{
            			filteredList.add(i);
            		}
            	}
            	
            	adapter.clear();
            	adapter.addAll(filteredList);
                adapter.notifyDataSetChanged();
            }
        };
        
        // Initialize filtered list
        filteredList = new ArrayList<Ingredient>();
        filteredList.addAll(ingredientList);
    }
    
    public void setInitialSearchableListSelection()
    {
    	// By default, set it to the first item in the list.
    	searchableListListener.onItemClick(null, null, 0, 1);
    }
    
    @Override
    public void onMissedClick(View v)
    {   	
        if (v.equals(searchableListView))
            dialog = alertBuilder.searchableListAlert(searchableListViewTitle, searchableListViewText, this.adapter, ingredientList, searchableListListener, textWatcher).create();
        else
            return;
        dialog.show();
    }
}
