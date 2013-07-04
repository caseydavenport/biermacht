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
import com.biermacht.brews.utils.ColorHandler;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.utils.*;
import android.opengl.*;

public class DetailArrayAdapter extends ArrayAdapter<Detail> {

	// Fields
	private Context context;
	private List<Detail> list;

	// Constructor
	public DetailArrayAdapter(Context context, List<Detail> list)
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
		
		// Detail we're looking at
		Detail detail = list.get(position);
		
		if (row == null)
		{
			// Get inflater
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.detail_row_layout, parent, false);
		}

		TextView titleView = (TextView) row.findViewById(R.id.tag);
		TextView rangeView = (TextView) row.findViewById(R.id.range);
		TextView valueView = (TextView) row.findViewById(R.id.value);
		
		if (detail.getType().equals(Detail.TYPE_TEXT))
		{
			titleView.setText(detail.getTitle());
			valueView.setText(detail.getContent());
			rangeView.setVisibility(View.GONE);
		}
		
		else if (detail.getType().equals(Detail.TYPE_RANGE))
		{
			
			// get the range
			String range, value;
			range = String.format(detail.getFormat(), detail.getMin());
			range += " - "; 
			range += String.format(detail.getFormat(), detail.getMax());
			value = String.format(detail.getFormat(), detail.getValue());
			
			// Set values
			titleView.setText(detail.getTitle());
			rangeView.setText(range);
			valueView.setText(value);
		
			// If its in this range, we're all good - make it green.
			Boolean isGood = Utils.isWithinRange(detail.getValue(), detail.getMin(), detail.getMax());

			// If it is in this range but not the GOOD ranges above, color it yellow
			Boolean isOk = Utils.isWithinRange(detail.getValue(), detail.getMin(), detail.getMax());
		
			// Set all the colors appropriately
			if (isGood)
				valueView.setTextColor(Color.parseColor(ColorHandler.GREEN));
			else if (isOk)
				valueView.setTextColor(Color.parseColor(ColorHandler.YELLOW));
			else
				valueView.setTextColor(Color.parseColor(ColorHandler.RED));
		}
		
		return row;
	}
}
