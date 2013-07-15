package com.biermacht.brews.frontend.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.utils.*;
import com.biermacht.brews.ingredient.*;

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
			row = inflater.inflate(R.layout.mash_step_row_layout, parent, false);
		}

		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView amountView = (TextView) row.findViewById(R.id.amount);
		ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
		TextView unitView = (TextView) row.findViewById(R.id.unit_text);
		TextView detailView = (TextView) row.findViewById(R.id.ing_detail_text);

		labelView.setText(list.get(position).getName());
		amountView.setText(String.format("%2.2f", list.get(position).getDisplayStepTemp()));
		unitView.setText("deg " + Units.FARENHEIT);

		// Set imageView based on ingredient type
		String ingType = list.get(position).getType();
		String detailText = "Ramp time: " + list.get(position).getRampTime();

		if(ingType == MashStep.DECOCTION)
		{
			imageView.setImageResource(R.drawable.icon_hops);
		}
		else if(ingType == MashStep.INFUSION) 
		{
			imageView.setImageResource(R.drawable.icon_wheat);
		}
		else if (ingType == MashStep.TEMPERATURE)
		{
			imageView.setImageResource(R.drawable.icon_yeast);
		}
		else
		{
			//TODO: Handle this
			//imageView.setImageResource(R.drawable.icon_idk);
		}

		detailView.setText(detailText);

		return row;
	}
}
