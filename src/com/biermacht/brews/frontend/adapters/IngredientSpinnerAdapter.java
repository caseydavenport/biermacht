package com.biermacht.brews.frontend.adapters;

import java.util.List;

import com.biermacht.brews.R;
import com.biermacht.brews.R.id;
import com.biermacht.brews.R.layout;

import com.biermacht.brews.ingredient.Ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngredientSpinnerAdapter extends ArrayAdapter<Ingredient> {
	
	// Fields
	private Context context;
	private List<Ingredient> list;

	// Constructor
	public IngredientSpinnerAdapter(Context context, List<Ingredient> list)
	{
		super(context, android.R.layout.simple_spinner_item, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Get inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// View to return
		View row = inflater.inflate(R.layout.spinner_row_layout, parent, false);
		TextView textView = (TextView) row.findViewById(R.id.selected_view);
		TextView descView = (TextView) row.findViewById(R.id.description_view);
		
		Ingredient ing = list.get(position);
		
		textView.setText(ing.getName());
		descView.setText(ing.getShortDescription());
		
		return row;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		// View to return
		View row = convertView;
				
		if (row == null)
		{
			// Get inflater
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.spinner_row_dropdown_layout, parent, false);
		}
		
		TextView textView = (TextView) row.findViewById(R.id.selected_view);
		TextView descView = (TextView) row.findViewById(R.id.description_view);
		
		Ingredient ing = list.get(position);
		
		textView.setText(ing.getName());
		descView.setText(ing.getShortDescription());
		
		descView.setVisibility(View.VISIBLE);
		
		return row;
	}
}
