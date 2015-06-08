package com.biermacht.brews.frontend.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.InstructionArrayAdapter;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;

import java.util.ArrayList;
import com.biermacht.brews.utils.*;

public class InstructionViewFragment extends Fragment {

  private int resource = R.layout.fragment_instruction_view;;
  private Recipe r;
  private OnItemClickListener mClickListener;
  private ListView instructionListView;
  private ArrayList<Instruction> instructionList;
  View pageView;

  public InstructionViewFragment() {
  }
  
  public static InstructionViewFragment instance(Recipe r) {
    // Create the fragment.
    InstructionViewFragment f = new InstructionViewFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_RECIPE, r);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Get arguments from the stored bundle.
    this.r = getArguments().getParcelable(Constants.KEY_RECIPE);
    
    // Inflate the resource.
    pageView = inflater.inflate(resource, container, false);

    // Don't show options menu.
    setHasOptionsMenu(false);

    // Initialize important junk
    instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);
    instructionList = new ArrayList<Instruction>();

    // Only show instructions tagged for instruction list
    for (Instruction i : r.getInstructionList()) {
      if (i.showInInstructionList()) {
        instructionList.add(i);
      }
    }

    if (instructionList.size() > 0) {
      InstructionArrayAdapter instructionArrayAdapter = new InstructionArrayAdapter(getActivity(), instructionList);
      instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);
      instructionListView.setAdapter(instructionArrayAdapter);
      instructionListView.setVisibility(View.VISIBLE);
    }
    else {
      TextView noInstructionsView = (TextView) pageView.findViewById(R.id.no_instructions_view);
      noInstructionsView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }
}
