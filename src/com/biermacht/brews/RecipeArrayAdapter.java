package com.biermacht.brews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		// Get inflater
		LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		// View to return
		View row = inflater.inflate(R.layout.recipe_row_layout, parent, false);
		TextView textView = (TextView) row.findViewById(R.id.label);
		ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
		textView.setText(list.get(position).getRecipeName());
		
		// Set imageView based on beer type
		String beerType = list.get(position).getBeerType();
		
		if(beerType == Recipe.BEERTYPE_STOUT)
			imageView.setImageResource(R.drawable.icon_stout);
		else if(beerType == Recipe.BEERTYPE_HEFEWEIZEN)
			imageView.setImageResource(R.drawable.icon_hefeweizen);
		else if(beerType == Recipe.BEERTYPE_IPA)
			imageView.setImageResource(R.drawable.icon_ipa);
		else
			imageView.setImageResource(R.drawable.icon_default);

		
		
		return row;
	}

}
