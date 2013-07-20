package com.biermacht.brews.frontend.adapters;

import java.util.List;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Instruction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InstructionArrayAdapter extends ArrayAdapter<Instruction> {
	
	private Context context;
	private List<Instruction> list;
	
	public InstructionArrayAdapter(Context c, List<Instruction> list)
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
			row = inflater.inflate(R.layout.instruction_row_layout, parent, false);
		}
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView tagView = (TextView) row.findViewById(R.id.tag);
		TextView durationView = (TextView) row.findViewById(R.id.duration_view);
		TextView startTimeView = (TextView) row.findViewById(R.id.start_time_view);
		TextView endTimeView = (TextView) row.findViewById(R.id.end_time_view);
		
		// Set what type of instruction it is
		tagView.setText(list.get(position).getInstructionType());
		
		// Set start time, end time, and duration
		startTimeView.setText(list.get(position).getStartTime() + " " + list.get(position).getDuration_units());
		endTimeView.setText(list.get(position).getEndTime() + " " + list.get(position).getDuration_units());
		durationView.setText(list.get(position).getDuration() + " " + list.get(position).getDuration_units());
		
		// Set instruction text
		labelView.setText(list.get(position).getInstructionText());
		
		// TODO: Set visibility based on instruction type
		
		return row;
	}
}
