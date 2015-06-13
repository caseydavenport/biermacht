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

import java.util.List;

public class CustomIngredientArrayAdapter extends ArrayAdapter<Ingredient> {

  private Context context;
  private List<Ingredient> list;

  // Storage for variables used in getView().  This is done at the class level so that they
  // do not require garbage collection after each call to getView().
  private ViewStorage vs;
  private LayoutInflater inflater;
  private View row;
  private Fermentable f;
  private Hop h;
  private Yeast y;
  String ingType;
  String detailText;

  public CustomIngredientArrayAdapter(Context c, List<Ingredient> list) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.context = c;
    this.list = list;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return.  By default, try to make this the given existing view.  If the 
    // given view is null, we will inflate a new one.
    row = convertView;

    if (row == null) {
      // Get inflater
      row = inflater.inflate(R.layout.row_layout_ingredient, parent, false);

      // Store component views so we don't have to perform a lookup every time 
      // this method is called.
      vs = new ViewStorage();
      vs.labelView = (TextView) row.findViewById(R.id.label);
      vs.amountView = (TextView) row.findViewById(R.id.amount);
      vs.imageView = (ImageView) row.findViewById(R.id.row_icon);
      vs.unitView = (TextView) row.findViewById(R.id.unit_text);
      vs.detailView = (TextView) row.findViewById(R.id.ing_detail_text);
      row.setTag(vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) row.getTag();

    // Set the label to be the ingredient name.
    vs.labelView.setText(list.get(position).getName());

    // Hide amount and unit views.
    vs.amountView.setVisibility(View.GONE);
    vs.unitView.setVisibility(View.GONE);

    // Set imageView based on ingredient type
    ingType = list.get(position).getType();
    detailText = "";

    if (ingType == Ingredient.HOP) {
      h = (Hop) list.get(position);
      vs.imageView.setImageResource(R.drawable.icon_hops);
      vs.labelView.setText(h.getName() + ", " + String.format("%1.1f", h.getAlphaAcidContent()) + "%");

      detailText = h.getHopType();
    }
    else if (ingType == Ingredient.FERMENTABLE) {
      f = (Fermentable) list.get(position);
      if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
        vs.imageView.setImageResource(R.drawable.icon_wheat);
      }
      else {
        vs.imageView.setImageResource(R.drawable.icon_extract);
      }

      detailText = f.getFermentableType();
    }
    else if (ingType == Ingredient.YEAST) {
      y = (Yeast) list.get(position);
      // TODO: Display type is always a packet.
      vs.amountView.setText("1.00");
      vs.unitView.setText("pkg");
      vs.imageView.setImageResource(R.drawable.icon_yeast);
      detailText = y.getArrayAdapterDescription();
    }
    else if (ingType == Ingredient.MISC) {
      vs.imageView.setImageResource(R.drawable.icon_idk);
      detailText = ((Misc) list.get(position)).getUseFor();
    }
    else {
      // We don't handle these ingredients!
      Log.d("CustomIngredientArrayAdapter", "Unknown ingredient type received");
    }
    vs.detailView.setText(detailText);

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
