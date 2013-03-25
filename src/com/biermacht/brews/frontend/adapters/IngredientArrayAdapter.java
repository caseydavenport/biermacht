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

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {
	
	private Context context;
	private List<Ingredient> list;
	private Recipe r;
	
	public IngredientArrayAdapter(Context c, List<Ingredient> list, Recipe r)
	{
		super(c, android.R.layout.simple_list_item_1, list);
		this.context = c;
		this.list = list;
		this.r = r;
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
		TextView detailView = (TextView) row.findViewById(R.id.ing_detail_text);
		
		labelView.setText(list.get(position).getName());
		amountView.setText(String.format("%2.2f", list.get(position).getAmount()));
		unitView.setText(list.get(position).getUnits());
		
		// Set imageView based on ingredient type
		String ingType = list.get(position).getType();
		String detailText = "";
		
		if(ingType == Ingredient.HOP)
		{
			Hop h = (Hop) list.get(position);
			imageView.setImageResource(R.drawable.icon_hops);
			labelView.setText(h.getName() + ", " + String.format("%1.1f", h.getAlphaAcidContent()) + "%");
			
			if(h.getUse().equals(Hop.USE_BOIL) || h.getUse().equals(Hop.USE_AROMA))
			{
				String s = String.format("%2.2f", BrewCalculator.calculateHopIbu(r, h));
				detailText += s; 
				detailText += " IBU";
			}
			else if (h.getUse().equals(Hop.USE_DRY_HOP))
			{
				detailText = "Dry Hop";
			}
		}
		else if(ingType == Ingredient.FERMENTABLE) 
		{
			imageView.setImageResource(R.drawable.icon_wheat);
			String s = String.format("%2.2f", BrewCalculator.calculateGrainPercent(r, list.get(position)));
		    String t = String.format("%2.2f", BrewCalculator.calculateGravityPoints(r, list.get(position)));
			detailText += s;
			detailText += "%, ";
			detailText += t + " GPts.";
		}
		else if (ingType == Ingredient.YEAST)
		{
			imageView.setImageResource(R.drawable.icon_yeast);
			detailText = ((Yeast) list.get(position)).getAttenuation() + "%";
		}
		else if(ingType == Ingredient.MISC)
		{
			imageView.setImageResource(R.drawable.icon_idk);
			detailText = ((Misc)list.get(position)).getUseFor();
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
