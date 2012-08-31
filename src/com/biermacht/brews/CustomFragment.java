package com.biermacht.brews;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomFragment extends Fragment {

	private int resource;
	private Recipe r;
	private boolean isIngredientList;
	
	public CustomFragment(int resource, Recipe r)
	{
		this.resource = resource;
		this.r = r;
		
		// Determine what kind of view this is
		if (resource == R.layout.ingredient_view)
			isIngredientList = true;
		else
			isIngredientList = false;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		TableLayout tableView = new TableLayout(DisplayRecipeActivity.appContext);

		if(isIngredientList)
		{
		  ArrayList<Ingredient> ingredientList = r.getIngredientList();
		  IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList);
		  ListView listView = new ListView(DisplayRecipeActivity.appContext);
		  listView.setAdapter(ingredientArrayAdapter);
		  tableView.addView(listView);
		}
		else
		{
		  TableRow tableRow = new TableRow(DisplayRecipeActivity.appContext);
		  TextView tv = new TextView(DisplayRecipeActivity.appContext);
		  tv.setText("1. This is the instructions for " + r.getRecipeName());
		  tableRow.addView(tv);
		  tableView.addView(tableRow);
		}

		
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(tableView);
		
		return inflater.inflate(resource, container, false);
	}
}
