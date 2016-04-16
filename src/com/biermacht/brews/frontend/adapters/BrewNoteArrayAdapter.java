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
import com.biermacht.brews.recipe.BrewNote;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.ColorHandler;
import com.biermacht.brews.utils.Units;

import java.util.HashMap;
import java.util.List;

public class BrewNoteArrayAdapter extends ArrayAdapter<BrewNote> {

  // Fields
  private List<BrewNote> list;
  private ViewStorage vs;
  private LayoutInflater inflater;
  private View row;
  private BrewNote note;

  // Constructor
  public BrewNoteArrayAdapter(Context context, List<BrewNote> list) {
    super(context, android.R.layout.simple_list_item_1, list);
    this.list = list;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    row = convertView;

    if (row == null) {
      // If the row does not yet exist, inflate a new row_layout_note.
      row = inflater.inflate(R.layout.row_layout_note, parent, false);

      // Get the note.
      note = list.get(position);

      // Create storage for the component views.
      vs = new ViewStorage();
      vs.dateView = (TextView) row.findViewById(R.id.date);
      vs.imageView = (ImageView) row.findViewById(R.id.row_icon);
      vs.gravityView = (TextView) row.findViewById(R.id.gravity);
      vs.temperatureView = (TextView) row.findViewById(R.id.temperature);
      row.setTag(vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) row.getTag();

    // Set the correct values for these views.
    vs.dateView.setText(note.getDate());
    vs.gravityView.setText(String.format("%2.2f", note.getGravity()));
    vs.temperatureView.setText(String.format("%2.2f %s", note.getTemperature(),
            Units.getTemperatureUnits()));

    return row;
  }

  // A private class for storing views for a given row.
  private class ViewStorage {
    public TextView dateView;
    public ImageView imageView;
    public TextView gravityView;
    public TextView temperatureView;
  }
}
