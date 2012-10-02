package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeReccomendedValues;
import com.biermacht.brews.utils.Utils;

public class CustomFragment extends Fragment {

	private int resource;
	private Recipe r;
	private boolean isIngredientList;
	private boolean isInstructionView;
	private OnItemClickListener mClickListener;
	private ListView ingredientListView;
	private ListView instructionListView;
	private ArrayList<Ingredient> ingredientList;
	private ArrayList<Instruction> instructionList;
	private RecipeReccomendedValues reccomendedValues;
	
	public CustomFragment(int resource, Recipe r)
	{
		this.resource = resource;
		this.r = r;
		this.reccomendedValues = Utils.getRecipeReccomendedValues(r);
		
		isIngredientList = false;
		isInstructionView = false;
		
		// Determine what kind of view this is
		if (resource == R.layout.ingredient_view)
			isIngredientList = true;
		else if(resource == R.layout.instruction_view)
			isInstructionView = true;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		LinearLayout pageView = new LinearLayout(DisplayRecipeActivity.appContext);
		inflater.inflate(resource, pageView);
		  
		TableLayout tableView = new TableLayout(DisplayRecipeActivity.appContext);
		tableView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		// Initialize important junk
		ingredientListView = (ListView) pageView.findViewById(R.id.ingredient_list);
		instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);

		// INGREDIENT VIEW STUFF
		if(isIngredientList)
		{
			
          // Set up the onClickListener
          mClickListener = new OnItemClickListener() 
          {
			public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id)
			{	
				Ingredient ing = r.getIngredientList().get(pos);
				
				if (ing.getType().equals(Ingredient.GRAIN))
				{
		  		Intent i = new Intent(DisplayRecipeActivity.appContext, EditGrainActivity.class);
			    i.putExtra("com.biermacht.brews.recipeID", r.getId());
			    i.putExtra("com.biermacht.brews.grainID", ing.getId());
		  		startActivity(i);
				}
			}
          };
          
		  ingredientList = r.getIngredientList();
		  
		  // Set whether or not we show the list view
		  if (ingredientList.size() > 0)
		  {
			  IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList);
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
		  
