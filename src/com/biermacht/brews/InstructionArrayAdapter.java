package com.biermacht.brews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		// View to return
		View row = inflater.inflate(R.layout.instruction_row_layout, parent, false);
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView numberView = (TextView) row.findViewById(R.id.numbering);
		numberView.setText(position+1 + ".");
		labelView.setText(list.get(position).getInstructionText());
		
		return row;
	}
}
