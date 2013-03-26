package com.biermacht.brews.frontend.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
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
		TextView beerStyleView = (TextView) row.findViewById(R.id.beer_style);
		TextView batchVolumeView = (TextView) row.findViewById(R.id.batch_volume);
		ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
		
		textView.setText(list.get(position).getRecipeName());
		beerStyleView.setText((list.get(position).getStyle().getName()));
		batchVolumeView.setText(String.format("%2.2f", list.get(position).getBatchSize()));
		
		// Set beer color here
		String color = ColorHandler.getSrmColor(list.get(position).getColor());
		imageView.setBackgroundColor(Color.parseColor(color));

		return row;
	}
}
