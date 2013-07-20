package com.biermacht.brews.frontend.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.*;

public class SettingsItemArrayAdapter extends ArrayAdapter<SettingsItem> {

	// Fields
	private Context context;
	private List<SettingsItem> list;

	// Constructor
	public SettingsItemArrayAdapter(Context context, List<SettingsItem> list)
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
			row = inflater.inflate(R.layout.settings_row_layout, parent, false);
		}

		TextView titleView = (TextView) row.findViewById(R.id.title);
		TextView textView = (TextView) row.findViewById(R.id.text);

		titleView.setText(list.get(position).getTitle());
		textView.setText(list.get(position).getText());

		return row;
	}
}
