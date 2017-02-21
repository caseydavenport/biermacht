package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Fermentable;

import java.util.List;

public class FermentableArrayAdapter extends ArrayAdapter<Fermentable> {

  private Context context;
  private List<Fermentable> list;
  private ViewStorage vs;

  // Variables for temporary storage.  These are declared at the class-level so that
  // they do not need to be allocated / cleaned up whenever getView is called, but rather
  // when the adapter is first created.
  private View row;
  private LayoutInflater inflater;
  private String s1;
  private String s2;
  private String ingType;
  private String detailText;
  private Fermentable f;

  public FermentableArrayAdapter(Context c, List<Fermentable> list) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.context = c;
    this.list = list;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    row = convertView;

    if (row == null) {
      // Get inflater
      row = inflater.inflate(R.layout.row_layout_ingredient, parent, false);

      // Store component views
      vs = new ViewStorage();
      vs.labelView = (TextView) row.findViewById(R.id.label);
      vs.amountView = (TextView) row.findViewById(R.id.amount);
      vs.imageView = (ImageView) row.findViewById(R.id.row_icon);
      vs.unitView = (TextView) row.findViewById(R.id.unit_text);
      vs.detailView = (TextView) row.findViewById(R.id.ing_detail_text);
      row.setTag(vs);
    }

    // Get component views from row
    vs = (ViewStorage) row.getTag();

    // Set label, amount, and units.
    vs.labelView.setText(list.get(position).getName());
    vs.amountView.setText(String.format("%2.2f", list.get(position).getDisplayAmount()));
    vs.unitView.setText(list.get(position).getDisplayUnits());

    // Set imageView based on ingredient type
    ingType = list.get(position).getType();
    detailText = String.format("%2.2f gravity points", list.get(position).gravityPoints());
    vs.detailView.setText(detailText);

    f = list.get(position);
    if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
      vs.imageView.setImageResource(R.drawable.icon_wheat);
    }
    else {
      vs.imageView.setImageResource(R.drawable.icon_extract);
    }

    return row;
  }

  private class ViewStorage {
    public TextView labelView;
    public TextView amountView;
    public ImageView imageView;
    public TextView unitView;
    public TextView detailView;
  }
}
