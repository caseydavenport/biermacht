package com.biermacht.brews.frontend.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeReccomendedValues;
import com.biermacht.brews.utils.Utils;

public class DetailsViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private RecipeReccomendedValues reccomendedValues;
	LinearLayout pageView;
	
	private LinearLayout beerTypeView;
	private LinearLayout gravityView;
	private LinearLayout bitternessView;
	private LinearLayout colorView;
	private LinearLayout abvView;
	private LinearLayout boilTimeView;
	
	public DetailsViewFragment(Recipe r)
	{
		this.resource = R.layout.details_view;
		this.r = r;
		this.reccomendedValues = Utils.getRecipeReccomendedValues(r);
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = new LinearLayout(DisplayRecipeActivity.appContext);
		inflater.inflate(resource, pageView);
		
		setHasOptionsMenu(true);
			  
		  // View to hold the description
		  TextView descriptionView = (TextView) pageView.findViewById(R.id.description_view);
		  descriptionView.setText(r.getDescription());
		  
		  // Add all the detail views here
		  beerTypeView = (LinearLayout) pageView.findViewById(R.id.beer_type_view);
		  gravityView = (LinearLayout) pageView.findViewById(R.id.beer_gravity_view);
		  bitternessView = (LinearLayout) pageView.findViewById(R.id.beer_bitterness_view);
		  colorView = (LinearLayout) pageView.findViewById(R.id.beer_color_view);
		  abvView = (LinearLayout) pageView.findViewById(R.id.beer_abv_view);
		  boilTimeView = (LinearLayout) pageView.findViewById(R.id.boil_time_view);

		  // Beer type detail view
		  TextView tag = (TextView) beerTypeView.findViewById(R.id.tag);
		  TextView content = (TextView) beerTypeView.findViewById(R.id.content);
		  tag.setText("Beer Style: ");
		  content.setText(r.getStyle());
		  
		  // Beer gravity detail view
		  TextView grav_tag = (TextView) gravityView.findViewById(R.id.tag);
		  TextView grav_content = (TextView) gravityView.findViewById(R.id.content);
		  grav_tag.setText("Orig. Gravity: ");
		  grav_content.setText("" + r.getOG() + "");
		  
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
		  
		  // Beer boil time detail view
		  TextView boilTimeTag = (TextView) boilTimeView.findViewById(R.id.tag);
		  TextView boilTimeContent = (TextView) boilTimeView.findViewById(R.id.content);
		  boilTimeTag.setText("Boil Time: ");
		  boilTimeContent.setText("" + r.getBoilTime() + " mins");
		  
		  // Set all the colors appropriately
		  String green = "#44AA44";
		  String red = "#FF0000";
		  
		  Boolean isGravGreen = Utils.isWithinRange(r.getOG(), reccomendedValues.getMinOG(), reccomendedValues.getMaxOG());
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

		
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(pageView);
		
		return inflater.inflate(resource, container, false);
	}
	
	public void update()
	{
		// Update reccomended values, recipe, etc
		r = Utils.getRecipeWithId(r.getId());
		r.update();
		reccomendedValues = Utils.getRecipeReccomendedValues(r);
		
		// UPDATE THE STUFF BELOW GOD THERES A LOT
		if (pageView != null)
		{
		  // View to hold the description
		  TextView descriptionView = (TextView) pageView.findViewById(R.id.description_view);
		  descriptionView.setText(r.getDescription());
		  
		  // Add all the detail views here
		  beerTypeView = (LinearLayout) pageView.findViewById(R.id.beer_type_view);
		  gravityView = (LinearLayout) pageView.findViewById(R.id.beer_gravity_view);
		  bitternessView = (LinearLayout) pageView.findViewById(R.id.beer_bitterness_view);
		  colorView = (LinearLayout) pageView.findViewById(R.id.beer_color_view);
		  abvView = (LinearLayout) pageView.findViewById(R.id.beer_abv_view);
		  boilTimeView = (LinearLayout) pageView.findViewById(R.id.boil_time_view);

		  // Beer type detail view
		  TextView tag = (TextView) beerTypeView.findViewById(R.id.tag);
		  TextView content = (TextView) beerTypeView.findViewById(R.id.content);
		  tag.setText("Beer Style: ");
		  content.setText(r.getStyle());
		  
		  // Beer gravity detail view
		  TextView grav_tag = (TextView) gravityView.findViewById(R.id.tag);
		  TextView grav_content = (TextView) gravityView.findViewById(R.id.content);
		  grav_tag.setText("Orig. Gravity: ");
		  grav_content.setText("" + r.getOG() + "");
		  
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
		  
		  // Beer boil time detail view
		  TextView boilTimeTag = (TextView) boilTimeView.findViewById(R.id.tag);
		  TextView boilTimeContent = (TextView) boilTimeView.findViewById(R.id.content);
		  boilTimeTag.setText("Boil Time: ");
		  boilTimeContent.setText("" + r.getBoilTime() + " mins");
		  
		  // Set all the colors appropriately
		  String green = "#44AA44";
		  String red = "#FF0000";
		  
		  Boolean isGravGreen = Utils.isWithinRange(r.getOG(), reccomendedValues.getMinOG(), reccomendedValues.getMaxOG());
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
		}
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.details_menu, menu);
	}
	
}
