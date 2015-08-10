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
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.ColorHandler;

import java.util.HashMap;
import java.util.List;

public class SnapshotArrayAdapter extends ArrayAdapter<RecipeSnapshot> {

  // Fields
  private Context context;
  private List<RecipeSnapshot> list;
  private ViewStorage vs;
  private String color;
  private LayoutInflater inflater;
  private View row;
  private HashMap<Integer, Boolean> checked;

  // Constructor
  public SnapshotArrayAdapter(Context context, List<RecipeSnapshot> list) {
    super(context, android.R.layout.simple_list_item_1, list);
    this.context = context;
    this.list = list;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.checked = new HashMap<Integer, Boolean>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    row = convertView;

    if (row == null) {
      // If the row does not yet exist, inflate a new row_layout_recipe.
      row = inflater.inflate(R.layout.row_layout_snapshot, parent, false);

      // Create storage for the component views.
      vs = new ViewStorage();
      vs.titleView = (TextView) row.findViewById(R.id.title);
      vs.subTitleView = (TextView) row.findViewById(R.id.subtitle);
      vs.volView = (TextView) row.findViewById(R.id.batch_volume);
      vs.unitsView = (TextView) row.findViewById(R.id.unit_text);
      row.setTag(vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) row.getTag();

    // Set the correct values for these views.
    vs.titleView.setText((list.get(position).getBrewDate()));
    vs.subTitleView.setText(list.get(position).getSnapshotTime());
    vs.volView.setText(String.format("%2.2f", list.get(position).getDisplayBatchSize()));
    vs.unitsView.setText(list.get(position).getVolumeUnits());

    // TODO - handle the tablet case.
    if (this.checked.get(position) != null && this.checked.get(position)) {
      // This item is checked - make the background selected.
      row.setBackgroundResource(R.color.theme_grey);
    }
    else {
      // Otherwise, set the background to be transparent.
      row.setBackgroundResource(R.color.transparent);
    }

    return row;
  }

  /**
   * Marks the given position in the list as checked.  This will cause the list item to be
   * highlighted.
   * @param pos Position to mark
   * @param checked Whether to mark as checked or un-checked.
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
    public TextView titleView;
    public TextView subTitleView;
    public TextView volView;
    public TextView unitsView;
  }
}
