package com.biermacht.brews.frontend.fragments;

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

  // Variables needed for onCreateView().  Declared here to improve
  // performance by reducing need for garbage collection.
  Ingredient ing;
  Intent intent;
  IngredientArrayAdapter ingredientArrayAdapter;

  /**
   * Public constructor.  All fragments must have an empty public constructor. Arguments are passed
   * via the setArguments method.  Use instance() to create new IngredientViewFragments rather than
   * this constructor.
   */
  public IngredientViewFragment() {
    // This fragment has no options menu.
    setHasOptionsMenu(false);
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
    r = getArguments().getParcelable(Constants.KEY_RECIPE);

    // Inflate the resource for this fragment, and find any
    // important component views.
    pageView = inflater.inflate(resource, container, false);
    ingredientListView = (ListView) pageView.findViewById(R.id.ingredient_list);

    // Get the ingredient list from the recipe.
    ingredientList = r.getIngredientList();

    // Set up the onClickListener. This handles click events when the 
    // user clicks on an ingredient in the list.
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        // Get the ingredient for this spot in the list.  We will then 
        // check what the ingredient type is, and cast it as appropriate.
        ing = r.getIngredientList().get(pos);

        if (ing.getType().equals(Ingredient.FERMENTABLE)) {
          // The user has selected a Fermentable.
          intent = new Intent(getActivity(), EditFermentableActivity.class);
          intent.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          intent.putExtra(Constants.KEY_RECIPE, r);
          intent.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          intent.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(intent);
        }
        else if (ing.getType().equals(Ingredient.HOP)) {
          // The user has selected a Hop.
          intent = new Intent(getActivity(), EditHopActivity.class);
          intent.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          intent.putExtra(Constants.KEY_RECIPE, r);
          intent.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          intent.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(intent);
        }
        else if (ing.getType().equals(Ingredient.YEAST)) {
          // The user has selected a Yeast.
          intent = new Intent(getActivity(), EditYeastActivity.class);
          intent.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          intent.putExtra(Constants.KEY_RECIPE, r);
          intent.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          intent.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(intent);
        }
        else if (ing.getType().equals(Ingredient.MISC)) {
          // The user has selected a Misc.
          intent = new Intent(getActivity(), EditMiscActivity.class);
          intent.putExtra(Constants.KEY_RECIPE_ID, r.getId());
          intent.putExtra(Constants.KEY_RECIPE, r);
          intent.putExtra(Constants.KEY_INGREDIENT_ID, ing.getId());
          intent.putExtra(Constants.KEY_INGREDIENT, ing);
          startActivity(intent);
        }
      }
    };

    // Set whether or not we show the list view
    if (ingredientList.size() > 0) {
      // There are ingredients.  Set up the ingredientArrayAdapter
      // and set the list view to be visible.
      ingredientArrayAdapter = new IngredientArrayAdapter(getActivity(), ingredientList, r);
      ingredientListView.setVisibility(View.VISIBLE);
      ingredientListView.setAdapter(ingredientArrayAdapter);
      registerForContextMenu(ingredientListView);
      ingredientListView.setOnItemClickListener(mClickListener);
    }
    else {
      // There are no ingredients.  Display a message indicating that 
      // no ingredients could be found.
      TextView noIngredientsView = (TextView) pageView.findViewById(R.id.no_ingredients_view);
      noIngredientsView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }
}
