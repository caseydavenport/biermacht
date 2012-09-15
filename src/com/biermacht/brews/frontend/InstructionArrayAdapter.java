package com.biermacht.brews.frontend;

import java.util.List;

import com.biermacht.brews.R;
import com.biermacht.brews.R.id;
import com.biermacht.brews.R.layout;
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
		// Get inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// View to return
		View row = inflater.inflate(R.layout.instruction_row_layout, parent, false);
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView numberView = (TextView) row.findViewById(R.id.numbering);
		TextView timeView = (TextView) row.findViewById(R.id.time_view);
		timeView.setText(list.get(position).getDuration() + " " + list.get(position).getDuration_units());
		numberView.setText(position+1 + ".");
		labelView.setText(list.get(position).getInstructionText());
		
		return row;
	}
}
