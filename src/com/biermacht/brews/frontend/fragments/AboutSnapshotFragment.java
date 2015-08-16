package com.biermacht.brews.frontend.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.DisplaySnapshotActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditHopActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditYeastActivity;
import com.biermacht.brews.frontend.adapters.IngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class AboutSnapshotFragment extends Fragment {

  private int resource = R.layout.fragment_about_snapshot;
  private RecipeSnapshot snapshot;
  private OnItemClickListener mClickListener;
  View pageView;

  /**
   * Public constructor.  All fragments must have an empty public constructor. Arguments are passed
   * via the setArguments method.  Use instance() to create new IngredientViewFragments rather than
   * this constructor.
   */
  public AboutSnapshotFragment() {
    // This fragment has no options menu.
    setHasOptionsMenu(false);
  }

  public static AboutSnapshotFragment instance(RecipeSnapshot r) {
    // Create the fragment.
    AboutSnapshotFragment f = new AboutSnapshotFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_SNAPSHOT, r);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Get arguments and store them in variables.
    snapshot = getArguments().getParcelable(Constants.KEY_SNAPSHOT);

    // Inflate the resource for this fragment, and find any
    // important component views.
    pageView = inflater.inflate(resource, container, false);
    Button b = (Button) pageView.findViewById(R.id.date_picker);

    // Set values
    b.setText("SomeText");
    Log.d("Snapshot", "HELP: " + snapshot.getBrewDate());
    b.setText(snapshot.getBrewDate());

    // Set up the onClickListener. This handles click events when the
    // user clicks on an ingredient in the list.
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        // Get the ingredient for this spot in the list.  We will then 
        // check what the ingredient type is, and cast it as appropriate.
      }
    };

    return pageView;
  }

  /**
   * Function to call onClick for the date picker button.
   */
  public void showDatePicker(View dateButton) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
  }

  /**
   * DatePicker dialog for when the user decides to modify the brew date.
   */
  public static class DatePickerFragment extends DialogFragment
          implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      // Do something with the date chosen by the user
    }
  }
}
