package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomHopActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomYeastActivity;
import com.biermacht.brews.frontend.adapters.CustomIngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.comparators.IngredientComparator;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

import java.util.ArrayList;
import java.util.Collections;

public class EditIngredientsFragment extends Fragment implements ClickableFragment
{
  private static int resource = R.layout.fragment_view;
  ;
  private OnItemClickListener mClickListener;
  private ListView listView;
  private ArrayList<Ingredient> list;
  View pageView;
  Context c;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);

    setHasOptionsMenu(true);

    // Context
    c = getActivity();

    // Get ingredient list
    list = Database.getIngredientsFromVirtualDatabase(Constants.DATABASE_CUSTOM);
    Collections.sort(list, new IngredientComparator());

    // Initialize important junk
    listView = (ListView) pageView.findViewById(R.id.listview);

    // Set up the onClickListener
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        Ingredient ing = list.get(pos);

        // Grain pressed
        if (ing.getType().equals(Ingredient.FERMENTABLE)) {
          Intent i = new Intent(c, EditCustomFermentableActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Hop Pressed
        if (ing.getType().equals(Ingredient.HOP)) {
          Intent i = new Intent(c, EditCustomHopActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Yeast Pressed
        if (ing.getType().equals(Ingredient.YEAST)) {
          Intent i = new Intent(c, EditCustomYeastActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Misc Pressed
        if (ing.getType().equals(Ingredient.MISC)) {
          Intent i = new Intent(c, EditCustomMiscActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }
      }
    };

    // Set whether or not we show the list view
    if (list.size() > 0) {
      CustomIngredientArrayAdapter ingredientArrayAdapter = new CustomIngredientArrayAdapter(c, list);
      listView.setVisibility(View.VISIBLE);
      listView.setAdapter(ingredientArrayAdapter);
      registerForContextMenu(listView);
      listView.setOnItemClickListener(mClickListener);
    }
    else {
      TextView noListView = (TextView) pageView.findViewById(R.id.nothing_to_show_view);
      noListView.setText("No ingredients to display");
      noListView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }

  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_ingredient_menu, menu);
  }

  //**************************************************************************
  // The following set of methods implement the Biermacht Fragment Interface
  //**************************************************************************
  @Override
  public void handleClick(View v) {

  }
  
  @Override
  public void update() {
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }
}
