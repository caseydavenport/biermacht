package com.biermacht.brews.frontend.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.DisplaySnapshotActivity;
import com.biermacht.brews.frontend.adapters.SnapshotArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.comparators.RecipeSnapshotComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;
import java.util.Collections;

public class SnapshotsViewFragment extends Fragment implements BiermachtFragment {

  private int resource = R.layout.fragment_snapshots_view;
  private Recipe r;
  private OnItemClickListener mClickListener;
  private ListView listView;
  private TextView noSnapshotsView;
  private ArrayList<RecipeSnapshot> snapshotList;
  View pageView;

  // Variables needed for onCreateView().  Declared here to improve
  // performance by reducing need for garbage collection.
  RecipeSnapshot snapshot;
  Intent intent;
  SnapshotArrayAdapter snapshotArrayAdapter;

  /**
   * Public constructor.  All fragments must have an empty public constructor. Arguments are passed
   * via the setArguments method.  Use instance() to create new fragments rather than
   * this constructor.
   */
  public SnapshotsViewFragment() {
    // This fragment has no options menu.
    setHasOptionsMenu(false);
  }

  public static SnapshotsViewFragment instance(Recipe r) {
    // Create the fragment.
    SnapshotsViewFragment f = new SnapshotsViewFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_RECIPE, r);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Get arguments and store them in variables.
    r = getArguments().getParcelable(Constants.KEY_RECIPE);

    // Inflate the resource for this fragment, and find any
    // important component views.
    pageView = inflater.inflate(resource, container, false);
    listView = (ListView) pageView.findViewById(R.id.list);
    noSnapshotsView = (TextView) pageView.findViewById(R.id.empty_view);

    // Get the snapshot list from the database.
    // TODO: Refactor for efficiency - don't want to do database lookups in onCreateView!
    snapshotList = DatabaseAPI.getSnapshots(r);
    Collections.sort(snapshotList, new RecipeSnapshotComparator<RecipeSnapshot>());
    snapshotArrayAdapter = new SnapshotArrayAdapter(getActivity(), snapshotList);
    listView.setAdapter(snapshotArrayAdapter);

    // Set up the onClickListener. This handles click events when the 
    // user clicks on snapshot in the list.
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        // Start the "display" activity for snapshots.
        Intent i = new Intent(getActivity(), DisplaySnapshotActivity.class);
        i.putExtra(Constants.KEY_SNAPSHOT, snapshotList.get(pos));
        startActivity(i);
      }
    };
    listView.setOnItemClickListener(mClickListener);

    // Set whether or not we show the list view
    setCorrectView();

    return pageView;
  }

  /**
   * Sets the correct view to display - either a list of snapshots, or a view which indicates
   * that there are no snapshots to display.
   */
  public void setCorrectView() {
    if (snapshotList.size() > 0) {
      // There are snapshots.  Set up the ArrayAdapter
      // and set the list view to be visible.
      listView.setVisibility(View.VISIBLE);
      noSnapshotsView.setVisibility(View.GONE);
    }
    else {
      // There are no snapshots for this recipe.  Display a message indicating that
      // no snapshots could be found.
      noSnapshotsView.setVisibility(View.VISIBLE);
      listView.setVisibility(View.GONE);
    }
  }

  @Override
  public void handleClick(View v) {
    // Not used.
  }

  @Override
  public void update() {
    // Not used.
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_new_snapshot:
        newSnapshotAlert(this.r).show();
        return true;
    }

    return false;
  }

  @Override
  public String name() {
    return "Snapshots";
  }

  /**
   * Returns a builder for an alert which prompts the user for information used to create a new
   * snapshot for the current Recipe.
   */
  private AlertDialog.Builder newSnapshotAlert(final Recipe r) {
    return new AlertDialog.Builder(getActivity())
            .setTitle("New Snapshot")
            .setMessage("Create a new Snapshot of this recipe in its current state?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int which) {
                Log.d("DisplayRecipeActivity", "Creating new snapshot for Recipe " + r);
                RecipeSnapshot snap = r.createSnapshot();
                Snackbar.make(pageView, R.string.snapshot_created, Snackbar.LENGTH_LONG).show();

                // Update the displayed list.
                snapshotList.add(0, snap);
                snapshotArrayAdapter.notifyDataSetChanged();
                setCorrectView();
              }
            })
            .setNegativeButton(android.R.string.no, null);
  }
}
