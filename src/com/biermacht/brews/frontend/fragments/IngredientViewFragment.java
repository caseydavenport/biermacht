package com.biermacht.brews.frontend.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.frontend.EditGrainActivity;
import com.biermacht.brews.frontend.EditHopActivity;
import com.biermacht.brews.frontend.EditYeastActivity;
import com.biermacht.brews.frontend.adapters.IngredientArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class IngredientViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private ListView ingredientListView;
	private ArrayList<Ingredient> ingredientList;
	LinearLayout pageView;
	
	public IngredientViewFragment(Recipe r)
	{
		this.resource = R.layout.ingredient_view;
		this.r = r;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = new LinearLayout(DisplayRecipeActivity.appContext);
		inflater.inflate(resource, pageView);
		
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
			  		Intent editGrainIntent = new Intent(DisplayRecipeActivity.appContext, EditGrainActivity.class);
			  		editGrainIntent.putExtra("com.biermacht.brews.recipeID", r.getId());
				    editGrainIntent.putExtra("com.biermacht.brews.grainID", ing.getId());
			  		startActivity(editGrainIntent);
				}
			
				// Hop Pressed
				if (ing.getType().equals(Ingredient.HOP))
				{
			  		Intent editHopIntent = new Intent(DisplayRecipeActivity.appContext, EditHopActivity.class);
			  		editHopIntent.putExtra("com.biermacht.brews.recipeID", r.getId());
			  		editHopIntent.putExtra("com.biermacht.brews.grainID", ing.getId());
			  		startActivity(editHopIntent);
				}
				
				// Yeast Pressed
				if (ing.getType().equals(Ingredient.YEAST))
				{
			  		Intent editHopIntent = new Intent(DisplayRecipeActivity.appContext, EditYeastActivity.class);
			  		editHopIntent.putExtra("com.biermacht.brews.recipeID", r.getId());
			  		editHopIntent.putExtra("com.biermacht.brews.grainID", ing.getId());
			  		startActivity(editHopIntent);
				}
			}
		};
          
		ingredientList = r.getIngredientList();
		  
		// Set whether or not we show the list view
		if (ingredientList.size() > 0)
		{
			IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList, r);
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
	
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(pageView);
	
		return inflater.inflate(resource, container, false);
	}
	
	public void update()
	{
		// Update the recipe and its fields..
		r = Utils.getRecipeWithId(r.getId());
		r.update();
		ingredientList = r.getIngredientList();
		 
		
		// Set whether or not we show the list view
		if (ingredientList.size() > 0)
		{
			IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList, r);
			ingredientListView.setVisibility(View.VISIBLE);
			ingredientListView.setAdapter(ingredientArrayAdapter);
			registerForContextMenu(ingredientListView);
			ingredientListView.setOnItemClickListener(mClickListener);
			pageView.findViewById(R.id.no_ingredients_view).setVisibility(View.GONE);
		}
		else
		{
			pageView.findViewById(R.id.no_ingredients_view).setVisibility(View.VISIBLE);
			ingredientListView.setVisibility(View.GONE);
		}
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.ingredient_menu, menu);
	}
	
}
