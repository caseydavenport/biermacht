package com.biermacht.brews.frontend.adapters;

import java.util.List;

import com.biermacht.brews.recipe.BeerStyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biermacht.brews.*;

public class BeerStyleSpinnerAdapter<BeerStyle> extends ArrayAdapter<BeerStyle> {
	
	// Fields
	private Context context;
	private List<BeerStyle> list;

	// Constructor
	public BeerStyleSpinnerAdapter(Context context, List<BeerStyle> list)
	{
		super(context, android.R.layout.simple_spinner_item, list);
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
			row = inflater.inflate(R.layout.spinner_row_layout, parent, false);
		}
		
		TextView textView = (TextView) row.findViewById(R.id.selected_view);
		TextView descView = (TextView) row.findViewById(R.id.description_view);
		
		textView.setText((CharSequence) list.get(position).toString());
		descView.setText("");
		
		return row;
	}
}
