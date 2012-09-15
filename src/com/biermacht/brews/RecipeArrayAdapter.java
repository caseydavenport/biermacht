package com.biermacht.brews;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.utils.ColorHandler;

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
	
	// Fields
	private Context context;
	private List<Recipe> list;

	// Constructor
	public RecipeArrayAdapter(Context context, List<Recipe> list)
	{
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	
		// View to return
		View row = convertView;
				
		if (row == null)
		{
			// Get inflater
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.recipe_row_layout, parent, false);
		}
		
		TextView textView = (TextView) row.findViewById(R.id.label);
		ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
		
		if (list.size() != 0)
		{
			textView.setText(list.get(position).getRecipeName());
			
			// Set beer color here
			Log.e("COLOR", "COLOR: " + list.get(position).getColor() + " " + ColorHandler.getSrmColor(list.get(position).getColor()));
			String color = ColorHandler.getSrmColor(10); //ColorHandler.getSrmColor(list.get(position).getColor());
			imageView.setBackgroundColor(Color.parseColor("#a55936"));
			
			// Set imageView based on beer type
			String beerType = list.get(position).getBeerType();
			
		}
		return row;
	}
}
