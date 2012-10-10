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

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {
	
	private Context context;
	private List<Ingredient> list;
	
	public IngredientArrayAdapter(Context c, List<Ingredient> list)
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
			row = inflater.inflate(R.layout.ingredient_row_layout, parent, false);
		}
		
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView amountView = (TextView) row.findViewById(R.id.amount);
		ImageView imageView = (ImageView) row.findViewById(R.id.ingredient_row_icon);
		TextView unitView = (TextView) row.findViewById(R.id.unit_text);
		
		labelView.setText(list.get(position).getName());
		amountView.setText(list.get(position).getAmount() + "");
		unitView.setText(list.get(position).getUnits());
		
		// Set imageView based on ingredient type
		String ingType = list.get(position).getType();
		
		if(ingType == Ingredient.HOP)
			imageView.setImageResource(R.drawable.icon_hops);
		else if(ingType == Ingredient.FERMENTABLE)
			imageView.setImageResource(R.drawable.icon_wheat);
		else if(ingType == Utils.TYPE_SPICE)
			imageView.setImageResource(R.drawable.icon_idk);
		else if (ingType == Utils.TYPE_YEAST)
			imageView.setImageResource(R.drawable.icon_yeast);
		else
			imageView.setImageResource(R.drawable.icon_idk);
		
		return row;
	}
}
