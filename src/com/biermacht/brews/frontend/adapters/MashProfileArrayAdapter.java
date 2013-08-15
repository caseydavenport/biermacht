package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.MashProfile;

import java.util.List;

public class MashProfileArrayAdapter extends ArrayAdapter<MashProfile> {

	private Context context;
	private List<MashProfile> list;

	public MashProfileArrayAdapter(Context c, List<MashProfile> list)
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
			row = inflater.inflate(R.layout.row_layout_ingredient, parent, false);
		}
		
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView amountView = (TextView) row.findViewById(R.id.amount);
		ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
		TextView unitView = (TextView) row.findViewById(R.id.unit_text);
		TextView detailView = (TextView) row.findViewById(R.id.ing_detail_text);
		
		labelView.setText(list.get(position).getName());
		amountView.setText("");
		unitView.setText("");
		
		// Set imageView
        imageView.setImageResource(R.drawable.icon_mash_tun);

        String detailText = "Details go here!";
		detailView.setText(detailText);

		return row;
	}
}
