package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.ColorHandler;

import java.util.List;
import com.biermacht.brews.frontend.fragments.*;

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {

  // Fields
  private Context context;
  private List<Recipe> list;
  private RecipesFragment frag;

  // Constructor
  public RecipeArrayAdapter(Context context, List<Recipe> list, RecipesFragment fr) {
    super(context, android.R.layout.simple_list_item_1, list);
    this.context = context;
    this.list = list;
    this.frag = fr;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    View row = convertView;

    if (row == null) {
      // Get inflater
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.row_layout_recipe, parent, false);
    }

    TextView textView = (TextView) row.findViewById(R.id.label);
    TextView beerStyleView = (TextView) row.findViewById(R.id.beer_style);
    TextView batchVolumeView = (TextView) row.findViewById(R.id.batch_volume);
    ImageView imageView = (ImageView) row.findViewById(R.id.row_icon);
    TextView unitsView = (TextView) row.findViewById(R.id.unit_text);

    textView.setText(list.get(position).getRecipeName());
    beerStyleView.setText((list.get(position).getStyle().getName()));
    batchVolumeView.setText(String.format("%2.2f", list.get(position).getDisplayBatchSize()));
    unitsView.setText(list.get(position).getVolumeUnits());

    // Set beer color here
    String color = ColorHandler.getSrmColor(list.get(position).getColor());
    imageView.setBackgroundColor(Color.parseColor(color));
    
    // If we're running as a tablet, we should do some extra stuff here.
    if (frag.isTablet) {
      // If currently selected, set the background to indicate it.
      // Otherwise, set the background to transparent.
      if (position == frag.currentSelectedIndex)
      {
        row.setBackgroundResource(R.drawable.selector_tablet);
      }
      else {
        row.setBackgroundColor(Color.parseColor("#00FFFFFF"));
      }
    }
    return row;
  }
}
