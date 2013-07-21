package com.biermacht.brews.frontend.fragments;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.EditFermentableActivity;
import com.biermacht.brews.frontend.EditHopActivity;
import com.biermacht.brews.frontend.EditYeastActivity;
import com.biermacht.brews.frontend.adapters.IngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import android.content.*;
import com.biermacht.brews.frontend.*;

public class IngredientViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private ListView ingredientListView;
	private ArrayList<Ingredient> ingredientList;
	View pageView;
	Context c;
	
	public IngredientViewFragment(Context c, Recipe r)
	{
		this.resource = R.layout.ingredient_view;
		this.r = r;
		this.c = c;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = inflater.inflate(resource, container, false);
		
		setHasOptionsMenu(true);
		
		// Initialize important junk
		ingredientListView = (ListView) pageView.findViewById(R.id.ingredient_list);
			
        // Set up the onClickListener
		mClickListener = new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id)
			{	
				Ingredient ing = r.getIngredientList().get(pos);
			
				// Grain pressed
				if (ing.getType().equals(Ingredient.FERMENTABLE))
				{
			  		Intent editGrainIntent = new Intent(c, EditFermentableActivity.class);
			  		editGrainIntent.putExtra(Utils.INTENT_RECIPE_ID, r.getId());
				    editGrainIntent.putExtra(Utils.INTENT_INGREDIENT_ID, ing.getId());
			  		startActivity(editGrainIntent);
				}
			
				// Hop Pressed
				if (ing.getType().equals(Ingredient.HOP))
				{
			  		Intent editHopIntent = new Intent(c, EditHopActivity.class);
			  		editHopIntent.putExtra(Utils.INTENT_RECIPE_ID, r.getId());
			  		editHopIntent.putExtra(Utils.INTENT_INGREDIENT_ID, ing.getId());
			  		startActivity(editHopIntent);
				}
				
				// Yeast Pressed
				if (ing.getType().equals(Ingredient.YEAST))
				{
			  		Intent editYeastIntent = new Intent(c, EditYeastActivity.class);
			  		editYeastIntent.putExtra(Utils.INTENT_RECIPE_ID, r.getId());
			  		editYeastIntent.putExtra(Utils.INTENT_INGREDIENT_ID, ing.getId());
			  		startActivity(editYeastIntent);
				}
				
				// Misc Pressed
				if (ing.getType().equals(Ingredient.MISC))
				{
			  		Intent editMiscIntent = new Intent(c, EditMiscActivity.class);
			  		editMiscIntent.putExtra(Utils.INTENT_RECIPE_ID, r.getId());
			  		editMiscIntent.putExtra(Utils.INTENT_INGREDIENT_ID, ing.getId());
			  		startActivity(editMiscIntent);
				}
			}
		};
          
		ingredientList = r.getIngredientList();
		  
		// Set whether or not we show the list view
		if (ingredientList.size() > 0)
		{
			IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(c, ingredientList, r);
			ingredientListView.setVisibility(View.VISIBLE);
			ingredientListView.setAdapter(ingredientArrayAdapter);
			registerForContextMenu(ingredientListView);
			ingredientListView.setOnItemClickListener(mClickListener);
		}
		else
		{
			TextView noIngredientsView = (TextView) pageView.findViewById(R.id.no_ingredients_view);
			noIngredientsView.setVisibility(View.VISIBLE);
		}
	
		return pageView;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.ingredient_menu, menu);
	}
	
}
