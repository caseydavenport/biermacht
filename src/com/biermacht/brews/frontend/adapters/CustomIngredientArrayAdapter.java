package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.BrewCalculator;

import java.util.List;

public class CustomIngredientArrayAdapter extends ArrayAdapter<Ingredient> {

	private Context context;
	private List<Ingredient> list;

	public CustomIngredientArrayAdapter(Context c, List<Ingredient> list)
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

        // Hide amouunt and unit views
        amountView.setVisibility(View.GONE);
        unitView.setVisibility(View.GONE);
		
		// Set imageView based on ingredient type
		String ingType = list.get(position).getType();
		String detailText = "";
		
		if(ingType == Ingredient.HOP)
		{
			Hop h = (Hop) list.get(position);
			imageView.setImageResource(R.drawable.icon_hops);
			labelView.setText(h.getName() + ", " + String.format("%1.1f", h.getAlphaAcidContent()) + "%");

            detailText = h.getHopType();
		}
		else if(ingType == Ingredient.FERMENTABLE) 
		{
			Fermentable f = (Fermentable) list.get(position);
			if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
				imageView.setImageResource(R.drawable.icon_wheat);
			else
				imageView.setImageResource(R.drawable.icon_extract);
			
            detailText = f.getFermentableType();
		}
		else if (ingType == Ingredient.YEAST)
		{
            Yeast y = (Yeast) list.get(position);
			// Display type is always a packet really...
			amountView.setText("1.00");
			unitView.setText("pkg");
			imageView.setImageResource(R.drawable.icon_yeast);
			detailText = y.getArrayAdapterDescription();
		}
		else if(ingType == Ingredient.MISC)
		{
			imageView.setImageResource(R.drawable.icon_idk);
			detailText = ((Misc)list.get(position)).getUseFor();
		}
		else
		{
            // We don't handle these ingredients!
            Log.d("CustomIngredientArrayAdapter", "Unknown ingredient type received");
		}

		detailView.setText(detailText);

		return row;
	}
}
