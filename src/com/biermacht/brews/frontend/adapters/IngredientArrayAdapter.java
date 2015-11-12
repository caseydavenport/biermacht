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

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {

  private Context context;
  private List<Ingredient> list;
  private Recipe r;
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
  private Hop h;

  public IngredientArrayAdapter(Context c, List<Ingredient> list, Recipe r) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.context = c;
    this.list = list;
    this.r = r;
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
    detailText = "";

    if (ingType == Ingredient.HOP) {
      h = (Hop) list.get(position);
      vs.imageView.setImageResource(R.drawable.icon_hops);
      vs.labelView.setText(h.getName() + ", " + String.format("%1.1f", h.getAlphaAcidContent()) + "%");

      if (h.getUse().equals(Hop.USE_BOIL) || h.getUse().equals(Hop.USE_AROMA)) {
        detailText += String.format("%d", h.getDisplayTime()) + " mins, ";
        detailText += String.format("%2.2f", BrewCalculator.Bitterness(r, h));
        detailText += " IBU";
      }
      else if (h.getUse().equals(Hop.USE_FIRST_WORT)) {
        detailText += "First Wort, ";
        detailText += String.format("%2.2f", BrewCalculator.Bitterness(r, h));
        detailText += " IBU";
      }
      else if (h.getUse().equals(Hop.USE_DRY_HOP)) {
        detailText = "Dry Hop, " + String.format("%d", h.getDisplayTime()) + " days";
      }
    }
    else if (ingType == Ingredient.FERMENTABLE) {
      f = (Fermentable) list.get(position);
      if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
        vs.imageView.setImageResource(R.drawable.icon_wheat);
      }
      else {
        vs.imageView.setImageResource(R.drawable.icon_extract);
      }

      s1 = String.format("%2.2f", BrewCalculator.GrainPercent(r, list.get(position)));
      s2 = String.format("%2.2f", BrewCalculator.FermentableGravityPoints(r, list.get(position)));
      detailText += s1;
      detailText += "%, ";
      detailText += s2 + " GPts.";
    }
    else if (ingType == Ingredient.YEAST) {
      // TODO: Display type is always a packet.
      vs.amountView.setText("1.00");
      vs.unitView.setText("pkg");
      vs.imageView.setImageResource(R.drawable.icon_yeast);
      detailText = ((Yeast) list.get(position)).getArrayAdapterDescription();
    }
    else if (ingType == Ingredient.MISC) {
      vs.imageView.setImageResource(R.drawable.icon_idk);
      detailText = ((Misc) list.get(position)).getArrayAdapterDescription();
    }
    else {
      // We don't handle these!
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
