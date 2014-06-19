package com.biermacht.brews.frontend.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.ColorHandler;

public class RecipeCheckboxArrayAdapter extends ArrayAdapter<Recipe> {
	
	// Fields
	private Context context;
	private List<Recipe> list;
	private Map<String, Boolean> checkedByName;

	// Constructor
	public RecipeCheckboxArrayAdapter(Context context, List<Recipe> list)
	{
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
		this.list = list;
		this.checkedByName = new HashMap<String, Boolean>();
		
		// Initialize checkedByName
		for (Recipe r : list) 
		{
			checkedByName.put(r.getRecipeName(), false);
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{	
		// View to return
		View row = convertView;
				
		if (row == null)
		{
			// Get inflater
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_layout_checkbox, parent, false);
		}
		
		TextView textView = (TextView) row.findViewById(R.id.label);
		textView.setText(list.get(position).getRecipeName());
		final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkbox);
		
		// Set on click listener for checkbox
		checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	checkedByName.put(list.get(position).getRecipeName(), checkBox.isChecked());
            }
        });
		
		// We also want one for the text view
		textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkBox.setChecked(!checkBox.isChecked());
            	checkedByName.put(list.get(position).getRecipeName(), checkBox.isChecked());
			}
		});
		
		// Set the checkbox accordingly
		checkBox.setChecked(checkedByName.get(list.get(position).getRecipeName()));

		return row;
	}
	
	public boolean isChecked(String name)
	{
		return checkedByName.get(name);
	}
}
