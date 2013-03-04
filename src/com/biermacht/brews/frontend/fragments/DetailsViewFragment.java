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
import com.biermacht.brews.utils.ColorHandler;

public class DetailsViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private RecipeReccomendedValues reccomendedValues;
	LinearLayout pageView;
	
	private LinearLayout beerTypeView;
	private LinearLayout ogView;
	private LinearLayout fgView;
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
		  
		  // View to hold the beer color
		  LinearLayout showColorView = (LinearLayout) pageView.findViewById(R.id.color_space);
		  String color = ColorHandler.getSrmColor(r.getColor());
		  showColorView.setBackgroundColor(Color.parseColor(color));
		  
		  // Add all the detail views here
		  beerTypeView = (LinearLayout) pageView.findViewById(R.id.beer_type_view);
		  ogView = (LinearLayout) pageView.findViewById(R.id.beer_OG_view);
		  fgView = (LinearLayout) pageView.findViewById(R.id.beer_FG_view);
		  bitternessView = (LinearLayout) pageView.findViewById(R.id.beer_bitterness_view);
		  colorView = (LinearLayout) pageView.findViewById(R.id.beer_color_view);
		  abvView = (LinearLayout) pageView.findViewById(R.id.beer_abv_view);
		  boilTimeView = (LinearLayout) pageView.findViewById(R.id.boil_time_view);

		  // Beer type detail view
		  TextView tag = (TextView) beerTypeView.findViewById(R.id.tag);
		  TextView content = (TextView) beerTypeView.findViewById(R.id.content);
		  tag.setText("Beer Style: ");
		  content.setText(r.getStyle());
		  
		  // Beer OG detail view
		  TextView og_tag = (TextView) ogView.findViewById(R.id.tag);
		  TextView og_content = (TextView) ogView.findViewById(R.id.content);
		  og_tag.setText("Orig. Gravity: ");
		  og_content.setText("" + r.getOG() + "");
		  
		  // Beer FG detail view
		  TextView fg_tag = (TextView) fgView.findViewById(R.id.tag);
		  TextView fg_content = (TextView) fgView.findViewById(R.id.content);
		  fg_tag.setText("FG Estimate: ");
		  fg_content.setText("" + r.getFG() + "");
		  
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
		  String yellow = "#FFAA00";
		  
		  // If its in this range, we're all good - make it green.
		  Boolean isOG_GOOD = Utils.isWithinRange(r.getOG(), reccomendedValues.getMinOG(), reccomendedValues.getMaxOG());
		  Boolean isFG_GOOD = Utils.isWithinRange(r.getFG(), reccomendedValues.getMinFG(), reccomendedValues.getMaxFG());
		  Boolean isBit_GOOD = Utils.isWithinRange(r.getBitterness(), reccomendedValues.getMinBitter(), reccomendedValues.getMaxBitter());
		  Boolean isColor_GOOD = Utils.isWithinRange(r.getColor(), reccomendedValues.getMinColor(), reccomendedValues.getMaxColor());
		  Boolean isAbv_GOOD = Utils.isWithinRange(r.getABV(), reccomendedValues.getMinAbv(), reccomendedValues.getMaxAbv());
		  
		  // If it is in this range but not the GOOD ranges above, color it yellow
	      Boolean isOG_OK = Utils.isWithinRange(r.getOG(), reccomendedValues.getMinOG()-.002, reccomendedValues.getMaxOG()+.002);
		  Boolean isFG_OK = Utils.isWithinRange(r.getFG(), reccomendedValues.getMinFG()-.002, reccomendedValues.getMaxFG()+.002);
		  Boolean isBit_OK = Utils.isWithinRange(r.getBitterness(), .9*reccomendedValues.getMinBitter(), 1.1*reccomendedValues.getMaxBitter());
		  Boolean isColor_OK = Utils.isWithinRange(r.getColor(), reccomendedValues.getMinColor()-3, reccomendedValues.getMaxColor()+3);
		  Boolean isAbv_OK = Utils.isWithinRange(r.getABV(), reccomendedValues.getMinAbv()-.2, reccomendedValues.getMaxAbv()+.2);
		
		  
		  if (isOG_GOOD)
			  og_content.setTextColor(Color.parseColor(green));
		  else if (isOG_OK)
		  	  og_content.setTextColor(Color.parseColor(yellow));
		  else
			  og_content.setTextColor(Color.parseColor(red));
		  
		  if (isFG_GOOD)
			  fg_content.setTextColor(Color.parseColor(green));
	      else if (isFG_OK)
			  fg_content.setTextColor(Color.parseColor(yellow));
		  else
			  fg_content.setTextColor(Color.parseColor(red));
		  
		  if (isBit_GOOD)
			  bitt_content.setTextColor(Color.parseColor(green));  
		  else if (isBit_OK)
			  bitt_content.setTextColor(Color.parseColor(yellow));
		  else
			  bitt_content.setTextColor(Color.parseColor(red));
		  
		  if (isColor_GOOD)
			  color_content.setTextColor(Color.parseColor(green));
		  else if (isColor_OK)
			  color_content.setTextColor(Color.parseColor(yellow));
		  else
			  color_content.setTextColor(Color.parseColor(red));
		  
		  if (isAbv_GOOD)
			  abv_content.setTextColor(Color.parseColor(green));
		  else if (isAbv_OK)
			  abv_content.setTextColor(Color.parseColor(yellow));
		  else
			  abv_content.setTextColor(Color.parseColor(red));
		  
		  // ===========================================
		  // RECCOMENDED VALUES VIEWS
		  // ===========================================

		  
		  // Beer OG detail view
		  TextView og_range = (TextView) ogView.findViewById(R.id.OG_range);
		  og_range.setText(reccomendedValues.getOGRange());
		  
		  // Beer FG detail view
		  TextView fg_range = (TextView) fgView.findViewById(R.id.FG_range);
		  fg_range.setText(reccomendedValues.getFGRange());
		  
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
		  ogView = (LinearLayout) pageView.findViewById(R.id.beer_OG_view);
		  fgView = (LinearLayout) pageView.findViewById(R.id.beer_FG_view);
		  bitternessView = (LinearLayout) pageView.findViewById(R.id.beer_bitterness_view);
		  colorView = (LinearLayout) pageView.findViewById(R.id.beer_color_view);
		  abvView = (LinearLayout) pageView.findViewById(R.id.beer_abv_view);
		  boilTimeView = (LinearLayout) pageView.findViewById(R.id.boil_time_view);

		  // Beer type detail view
		  TextView tag = (TextView) beerTypeView.findViewById(R.id.tag);
		  TextView content = (TextView) beerTypeView.findViewById(R.id.content);
		  tag.setText("Beer Style: ");
		  content.setText(r.getStyle());
		  
		  // Beer OG detail view
		  TextView og_tag = (TextView) ogView.findViewById(R.id.tag);
		  TextView og_content = (TextView) ogView.findViewById(R.id.content);
		  og_tag.setText("Orig. Gravity: ");
		  og_content.setText("" + r.getOG() + "");
		  
		  // Beer FG detail view
		  TextView fg_tag = (TextView) fgView.findViewById(R.id.tag);
		  TextView fg_content = (TextView) fgView.findViewById(R.id.content);
		  fg_tag.setText("FG Estimate: ");
		  fg_content.setText("" + r.getFG() + "");
		  
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
		  
		  Boolean isOGGreen = Utils.isWithinRange(r.getOG(), reccomendedValues.getMinOG(), reccomendedValues.getMaxOG());
		  Boolean isFGGreen = Utils.isWithinRange(r.getFG(), reccomendedValues.getMinFG(), reccomendedValues.getMaxFG());
		  Boolean isBitGreen = Utils.isWithinRange(r.getBitterness(), reccomendedValues.getMinBitter(), reccomendedValues.getMaxBitter());
		  Boolean isColorGreen = Utils.isWithinRange(r.getColor(), reccomendedValues.getMinColor(), reccomendedValues.getMaxColor());
		  Boolean isAbvGreen = Utils.isWithinRange(r.getABV(), reccomendedValues.getMinAbv(), reccomendedValues.getMaxAbv());
		  
		  if (isOGGreen)
			  og_content.setTextColor(Color.parseColor(green));
		  else
			  og_content.setTextColor(Color.parseColor(red));
		  
		  if (isFGGreen)
			  fg_content.setTextColor(Color.parseColor(green));
		  else
			  fg_content.setTextColor(Color.parseColor(red));
		  
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

		  // Beer OG detail view
		  TextView og_range = (TextView) ogView.findViewById(R.id.OG_range);
		  og_range.setText(reccomendedValues.getOGRange());
		  
		  // Beer FG detail view
		  TextView fg_range = (TextView) fgView.findViewById(R.id.FG_range);
		  fg_range.setText(reccomendedValues.getFGRange());
		  
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
