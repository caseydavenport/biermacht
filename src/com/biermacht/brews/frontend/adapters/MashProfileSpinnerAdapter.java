package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.MashProfile;

import java.util.List;

public class MashProfileSpinnerAdapter extends ArrayAdapter<MashProfile> {

  // Fields
  private Context context;
  private List<MashProfile> list;

  // Constructor
  public MashProfileSpinnerAdapter(Context context, List<MashProfile> list) {
    super(context, android.R.layout.simple_spinner_item, list);
    this.context = context;
    this.list = list;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    View row = convertView;

    if (row == null) {
      // Get inflater
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.row_layout_edit_text, parent, false);
    }

    TextView titleView = (TextView) row.findViewById(R.id.title);
    TextView textView = (TextView) row.findViewById(R.id.text);

    textView.setText((CharSequence) list.get(position).getName());
    titleView.setText("Mash Profile");

    return row;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    // View to return
    View row = convertView;

    if (row == null) {
      // Get inflater
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.spinner_row_layout, parent, false);
    }

    TextView textView = (TextView) row.findViewById(R.id.selected_view);
    TextView descView = (TextView) row.findViewById(R.id.description_view);

    textView.setText((CharSequence) list.get(position).toString());
    descView.setText("");

    return row;
  }
}
