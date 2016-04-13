package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomHopActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomYeastActivity;
import com.biermacht.brews.frontend.adapters.CustomIngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.comparators.IngredientsComparator;
import com.biermacht.brews.utils.comparators.RecipeIngredientsComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;
import java.util.Collections;

public class EditIngredientsFragment extends Fragment implements BiermachtFragment {
  private static int resource = R.layout.fragment_view;
  ;
  private OnItemClickListener mClickListener;
  private ListView listView;
  private ArrayList<Ingredient> list;
  private CustomIngredientArrayAdapter ingredientArrayAdapter;
  private DatabaseAPI databaseApi;
  View pageView;
  Context context;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);

    setHasOptionsMenu(true);

    // Context
    context = getActivity();
    databaseApi = new DatabaseAPI(context);

    // Get ingredient list
    list = databaseApi.getIngredients(Constants.DATABASE_CUSTOM);
    list.addAll(databaseApi.getIngredients(Constants.DATABASE_PERMANENT));
    Collections.sort(list, new IngredientsComparator());

    // Set up the list adapter
    ingredientArrayAdapter = new CustomIngredientArrayAdapter(context, list);

    // Initialize important junk
    listView = (ListView) pageView.findViewById(R.id.listview);

    // Set up the onClickListener
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        Ingredient ing = list.get(pos);

        // Grain pressed
        if (ing.getType().equals(Ingredient.FERMENTABLE)) {
          Intent i = new Intent(context, EditCustomFermentableActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Hop Pressed
        if (ing.getType().equals(Ingredient.HOP)) {
          Intent i = new Intent(context, EditCustomHopActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Yeast Pressed
        if (ing.getType().equals(Ingredient.YEAST)) {
          Intent i = new Intent(context, EditCustomYeastActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Misc Pressed
        if (ing.getType().equals(Ingredient.MISC)) {
          Intent i = new Intent(context, EditCustomMiscActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }
      }
    };

    // Set whether or not we show the list view
    if (list.size() > 0) {
      listView.setVisibility(View.VISIBLE);
      listView.setAdapter(ingredientArrayAdapter);
      registerForContextMenu(listView);
      listView.setOnItemClickListener(mClickListener);
    }
    else {
      TextView noListView = (TextView) pageView.findViewById(R.id.nothing_to_show_view);
      noListView.setText("If you can't find the ingredient you need for your recipe, create a custom one here!");
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
    // Get the full list of ingredients from the custom database and permanent database.
    ArrayList<Ingredient> loadedList = databaseApi.getIngredients(Constants.DATABASE_CUSTOM);
    loadedList.addAll(databaseApi.getIngredients(Constants.DATABASE_PERMANENT));

    // Add the loaded ingredients to the list for the list view.
    list.removeAll(list);
    list.addAll(loadedList);

    // Sort the list.
    Collections.sort(list, new RecipeIngredientsComparator());

    // Notify the adapter that the list has changed.
    ingredientArrayAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }

  @Override
  public String name() {
    return "Ingredients";
  }
}
