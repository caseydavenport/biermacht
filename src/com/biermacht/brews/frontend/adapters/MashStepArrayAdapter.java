package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.utils.Units;

import java.util.List;

public class MashStepArrayAdapter extends ArrayAdapter<MashStep> {

	private Context context;
	private List<MashStep> list;

	public MashStepArrayAdapter(Context c, List<MashStep> list)
	{
		super(c, android.R.layout.simple_list_item_1, list);
		this.context = c;
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
			row = inflater.inflate(R.layout.row_layout_mash_step, parent, false);
		}
		
		TextView titleView = (TextView) row.findViewById(R.id.title);
		TextView textView = (TextView) row.findViewById(R.id.text);

        String temp = String.format("%2.0f", list.get(position).getDisplayStepTemp());
        String time = String.format("%2.0f", list.get(position).getStepTime());
        titleView.setText(list.get(position).getName());
        textView.setText(time + " mins at " + temp + Units.getTemperatureUnits());

		return row;
	}
}
