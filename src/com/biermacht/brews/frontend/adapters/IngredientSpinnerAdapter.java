package com.biermacht.brews.frontend.adapters;

import java.util.List;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngredientSpinnerAdapter extends ArrayAdapter<Ingredient> {
	
	// Fields
	private Context context;
	private List<Ingredient> list;
    private String title;

	// Constructor
	public IngredientSpinnerAdapter(Context context, List<Ingredient> list, String title)
	{
		super(context, android.R.layout.simple_spinner_item, list);
        Log.d("IngredientSpinnerAdapter", "Creating spinner adapter with length: " + list.size());
		this.context = context;
		this.list = list;
        this.title = title;
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
            row = inflater.inflate(R.layout.row_layout_edit_text, parent, false);
        }

        TextView titleView = (TextView) row.findViewById(R.id.title);
        TextView textView = (TextView) row.findViewById(R.id.text);

        textView.setText((CharSequence) list.get(position).getName());
        titleView.setText(title);

        return row;
    }
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		// View to return
		View row = convertView;
				
		if (row == null)
		{
			// Get inflater
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.spinner_row_dropdown_layout, parent, false);
		}
		
		TextView textView = (TextView) row.findViewById(R.id.selected_view);
		TextView descView = (TextView) row.findViewById(R.id.description_view);
		
		Ingredient ing = list.get(position);
		
		textView.setText(ing.toString());
		descView.setText(ing.getShortDescription());
		
		descView.setVisibility(View.VISIBLE);
		
		return row;
	}
}
