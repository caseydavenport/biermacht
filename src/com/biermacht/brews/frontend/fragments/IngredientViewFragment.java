package com.biermacht.brews.frontend.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.EditFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditHopActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditYeastActivity;
import com.biermacht.brews.frontend.adapters.IngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;


public class IngredientViewFragment extends Fragment {

  private int resource = R.layout.fragment_ingredient_view;
  private Recipe r;
  private OnItemClickListener mClickListener;
  private ListView ingredientListView;
  private ArrayList<Ingredient> ingredientList;
  View pageView;

  public IngredientViewFragment() {
  }
  
  public static IngredientViewFragment instance(Recipe r) {
    // Create the fragment.
    IngredientViewFragment f = new IngredientViewFragment();
    
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
    this.r = getArguments().getParcelable(Constants.KEY_RECIPE);
    
    // Inflate the resource for this fragment.
    pageView = inflater.inflate(resource, container, false);

    setHasOptionsMenu(false);

    // Initialize important junk
    ingredientListView = (ListView) pageView.findViewById(R.id.ingredient_list);
    ingredientList = r.getIngredientList();

    // Set up the onClickListener
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        Ingredient ing = r.getIngredientList().get(pos);

        // Grain pressed
        if (ing.getType().equals(Ingredient.FERMENTABLE)) {
          Intent i = new Intent(getActivity(), EditFermentableActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          i.putExtra(Constants.KEY_RECIPE, r);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Hop Pressed
        if (ing.getType().equals(Ingredient.HOP)) {
          Intent i = new Intent(getActivity(), EditHopActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          i.putExtra(Constants.KEY_RECIPE, r);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Yeast Pressed
        if (ing.getType().equals(Ingredient.YEAST)) {
          Intent i = new Intent(getActivity(), EditYeastActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          i.putExtra(Constants.KEY_RECIPE, r);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }

        // Misc Pressed
        if (ing.getType().equals(Ingredient.MISC)) {
          Intent i = new Intent(getActivity(), EditMiscActivity.class);
          i.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          i.putExtra(Constants.KEY_RECIPE, r);
          i.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          i.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(i);
        }
      }
    };

    // Set whether or not we show the list view
    if (ingredientList.size() > 0) {
      IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(getActivity(), ingredientList, r);
      ingredientListView.setVisibility(View.VISIBLE);
      ingredientListView.setAdapter(ingredientArrayAdapter);
      registerForContextMenu(ingredientListView);
      ingredientListView.setOnItemClickListener(mClickListener);
    }
    else {
      TextView noIngredientsView = (TextView) pageView.findViewById(R.id.no_ingredients_view);
      noIngredientsView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }
}
