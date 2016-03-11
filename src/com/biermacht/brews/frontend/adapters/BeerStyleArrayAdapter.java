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
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.ColorHandler;

import java.util.List;

public class BeerStyleArrayAdapter extends ArrayAdapter<BeerStyle> {

  private Context context;
  private List<BeerStyle> list;
  private Recipe r;
  private ViewStorage vs;

  // Variables for temporary storage.  These are declared at the class-level so that
  // they do not need to be allocated / cleaned up whenever getView is called, but rather
  // when the adapter is first created.
  private View row;
  private LayoutInflater inflater;
  private String detailText;
  private BeerStyle style;
  private String color;

  public BeerStyleArrayAdapter(Context c, List<BeerStyle> list) {
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
      row = inflater.inflate(R.layout.row_layout_beer_style, parent, false);

      // Store component views
      vs = new ViewStorage();
      vs.titleView = (TextView) row.findViewById(R.id.title);
      vs.amountView = (TextView) row.findViewById(R.id.amount);
      vs.imageView = (ImageView) row.findViewById(R.id.row_icon);
      vs.unitView = (TextView) row.findViewById(R.id.unit_text);
      vs.detailView = (TextView) row.findViewById(R.id.style_details_text);
      row.setTag(vs);
    }

    // Get component views from row
    vs = (ViewStorage) row.getTag();

    style = list.get(position);

    // Set style name.
    vs.titleView.setText(style.getName());
    vs.detailView.setText(style.getCategory());
    vs.amountView.setText(style.getCatNum() + style.getStyleLetter());
    vs.unitView.setText(style.getStyleGuide());

    // Set color
    color = ColorHandler.getSrmColor(style.getAverageColor());
    vs.imageView.setColorFilter(Color.parseColor(color));

    return row;
  }

  private class ViewStorage {
    public TextView titleView;
    public TextView amountView;
    public ImageView imageView;
    public TextView unitView;
    public TextView detailView;
  }
}
