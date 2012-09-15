package com.biermacht.brews.frontend;

import java.util.List;

import com.biermacht.brews.R;
import com.biermacht.brews.R.id;
import com.biermacht.brews.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter<String> extends ArrayAdapter<String> {
	
	// Fields
	private Context context;
	private List<String> list;

	// Constructor
	public SpinnerAdapter(Context context, List<String> list)
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
		
		textView.setText((CharSequence) list.get(position));
		
		return row;
	}
}