		  tableView.addView(pageView);
		}
		
		// INSTRUCTION VIEW STUFF
		else if(isInstructionView)
		{
		  instructionList = r.getInstructionList();
		  
		  if (instructionList.size() > 0)
		  {
			  InstructionArrayAdapter instructionArrayAdapter = new InstructionArrayAdapter(DisplayRecipeActivity.appContext, instructionList);
			  instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);
			  instructionListView.setAdapter(instructionArrayAdapter);
			  instructionListView.setVisibility(View.VISIBLE);
		  }
		  else
		  {
			  TextView noInstructionsView = (TextView) pageView.findViewById(R.id.no_instructions_view);
			  noInstructionsView.setVisibility(View.VISIBLE);
		  }
		  tableView.addView(pageView);
		}
		else
		{	  
		  // View to hold the description
		  TextView descriptionView = (TextView) pageView.findViewById(R.id.description_view);
		  descriptionView.setText(r.getDescription());
		  
		  // Add all the detail views here
		  LinearLayout beerTypeView = (LinearLayout) pageView.findViewById(R.id.beer_type_view);
		  LinearLayout gravityView = (LinearLayout) pageView.findViewById(R.id.beer_gravity_view);
		  LinearLayout bitternessView = (LinearLayout) pageView.findViewById(R.id.beer_bitterness_view);
		  LinearLayout colorView = (LinearLayout) pageView.findViewById(R.id.beer_color_view);
		  LinearLayout abvView = (LinearLayout) pageView.findViewById(R.id.beer_abv_view);

		  // Beer type detail view
		  TextView tag = (TextView) beerTypeView.findViewById(R.id.tag);
		  TextView content = (TextView) beerTypeView.findViewById(R.id.content);
		  tag.setText("Beer Style: ");
		  content.setText(r.getBeerType());
		  
		  // Beer gravity detail view
		  TextView grav_tag = (TextView) gravityView.findViewById(R.id.tag);
		  TextView grav_content = (TextView) gravityView.findViewById(R.id.content);
		  grav_tag.setText("Orig. Gravity: ");
		  grav_content.setText("" + r.getGravity() + "");
		  
		  // Beer bitterness detail view
		  TextView bitt_tag = (TextView) bitternessView.findViewById(R.id.tag);
		  TextView bitt_content = (TextView) bitternessView.findViewById(R.id.content);
		  bitt_tag.setText("Bitter (IBU): ");
		  bitt_content.setText("" + r.getBitterness() + "");	
		  
		  // Beer color detail view
		  TextView color_tag = (TextView) colorView.findViewById(R.id.tag);
		  TextView color_content = (TextView) colorView.findViewById(R.id.content);
		  color_tag.setText("Color, SRM: ");
		  color_content.setText("" + r.getColor() + "");
		  
		  // Beer abv detail view
		  TextView abv_tag = (TextView) abvView.findViewById(R.id.tag);
		  TextView abv_content = (TextView) abvView.findViewById(R.id.content);
		  abv_tag.setText("ABV: ");
		  abv_content.setText("" + r.getABV() + "%");
		  
		  // Set all the colors appropriately
		  String green = "#44AA44";
		  String red = "#FF0000";
		  
		  Boolean isGravGreen = Utils.isWithinRange(r.getGravity(), reccomendedValues.getMinOG(), reccomendedValues.getMaxOG());
		  Boolean isBitGreen = Utils.isWithinRange(r.getBitterness(), reccomendedValues.getMinBitter(), reccomendedValues.getMaxBitter());
		  Boolean isColorGreen = Utils.isWithinRange(r.getColor(), reccomendedValues.getMinColor(), reccomendedValues.getMaxColor());
		  Boolean isAbvGreen = Utils.isWithinRange(r.getABV(), reccomendedValues.getMinAbv(), reccomendedValues.getMaxAbv());
		  
		  if (isGravGreen)
			  grav_content.setTextColor(Color.parseColor(green));
		  else
			  grav_content.setTextColor(Color.parseColor(red));
		  
		  if (isBitGreen)
			  bitt_content.setTextColor(Color.parseColor(green));
		  else
			  bitt_content.setTextColor(Color.parseColor(red));
		  
		  if (isColorGreen)
			  color_content.setTextColor(Color.parseColor(green));
		  else
			  color_content.setTextColor(Color.parseColor(red));
		  
		  if (isAbvGreen)
			  abv_content.setTextColor(Color.parseColor(green));
		  else
			  abv_content.setTextColor(Color.parseColor(red));
		  
		  // ===========================================
		  // RECCOMENDED VALUES VIEWS
		  // ===========================================

		  
		  // Beer gravity detail view
		  TextView grav_range = (TextView) gravityView.findViewById(R.id.gravity_range);
		  grav_range.setText(reccomendedValues.getGravRange());
		  
		  // Beer bitterness detail view
		  TextView bitt_range = (TextView) bitternessView.findViewById(R.id.bitterness_range);
		  bitt_range.setText(reccomendedValues.getBitterRange());	
		  
		  // Beer color detail view
		  TextView color_range = (TextView) colorView.findViewById(R.id.color_range);
		  color_range.setText(reccomendedValues.getColorRange());
		  
		  // Beer abv detail view
		  TextView abv_range = (TextView) abvView.findViewById(R.id.ABV_range);
		  abv_range.setText(reccomendedValues.getAbvRange());
		  
		  
		  
		  //Add details page view to the table
		  tableView.addView(pageView);	
		}

		
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(tableView);
		
		return inflater.inflate(resource, container, false);
	}
	
	public void update()
	{
		Log.e("CustomFrag", "CustomFragment update()");
		
		// Ingredient updates
		r = Utils.getRecipeWithId(r.getId());
		r.update();
		ingredientList = r.getIngredientList();
		IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList);
		ingredientListView.setAdapter(ingredientArrayAdapter);

	}
}
