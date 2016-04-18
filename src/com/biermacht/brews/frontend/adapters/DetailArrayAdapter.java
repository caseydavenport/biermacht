package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.Detail;
import com.biermacht.brews.utils.ColorHandler;
import com.biermacht.brews.utils.Utils;

import java.util.List;

public class DetailArrayAdapter extends ArrayAdapter<Detail> {

  private List<Detail> list;

  // Variables used in getView().  Stored at class-level to 
  // improve performance.
  private View row;
  private LayoutInflater inflater;
  private Detail detail;
  private ViewStorage vs;
  private String range;
  private String value;
  private Boolean isGood;
  private Boolean isOk;

  // Constructor
  public DetailArrayAdapter(Context c, List<Detail> list) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.list = list;
    this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Assume convertView is a valid View.  If it is now, we will inflate
    // a  new View.
    this.row = convertView;

    // Get the detail from the list.
    this.detail = list.get(position);

    if (this.row == null) {
      // Inflate a new row layout.
      this.row = inflater.inflate(R.layout.row_layout_detail, parent, false);

      // Store the component views.
      this.vs = new ViewStorage();
      this.vs.titleView = (TextView) row.findViewById(R.id.tag);
      this.vs.rangeView = (TextView) row.findViewById(R.id.range);
      this.vs.valueView = (TextView) row.findViewById(R.id.value);
      this.vs.progressBar = (ProgressBar) row.findViewById(R.id.progress_bar);
      this.row.setTag(this.vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) this.row.getTag();


    if (detail.getType().equals(Detail.TYPE_TEXT)) {
      // Set visibilities.
      this.vs.rangeView.setVisibility(View.GONE);
      this.vs.titleView.setVisibility(View.VISIBLE);
      this.vs.progressBar.setVisibility(View.GONE);

      // Set values.
      this.vs.titleView.setText(detail.getTitle());
      this.vs.valueView.setText(detail.getContent());

      // Set color.
      this.vs.valueView.setTextColor(Color.DKGRAY);
    }
    else if (detail.getType().equals(Detail.TYPE_RANGE)) {
      // This is a range type detail.  These details contain a title, range, and value.
      // The value will be colored based on whether or not it falls within the range.
      this.vs.rangeView.setVisibility(View.VISIBLE);
      this.vs.titleView.setVisibility(View.VISIBLE);
      this.vs.progressBar.setVisibility(View.VISIBLE);

      this.range = String.format(this.detail.getFormat(), detail.getMin()) +
              " - " +
              String.format(this.detail.getFormat(), detail.getMax());
      this.value = String.format(this.detail.getFormat(), detail.getValue());

      // Set values
      this.vs.titleView.setText(this.detail.getTitle());
      this.vs.rangeView.setText(this.range);
      this.vs.valueView.setText(this.value);

      // Set progress bar progress.
      double progress = (this.detail.getValue() - this.detail.getMin()) / (this.detail.getMax() - this.detail.getMin());
      progress = progress * 40 + 30;
      if (progress > 100) {
        // Can't set progress above 100!
        progress = 100;
      }
      if (progress <= 2) {
        // Always show a little bit of red if it goes below.
        progress = 2;
      }
      this.vs.progressBar.setProgress((int) progress);

      // Determine if the value is squarely within the specified range.
      this.isGood = Utils.isWithinRange(this.detail.getValue(), this.detail.getMin(), this.detail.getMax());

      // Determine if the value is within the range + some tolerance.
      this.isOk = Utils.isWithinRange(detail.getValue(), detail.getMinOk(), detail.getMaxOk());

      // Set all the colors appropriately.
      int color;
      if (this.isGood) {
        // The value is within the specified range.  Color the text green.
        color = Color.parseColor(ColorHandler.GREEN);
      }
      else if (this.isOk) {
        // The value is within the range + tolerance.  Color the text yellow.
        color = Color.parseColor(ColorHandler.YELLOW);
      }
      else {
        // The value is not good enough.  Color the text red.
        color = Color.parseColor(ColorHandler.RED);
      }
      this.vs.valueView.setTextColor(color);
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.vs.progressBar.setProgressTintList(ColorStateList.valueOf(color));
      }
      else {
        this.vs.progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
      }
    }

    // Return the now populated row.
    return this.row;
  }

  private class ViewStorage {
    TextView titleView;
    TextView rangeView;
    TextView valueView;
    ProgressBar progressBar;
  }
}
