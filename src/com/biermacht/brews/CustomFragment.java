package com.biermacht.brews;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		TableRow tableRow1 = new TableRow(DisplayRecipeActivity.appContext);
		TableRow tableRow2 = new TableRow(DisplayRecipeActivity.appContext);
		
		TextView tv = new TextView(DisplayRecipeActivity.appContext);
		TextView tv2 = new TextView(DisplayRecipeActivity.appContext);
		
		if(isIngredientList)
		{		
		  tv.setText("1. This is the ingredient list for " + r.getRecipeName());
		  tv2.setText("2. Soon we will stick some ingredients here");	
		}
		else
		{
		  tv.setText("1. This is the instructions for " + r.getRecipeName());
		  tv2.setText("2. Soon we will stick some instructions here");
		}

		
		tableRow1.addView(tv);
		tableRow2.addView(tv2);
		
		tableView.addView(tableRow1);
		tableView.addView(tableRow2);
		
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(tableView);
		
		return inflater.inflate(resource, container, false);
	}
}
