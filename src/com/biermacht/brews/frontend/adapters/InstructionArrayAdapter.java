package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Instruction;

import java.util.List;

public class InstructionArrayAdapter extends ArrayAdapter<Instruction> {

  private List<Instruction> list;

  // Variables for use in getView().  These are owned at the class 
  // scope so that they do not get garbage collected as frequently,
  // improving list performance.
  private View row;
  private LayoutInflater inflater;
  private ViewStorage vs;
  private Context c;

  public InstructionArrayAdapter(Context c, List<Instruction> list) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.c = c;
    this.list = list;
    this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to populate.  Assume convertView is a valid view.  If it is 
    // not, then inflate a new view.
    this.row = convertView;

    if (this.row == null) {
      // Inflate a new view, since convertView was null.
      row = inflater.inflate(R.layout.row_layout_instruction, parent, false);

      // Store component views.
      vs = new ViewStorage();
      vs.labelView = (TextView) this.row.findViewById(R.id.label);
      vs.subtitleView = (TextView) (this.row.findViewById(R.id.subtitle));
      vs.tagView = (TextView) this.row.findViewById(R.id.tag);
      vs.durationView = (TextView) this.row.findViewById(R.id.duration_view);
      row.setTag(vs);
    }

    // Get the component views for this row.
    vs = (ViewStorage) row.getTag();

    // Set what type of instruction it is
    vs.tagView.setText(list.get(position).getInstructionType());

    // Set duration view if the instruction is timed.
    if (list.get(position).showTimer()) {
      // The instruction is timed - set the duration to the instructions
      // stored duration.
      vs.durationView.setText(list.get(position).getDuration() + " " + list.get(position).getDurationUnits());
    }
    else {
      // The instruction is not timed - set the duration view to indicate this.
      vs.durationView.setText("--");
    }

    // Set the instruction text view.
    vs.labelView.setText(list.get(position).getInstructionText());

    if (! list.get(position).getSubtitle().isEmpty()) {
      // If there is a subtitle, display it and make sure formatting is correct.
      vs.subtitleView.setText(list.get(position).getSubtitle());

      // Ensure correct formatting.
      vs.subtitleView.setVisibility(View.VISIBLE);
      vs.labelView.setGravity(Gravity.BOTTOM);

      // Set padding - we need to scale values into dp.
      float scale = c.getResources().getDisplayMetrics().density;
      int eight = (int) (8 * scale + 0.5f);
      int sixteen = (int) (16 * scale + 0.5f);
      vs.labelView.setPadding(eight, eight, sixteen, 0);
    }
    else {
      // Don't display subtitle and ensure formatting is correct.
      vs.subtitleView.setVisibility(View.GONE);
      vs.labelView.setGravity(Gravity.CENTER_VERTICAL);

      // Set padding - we need to scale values into dp.
      float scale = c.getResources().getDisplayMetrics().density;
      int eight = (int) (8 * scale + 0.5f);
      int sixteen = (int) (16 * scale + 0.5f);
      vs.labelView.setPadding(eight, eight, sixteen, eight);
    }

    return row;
  }

  private class ViewStorage {
    TextView labelView;
    TextView subtitleView;
    TextView tagView;
    TextView durationView;
  }
}
