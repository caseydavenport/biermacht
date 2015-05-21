package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Instruction;

import java.util.List;

public class InstructionArrayAdapter extends ArrayAdapter<Instruction> {

  private Context context;
  private List<Instruction> list;

  public InstructionArrayAdapter(Context c, List<Instruction> list) {
    super(c, android.R.layout.simple_list_item_1, list);
    this.context = c;
    this.list = list;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // View to return
    View row = convertView;

    if (row == null) {
      // Get inflater
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.row_layout_instruction, parent, false);
    }
    TextView labelView = (TextView) row.findViewById(R.id.label);
    TextView tagView = (TextView) row.findViewById(R.id.tag);
    TextView durationView = (TextView) row.findViewById(R.id.duration_view);

    // Set what type of instruction it is
    tagView.setText(list.get(position).getInstructionType());

    // Set duration
    if (list.get(position).showTimer()) {
      durationView.setText(list.get(position).getDuration() + " " + list.get(position).getDurationUnits());
    }
    else {
      durationView.setText("--");
    }

    // Set instruction text
    labelView.setText(list.get(position).getInstructionText());

    return row;
  }
}
