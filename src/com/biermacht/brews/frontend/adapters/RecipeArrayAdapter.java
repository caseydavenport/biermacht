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
import com.biermacht.brews.frontend.fragments.RecipesFragment;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.ColorHandler;

import java.util.HashMap;
import java.util.List;

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {

  // Fields
  private List<Recipe> list;
  private RecipesFragment frag;
  private ViewStorage vs;
  private String color;
  private LayoutInflater inflater;
  private View row;
  private HashMap<Integer, Boolean> checked;

  // Constructor
  public RecipeArrayAdapter(Context context, List<Recipe> list, RecipesFragment fr) {
    super(context, android.R.layout.simple_list_item_1, list);
    this.list = list;
    this.frag = fr;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.checked = new HashMap<Integer, Boolean>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    row = convertView;

    if (row == null) {
      // If the row does not yet exist, inflate a new row_layout_recipe.
      row = inflater.inflate(R.layout.row_layout_recipe, parent, false);

      // Create storage for the component views.
      vs = new ViewStorage();
      vs.textView = (TextView) row.findViewById(R.id.label);
      vs.styleView = (TextView) row.findViewById(R.id.beer_style);
      vs.volView = (TextView) row.findViewById(R.id.batch_volume);
      vs.imageView = (ImageView) row.findViewById(R.id.row_icon);
      vs.unitsView = (TextView) row.findViewById(R.id.unit_text);
      row.setTag(vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) row.getTag();

    // Set the correct values for these views.
    vs.textView.setText(list.get(position).getRecipeName());
    vs.styleView.setText((list.get(position).getStyle().getName()));
    vs.volView.setText(String.format("%2.2f", list.get(position).getDisplayBatchSize()));
    vs.unitsView.setText(list.get(position).getVolumeUnits());

    if (this.checked.get(position) != null && this.checked.get(position)) {
      // This item is checked - make the background selected.
      row.setBackgroundResource(R.color.theme_grey);
    }
    else {
      // Otherwise, set the background to be transparent.
      row.setBackgroundResource(R.color.transparent);
    }

    // If we're running as a tablet, we should do some extra stuff here.
    if (frag.isTablet) {
      // If currently selected, set the background to indicate it.
      // Otherwise, set the background to transparent.
      if (position == frag.currentSelectedIndex) {
        row.setBackgroundResource(R.drawable.selector_tablet);
      }
      else {
        // Set background color transparent.
        row.setBackgroundResource(R.color.transparent);
      }
    }

    // Set beer color
    color = ColorHandler.getSrmColor(list.get(position).getColor());
    vs.imageView.setColorFilter(Color.parseColor(color));

    return row;
  }

  /**
   * Marks the given position in the list as checked.  This will cause the list item to be
   * highlighted.
   *
   * @param pos
   *         Position to mark
   * @param checked
   *         Whether to mark as checked or un-checked.
   */
  public void setChecked(int pos, boolean checked) {
    this.checked.put(pos, checked);
  }

  /**
   * Un-checks all items in the list.
   */
  public void clearChecks() {
    this.checked = new HashMap<Integer, Boolean>();
  }

  // A private class for storing views for a given row.
  private class ViewStorage {
    public TextView textView;
    public ImageView imageView;
    public TextView styleView;
    public TextView volView;
    public TextView unitsView;
  }
}
