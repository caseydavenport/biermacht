package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.DisplayStyleActivity;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.BeerStyleArrayAdapter;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;
import java.util.Collections;

public class ViewStylesFragment extends Fragment implements BiermachtFragment {
  private static int resource = R.layout.fragment_view;
  ;
  private OnItemClickListener mClickListener;
  private ListView listView;
  private ArrayList<BeerStyle> list;
  private BeerStyleArrayAdapter arrayAdapter;
  View pageView;
  Context c;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);

    setHasOptionsMenu(true);

    // Context
    c = getActivity();

    // Get ingredient list
    list = new IngredientHandler(c).getStylesList();
    Collections.sort(list, new ToStringComparator());

    // Set up the list adapter
    arrayAdapter = new BeerStyleArrayAdapter(c, list);

    // Initialize important junk
    listView = (ListView) pageView.findViewById(R.id.listview);

    // Set up the onClickListener
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        BeerStyle s = list.get(pos);
        Intent i = new Intent(c, DisplayStyleActivity.class);
        i.putExtra(Constants.KEY_STYLE, s);
        startActivity(i);
      }
    };

    // Set whether or not we show the list view
    if (list.size() > 0) {
      listView.setVisibility(View.VISIBLE);
      listView.setAdapter(arrayAdapter);
      registerForContextMenu(listView);
      listView.setOnItemClickListener(mClickListener);
    }
    else {
      TextView noListView = (TextView) pageView.findViewById(R.id.nothing_to_show_view);
      noListView.setText("Hmm, something has gone wrong and I can't find any styles.  Re-set the " +
                                 "database from the settings activity.");
      noListView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }

  //**************************************************************************
  // The following set of methods implement the Biermacht Fragment Interface
  //**************************************************************************
  @Override
  public void handleClick(View v) {

  }

  @Override
  public void update() {
    // No need to do anything - this list is static.
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }

  @Override
  public String name() {
    return "Style Viewer";
  }
}
