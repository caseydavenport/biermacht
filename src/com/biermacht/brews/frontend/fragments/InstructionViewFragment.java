package com.biermacht.brews.frontend.fragments;

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
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;

public class InstructionViewFragment extends Fragment {

  private int resource = R.layout.fragment_instruction_view;;
  private Recipe r;
  private ListView instructionListView;
  private ArrayList<Instruction> instructionList;
  private View pageView;
  private InstructionArrayAdapter instructionArrayAdapter;
  

  /**
  * Public constructor.  All fragments must have an empty public constructor.
  * Arguments are passed via the setArguments method.  Use instance() to 
  * create new InstructionViewFragments rather than this constructor.
  */
  public InstructionViewFragment() {
    // Don't show options menu.
    setHasOptionsMenu(false);
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
    
    // Inflate the UI resource for this fragment..
    this.pageView = inflater.inflate(this.resource, container, false);
    this.instructionListView = (ListView) this.pageView.findViewById(R.id.instruction_list);
    
    // Initialize a new empty list to hold instructions.
    this.instructionList = new ArrayList<Instruction>();

    // Only show instructions tagged to be shown in the instruction list.
    // Instructions can be shown in the instruction list, brew timer, or both.
    for (Instruction i : this.r.getInstructionList()) {
      if (i.showInInstructionList()) {
        this.instructionList.add(i);
      }
    }

    if (this.instructionList.size() > 0) {
      // There are instructions to show.  Configure the list adapter
      // and set the correct visibility on the listView.
      this.instructionArrayAdapter = new InstructionArrayAdapter(getActivity(), instructionList);
      this.instructionListView = (ListView) this.pageView.findViewById(R.id.instruction_list);
      this.instructionListView.setAdapter(this.instructionArrayAdapter);
      this.instructionListView.setVisibility(View.VISIBLE);
    }
    else {
      // No instructions to show.  Set to the view which 
      // indicates this to the user.
      TextView noInstructionsView = (TextView) this.pageView.findViewById(R.id.no_instructions_view);
      noInstructionsView.setVisibility(View.VISIBLE);
    }

    return this.pageView;
  }
}
