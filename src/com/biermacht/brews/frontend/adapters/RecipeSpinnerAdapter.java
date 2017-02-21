package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;

import java.util.List;

public class RecipeSpinnerAdapter extends ArrayAdapter<Recipe> {

  // Fields
  private Context context;
  private List<Recipe> list;
  private String title;
  private boolean isListAdapter;

  // Constructor
  public RecipeSpinnerAdapter(Context context, List<Recipe> list, String title) {
    super(context, android.R.layout.simple_spinner_item, list);
    Log.d("RecSpinnerAdapter", "Creating recipe spinner adapter with length: " + list.size());
    this.context = context;
    this.list = list;
    this.title = title;
    this.isListAdapter = false;
  }

  // Constructor
  public RecipeSpinnerAdapter(Context context, List<Recipe> list, String title, boolean listAdapter) {
    super(context, android.R.layout.simple_spinner_item, list);
    Log.d("RecSpinnerAdapter", "Creating recipe spinner adapter with length: " + list.size());
    this.context = context;
    this.list = list;
    this.title = title;
    this.isListAdapter = true;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (! isListAdapter) {
      // View to return
      View row = convertView;

      if (row == null) {
        // Get inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_layout_edit_text, parent, false);
      }

      // Configure the view based on the recipe.
      TextView titleView = (TextView) row.findViewById(R.id.title);
      TextView textView = (TextView) row.findViewById(R.id.text);

      Recipe r = list.get(position);

      titleView.setText("Recipe");
      textView.setText(r.toString());

      return row;
    }
    else {
      return getDropDownView(position, convertView, parent);
    }
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    // View to return
    View row = convertView;

    if (row == null) {
      // Get inflater
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.row_layout_edit_text, parent, false);
    }

    TextView titleView = (TextView) row.findViewById(R.id.title);
    TextView textView = (TextView) row.findViewById(R.id.text);

    Recipe r = list.get(position);

    titleView.setText(r.toString());
    textView.setText(r.getType());
    return row;
  }
}
